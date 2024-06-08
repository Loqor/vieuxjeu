package com.loqor.core.blocks;

import static net.minecraft.state.property.Properties.DOUBLE_BLOCK_HALF;
import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

import java.util.function.BiFunction;

import org.jetbrains.annotations.Nullable;

import com.loqor.core.blockentities.TicketReturningBlockEntity;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.enums.DoubleBlockHalf;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.state.StateManager;
import net.minecraft.util.ActionResult;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;
import net.minecraft.world.WorldView;

public abstract class TallGameBlock extends BlockWithEntity {

	BiFunction<BlockPos, BlockState, TicketReturningBlockEntity> createBlockEntity;
	
	public TallGameBlock(BiFunction<BlockPos, BlockState, TicketReturningBlockEntity> createBlockEntity, Settings settings) {
		super(settings);
		this.createBlockEntity = createBlockEntity;
		this.setDefaultState(this.stateManager.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(DOUBLE_BLOCK_HALF, DoubleBlockHalf.LOWER));
	}
	
	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		if (state.get(DOUBLE_BLOCK_HALF).equals(DoubleBlockHalf.UPPER)) return null;
		return createBlockEntity.apply(pos, state);
	}
	
	// Yoinkied from the tall plant with tweaks
	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		DoubleBlockHalf doubleBlockHalf = state.get(DOUBLE_BLOCK_HALF);
		if (!(direction.getAxis() != Direction.Axis.Y || doubleBlockHalf == DoubleBlockHalf.LOWER != (direction == Direction.UP) || neighborState.isOf(this) && neighborState.get(DOUBLE_BLOCK_HALF) != doubleBlockHalf))
			return Blocks.AIR.getDefaultState();
		if (doubleBlockHalf == DoubleBlockHalf.LOWER && direction == Direction.DOWN && !state.canPlaceAt(world, pos))
			return Blocks.AIR.getDefaultState();
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	@Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		BlockPos blockPos = ctx.getBlockPos();
		World world = ctx.getWorld();
		if (blockPos.getY() < world.getTopY() - 1 && world.getBlockState(blockPos.up()).canReplace(ctx))
			return super.getPlacementState(ctx).with(HORIZONTAL_FACING, ctx.getHorizontalPlayerFacing().getOpposite());
		return null;
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		world.setBlockState(pos.up(), this.getDefaultState().with(DOUBLE_BLOCK_HALF, DoubleBlockHalf.UPPER), Block.NOTIFY_ALL);
	}

	@Override
	protected boolean canPlaceAt(BlockState state, WorldView world, BlockPos pos) {
		if (state.get(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.UPPER) {
			BlockState blockState = world.getBlockState(pos.down());
			return blockState.isOf(this) && blockState.get(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER;
		}
		return super.canPlaceAt(state, world, pos);
	}

	public static void placeAt(WorldAccess world, BlockState state, BlockPos pos, int flags) {
		world.setBlockState(pos, state, flags);
		world.setBlockState(pos.up(), state, flags);
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (player.isCreative()) TallGameBlock.onBreakInCreative(world, pos, state, player);
			else TallGameBlock.dropStacks(state, world, pos, null, player, player.getMainHandStack());
		}
		return super.onBreak(world, pos, state, player);
	}

	@Override
	public void afterBreak(World world, PlayerEntity player, BlockPos pos, BlockState state, @Nullable BlockEntity blockEntity, ItemStack tool) {
		super.afterBreak(world, player, pos, Blocks.AIR.getDefaultState(), blockEntity, tool);
	}

	protected static void onBreakInCreative(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		BlockPos blockPos = pos.down();
		BlockState blockState = world.getBlockState(blockPos);
		DoubleBlockHalf doubleBlockHalf = state.get(DOUBLE_BLOCK_HALF);
		if (doubleBlockHalf == DoubleBlockHalf.UPPER && blockState.isOf(state.getBlock()) && blockState.get(DOUBLE_BLOCK_HALF) == DoubleBlockHalf.LOWER) {
			world.setBlockState(blockPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
			world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, blockPos, Block.getRawIdFromState(blockState));
		}
	}

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, DOUBLE_BLOCK_HALF);
	}

}
