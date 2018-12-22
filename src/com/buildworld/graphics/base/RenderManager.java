package com.buildworld.graphics.base;

import com.buildworld.graphics.Graphic;

import java.util.ArrayList;

public class RenderManager {

    private ArrayList<RenderedObject> renderObjects = new ArrayList<RenderedObject>();

    public void render(){
        for(RenderedObject o : renderObjects){
            if(o != null) {
                o.render();
            } else {
                this.renderObjects.remove(o);
            }
        }
    }

    public void addResource(RenderedObject obj){
        this.renderObjects.add(obj);
    }

    public void removeResource(RenderedObject obj){
        if(this.renderObjects.contains(obj)){
            this.renderObjects.remove(obj);
        }
    }

}
