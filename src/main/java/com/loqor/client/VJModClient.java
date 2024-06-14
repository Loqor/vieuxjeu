package com.loqor.client;

import com.loqor.client.renderers.TestBlockEntityRenderer;
import com.loqor.core.VJBlockEntityTypes;
import com.loqor.core.rwguia.Canvas;
import com.loqor.core.rwguia.CanvasRegistry;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;

@Environment(value = EnvType.CLIENT)
public class VJModClient implements ClientModInitializer {

    @Override
    public void onInitializeClient() {
        setupBlockEntityRendering();

        WorldRenderEvents.END.register(context -> {
            for (Canvas canvase : CanvasRegistry.getCanvases()) {
                canvase.render(context);
            }
        });
    }

    public void setupBlockEntityRendering() {
        BlockEntityRendererFactories.register(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, TestBlockEntityRenderer::new);
    }
}
