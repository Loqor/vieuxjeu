package com.loqor.client.renderers;

import com.loqor.core.entities.PunchGameEntity;

import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory.Context;
import net.minecraft.util.Identifier;

/**
 * This is just used for crit-particles and bounding boxes. 
 * {@link PunchGameBlockEntityRenderer} will render the punch bag, not this.
 * @see PunchGameBlockEntityRenderer
 */
public class PunchGameEntityRenderer extends EntityRenderer<PunchGameEntity> {

	public PunchGameEntityRenderer(Context ctx) { super(ctx); }
	@Override public Identifier getTexture(PunchGameEntity var1) { return null; }

}
