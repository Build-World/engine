package com.buildworld.graphics.base;

import com.buildworld.graphics.Renderer;

public abstract class RenderedObject {

    public RenderedObject(){
        Renderer.renderManager.addResource(this);
    }

    public abstract void render();

}