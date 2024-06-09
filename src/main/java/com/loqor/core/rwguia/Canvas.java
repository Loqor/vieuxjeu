package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Vector3f;

import java.util.HashMap;


/*
*
* 2D Canvas using X/Z and Y
* with Y being a constant
*
* We could make it a 3D Canvas by also using Y as a variable, and check directions from there, but for the Arcade Machines no need
*
* */
public class Canvas {

    public Vector3f position;

    /*
            String is the id for indexing,
            CanvasObject is the object to render
     */
    private HashMap<String, CanvasObject> objects = new HashMap<>();
    private Direction canvasDirection = Direction.NORTH;

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


    public Vector2f get2DPosition(Direction direction) {
        //TODO DIRECTION CHECKS (IF WE USE X OR Z)
        return new Vector2f(position.x, position.y);
    }

    public Vector2f get2DPosition() {
        return get2DPosition(canvasDirection);
    }

}
