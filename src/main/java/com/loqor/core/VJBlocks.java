package com.loqor.core;

import com.loqor.VieuxJeu;
import com.loqor.core.blocks.PunchGameBlock;
import com.loqor.core.blocks.TestBlock;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.Identifier;

public class VJBlocks {

    public static void initialize() {}

    public static <T extends Block> T register(T block, String name, boolean hasItem) {
        Identifier blockId = Identifier.of(VieuxJeu.MOD_ID, name);

        if (hasItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, blockId, blockItem);
        }

        return Registry.register(Registries.BLOCK, blockId, block);
    }

    public static final TestBlock TEST_BLOCK = register(new TestBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.ANVIL)), "test_block", true);
    public static final PunchGameBlock PUNCH_GAME = register(new PunchGameBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.ANVIL)), "punch_game", true);
}
