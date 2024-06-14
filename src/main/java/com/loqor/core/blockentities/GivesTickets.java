package com.loqor.core.blockentities;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

import gay.lemmaeof.terrifictickets.TerrificTickets;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;

public interface GivesTickets {
	
	public BlockState getCachedState();
	public BlockPos getPos();
	public World getWorld();
	
	public default void giveTickets(int amount) {
		final BlockState state = getCachedState();
		if (state == null) return;
		final Direction dir = state.getOrEmpty(HORIZONTAL_FACING).orElse(Direction.NORTH);
		ItemDispenserBehavior.spawnItem(getWorld(), new ItemStack(TerrificTickets.TICKET, amount), 6, dir, getPos().toCenterPos().offset(dir, 0.8F));
	}

}
