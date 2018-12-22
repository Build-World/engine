package com.buildworld.math;

import java.nio.FloatBuffer;

public class Vector4f extends org.joml.Vector4f {
    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z).put(w);
        buffer.flip();
    }
}
