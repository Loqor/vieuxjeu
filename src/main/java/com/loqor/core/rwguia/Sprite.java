package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix3f;
import org.joml.Matrix4f;
import org.joml.Vector2f;

import java.awt.*;

public class Sprite implements CanvasObject {

    private Identifier texture;
    private Vector2f position;

    public Sprite(Identifier texture) {
        this.texture = texture;
    }


    @Override
    public Identifier getTexture() {
        return texture;
    }

    @Override
    public void render(WorldRenderContext context, Canvas canvas) {
        context.matrixStack().push();
        context.matrixStack().translate(canvas.get2DPosition().getX() + getPosition().x,canvas.get2DPosition().getY() + getPosition().y, canvas.get2DPosition().getX() + getPosition().x);
        context.matrixStack().scale(15, 15, 15);
        CanvasObject.drawTextureInWorld(canvas, context.matrixStack(), context.consumers(), RenderLayer.getEndPortal(), 15728880);
        context.matrixStack().pop();
    }

    @Override
    public void render(MatrixStack stack, VertexConsumerProvider provider, @Nullable Canvas canvas) {
        CanvasObject.drawTextureInWorld(canvas, stack, provider, RenderLayer.getEntityCutout(getTexture()), 15728880);
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void updateTexture(Identifier texture) {
        this.texture = texture;
    }
}
