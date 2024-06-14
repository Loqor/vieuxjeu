package com.loqor.core;

import com.loqor.VieuxJeu;
import com.loqor.core.entities.CameraEntity;
import com.loqor.core.entities.PunchGameEntity;
import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricDefaultAttributeRegistry;
import net.fabricmc.fabric.api.object.builder.v1.entity.FabricEntityTypeBuilder;
import net.minecraft.entity.EntityDimensions;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public class VJEntityTypes {

    public static void initialize() {
    }

	public static final EntityType<PunchGameEntity> PUNCH_GAME = Registry.register(
        Registries.ENTITY_TYPE,
        Identifier.of(VieuxJeu.MOD_ID, "punch_game"),
        EntityType.Builder.create(PunchGameEntity::new, SpawnGroup.MISC).dimensions(8/16F, 10/16F).build());

    // apparently not needed to register

    /*public static final EntityType<?> CAMERA_ENTITY_TYPE = Registry.register(
            Registries.ENTITY_TYPE,
            Identifier.of(VieuxJeu.MOD_ID, "camera_entity"),
            EntityType.Builder.create(CameraEntity::new, SpawnGroup.MISC).dimensions(0.75f, 0.75f).build()
    );*/

}
