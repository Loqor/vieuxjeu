package com.loqor.client.renderers;

import com.loqor.core.blockentities.ClawBlockEntity;

import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;

public class ClawBlockEntityRenderer<T extends ClawBlockEntity> implements BlockEntityRenderer<T> {
	private static final float ITEM_SCALE = 0.7F;
	private final ItemRenderer itemRenderer;

	public ClawBlockEntityRenderer(BlockEntityRendererFactory.Context ctx) {
		this.itemRenderer = ctx.getItemRenderer();
	}

	@Override
	public void render(T be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
		matrices.push();
		matrices.translate(0.5F, 1.5F, 0.5F);
		matrices.scale(ITEM_SCALE, ITEM_SCALE, ITEM_SCALE);
		this.itemRenderer.renderItem(be.collectedItem, ModelTransformationMode.FIXED, light, overlay, matrices, vertexConsumers, be.getWorld(), (int) be.getPos().asLong());
		matrices.pop();

		// TODO: Animation
	}

}
