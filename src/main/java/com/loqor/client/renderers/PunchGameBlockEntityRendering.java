package com.loqor.client.renderers;

import com.loqor.core.blockentities.PunchGameBlockEntity;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class PunchGameBlockEntityRendering<T extends PunchGameBlockEntity> implements BlockEntityRenderer<T> {
	
	private final TextRenderer textRenderer;
	
	private static final double easeOutMath(double startTime, double startValue, double endTime, double endValue, double currentTime) {
        double fullDuration = endTime - startTime;
        double timePassed = currentTime - startTime;

        double x = timePassed / fullDuration;
        double function = Math.sqrt(1 - Math.pow(1 - x, 4)); // Circ
//        double function = 1 - Math.pow(1 - x, 6); // Cubic
//        double function = Math.sin((x * Math.PI) / 2); // Sine
        
        return startValue + (endValue - startValue) * function;
	}
		
	public PunchGameBlockEntityRendering(BlockEntityRendererFactory.Context ctx) {
		this.textRenderer = ctx.getTextRenderer();
	}

	@Override
	public void render(T be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		float f = 0.015625f;
		matrices.scale(f, -f, f);
		
		int score;
		if (be.getScore() == 0 || be.currentValueTick + PunchGameBlockEntity.DELAY <= be.getWorld().getTime() + tickDelta) {
			score = be.getScore();
		} else {
			double longNumbie = easeOutMath(be.currentValueTick, be.previousValue, be.currentValueTick + PunchGameBlockEntity.DELAY, be.getScore(), be.getWorld().getTime() + tickDelta);
			score = (int) Math.floor(longNumbie + 0.25);
		}
		
		String text = String.format("%03d", score);
				
		this.textRenderer.draw(text, 0.5f/f - this.textRenderer.getWidth(text)/2f, -this.textRenderer.fontHeight - (1.5f/f), 0xE14D2F, false, matrices.peek().getPositionMatrix(), vertexConsumers, 
				TextRenderer.TextLayerType.POLYGON_OFFSET, 0, light);
		matrices.pop();

	}

}
