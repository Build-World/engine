package com.buildworld.math;

import java.nio.FloatBuffer;

public class Vector2f extends org.joml.Vector2f {
    /**
     * Stores the vector in a given Buffer.
     *
     * @param buffer The buffer to store the vector data
     */
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(x).put(y);
        buffer.flip();
    }
}
