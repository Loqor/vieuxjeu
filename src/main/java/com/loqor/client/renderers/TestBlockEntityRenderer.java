package com.loqor.client.renderers;

import com.loqor.core.blockentities.TestBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

public class TestBlockEntityRenderer<T extends TestBlockEntity> implements BlockEntityRenderer<T> {
    public TestBlockEntityRenderer(BlockEntityRendererFactory.Context context) {

    }

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d targetPosition = new Vec3d(entity.getPos().getX(), entity.getPos().getY(), entity.getPos().getZ());
        Vec3d transformedPosition = targetPosition.subtract(camera.getPos());
        //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrices.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

        builder.vertex(positionMatrix, 0, 0, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f);
        builder.vertex(positionMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(0f, 1f);
        builder.vertex(positionMatrix, 1, 1, 0).color(1f, 1f, 1f, 1f).texture(1f, 1f);
        builder.vertex(positionMatrix, 1, 0, 0).color(1f, 1f, 1f, 1f).texture(1f, 0f);

        RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
        //RenderSystem.bindTexture(MinecraftClient.getInstance().getFramebuffer().fbo);
        RenderSystem._setShaderTexture(0, MinecraftClient.getInstance().getFramebuffer().fbo);
        //RenderSystem.setShaderTexture(0, Identifier.of(VieuxJeu.MOD_ID, "icon.png"));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        RenderSystem.disableCull();
        //RenderSystem.depthFunc(GL11.GL_ALWAYS);

        BufferRenderer.drawWithGlobalProgram(builder.end());

        //RenderSystem.depthFunc(GL11.GL_LEQUAL);
        RenderSystem.enableCull();
    }
}
