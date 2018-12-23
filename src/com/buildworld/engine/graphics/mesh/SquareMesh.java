package com.buildworld.engine.graphics.mesh;

import com.buildworld.engine.graphics.colors.RGBAColor;
import org.joml.Vector3f;

public class SquareMesh extends Mesh {

    private TriangleMesh triangle1;
    private TriangleMesh triangle2;

    public SquareMesh(Vector3f position, Vector3f[] face, RGBAColor color) {
        triangle1 = new TriangleMesh(position, color, face[0], face[1]);
        triangle2 = new TriangleMesh(position.add(face[0]).add(face[1]), color, face[2], face[3]);
    }
}
