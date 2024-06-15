package com.loqor.core;

import com.loqor.VieuxJeu;
import com.loqor.core.blocks.ClawBlock;
import com.loqor.core.blocks.PunchGameBlock;
import com.loqor.core.blocks.TallGameBlock;
import com.loqor.core.blocks.TestBlock;
import com.loqor.datagen.GameBlockDataGenProvider;
import com.loqor.datagen.TallBlockDataGenProvider;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.block.FabricBlockSettings;
import net.minecraft.block.AbstractBlock;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.sound.BlockSoundGroup;
import net.minecraft.util.DyeColor;
import net.minecraft.util.Identifier;

public class VJBlocks {

    public static void initialize() {}

    public static <T extends Block> T register(T block, String name, boolean hasItem) {
        Identifier blockId = Identifier.of(VieuxJeu.MOD_ID, name);

        if (hasItem) {
            BlockItem blockItem = new BlockItem(block, new Item.Settings());
            Registry.register(Registries.ITEM, blockId, blockItem);
            ItemGroupEvents.modifyEntriesEvent(ItemGroups.REDSTONE).register(entries -> {
                entries.add(block.asItem());
            });
        }
        
        if (block instanceof TallGameBlock) TallBlockDataGenProvider.TWO_TALL_BLOCKS.add(block);
        GameBlockDataGenProvider.GAME_BLOCKS.add(block);
        
        return Registry.register(Registries.BLOCK, blockId, block);
    }

    public static final TestBlock TEST_BLOCK = register(new TestBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.ANVIL)), "test_block", true);
    public static final PunchGameBlock PUNCH_GAME = register(new PunchGameBlock(AbstractBlock.Settings.create().sounds(BlockSoundGroup.ANVIL)), "punch_game", true);
	
    public static final ClawBlock CLAW = register(new ClawBlock(AbstractBlock.Settings.create()
    		.mapColor(DyeColor.LIGHT_GRAY)
    		.strength(5.0f, 6.0f)
    		.requiresTool()
    		.sounds(BlockSoundGroup.METAL)
			.pistonBehavior(PistonBehavior.IGNORE)
		), "claw_game", true);
}
