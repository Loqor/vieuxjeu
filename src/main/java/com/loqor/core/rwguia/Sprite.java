package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
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
    public void render(WorldRenderContext context) {
        CanvasObject.drawTextureInWorld(context.matrixStack(), context.consumers(), RenderLayer.getEntityCutout(getTexture()), 15728880);
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
