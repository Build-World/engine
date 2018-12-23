package com.buildworld.engine.math;

import java.nio.FloatBuffer;

public class Matrix3f extends org.joml.Matrix3f {
    public void toBuffer(FloatBuffer buffer)
    {
        buffer.put(m00).put(m10).put(m20);
        buffer.put(m01).put(m11).put(m21);
        buffer.put(m02).put(m12).put(m22);
        buffer.flip();
    }
}
