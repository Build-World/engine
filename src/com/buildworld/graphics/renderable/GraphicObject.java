package com.buildworld.graphics.renderable;

import com.buildworld.graphics.RenderService;
import com.buildworld.graphics.colors.RGBAColor;
import com.shawnclake.morgencore.core.component.services.Services;

public abstract class GraphicObject implements Renderable {

    private int vertexCount = 3;
    private float[] vertices;
    private RGBAColor color;

    public GraphicObject()
    {
        Services.getService(RenderService.class).add(this);
    }

    public int getVertexCount() {
        return vertexCount;
    }

    public void setVertexCount(int vertexCount) {
        this.vertexCount = vertexCount;
    }

    public RGBAColor getColor() {
        return color;
    }

    public void setColor(RGBAColor color) {
        this.color = color;
    }

    public float[] getVertices() {
        return vertices;
    }

    public void setVertices(float[] vertices) {
        this.vertices = vertices;
    }

}
