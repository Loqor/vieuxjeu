package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.HashMap;

public class Canvas {

    public Vector3f position;

    /*
            String is the id for indexing,
            CanvasObject is the object to render
     */
    private HashMap<String, CanvasObject> objects = new HashMap<>();

    public void render(WorldRenderContext context) {
        for (CanvasObject value : objects.values()) {
            value.render(context, this);
        }
    }

    public void setPosition(Vector3f position) {
        this.position = position;

    }

    public Vector3f getPosition() {
        return position;
    }

    public Vector2f get2DPosition() {
        return new Vector2f(position.x, position.y);
    }

}
