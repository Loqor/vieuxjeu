package com.loqor.core;

import com.loqor.VieuxJeu;
import com.loqor.core.blockentities.TestBlockEntity;
import net.fabricmc.fabric.api.object.builder.v1.block.entity.FabricBlockEntityTypeBuilder;
import net.minecraft.block.entity.BlockEntityType;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class VJBlockEntityTypes {

    public static void initialize() {}

    public static final BlockEntityType<TestBlockEntity> TEST_BLOCK_ENTITY_TYPE = Registry.register(
    Registries.BLOCK_ENTITY_TYPE, Identifier.of(VieuxJeu.MOD_ID, "test_block_entity"),
            BlockEntityType.Builder.create(TestBlockEntity::new, VJBlocks.TEST_BLOCK).build());
}
