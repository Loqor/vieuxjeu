package com.loqor.core.blockentities;

import com.loqor.core.rwguia.Canvas;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;

public interface CanvasBlockEntity {
    Canvas getCanvas();
    default void renderCanvas(MatrixStack stack, VertexConsumerProvider vertexConsumerProvider) {
        getCanvas().render(stack, vertexConsumerProvider);
    }

    default void tickCanvas(float tickDelta) {
        getCanvas().tick(tickDelta);
    }
}
