package com.loqor.datagen;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.loqor.VieuxJeu;
import com.loqor.core.VJBlocks;

import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricModelProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricTagProvider;
import net.minecraft.advancement.Advancement;
import net.minecraft.advancement.AdvancementEntry;
import net.minecraft.advancement.criterion.InventoryChangedCriterion;
import net.minecraft.advancement.criterion.RecipeUnlockedCriterion;
import net.minecraft.block.Block;
import net.minecraft.data.client.BlockStateModelGenerator;
import net.minecraft.data.client.ItemModelGenerator;
import net.minecraft.data.client.Models;
import net.minecraft.item.Items;
import net.minecraft.registry.Registries;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.RegistryWrapper.WrapperLookup;
import net.minecraft.registry.tag.BlockTags;
import net.minecraft.util.Identifier;

/**
 * This is used for our generic game consoles to
 * <br> 1a. Datagen a base block model for particles
 * <br> 1b. Datagen a generated item model
 * <br> 2. Add related tags
 * <br> 3. Add an advancement to unlock the recipe -- based on having redstone
 */
public final class GameBlockDataGenProvider {
	
	/**
	 * {@linkplain VJBlocks#register(Block, String, boolean)} will set all the blocks because that's all this mod is
	 */
	public static final List<Block> GAME_BLOCKS = new ArrayList<>();
	
	public static final void initialize(FabricDataGenerator.Pack pack) {	
		pack.addProvider(ModelProvider::new);
		pack.addProvider(BlockTagProvider::new);
		pack.addProvider(AdvancementProvider::new);
	}
	
	private static final class ModelProvider extends FabricModelProvider {
		private ModelProvider(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
			for (Block block : GAME_BLOCKS) blockStateModelGenerator.registerSimpleCubeAll(block);
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			for (Block block : GAME_BLOCKS) itemModelGenerator.register(block.asItem(), Models.GENERATED);
		}
	}

	private static final class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
		private BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(WrapperLookup arg) {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).setReplace(false).add(GAME_BLOCKS.toArray(new Block[] {}));
			getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).setReplace(false).add(GAME_BLOCKS.toArray(new Block[] {}));
		}
	}
	
	private static final class AdvancementProvider extends FabricAdvancementProvider {
		private AdvancementProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
			super(output, registryLookup);
		}

		@Override
		public void generateAdvancement(WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
			for (Block block : GAME_BLOCKS)
				Advancement.Builder.create()
					.parent(new AdvancementEntry(Identifier.of("recipes/root"), null))
					.criterion("has_redstone", InventoryChangedCriterion.Conditions.items(Items.REDSTONE))
					.criterion("has_recipe", RecipeUnlockedCriterion.create(Registries.BLOCK.getId(block)))
					.build(consumer, Identifier.of(VieuxJeu.MOD_ID, "recipes/" + Registries.BLOCK.getId(block).getPath()).toString());
		}
	}
	
}