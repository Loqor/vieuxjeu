package com.loqor.client;

import com.loqor.client.renderers.TestBlockEntityRenderer;
import com.loqor.core.VJBlockEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

public class VieuxJeuClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockEntityRenderers();
    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, TestBlockEntityRenderer::new);
    }
}
