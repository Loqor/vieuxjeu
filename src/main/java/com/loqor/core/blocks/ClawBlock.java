package com.loqor.core.blocks;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.function.BiConsumer;

import org.apache.commons.lang3.function.TriConsumer;
import org.jetbrains.annotations.Nullable;

import com.loqor.core.blockentities.ClawBlockEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.Blocks;
import net.minecraft.block.InventoryProvider;
import net.minecraft.block.ShapeContext;
import net.minecraft.block.dispenser.ItemDispenserBehavior;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.inventory.SidedInventory;
import net.minecraft.item.ItemPlacementContext;
import net.minecraft.item.ItemStack;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.StateManager;
import net.minecraft.state.property.IntProperty;
import net.minecraft.util.Hand;
import net.minecraft.util.ItemActionResult;
import net.minecraft.util.ItemScatterer;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.random.Random;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.util.shape.VoxelShapes;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;
import net.minecraft.world.WorldAccess;
import net.minecraft.world.WorldEvents;

public class ClawBlock extends BlockWithEntity implements InventoryProvider {

	public static final MapCodec<ClawBlock> CODEC = ClawBlock.createCodec(ClawBlock::new);
	@Override protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }

	// 3 4
	// 1 2
	public static final IntProperty CLAW_PIECE = IntProperty.of("piece", 1, 4);

	public static final Map<Direction, VoxelShape> 
		TOP_SHAPES = Map.of(
			Direction.NORTH, VoxelShapes.union(Block.createCuboidShape(0, 0, 4, 16, 12, 16), Block.createCuboidShape(0, 12, 0, 16, 16, 16)),
			Direction.EAST,  VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 12, 12, 16), Block.createCuboidShape(0, 12, 0, 16, 16, 16)),
			Direction.SOUTH, VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 12, 12), Block.createCuboidShape(0, 12, 0, 16, 16, 16)),
			Direction.WEST,  VoxelShapes.union(Block.createCuboidShape(4, 0, 0, 16, 12, 16), Block.createCuboidShape(0, 12, 0, 16, 16, 16))
		),	
		BOTTOM_SHAPES = Map.of(
			Direction.NORTH, VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 12, 16), Block.createCuboidShape(0, 12, 4, 16, 16, 16)),
			Direction.EAST,  VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 12, 16), Block.createCuboidShape(0, 12, 0, 12, 16, 16)),
			Direction.SOUTH, VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 12, 16), Block.createCuboidShape(0, 12, 0, 16, 16, 12)),
			Direction.WEST,  VoxelShapes.union(Block.createCuboidShape(0, 0, 0, 16, 12, 16), Block.createCuboidShape(4, 12, 0, 16, 16, 16))
		);

	public ClawBlock(Settings settings) {
		super(settings);
		this.setDefaultState(this.stateManager.getDefaultState().with(HORIZONTAL_FACING, Direction.NORTH).with(CLAW_PIECE, 1));
	}

	// ==========================
	// Section: Setup Methods
	// ==========================

	@Override
	protected void appendProperties(StateManager.Builder<Block, BlockState> builder) {
		builder.add(HORIZONTAL_FACING, CLAW_PIECE);
	}

	@Override
	public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
		if (state.get(CLAW_PIECE) == 1) return new ClawBlockEntity(pos, state);
		return null;
	}

	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		switch (state.get(CLAW_PIECE)) {
			case 3: case 4: return TOP_SHAPES.get(state.get(HORIZONTAL_FACING));
			case 1: case 2: return BOTTOM_SHAPES.get(state.get(HORIZONTAL_FACING));
		}
		return VoxelShapes.empty();
	}

	// ==========================
	// Section: Helper Methods
	// ==========================

	@Nullable
	private ClawBlockEntity getBlockEntityIfBottom(BlockState state, WorldAccess world, BlockPos pos) {
		int piece = state.get(CLAW_PIECE);
		if (piece == 1 || piece == 2) {
			final BlockPos realPos;
			if (piece == 1) realPos = pos;
			else realPos = pos.offset(state.get(HORIZONTAL_FACING).rotateYClockwise());

			final BlockEntity be = world.getBlockEntity(realPos);
			if (be instanceof final ClawBlockEntity claw) return claw;
		}

		return null;
	}

	private Collection<BlockPos> getNeighborsPos(WorldAccess world, BlockPos pos, BlockState state, boolean onlyBlocks) {
		int part = state.get(CLAW_PIECE);
		Set<BlockPos> positions = new HashSet<>();

		TriConsumer<BlockPos, int[], Integer> addIfPart = (otherPos, offsets, otherPart) -> {
			BlockPos offsetPos = otherPos
					.offset(state.get(HORIZONTAL_FACING).rotateYCounterclockwise(), offsets[0])
					.offset(Direction.UP, offsets[1]);
			BlockState otherState = world.getBlockState(offsetPos);
			if (otherState.isOf(state.getBlock()) && (onlyBlocks || otherState.get(CLAW_PIECE) == otherPart)) positions.add(offsetPos);
		};

		// 3 4
		// 1 2
		switch (part) {
			case 1:
				addIfPart.accept(pos, new int[] { 0, 1}, 3);
				addIfPart.accept(pos, new int[] { 1, 1}, 4);
				addIfPart.accept(pos, new int[] { 1, 0}, 2);
				break;
			case 2:
				addIfPart.accept(pos, new int[] { 0, 1}, 4);
				addIfPart.accept(pos, new int[] {-1, 1}, 3);
				addIfPart.accept(pos, new int[] {-1, 0}, 1);
				break;
			case 3:
				addIfPart.accept(pos, new int[] { 0,-1}, 1);
				addIfPart.accept(pos, new int[] { 1,-1}, 2);
				addIfPart.accept(pos, new int[] { 1, 0}, 4);
				break;
			case 4:
				addIfPart.accept(pos, new int[] { 0,-1}, 2);
				addIfPart.accept(pos, new int[] {-1,-1}, 1);
				addIfPart.accept(pos, new int[] {-1, 0}, 3);
				break;
		}

		return positions;
	}

	// ==========================
	// Section: Game Mechanic Methods
	// ==========================

	@Override
	protected ItemActionResult onUseWithItem(ItemStack stack, BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
		if (getBlockEntityIfBottom(state, world, pos) instanceof final ClawBlockEntity claw &&
			claw.feedTokens(stack, player)) return ItemActionResult.success(world.isClient());

		return super.onUseWithItem(stack, state, world, pos, player, hand, hit);
	}

	/**
	 * This is used for providing the item when the grabbing animation is finished
	 * {@link ClawBlockEntity#activate()} will schedule a tick when activated
	 */
	@Override
	protected void scheduledTick(BlockState state, ServerWorld world, BlockPos pos, Random random) {
		super.scheduledTick(state, world, pos, random);

		final BlockEntity be = world.getBlockEntity(pos);
		if (be != null && be instanceof final ClawBlockEntity claw && claw.isActive()) {
			final Direction dir = state.getOrEmpty(HORIZONTAL_FACING).orElse(Direction.NORTH);
			ItemDispenserBehavior.spawnItem(world, claw.collectedItem, 6, dir, pos.toCenterPos().offset(dir, 0.8F));
			claw.deactivate();
		}
	}

	// ==========================
	// Section: Comparator Methods
	// ==========================

	@Override
	public SidedInventory getInventory(BlockState state, WorldAccess world, BlockPos pos) {
		if (getBlockEntityIfBottom(state, world, pos) instanceof final SidedInventory inventory) return inventory;
		return null;
	}

	@Override
	protected boolean hasComparatorOutput(BlockState state) {
		return state.get(CLAW_PIECE) == 1 || state.get(CLAW_PIECE) == 2;
	}

	@Override
	protected int getComparatorOutput(BlockState state, World world, BlockPos pos) {
		if (state.get(CLAW_PIECE) == 1)	world.updateComparators(pos.offset(state.get(HORIZONTAL_FACING).rotateYCounterclockwise()), this);
		return ScreenHandler.calculateComparatorOutput((BlockEntity) getBlockEntityIfBottom(state, world, pos));
	}

	// ==========================
	// Section: Multi-Block Methods
	// ==========================

	@Override @Nullable
	public BlockState getPlacementState(ItemPlacementContext ctx) {
		World world = ctx.getWorld();
		BlockPos pos = ctx.getBlockPos();
		Direction direction = ctx.getHorizontalPlayerFacing().getOpposite();

		if (
			!world.getBlockState(pos).isReplaceable() ||
			!world.getBlockState(pos.up()).isReplaceable() ||
			!world.getBlockState(pos.offset(direction.rotateYCounterclockwise(), 1)).isReplaceable() ||
			!world.getBlockState(pos.up().offset(direction.rotateYCounterclockwise(), 1)).isReplaceable()
		) return null;

		BlockState blockState = this.getDefaultState();

		return blockState.with(HORIZONTAL_FACING, direction);
	}

	@Override
	protected BlockState getStateForNeighborUpdate(BlockState state, Direction direction, BlockState neighborState, WorldAccess world, BlockPos pos, BlockPos neighborPos) {
		if (!neighborState.isOf(this) && getNeighborsPos(world, pos, state, true).size() < 3) return Blocks.AIR.getDefaultState();
		return super.getStateForNeighborUpdate(state, direction, neighborState, world, pos, neighborPos);
	}

	@Override
	protected void onStateReplaced(BlockState state, World world, BlockPos pos, BlockState newState, boolean moved) {
		ItemScatterer.onStateReplaced(state, newState, world, pos);
		super.onStateReplaced(state, world, pos, newState, moved);
	}

	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient()) {
			BiConsumer<BlockPos, Integer> addPart = (newPos, newPart) -> {
				world.setBlockState(newPos, state.with(CLAW_PIECE, newPart), 3);
				state.updateNeighbors(world, newPos, Block.NOTIFY_ALL);
			};

			Direction direction = state.get(HORIZONTAL_FACING);

			// 3 4
			// 1 2
			addPart.accept(pos.up(), 3);
			addPart.accept(pos.offset(direction.rotateYCounterclockwise(), 1), 2);
			addPart.accept(pos.up().offset(direction.rotateYCounterclockwise(), 1), 4);
		}
	}

	@Override
	public BlockState onBreak(World world, BlockPos pos, BlockState state, PlayerEntity player) {
		if (!world.isClient) {
			if (player.isCreative()) getNeighborsPos(world, pos, state, false).forEach(newPos -> {
				world.setBlockState(newPos, Blocks.AIR.getDefaultState(), Block.NOTIFY_ALL | Block.SKIP_DROPS);
				world.syncWorldEvent(player, WorldEvents.BLOCK_BROKEN, newPos, Block.getRawIdFromState(state));
			}); 
			else Block.dropStacks(state, world, pos, null, player, player.getMainHandStack());
		}
		return super.onBreak(world, pos, state, player);
	}

}
