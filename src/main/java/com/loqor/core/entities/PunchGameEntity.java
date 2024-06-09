package com.loqor.core.entities;

import java.util.List;

import org.jetbrains.annotations.Nullable;

import com.loqor.VieuxJeu;
import com.loqor.core.blockentities.PunchGameBlockEntity;

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
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PunchGameEntity extends LivingEntity {

	private static final TrackedData<BlockPos> BLOCK_ENTITY_POS = DataTracker.registerData(PunchGameEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
	public static final String NBT_KEY_POS = "BlockPos";
    public static final SoundEvent WIN_SOUND = SoundEvent.of(Identifier.of(VieuxJeu.MOD_ID, "punch_game_win"));

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
			PunchGameBlockEntity gameBlockEntity = (PunchGameBlockEntity) be;
			
			gameBlockEntity.score = Math.min((int) (Math.ceil(amount  * 10)) + Math.abs(this.getRandom().nextInt()) % 7, 999);
			gameBlockEntity.deactivate();
			
			final int tickets = ((int) (gameBlockEntity.score / 10) - 1) * 5;
			
			if (tickets > 0) {
				((PunchGameBlockEntity) be).giveTickets(tickets);
				world.playSound(null, this.blockPos, WIN_SOUND, SoundCategory.BLOCKS);
			}
			
			return true;
		}
		
		return false;
	}
	
	@Override
	public void tick() {
		if (!this.getWorld().isClient() && this.blockPos != null && (
			this.getWorld().getBlockEntity(this.blockPos) == null ||
			!(this.getWorld().getBlockEntity(this.blockPos) instanceof PunchGameBlockEntity)
		)) this.remove(RemovalReason.DISCARDED);
		
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

	@Override public Iterable<ItemStack> getArmorItems() { return List.of(); }
	@Override public ItemStack getEquippedStack(EquipmentSlot var1) { return ItemStack.EMPTY; }
	@Override public void equipStack(EquipmentSlot var1, ItemStack var2) {}
	@Override public Arm getMainArm() { return Arm.RIGHT;}

}
