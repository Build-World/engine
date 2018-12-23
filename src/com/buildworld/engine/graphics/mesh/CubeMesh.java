package com.buildworld.engine.graphics.mesh;

import com.buildworld.engine.graphics.Texture;

public class CubeMesh extends Mesh {
    private static final float[] vertices = new float[]{
            // V0
            -1f, 1f, 1f,
            // V1
            -1f, -1f, 1f,
            // V2
            1f, -1f, 1f,
            // V3
            1f, 1f, 1f,
            // V4
            -1f, 1f, -1f,
            // V5
            1f, 1f, -1f,
            // V6
            -1f, -1f, -1f,
            // V7
            1f, -1f, -1f,
            // For texture coords in top face
            // V8: V4 repeated
            -1f, 1f, -1f,
            // V9: V5 repeated
            1f, 1f, -1f,
            // V10: V0 repeated
            -1f, 1f, 1f,
            // V11: V3 repeated
            1f, 1f, 1f,
            // For texture coords in right face
            // V12: V3 repeated
            1f, 1f, 1f,
            // V13: V2 repeated
            1f, -1f, 1f,
            // For texture coords in left face
            // V14: V0 repeated
            -1f, 1f, 1f,
            // V15: V1 repeated
            -1f, -1f, 1f,
            // For texture coords in bottom face
            // V16: V6 repeated
            -1f, -1f, -1f,
            // V17: V7 repeated
            1f, -1f, -1f,
            // V18: V1 repeated
            -1f, -1f, 1f,
            // V19: V2 repeated
            1f, -1f, 1f
    };

    private static final float[] textureCoordinates = new float[]{
            0.0f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.5f, 0.0f,
            0.0f, 0.0f,
            0.5f, 0.0f,
            0.0f, 0.5f,
            0.5f, 0.5f,
            // For texture coords in top face
            0.0f, 0.5f,
            0.5f, 0.5f,
            0.0f, 1.0f,
            0.5f, 1.0f,
            // For texture coords in right face
            0.0f, 0.0f,
            0.0f, 0.5f,
            // For texture coords in left face
            0.5f, 0.0f,
            0.5f, 0.5f,
            // For texture coords in bottom face
            0.5f, 0.0f,
            1.0f, 0.0f,
            0.5f, 0.5f,
            1.0f, 0.5f
    };

    private static final int[] indices = new int[]{
            // Front face
            0, 1, 3, 3, 1, 2,
            // Top Face
            8, 10, 11, 9, 8, 11,
            // Right face
            12, 13, 7, 5, 12, 7,
            // Left face
            14, 15, 6, 4, 14, 6,
            // Bottom face
            16, 18, 19, 17, 16, 19,
            // Back face
            4, 6, 7, 5, 4, 7,};

    public CubeMesh(Texture texture) {
        super(vertices, textureCoordinates, indices, texture);
    }

    public static CubeMesh make(Texture texture) {
        return new CubeMesh(texture);
    }
}
