package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;

public interface CanvasObject {

    void render(WorldRenderContext context, Canvas canvas);
    Vector2f getPosition();
    Identifier getTexture();


    static VertexConsumer vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, MatrixStack.Entry entry, float x, float y, int red, int green, int blue, int alpha, float u, float v, int light) {
        return vertexConsumer.vertex(positionMatrix, x, y, 0.0F).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0F, 1.0F, 0.0F);
    }


    /*
     * Make sure to push a matrixstack before this call and to pop a matrixstack after this call
     * */
    static void drawTextureInWorld(MatrixStack stack, VertexConsumerProvider provider, RenderLayer layer, int light) {
        drawTextureInWorld(stack, provider, layer, new Color(255, 255, 255, 255), light);
    }


    /*
     * Make sure to push a matrixstack before this call and to pop a matrixstack after this call
     * */
    static void drawTextureInWorld(MatrixStack stack, VertexConsumerProvider provider, RenderLayer layer, Color color, int light) {
        VertexConsumer vertexConsumer = provider.getBuffer(layer);
        MatrixStack.Entry entry = stack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        vertex(vertexConsumer, matrix4f, entry, -1F, -1F, color.getRed(), color.getGreen(), color.getBlue(),color.getAlpha(), 0, 1, light);
        vertex(vertexConsumer, matrix4f, entry, 1, -1F, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 1, 1, light);
        vertex(vertexConsumer, matrix4f, entry, 1, 1F, color.getRed(), color.getGreen(),color.getBlue(), color.getAlpha(), 1, 0, light);
        vertex(vertexConsumer, matrix4f, entry, -1F, 1F, color.getRed(), color.getGreen(),color.getBlue(), color.getAlpha(), 0, 0, light);
    }

}
