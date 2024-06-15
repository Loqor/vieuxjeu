package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.*;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Random;
import org.joml.Vector2f;

public class Sprite implements CanvasObject {

    private Identifier texture;
    private Vector2f position = new Vector2f(0, 0);
    private Vector2f scale = new Vector2f(1, 1);

    public Sprite(Identifier texture) {
        this.texture = texture;
    }


    @Override
    public Identifier getTexture() {
        return texture;
    }

    @Override
    public void tick(float tickDelta) {
        getPosition().x += 0.1f * tickDelta;
        if(getPosition().x >= 8) {
            getPosition().x = 0;
        }
    }

    @Override
    public void render(WorldRenderContext context, Canvas canvas) {
        context.matrixStack().push();
        context.matrixStack().translate(canvas.get2DPosition().getX() + getPosition().x,canvas.get2DPosition().getY() + getPosition().y, canvas.get2DPosition().getX() + getPosition().x);
        context.matrixStack().scale(15, 15, 15);
        CanvasObject.drawTextureInWorld(canvas, this, context.matrixStack(), context.consumers(), RenderLayer.getEndPortal(), 15728880);
        context.matrixStack().pop();
    }

    @Override
    public void render(MatrixStack stack, VertexConsumerProvider provider, @Nullable Canvas canvas) {
        stack.push();
        //USES BLOCKENTITY RENDERER, NO RELATIVE CANVAS POSITION NEEDED HERE
        stack.translate(getPosition().x + 1, getPosition().y, 0);
        //setScale(new Vector2f(new Random().nextFloat(), new Random().nextFloat()));
        stack.scale(getScale().x, getScale().y, getScale().x);
        CanvasObject.drawTextureInWorld(canvas, this, stack, provider, RenderLayer.getEntityCutout(getTexture()), 15728880);
        stack.pop();
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    public Vector2f getScale() {
        return scale;
    }

    public void setScale(Vector2f scale) {
        this.scale = scale;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void updateTexture(Identifier texture) {
        this.texture = texture;
    }
}
