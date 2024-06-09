package com.loqor.client.renderers;

import com.loqor.VieuxJeu;
import com.loqor.core.blockentities.TestBlockEntity;
import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gl.Framebuffer;
import net.minecraft.client.gl.SimpleFramebuffer;
import net.minecraft.client.render.*;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;

import static net.minecraft.client.MinecraftClient.IS_SYSTEM_MAC;

public class TestBlockEntityRenderer<T extends TestBlockEntity> implements BlockEntityRenderer<T> {
    private Framebuffer framebuffer;

    public TestBlockEntityRenderer(BlockEntityRendererFactory.Context context) {}

    @Override
    public void render(T entity, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
        Camera camera = MinecraftClient.getInstance().gameRenderer.getCamera();
        Vec3d targetPosition = new Vec3d((float) entity.getPos().getX(),(float) entity.getPos().getY(),(float) entity.getPos().getZ());
        Vec3d transformedPosition = targetPosition.subtract(camera.getPos());
        //matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(camera.getPitch()));
        //matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(camera.getYaw() + 180.0F));
        matrices.translate(transformedPosition.x, transformedPosition.y, transformedPosition.z);

        Framebuffer buffer = this.render(matrices, vertexConsumers, MinecraftClient.getInstance().getFramebuffer());

        Matrix4f positionMatrix = matrices.peek().getPositionMatrix();
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE);

        builder.vertex(positionMatrix, 0, 0, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f);
        builder.vertex(positionMatrix, 0, 2, 0).color(1f, 1f, 1f, 1f).texture(0f, 1f);
        builder.vertex(positionMatrix, 2, 2, 0).color(1f, 1f, 1f, 1f).texture(1f, 1f);
        builder.vertex(positionMatrix, 2, 0, 0).color(1f, 1f, 1f, 1f).texture(1f, 0f);

        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem._setShaderTexture(1, buffer.fbo);
        //RenderSystem._setShaderTexture(0, MinecraftClient.getInstance().getFramebuffer().fbo);
        BufferRenderer.drawWithGlobalProgram(builder.end());
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);

    }
    public Framebuffer render(MatrixStack stack, VertexConsumerProvider provider, Framebuffer mcFramebuffer) {
        if(this.framebuffer == null)
            this.framebuffer = new SimpleFramebuffer(mcFramebuffer.viewportWidth, mcFramebuffer.viewportHeight, true, IS_SYSTEM_MAC);

        RenderSystem.enableBlend();
        RenderSystem.defaultBlendFunc();

        // Sample rendering into the framebuffer
        framebuffer.beginWrite(false);
        // Add rendering code here
        Tessellator tessellator = Tessellator.getInstance();
        BufferBuilder builder = tessellator.begin(VertexFormat.DrawMode.QUADS, VertexFormats.POSITION_TEXTURE_COLOR);
        Matrix4f positionMatrix = stack.peek().getPositionMatrix();
        RenderSystem.setShader(GameRenderer::getPositionTexProgram);
        RenderSystem.setShaderTexture(0, Identifier.of(VieuxJeu.MOD_ID, "/textures/item/test.png"));
        RenderSystem.setShaderColor(1f, 1f, 1f, 1f);
        builder.vertex(positionMatrix, 0, 0, 0).color(1f, 1f, 1f, 1f).texture(0f, 0f);
        builder.vertex(positionMatrix, 0, 1, 0).color(1f, 1f, 1f, 1f).texture(0f, 1f);
        builder.vertex(positionMatrix, 1, 1, 0).color(1f, 1f, 1f, 1f).texture(1f, 1f);
        builder.vertex(positionMatrix, 1, 0, 0).color(1f, 1f, 1f, 1f).texture(1f, 0f);
        BufferRenderer.drawWithGlobalProgram(builder.end());
        framebuffer.endWrite();

        // render the framebuffer
        framebuffer.draw(mcFramebuffer.viewportWidth, mcFramebuffer.viewportHeight);

        // restore the original framebuffer
        mcFramebuffer.beginWrite(false);

        return this.framebuffer;
    }
}
