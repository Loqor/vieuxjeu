package com.loqor.core.entities;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.loqor.core.blockentities.PunchGameBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.effect.StatusEffectInstance;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.Arm;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.tick.OrderedTick;
import net.minecraft.world.tick.TickPriority;

public class PunchGameEntity extends LivingEntity {

	private static final TrackedData<BlockPos> BLOCK_ENTITY_POS = DataTracker.registerData(PunchGameEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
	private static final String NBT_KEY_POS = "BlockPos";

	@Nullable
	public BlockPos blockPos;

	public PunchGameEntity(EntityType<? extends LivingEntity> type, World world) {
		super(type, world);
	}
	
	@Override public boolean addStatusEffect(StatusEffectInstance effect, Entity source) { return false; }
	@Override public boolean hasNoGravity() { return true; }
	@Override public boolean isPushable() { return false; }
	@Override public boolean isFireImmune() { return true; }
	@Override public boolean doesRenderOnFire() { return false; }
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.blockPos == null) return false;

		final World world = this.getWorld();
		final BlockEntity be = world.getBlockEntity(this.blockPos);
				
		if (this.blockPos != null && !world.isClient() && be != null && be instanceof PunchGameBlockEntity) {
			final PunchGameBlockEntity gameBlockEntity = (PunchGameBlockEntity) be;
			
			// Set the score to (damage_amount * 10) + rand(0-6), rounded up, max score of 999
			gameBlockEntity.setScore(Math.min((int) (Math.ceil(amount  * 10)) + this.getRandom().nextInt(7), 999));
						
			world.getBlockTickScheduler().scheduleTick(new OrderedTick<Block>(
					be.getCachedState().getBlock(), 
					blockPos, 
					PunchGameBlockEntity.WIN_DELAY + world.getTime(), 
					TickPriority.NORMAL, 
					0)
			);
			
			gameBlockEntity.deactivate();
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void tick() {
		// If the game is gone, remove the entity
		if (!this.getWorld().isClient() && this.blockPos != null && (
			this.getWorld().getBlockEntity(this.blockPos) == null ||
			!(this.getWorld().getBlockEntity(this.blockPos) instanceof PunchGameBlockEntity)
		)) this.discard();
		
		super.tick();
	}
	
	@Override
	protected void initDataTracker(Builder builder) {
		super.initDataTracker(builder);
		builder.add(BLOCK_ENTITY_POS, BlockPos.ORIGIN);
	}

	@Override
	public void readCustomDataFromNbt(NbtCompound nbt) {
		super.readCustomDataFromNbt(nbt);
		
		if (nbt.contains(NBT_KEY_POS, NbtElement.INT_ARRAY_TYPE))
			NbtHelper.toBlockPos(nbt, NBT_KEY_POS).filter(World::isValid).ifPresent(pos -> this.blockPos = pos);
	}

	@Override
	public void writeCustomDataToNbt(NbtCompound nbt) {
		super.writeCustomDataToNbt(nbt);
		
		if (this.blockPos != null)
			nbt.put(NBT_KEY_POS, NbtHelper.fromBlockPos(this.blockPos));
	}
	
	// Required by LivingEntity. This class really acts as Entity but with 
	// criticals added without re-inventing how critical hits work
	@Override public Iterable<ItemStack> getArmorItems() { return List.of(); }
	@Override public ItemStack getEquippedStack(EquipmentSlot var1) { return ItemStack.EMPTY; }
	@Override public void equipStack(EquipmentSlot var1, ItemStack var2) {}
	@Override public Arm getMainArm() { return Arm.RIGHT;}

}
