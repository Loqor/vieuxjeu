package com.loqor.core.blocks;

import com.loqor.core.VJSoundEvents;
import com.loqor.core.blockentities.PunchGameBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.damage.DamageSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;

public class PunchGameBlock extends TallGameBlock.RequiresToken {
	
	public static final MapCodec<PunchGameBlock> CODEC = PunchGameBlock.createCodec(PunchGameBlock::new);
	@Override protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }

	public PunchGameBlock(Settings settings) {
		super(PunchGameBlockEntity::new, settings);
	}
	
	/**
	 * This is used for providing the game tickets based on the BlockEntity's score
	 * {@link PunchGameEntity#damage(DamageSource,float)} will schedule a tick when the punching bag is hit
	 */
	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		final BlockEntity be = world.getBlockEntity(pos);
		if (be != null && be instanceof PunchGameBlockEntity) {
			final PunchGameBlockEntity gameBlockEntity = (PunchGameBlockEntity) be;
			// Tickets are ((score / 10 - 1) rounded down) * 5 
			// This means empty-handed crits can win by random chance, giving the minimum score providing 5 tickets
			// https://www.desmos.com/calculator/v18vwzauwk
			final int tickets = ((int) (gameBlockEntity.getScore() / 10) - 1) * 5;
			
			if (tickets > 0) {
				gameBlockEntity.giveTickets(tickets);
				world.playSound(null, pos, VJSoundEvents.PUNCH_GAME_WIN, SoundCategory.BLOCKS);
			}
		}
		
		super.scheduledTick(state, world, pos, random);
	}
	
	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		// TODO: Map to real shape when assets come in
		return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
	}
	
}
