package com.buildworld.graphics.base;

import com.buildworld.graphics.Renderer;
import com.buildworld.graphics.colors.RGBAColor;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public abstract class GraphicObject extends RenderedObject {

    private RawModel model;
    protected RGBAColor color;

    public void addVertices(float[] vertices){
        this.model = Renderer.modelLoader.loadToVAO(vertices);
    }

    public void render(){
        GL30.glBindVertexArray(model.getVaoID());   // Activate the VAO Id
        GL20.glEnableVertexAttribArray(0);      // Get the VAO's vertices list

        // Draw the vertices
        GL11.glColor3f(color.getRed(), color.getGreen(), color.getBlue());
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());

        // Disable the VAO
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }


}
