package com.loqor.core.blockentities;

import com.loqor.core.VJBlockEntityTypes;
import net.minecraft.block.BlockState;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

public class TestBlockEntity extends BlockEntity {
    public TestBlockEntity(BlockPos pos, BlockState state) {
        super(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, pos, state);
    }

    public void onUse(BlockState state, World world, BlockPos pos, PlayerEntity player, BlockHitResult hit) {
        player.sendMessage(Text.literal("What the fuck"), false);
        world.playSound(null, pos, SoundEvents.MUSIC_DISC_CREATOR_MUSIC_BOX.value(), SoundCategory.BLOCKS, 1.0F, 1.0F);
    }
}
