package com.loqor.core;

import com.loqor.VieuxJeu;
import com.loqor.core.entities.PunchGameEntity;

import net.minecraft.entity.EntityType;
import net.minecraft.entity.SpawnGroup;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.util.Identifier;

public final class VJEntities {

	public static final void initialize() {}

	private static final <T extends EntityType<?>> T register(T entityType, String name) {
		return Registry.register(Registries.ENTITY_TYPE, Identifier.of(VieuxJeu.MOD_ID, name), entityType);
	}

	public static final EntityType<PunchGameEntity> PUNCH_GAME = VJEntities.register(EntityType.Builder.create(PunchGameEntity::new, SpawnGroup.MISC).dimensions(8/16F, 10/16F).build(), "punch_game");
}
