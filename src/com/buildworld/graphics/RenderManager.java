package com.buildworld.graphics;

import com.buildworld.graphics.Graphic;
import com.buildworld.graphics.base.RenderedObject;

import java.util.ArrayList;

import static org.lwjgl.opengl.GL11.glRotatef;

public class RenderManager {

    private ArrayList<RenderedObject> renderObjects = new ArrayList<RenderedObject>();

    public void render(){

        // rotate
        glRotatef(1, 1, 1, 0);
        //glRotatef(1, 0, 1, 0);

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
