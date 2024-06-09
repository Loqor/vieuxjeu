package com.loqor.client.renderers;

import com.loqor.core.entities.PunchGameEntity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

public class PunchGameEntityRendering extends EntityRenderer<PunchGameEntity> {

	public PunchGameEntityRendering(Context ctx) {
		super(ctx);
	}
	
	@Override
	public Identifier getTexture(PunchGameEntity var1) {
		return null;
	}

}
