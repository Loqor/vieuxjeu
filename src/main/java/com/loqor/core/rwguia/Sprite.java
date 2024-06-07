package com.loqor.core.rwguia;

import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public class Sprite implements GraphicalObject {

    private Identifier texture;
    private Vector2f position;

    public Sprite(Identifier texture) {
        this.texture = texture;
    }


    @Override
    public Identifier getTexture() {
        return texture;
    }

    @Override
    public void render() {

    }

    @Override
    public Vector2f getPosition() {
        return position;
    }

    public void setPosition(Vector2f position) {
        this.position = position;
    }

    public void updateTexture(Identifier texture) {
        this.texture = texture;
    }
}
