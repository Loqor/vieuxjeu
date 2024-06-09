package com.loqor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.VJBlocks;
import com.loqor.core.VJEntityTypes;
import com.loqor.core.VJItems;
import com.loqor.core.entities.PunchGameEntity;

import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;

public class VieuxJeu implements ModInitializer {
	public static final String MOD_ID = "vieuxjeu";
	public static final Logger VJ_LOGGER = LoggerFactory.getLogger("vieuxjeu");

	@Override
	public void onInitialize() {
		VJBlocks.initialize();
		VJItems.initialize();
		VJBlockEntityTypes.initialize();
		VJEntityTypes.initialize();
		
		FabricDefaultAttributeRegistry.register(VJEntityTypes.PUNCH_GAME, PunchGameEntity.createLivingAttributes());
	}
}