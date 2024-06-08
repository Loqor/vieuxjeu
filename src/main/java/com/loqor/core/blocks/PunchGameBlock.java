package com.loqor.core.blocks;

import com.loqor.core.blockentities.PunchGameBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.util.math.BlockPos;

public class PunchGameBlock extends BlockWithEntity {

	public static final MapCodec<PunchGameBlock> CODEC = PunchGameBlock.createCodec(PunchGameBlock::new);

	public PunchGameBlock(Settings settings) {
		super(settings);
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos var1, BlockState var2) {
		return new PunchGameBlockEntity(var1, var2);
	}

	@Override
	protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }

}
