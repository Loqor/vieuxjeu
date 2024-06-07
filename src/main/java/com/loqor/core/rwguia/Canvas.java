package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;

import java.util.HashMap;

public class Canvas {

    /*
            String is the id for indexing,
            CanvasObject is the object to render
     */
    private HashMap<String, CanvasObject> objects = new HashMap<>();

    public void render(WorldRenderContext context) {
        for (CanvasObject value : objects.values()) {
            value.render(context);
        }
    }

}
