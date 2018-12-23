package com.buildworld.engine.math;

import java.nio.FloatBuffer;

public class Matrix4f extends org.joml.Matrix4f {
    /**
     * Stores the matrix in a given Buffer.
     *
     * @param buffer The buffer to store the matrix data
     */
    public void toBuffer(FloatBuffer buffer) {
        buffer.put(m00()).put(m10()).put(m20()).put(m30());
        buffer.put(m01()).put(m11()).put(m21()).put(m31());
        buffer.put(m02()).put(m12()).put(m22()).put(m32());
        buffer.put(m03()).put(m13()).put(m23()).put(m33());
        buffer.flip();
    }

    /**
     * Creates a orthographic projection matrix. Similar to
     * <code>glOrtho(left, right, bottom, top, near, far)</code>.
     *
     * @param left   Coordinate for the left vertical clipping pane
     * @param right  Coordinate for the right vertical clipping pane
     * @param bottom Coordinate for the bottom horizontal clipping pane
     * @param top    Coordinate for the bottom horizontal clipping pane
     * @param near   Coordinate for the near depth clipping pane
     * @param far    Coordinate for the far depth clipping pane
     *
     * @return Orthographic matrix
     */
    public static Matrix4f orthographic(float left, float right, float bottom, float top, float near, float far) {
        Matrix4f ortho = new Matrix4f();

        float tx = -(right + left) / (right - left);
        float ty = -(top + bottom) / (top - bottom);
        float tz = -(far + near) / (far - near);

        ortho.m00(2f / (right - left));
        ortho.m11(2f / (top - bottom));
        ortho.m22(-2f / (far - near));
        ortho.m03(tx);
        ortho.m13(ty);
        ortho.m23(tz);

        return ortho;
    }

}
