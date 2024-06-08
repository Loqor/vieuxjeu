package com.loqor.client;

import com.loqor.client.renderers.PunchGameEntityRenderer;
import com.loqor.client.renderers.TestBlockEntityRenderer;
import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.VJEntities;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.EntityRendererRegistry;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(value = EnvType.CLIENT)
public class VJModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        setupBlockEntityRendering();
        registerEntityRenderers();
    }

    public void setupBlockEntityRendering() {
        BlockEntityRendererFactories.register(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, TestBlockEntityRenderer::new);
    }
    
    public static final void registerEntityRenderers() {
        EntityRendererRegistry.register(VJEntities.PUNCH_GAME, PunchGameEntityRenderer::new);
    }
}
