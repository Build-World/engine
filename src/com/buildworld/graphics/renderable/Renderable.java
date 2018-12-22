package com.buildworld.graphics.renderable;

import com.buildworld.graphics.colors.RGBAColor;

public interface Renderable {
    int getVertexCount();
    float[] getVertices();
    default RGBAColor getColor() {
        return new RGBAColor();
    }
}
