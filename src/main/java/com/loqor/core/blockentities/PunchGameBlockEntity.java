package com.loqor.core.blockentities;

import com.loqor.core.VJBlockEntityTypes;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PunchGameBlockEntity extends BlockEntity {

	public PunchGameBlockEntity(BlockPos pos, BlockState state) {
		super(VJBlockEntityTypes.PUNCH_GAME, pos, state);
	}
	
	public void giveTickets(int amount) {
		
	}

}
