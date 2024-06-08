package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
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
    public Vector2f getPosition() {
        return position;
    }

    @Override
    public Identifier getTexture() {
        return null;
    }
}
