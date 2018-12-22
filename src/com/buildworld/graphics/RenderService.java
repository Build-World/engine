package com.buildworld.graphics;

import com.buildworld.graphics.renderable.Renderable;
import com.shawnclake.morgencore.core.component.services.ListService;

import static org.lwjgl.opengl.GL11.*;

public class RenderService extends ListService<Renderable> {
    public void render()
    {
        // rotate camera
        glRotatef(1, 1, 1, 0);

        int first = 0;
        for(Renderable renderable : this.getItems()){
            // TODO: Draw arrays here
            glColor3f(renderable.getColor().getRed(), renderable.getColor().getGreen(), renderable.getColor().getBlue());
            glDrawArrays(GL_TRIANGLES, first, renderable.getVertexCount());
            first += renderable.getVertexCount();
        }
    }
}
