package com.loqor.core.blockentities;

import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.rwguia.Canvas;
import com.loqor.core.rwguia.CanvasObject;
import com.loqor.core.rwguia.Sprite;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class TestBlockEntity extends BlockEntity implements CanvasBlockEntity {
    Canvas canvas;

    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, pos, state);
        canvas = Canvas.create(new BlockPos(5, -60, 5), Direction.EAST, 5f, 10);
        Sprite testSprite = new Sprite(Identifier.of("stone"));
        testSprite.setPosition(new Vector2f(1, 1));
        canvas.addObject("test_object", testSprite);
    }

    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        player.sendMessage(Text.literal("What the fuck"), false);
        world.playSound(null, pos, SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX.value(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }


    @Override
    public Canvas getCanvas() {
        return canvas;
    }
}
