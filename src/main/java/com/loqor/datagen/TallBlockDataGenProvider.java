package com.loqor.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.loqor.core.VJBlocks;
import com.loqor.core.blocks.TallGameBlock;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
import net.minecraft.block.Block;
import net.minecraft.registry.RegistryWrapper;

/**
 * This is used for creating loot tables for our 1x2 blocks
 */
public final class TallBlockDataGenProvider {

	/**
	 * {@linkplain VJBlocks#register(Block, String, boolean)} will set blocks extending {@linkplain TallGameBlock}
	 */
	public static final List<Block> TWO_TALL_BLOCKS = new ArrayList<>();

	public static final void initialize(FabricDataGenerator.Pack pack) {
		pack.addProvider(LootTableProvider::new);
	}

	private static final class LootTableProvider extends FabricBlockLootTableProvider {
		private LootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generate() {
			for (Block block : TWO_TALL_BLOCKS) addDrop(block, doorDrops(block));
		}
	}
	
}
