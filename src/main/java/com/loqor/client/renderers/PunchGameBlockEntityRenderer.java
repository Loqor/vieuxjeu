package com.loqor.client.renderers;

import com.loqor.core.blockentities.PunchGameBlockEntity;

import com.loqor.core.blocks.PunchGameBlock;
import com.loqor.core.blocks.TallGameBlock;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

import static net.minecraft.state.property.Properties.HORIZONTAL_FACING;

public class PunchGameBlockEntityRenderer<T extends PunchGameBlockEntity> implements BlockEntityRenderer<T> {
	
	private final TextRenderer textRenderer;
	
	private static double easeOutMath(double startTime, double startValue, double endTime, double endValue, double currentTime) {
		final double fullDuration = endTime - startTime;
		final double timePassed = currentTime - startTime;

		final double x = timePassed / fullDuration;
		final double function = Math.sqrt(1 - Math.pow(1 - x, 4)); // Circ
//		final double function = 1 - Math.pow(1 - x, 6); // Cubic
//		final double function = Math.sin((x * Math.PI) / 2); // Sine

		return startValue + (endValue - startValue) * function;
	}
		
	public PunchGameBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.textRenderer = ctx.getTextRenderer();
	}

	@Override
	public void render(T be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		final float fontScale = 0.015625f;
		matrices.scale(fontScale, -fontScale, fontScale);

		// TODO: Rotate based on state. 
		// I, @Bug1312, can't visualize this without it taking so much longer than someone who just understands
		float rot = be.getCachedState().get(TallGameBlock.HORIZONTAL_FACING).asRotation();
		
		// If score was just set to 0 or time is after the end of the ease-out, just render the score
		String scoreText = String.format("%03d", 
			(be.getScore() == 0 || be.currentValueTick + PunchGameBlockEntity.WIN_DELAY <= be.getWorld().getTime() + tickDelta) ?
				be.getScore() :
				(int) Math.floor(easeOutMath(be.currentValueTick, be.previousValue, be.currentValueTick + PunchGameBlockEntity.WIN_DELAY, be.getScore(), be.getWorld().getTime() + tickDelta) + 0.25f)
		);

		this.textRenderer.draw(scoreText, 0.5f/fontScale - this.textRenderer.getWidth(scoreText)/2.0f, -this.textRenderer.fontHeight - (1.5f/fontScale), 0xE14D2F, false, matrices.peek().getPositionMatrix(), vertexConsumers, TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light);
		matrices.pop();
	}

}
