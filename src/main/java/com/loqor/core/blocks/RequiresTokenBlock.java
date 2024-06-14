package com.loqor.core.blocks;

import java.util.function.BiFunction;

import com.loqor.core.blockentities.TakesTokens;

import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public abstract class RequiresTokenBlock<T extends BlockEntity & TakesTokens> extends TallHorizontalBlockWithEntity<T> {
	
	public RequiresTokenBlock(BiFunction<BlockPos, BlockState, T> createBlockEntity, Settings settings) {
		super(createBlockEntity, settings);
	}

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		final BlockEntity be = world.getBlockEntity(pos);
		if (be instanceof TakesTokens) {
			final TakesTokens gameBlockEntity = (TakesTokens) be;
			if (gameBlockEntity.feedTokens(stack, player)) return ItemActionResult.success(world.isClient());
		}
		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

}