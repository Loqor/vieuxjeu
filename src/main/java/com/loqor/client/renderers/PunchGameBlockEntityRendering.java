package com.loqor.client.renderers;

import com.loqor.core.blockentities.PunchGameBlockEntity;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class PunchGameBlockEntityRendering<T extends PunchGameBlockEntity> implements BlockEntityRenderer<T> {
	
	private final TextRenderer textRenderer;

	public PunchGameBlockEntityRendering(BlockEntityRendererFactory.Context ctx) {
		this.textRenderer = ctx.getTextRenderer();
	}

	@Override
	public void render(T be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
	

		float f = 0.015625f;
		matrices.scale(f, -f, f);

		// TODO: Make this ease. 0 -> X ease out. X -> 0 ease in.
		
		String text = String.valueOf(be.score);
				
		this.textRenderer.draw(text, 0.5f/f - this.textRenderer.getWidth(text)/2f, -this.textRenderer.fontHeight - (1.5f/f), 0xE14D2F, false, matrices.peek().getPositionMatrix(), vertexConsumers,
				TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light);
		matrices.pop();

	}

}
