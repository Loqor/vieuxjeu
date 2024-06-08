package com.loqor;

import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.VJBlocks;
import com.loqor.core.VJItems;
import net.fabricmc.api.ModInitializer;

import net.fabricmc.fabric.api.itemgroup.v1.FabricItemGroup;
import net.minecraft.item.ItemGroup;
import net.minecraft.item.ItemStack;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class VieuxJeu implements ModInitializer {
	public static final String MOD_ID = "vieuxjeu";
	public static final Logger VJ_LOGGER = LoggerFactory.getLogger("vieuxjeu");

	@Override
	public void onInitialize() {
		VJBlocks.initialize();
		VJItems.initialize();
		VJBlockEntityTypes.initialize();
	}
}