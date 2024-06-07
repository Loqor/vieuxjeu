package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public class Text implements CanvasObject{


    @Override
    public void render(WorldRenderContext context) {
        MinecraftClient.getInstance().textRenderer.draw(net.minecraft.text.Text.literal(""), getPosition().x, getPosition().y, 0xFFFFFFFF, false, context.matrixStack().peek().getPositionMatrix(), context.consumers(), TextRenderer.TextLayerType.POLYGON_OFFSET, 0x0, 15728880);
    }

    @Override
    public Vector2f getPosition() {
        return null;
    }

    @Override
    public Identifier getTexture() {
        return null;
    }
}
