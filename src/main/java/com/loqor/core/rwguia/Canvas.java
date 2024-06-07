package com.loqor.core.rwguia;

import java.util.ArrayList;
import java.util.HashMap;

public class Canvas {

    /*
            String is the id for indexing,
            GraphicalObject is the object to render
     */
    private HashMap<String, GraphicalObject> objects = new HashMap<>();

    public void render() {
        for (GraphicalObject value : objects.values()) {
            value.render();
        }
    }

}
