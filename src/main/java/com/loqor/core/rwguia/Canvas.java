package com.loqor.core.rwguia;

import net.fabricmc.fabric.api.client.rendering.v1.WorldRenderContext;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.client.util.math.Vector2f;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import org.joml.Matrix4f;
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
    public Vector2f dimensions;

    /*
            String is the id for indexing,
            CanvasObject is the object to render
     */
    private HashMap<String, CanvasObject> objects = new HashMap<>();
    private Direction canvasDirection = Direction.NORTH;

    private Canvas() {

    }

    public static Canvas create(BlockPos pos, Direction direction, float width, float height) {
        Canvas canvas = new Canvas();
        canvas.position = new Vector3f(pos.getX(), pos.getY(), pos.getZ());
        canvas.canvasDirection = direction;
        canvas.dimensions = new Vector2f(width, height);
        CanvasRegistry.getCanvases().add(canvas);
        return canvas;
    }

    public void tick(float tickDelta) {
        for (CanvasObject value : objects.values()) {
            value.tick(tickDelta);
        }
    }

    public void render(WorldRenderContext context) {
        for (CanvasObject value : objects.values()) {
            value.render(context, this);
        }
    }

    public void render(MatrixStack stack, VertexConsumerProvider provider) {
        for (CanvasObject value : objects.values()) {
            value.render(stack, provider, this);
        }
        stack.push();
        stack.translate(dimensions.getX(), 0, 0);
        stack.scale(0.05f, 2f, 0);
        VertexConsumer buffer = provider.getBuffer(RenderLayer.getDebugQuads());
        MatrixStack.Entry entry = stack.peek();
        Matrix4f matrix4f = entry.getPositionMatrix();
        CanvasObject.vertex(buffer, matrix4f, entry, -1f, -1f,255, 255, 255, 255, 0, 1, 15728880);
        CanvasObject.vertex(buffer, matrix4f, entry, 1f, -1f, 255, 255, 255, 255, 1, 1, 15728880);
        CanvasObject.vertex(buffer, matrix4f, entry, 1f, 1f, 255, 255, 255, 255, 1, 0, 15728880);
        CanvasObject.vertex(buffer, matrix4f, entry, -1f, 1f, 255, 255, 255, 255, 0, 0, 15728880);
        stack.pop();
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

    public void addObject(String id, CanvasObject object) {
        objects.put(id, object);
    }

}
