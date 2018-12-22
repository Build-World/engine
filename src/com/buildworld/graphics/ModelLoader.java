package com.buildworld.graphics;

import com.buildworld.graphics.base.RawModel;
import org.lwjgl.BufferUtils;
import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL15;
import org.lwjgl.opengl.GL20;
import org.lwjgl.opengl.GL30;
import java.nio.FloatBuffer;
import java.util.ArrayList;
import java.util.List;

public class ModelLoader {

    private List<Integer> vaos = new ArrayList<>();
    private List<Integer> vbos = new ArrayList<>();

    public RawModel loadToVAO(float[] vertices){
        int vaoID = createVAO();    // Create a VAO and activate it.
        storeDataInAttributeList(0, vertices);
        this.unbindVao();
        return new RawModel(vaoID, vertices.length/3);
    }

    private int createVAO(){
        int vaoID = GL30.glGenVertexArrays();   // Generate a vertex array
        GL30.glBindVertexArray(vaoID);          // Bind the VAO to do something with it.
        vaos.add(vaoID);                        // Add the VaoId to the vaoList
        return vaoID;
    }

    public void cleanUp(){
        for(int vao : vaos){
            GL30.glDeleteVertexArrays(vao);
        }
        for(int vbo : vbos){
            GL30.glDeleteBuffers(vbo);
        }
    }

    private void storeDataInAttributeList(int attributeNumber, float[] data){
        int vboID = GL15.glGenBuffers();        // Generate a VBO and get the id
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, vboID); // Bind the VBO to the graphics context
        FloatBuffer buffer = storeDataInFloatBuffer(data);  // Convert the vertex list to a FloatBuffer
        GL15.glBufferData(GL15.GL_ARRAY_BUFFER, buffer, GL15.GL_STATIC_DRAW);   // buffer the data into the VBO
        GL20.glVertexAttribPointer(attributeNumber, 3, GL11.GL_FLOAT, false, 0,0);  // Bind attributes to the VBO
        GL15.glBindBuffer(GL15.GL_ARRAY_BUFFER, 0); // Unbind the current VBO
    }

    private FloatBuffer storeDataInFloatBuffer(float[] data){
        FloatBuffer buffer = BufferUtils.createFloatBuffer(data.length); // Create a float buffer in memory
        buffer.put(data);   // Store data to the buffer
        buffer.flip();      // Indicate the buffer is done storing data
        return buffer;
    }

    private void unbindVao(){
        GL30.glBindVertexArray(0);
    }
}
