package com.loqor.client;

import com.loqor.client.renderers.TestBlockEntityRendering;
import com.loqor.core.VJBlockEntityTypes;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(value = EnvType.CLIENT)
public class VJModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        setupBlockEntityRendering();
    }

    public void setupBlockEntityRendering() {
        BlockEntityRendererFactories.register(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, TestBlockEntityRendering::new);
    }
}
