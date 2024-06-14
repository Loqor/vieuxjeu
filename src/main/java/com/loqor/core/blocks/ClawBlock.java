package com.loqor.core.blocks;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

import com.loqor.core.blockentities.ClawBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;

public class ClawBlock extends TallGameBlock.RequiresToken {

	public static final MapCodec<ClawBlock> CODEC = ClawBlock.createCodec(ClawBlock::new);
	@Override protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }
	
	public ClawBlock(Settings settings) {
		super(ClawBlockEntity::new, settings);
	}
	
    @Override
    protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
        ItemScatterer.onStateReplaced(state, newState, world, pos);
        super.onStateReplaced(state, world, pos, newState, moved);
    }
	
	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);

		final BlockEntity be = world.getBlockEntity(pos);
		if (be != null && be instanceof ClawBlockEntity) {
			ClawBlockEntity claw = (ClawBlockEntity) be;
			if (claw.isActive()) {
				final Direction dir = state.getOrEmpty(HORIZONTAL_FACING).orElse(Direction.NORTH);
				ItemDispenserBehavior.spawnItem(world, claw.collectedItem, 6, dir, pos.toCenterPos().offset(dir, 0.8F));
				claw.deactivate();
			}
		}
	}
}
