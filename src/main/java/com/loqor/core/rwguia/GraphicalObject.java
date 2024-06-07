package com.loqor.core.rwguia;

import net.minecraft.util.Identifier;
import org.joml.Vector2f;

public interface GraphicalObject {

    void render();
    Vector2f getPosition();
    Identifier getTexture();
}
