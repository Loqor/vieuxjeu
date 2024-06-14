package com.loqor.client.renderers;

import com.loqor.core.blockentities.ClawBlockEntity;
import com.loqor.core.blockentities.PunchGameBlockEntity;

import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.entity.BlockEntityRenderer;
import net.minecraft.client.render.block.entity.BlockEntityRendererFactory;
import net.minecraft.client.util.math.MatrixStack;

public class ClawBlockEntityRenderer<T extends ClawBlockEntity> implements BlockEntityRenderer<T> {

	@Override
	public void render(T be, float tickDelta, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, int overlay) {
	}


}
