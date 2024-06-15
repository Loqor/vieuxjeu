package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.joml.Vector2f;

public class Text implements CanvasObject {

    private Vector2f position;
    private net.minecraft.text.Text text = net.minecraft.text.Text.literal("");

    public Text(net.minecraft.text.Text text, Vector2f position) {
        this.text = text;
        this.position = position;
    }

    @Override
    public void render(WorldRenderContext context, Canvas canvas) {
        MinecraftClient.getInstance().textRenderer.draw(text, getPosition().x, getPosition().y, 0xFFFFFFFF, false, context.matrixStack().peek().getPositionMatrix(), context.consumers(), TextRenderer.TextLayerType.POLYGON_OFFSET, 0x0, 15728880);
    }

    @Override
    public void render(MatrixStack stack, VertexConsumerProvider provider, @Nullable Canvas canvas) {
        MinecraftClient.getInstance().textRenderer.draw(net.minecraft.text.Text.literal(""), getPosition().x, getPosition().y, 0xFFFFFFFF, false, stack.peek().getPositionMatrix(), provider, TextRenderer.TextLayerType.POLYGON_OFFSET, 0x0, 15728880);
    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Identifier getTexture() {
        return null;
    }

    @Override
    public void tick(float tickDelta) {

    }

    @Override
    public Vector2f getScale() {
        return null;
    }
}
