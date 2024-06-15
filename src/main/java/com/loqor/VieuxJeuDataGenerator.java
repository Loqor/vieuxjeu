package com.loqor;

import com.loqor.datagen.GameBlockDataGenProvider;
import com.loqor.datagen.TallBlockDataGenProvider;

import net.fabricmc.fabric.api.datagen.v1.DataGeneratorEntrypoint;
import net.fabricmc.fabric.api.datagen.v1.FabricDataGenerator;

public class VieuxJeuDataGenerator implements DataGeneratorEntrypoint {

	@Override
	public void onInitializeDataGenerator(FabricDataGenerator fabricDataGenerator) {
		FabricDataGenerator.Pack pack = fabricDataGenerator.createPack();

		GameBlockDataGenProvider.initialize(pack);
		TallBlockDataGenProvider.initialize(pack);
	}

}
