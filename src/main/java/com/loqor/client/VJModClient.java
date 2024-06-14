package com.loqor.client;

import com.loqor.client.renderers.PunchGameBlockEntityRenderer;
import com.loqor.client.renderers.PunchGameEntityRenderer;
import com.loqor.client.renderers.TestBlockEntityRenderer;
import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.VJEntityTypes;

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

    public static void setupBlockEntityRendering() {
        BlockEntityRendererFactories.register(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, TestBlockEntityRenderer::new);
        BlockEntityRendererFactories.register(VJBlockEntityTypes.PUNCH_GAME, PunchGameBlockEntityRenderer::new);
    }
    
    public static void registerEntityRenderers() {
        EntityRendererRegistry.register(VJEntityTypes.PUNCH_GAME, PunchGameEntityRenderer::new);
    }
}
