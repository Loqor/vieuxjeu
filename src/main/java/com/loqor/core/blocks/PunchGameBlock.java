package com.loqor.core.blocks;

import com.loqor.core.VJEntityTypes;
import com.loqor.core.blockentities.PunchGameBlockEntity;
import com.loqor.core.entities.PunchGameEntity;
import com.mojang.serialization.MapCodec;

import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.block.BlockWithEntity;
import net.minecraft.block.ShapeContext;
import net.minecraft.entity.LivingEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.shape.VoxelShape;
import net.minecraft.world.BlockView;
import net.minecraft.world.World;

public class PunchGameBlock extends TallGameBlock {

	public static final MapCodec<PunchGameBlock> CODEC = PunchGameBlock.createCodec(PunchGameBlock::new);
	@Override protected MapCodec<? extends BlockWithEntity> getCodec() { return CODEC; }

	public PunchGameBlock(Settings settings) {
		super(PunchGameBlockEntity::new, settings);
	}
	
	@Override
	protected VoxelShape getOutlineShape(BlockState state, BlockView world, BlockPos pos, ShapeContext context) {
		
		return Block.createCuboidShape(0.0, 0.0, 0.0, 16.0, 16.0, 3.0);
	}
	
	@Override
	public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack) {
		super.onPlaced(world, pos, state, placer, itemStack);
		if (!world.isClient()) {
			PunchGameEntity entity = VJEntityTypes.PUNCH_GAME.create(world);
			final float offset = 2.0F - 2/16F - entity.getHeight(); // 2 blocks up, 2 pixels down
			entity.setPosition(pos.toBottomCenterPos().offset(Direction.UP, offset));
			entity.blockPos = pos;
			world.spawnEntity(entity);
		}
	}

}
