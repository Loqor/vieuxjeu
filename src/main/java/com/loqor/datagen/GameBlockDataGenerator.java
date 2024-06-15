package com.loqor.datagen;

import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

import com.loqor.VieuxJeu;
import com.loqor.core.VJBlocks;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;
import net.fabricmc.fabric.api.datagen.v1.FabricDataOutput;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricAdvancementProvider;
import net.fabricmc.fabric.api.datagen.v1.provider.FabricBlockLootTableProvider;
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
 * <br> 1. Add a plain loottable
 * <br> 2a. Datagen a base block model for particles
 * <br> 2b. Datagen a generated item model
 * <br> 3. Add related tags
 * <br> 4. Add an advancement to unlock the recipe -- based on having redstone
 */
public final class GameBlockDataGenerator implements DataGeneratorEntrypoint {
	
	private static final Block[] GAMES = {
		VJBlocks.PUNCH_GAME,
		VJBlocks.CLAW,
	};
	
	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();
		
		pack.addProvider(BlockLootTableProvider::new);
		pack.addProvider(ModelProvider::new);
		pack.addProvider(BlockTagProvider::new);
		pack.addProvider(AdvancementProvider::new);
	}

	private static final class BlockLootTableProvider extends FabricBlockLootTableProvider {
		private BlockLootTableProvider(FabricDataOutput dataOutput, CompletableFuture<RegistryWrapper.WrapperLookup> registryLookup) {
			super(dataOutput, registryLookup);
		}

		@Override
		public void generate() {
			for (Block block : GAMES) addDrop(block);
		}
	}
	
	private static final class ModelProvider extends FabricModelProvider {
		private ModelProvider(FabricDataOutput generator) {
			super(generator);
		}

		@Override
		public void generateBlockStateModels(BlockStateModelGenerator blockStateModelGenerator) {
			for (Block block : GAMES) blockStateModelGenerator.registerSimpleCubeAll(block);
		}

		@Override
		public void generateItemModels(ItemModelGenerator itemModelGenerator) {
			for (Block block : GAMES) itemModelGenerator.register(block.asItem(), Models.GENERATED);
		}
	}

	private static final class BlockTagProvider extends FabricTagProvider.BlockTagProvider {
		private BlockTagProvider(FabricDataOutput output, CompletableFuture<RegistryWrapper.WrapperLookup> registriesFuture) {
			super(output, registriesFuture);
		}

		@Override
		protected void configure(WrapperLookup arg) {
			getOrCreateTagBuilder(BlockTags.PICKAXE_MINEABLE).setReplace(false).add(GAMES);
			getOrCreateTagBuilder(BlockTags.NEEDS_STONE_TOOL).setReplace(false).add(GAMES);
		}
	}
	
	private static final class AdvancementProvider extends FabricAdvancementProvider {
		private AdvancementProvider(FabricDataOutput output, CompletableFuture<WrapperLookup> registryLookup) {
			super(output, registryLookup);
		}

		@Override
		public void generateAdvancement(WrapperLookup registryLookup, Consumer<AdvancementEntry> consumer) {
			System.out.println(Identifier.of(VieuxJeu.MOD_ID, "recipes/" + Registries.BLOCK.getId(GAMES[0]).getPath()).toString());
			for (Block block : GAMES)
				Advancement.Builder.create()
					.parent(new AdvancementEntry(Identifier.of("recipes/root"), null))
					.criterion("has_redstone", InventoryChangedCriterion.Conditions.items(Items.REDSTONE))
					.criterion("has_recipe", RecipeUnlockedCriterion.create(Registries.BLOCK.getId(block)))
					.build(consumer, Identifier.of(VieuxJeu.MOD_ID, "recipes/" + Registries.BLOCK.getId(block).getPath()).toString());
		}
	}
}