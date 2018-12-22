package com.buildworld.graphics;

import com.buildworld.graphics.base.RawModel;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;

public class ModelRenderer {

    public void prepare(){
        GL11.glClearColor(0, 1, 0, 1);
    }

    // Render a raw model from its vertices
    public void render(RawModel model){
        GL11.glClearColor(1, 0, 0, 1);
        GL30.glBindVertexArray(model.getVaoID());   // Activate the VAO Id
        GL20.glEnableVertexAttribArray(0);      // Get the VAO's vertices list

        // Draw the vertices
        GL11.glDrawArrays(GL11.GL_TRIANGLES, 0, model.getVertexCount());

        // Disable the VAO
        GL20.glDisableVertexAttribArray(0);
        GL30.glBindVertexArray(0);
    }
}
