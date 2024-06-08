package com.loqor.core.blockentities;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

import gay.lemmaeof.terrifictickets.TerrificTickets;
import net.minecraft.block.BlockState;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;

public abstract class TicketReturningBlockEntity extends BlockEntity {

	public TicketReturningBlockEntity(BlockEntityType<? extends TicketReturningBlockEntity> type, BlockPos pos, BlockState state) {
		super(type, pos, state);
	}

	public void giveTickets(int amount) {
		BlockState state = getCachedState();
		if (state == null) return;
		Direction dir = state.getOrEmpty(HORIZONTAL_FACING).orElse(Direction.NORTH);
		ItemDispenserBehavior.spawnItem(world, new ItemStack(TerrificTickets.TICKET, amount), 6, dir, pos.toCenterPos().offset(dir, 0.8F));
	}

}
