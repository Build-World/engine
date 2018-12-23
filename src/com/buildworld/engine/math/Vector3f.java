package com.buildworld.engine.math;

import java.nio.FloatBuffer;

public class Vector3f extends org.joml.Vector3f {
    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y).put(z);
        buffer.flip();
    }
}
