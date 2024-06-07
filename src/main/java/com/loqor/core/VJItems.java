package com.loqor.core;

import com.loqor.VieuxJeu;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.util.Identifier;

public class VJItems {

    public static void initialize() {
        var groupRegKey = RegistryKey.of(Registries.ITEM_GROUP.getKey(), Identifier.of(VieuxJeu.MOD_ID, "test"));
        ItemGroupEvents.modifyEntriesEvent(groupRegKey).register(entries -> {
                entries.add(TEST);
        });
    }

    public static <T extends Item> T register(T item, String name) {
        Identifier itemId = Identifier.of(VieuxJeu.MOD_ID, name);
        return Registry.register(Registries.ITEM, itemId, item);
    }

    public static final Item TEST = register(new Item(new Item.Settings()), "test");

}
