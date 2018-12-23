package com.buildworld.engine.graphics.services;

import com.buildworld.engine.graphics.Renderer;
import com.shawnclake.morgencore.core.component.services.Service;

import static org.lwjgl.opengl.GL11.*;

public class RenderService extends Service {

    private Renderer renderer;

    public RenderService(Renderer renderer) {
        super();
        this.renderer = renderer;
    }

    public Renderer getRenderer() {
        return renderer;
    }

    public void render()
    {
        // rotate camera
        glRotatef(1, 1, 1, 0);

        int first = 0;
//        for(Renderable renderable : this.getItems()){
//            // TODO: Draw arrays here
//            glColor3f(renderable.getColor().getRed(), renderable.getColor().getGreen(), renderable.getColor().getBlue());
//            glDrawArrays(GL_TRIANGLES, first, renderable.getVertexCount());
//            first += renderable.getVertexCount();
//        }
    }
}
