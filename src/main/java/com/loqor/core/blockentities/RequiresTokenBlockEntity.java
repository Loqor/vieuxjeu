package com.loqor.core.blockentities;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.util.math.BlockPos;

public abstract class RequiresTokenBlockEntity extends BlockEntity implements TakesTokens {
	
	private final int neededTokens;
	private int currentTokens = 0;
	private boolean active = false;
	
	public RequiresTokenBlockEntity(BlockEntityType<? extends RequiresTokenBlockEntity> type, BlockPos pos, BlockState state) {
		this(type, pos, state, 1);
	}
	
	public RequiresTokenBlockEntity(BlockEntityType<? extends RequiresTokenBlockEntity> type, BlockPos pos, BlockState state, int neededTokens) {
		super(type, pos, state);
		this.neededTokens = Math.max(0, neededTokens);
	}
	
	@Override public boolean isActive() { return this.active; }
	@Override public void setActive(boolean active) { this.active = active; }
	@Override public int getNeededTokens() { return this.neededTokens; }
	@Override public int getCurrentTokens() { return this.currentTokens; }
	@Override public void setCurrentTokens(int tokens) { this.currentTokens = tokens; }
	
	@Override
	protected void writeNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.writeNbt(nbt, registryLookup);
		TakesTokens.super.iWriteNbt(nbt, registryLookup);
	}
	
	@Override
	protected void readNbt(NbtCompound nbt, WrapperLookup registryLookup) {
		super.readNbt(nbt, registryLookup);
		TakesTokens.super.iReadNbt(nbt, registryLookup);
	}

}