package com.loqor.core;

import com.loqor.VieuxJeu;
import com.loqor.core.blockentities.ClawBlockEntity;
import com.loqor.core.blockentities.PunchGameBlockEntity;
import com.loqor.core.blockentities.TestBlockEntity;

import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class VJBlockEntityTypes {

    public static void initialize() {}

    public static final BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY_TYPE = Registry.register(
    Registries.BLOCK_ENTITY_TYPE, Identifier.of(VieuxJeu.MOD_ID, "test_block_entity"),
            BlockEntityType.Builder.create(TestBlockEntity::new, VJBlocks.TEST_BLOCK).build());
    
    public static final BlockEntityType<PunchGameBlockEntity> PUNCH_GAME = Registry.register(
    Registries.BLOCK_ENTITY_TYPE, Identifier.of(VieuxJeu.MOD_ID, "punch_game"),
            BlockEntityType.Builder.create(PunchGameBlockEntity::new, VJBlocks.PUNCH_GAME).build());
    
    public static final BlockEntityType<ClawBlockEntity> CLAW = Registry.register(
    Registries.BLOCK_ENTITY_TYPE, Identifier.of(VieuxJeu.MOD_ID, "claw"),
            BlockEntityType.Builder.create(ClawBlockEntity::new, VJBlocks.CLAW).build());

}
