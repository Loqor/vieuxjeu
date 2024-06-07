package com.loqor.client;

import com.loqor.VieuxJeu;
import com.loqor.client.renderers.TestBlockEntityRenderer;
import com.loqor.core.VJBlockEntityTypes;
import com.mojang.blaze3d.systems.RenderSystem;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactories;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.RotationAxis;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
import org.lwjgl.opengl.GL11;

public class VieuxJeuClient implements ClientModInitializer {
    @Override
    public void onInitializeClient() {
        registerBlockEntityRenderers();
        /*WorldRenderEvents.END.register(context -> {
            Camera camera = context.camera();
            MatrixStack matrixStack = context.matrixStack();
            Vec3d targetPosition = new Vec3d(-1, -59, -14);
            Vec3d transformedPosition = targetPosition.subtract(camera.getPos());
            matrixStack.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
            matrixStack.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);
            matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(180.0F));
            Matrix4f positionMatrix = matrixStack.peek().getPositionMatrix();
            Tessellator tessellator = Tessellator.getInstance();
            BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);

            builder.vertex(positionMatrix, 0, 0, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f);
            builder.vertex(positionMatrix, 0, 1, 0).color(1f, 0f, 0f, 1f).texture(0f, 1f);
            builder.vertex(positionMatrix, 1, 1, 0).color(0f, 1f, 0f, 1f).texture(1f, 1f);
            builder.vertex(positionMatrix, 1, 0, 0).color(0f, 0f, 1f, 1f).texture(1f, 0f);

            RenderSystem.setShader(GameRenderer::getPositionTexColorProgram);
            //RenderSystem.bindTexture(MinecraftClient.getInstance().getFramebuffer().fbo);
            //RenderSystem._setShaderTexture(0, MinecraftClient.getInstance().getFramebuffer().fbo);
            RenderSystem.setShaderTexture(0, Identifier.of(VieuxJeu.MOD_ID, "icon.png"));
            RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
            RenderSystem.disableCull();
            RenderSystem.depthFunc(GL11.GL_ALWAYS);

            BufferRenderer.drawWithGlobalProgram(builder.end());

            RenderSystem.depthFunc(GL11.GL_LEQUAL);
            RenderSystem.enableCull();
        });*/
    }

    public void registerBlockEntityRenderers() {
        BlockEntityRendererFactories.register(VJBlockEntityTypes.TEST_BLOCK_ENTITY_TYPE, TestBlockEntityRenderer::new);
    }
}
