package com.loqor.core.blocks;

import com.loqor.core.VJBlocks;
import com.loqor.core.blockentities.TestBlockEntity;
import com.loqor.core.rwguia.Canvas;
import com.loqor.core.rwguia.Sprite;
import com.mojang.serialization.MapCodec;
import net.minecraft.block.*;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class TestBlock extends BlockWithEntity {
    public static final MapCodec<TestBlock> CODEC = TestBlock.createCodec(TestBlock::new);
    public TestBlock(Settings settings) {
        super(settings);
    }

    @Override
    protected MapCodec<? extends BlockWithEntity> getCodec() {
        return CODEC;
    }

    @Nullable
    @Override
    public BlockEntity createBlockEntity(BlockPos pos, BlockState state) {
        return new TestBlockEntity(pos, state);
    }

    @Override
    protected BlockRenderType getRenderType(BlockState state) {
        return BlockRenderType.MODEL;
    }

    @Override
    protected ActionResult onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        if(world.getBlockEntity(pos) instanceof TestBlockEntity testBlockEntity) {
            testBlockEntity.onUse(state, world, pos, player, hit);
        }
        Canvas canvas = Canvas.create(pos.up(), Direction.EAST, 500, 500);
        Sprite testSprite = new Sprite(Identifier.of("stone"));
        testSprite.setPosition(new Vector2f(1, 1));
        canvas.addObject("test_object",testSprite);
        return super.onUse(state, world, pos, player, hit);
    }
}
