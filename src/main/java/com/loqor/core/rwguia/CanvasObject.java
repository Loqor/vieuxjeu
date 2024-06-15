package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;

public interface CanvasObject {

    void render(WorldRenderContext context, Canvas canvas);

    void render(MatrixStack stack, VertexConsumerProvider provider, @Nullable Canvas canvas);

    Vector2f getPosition();

    Identifier getTexture();

    void tick(float tickDelta);

    Vector2f getScale();

    static VertexConsumer vertex(VertexConsumer vertexConsumer, Matrix4f positionMatrix, MatrixStack.Entry entry, float x, float y, int red, int green, int blue, int alpha, float u, float v, int light) {
        return vertexConsumer.vertex(positionMatrix, x, y, 0.0F).color(red, green, blue, alpha).texture(u, v).overlay(OverlayTexture.DEFAULT_UV).light(light).normal(entry, 0.0F, 1.0F, 0.0F);
    }


    /*
     * Make sure to push a matrixstack before this call and to pop a matrixstack after this call
     * */
    static void drawTextureInWorld(Canvas canvas, CanvasObject object, MatrixStack stack, VertexConsumerProvider provider, RenderLayer layer, int light) {
        drawTextureInWorld(canvas, object, stack, provider, layer, new Color(255, 255, 255, 255), light);
    }


    /*
     * Make sure to push a matrixstack before this call and to pop a matrixstack after this call
     * */
    static void drawTextureInWorld(Canvas canvas, CanvasObject object, MatrixStack stack, VertexConsumerProvider provider, RenderLayer layer, Color color, int light) {
        VertexConsumer vertexConsumer = provider.getBuffer(RenderLayer.getEndPortal());
        MatrixStack.Entry entry = stack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        float posX = 1f;
        if (object.getPosition().x + object.getScale().x * 2f >= canvas.dimensions.getX()) {
            posX = 1f - (((object.getPosition().x) / (canvas.dimensions.getX()) * 2f);
            //System.out.println(object.getPosition().x + "::" + canvas.dimensions.getX() + "::" + posX);
        }
        vertex(vertexConsumer, matrix4f, entry, -1F, -1f, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 0, 1, light);
        vertex(vertexConsumer, matrix4f, entry, posX, -1f, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 1, 1, light);
        vertex(vertexConsumer, matrix4f, entry, posX, 1f, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 1, 0, light);
        vertex(vertexConsumer, matrix4f, entry, -1F, 1f, color.getRed(), color.getGreen(), color.getBlue(), color.getAlpha(), 0, 0, light);
    }

}
