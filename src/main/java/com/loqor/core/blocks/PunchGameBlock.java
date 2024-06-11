package com.loqor.core.blocks;

import com.loqor.VieuxJeu;
import com.loqor.core.blockentities.PunchGameBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class PunchGameBlock extends TallGameBlock.RequiresToken {
	
	public static final SoundEvent WIN_SOUND = SoundEvent.of(Identifier.of(VieuxJeu.MOD_ID, "punch_game_win"));

	public static final MapCodec<PunchGameBlock> CODEC = PunchGameBlock.createCodec(PunchGameBlock::new);
	@Override protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }

	public PunchGameBlock(Settings settings) {
		super(PunchGameBlockEntity::new, settings);
	}
	
	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		BlockEntity be = world.getBlockEntity(pos);
		if (be != null && be instanceof PunchGameBlockEntity) {
			PunchGameBlockEntity gameBlockEntity = (PunchGameBlockEntity) be;
			final int tickets = ((int) (gameBlockEntity.getScore() / 10) - 1) * 5;
			gameBlockEntity.giveTickets(tickets);
			world.playSound(null, pos, WIN_SOUND, SoundCategory.BLOCKS);
		}
		
		super.scheduledTick(state, world, pos, random);
	}
	
	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
	}
	
}
