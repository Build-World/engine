package com.buildworld.graphics.base;

import com.buildworld.graphics.Renderer;

public abstract class GraphicObject extends RenderedObject {

    private RawModel model;

    private float[] vertices = {
            -0.5f, 0.5f, 0f,
            -0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, -0.5f, 0f,
            0.5f, 0.5f, 0f,
            -0.5f, 0.5f, 0f
    };

    public GraphicObject(){
        model = Renderer.modelLoader.loadToVAO(vertices);
    }
    
    public void render(){

        Renderer.modelRenderer.render(model);
    }


}
