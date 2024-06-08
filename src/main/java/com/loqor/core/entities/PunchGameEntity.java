package com.loqor.core.entities;

import org.jetbrains.annotations.Nullable;

import com.loqor.core.blockentities.PunchGameBlockEntity;
import com.loqor.core.blockentities.TicketReturningBlockEntity;

import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.DataTracker.Builder;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class PunchGameEntity extends Entity {

	private static final TrackedData<BlockPos> BLOCK_ENTITY_POS = DataTracker.registerData(PunchGameEntity.class, TrackedDataHandlerRegistry.BLOCK_POS);
	public static final String NBT_KEY_POS = "BlockPos";

	@Nullable
	public BlockPos blockPos;

	public PunchGameEntity(EntityType<?> type, World world) {
		super(type, world);
	}

	@Override
	public boolean canHit() { return true; }
	
	@Override
	public boolean damage(DamageSource source, float amount) {
		if (this.blockPos == null) return false;
		
		final World world = this.getWorld();
		final BlockEntity be = world.getBlockEntity(this.blockPos);
		
		if (this.blockPos != null && !world.isClient() && be != null && be instanceof TicketReturningBlockEntity) {
			final int tickets = (int) (Math.max(0, Math.ceil(amount - 1.5)) * 10);
			((TicketReturningBlockEntity) be).giveTickets(tickets);
			// TODO: Play a sound or smthn, as well as animations
			return true;
		}
		
		return false;
	}
	
	@Override
	public void tick() {
		if (!this.getWorld().isClient() && this.blockPos != null && (
			this.getWorld().getBlockEntity(this.blockPos) == null ||
			!(this.getWorld().getBlockEntity(this.blockPos) instanceof PunchGameBlockEntity)
		)) this.kill();
		
		super.tick();
	}

	@Override
	protected void initDataTracker(Builder builder) {
		builder.add(BLOCK_ENTITY_POS, BlockPos.ORIGIN);
	}

	@Override
	protected void readCustomDataFromNbt(NbtCompound nbt) {
		if (nbt.contains(NBT_KEY_POS, NbtElement.COMPOUND_TYPE))
			NbtHelper.toBlockPos(nbt, NBT_KEY_POS).filter(World::isValid).ifPresent(pos -> this.blockPos = pos);
	}

	@Override
	protected void writeCustomDataToNbt(NbtCompound nbt) {
		if (this.blockPos != null)
			nbt.put(NBT_KEY_POS, NbtHelper.fromBlockPos(this.blockPos));
	}

}
