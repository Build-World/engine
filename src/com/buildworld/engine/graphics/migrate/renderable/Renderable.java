package com.buildworld.engine.graphics.migrate.renderable;

import com.buildworld.engine.graphics.colors.RGBAColor;

public interface Renderable {
    int getVertexCount();
    float[] getVertices();
    default RGBAColor getColor() {
        return new RGBAColor();
    }
}
