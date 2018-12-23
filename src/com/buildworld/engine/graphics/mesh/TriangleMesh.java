package com.buildworld.engine.graphics.mesh;

import com.buildworld.engine.graphics.colors.RGBAColor;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class TriangleMesh extends Mesh {

    public TriangleMesh(Vector3f position, RGBAColor color, Vector3f point2Orientation, Vector3f point3Orientation) {
        Vector3f point1 = new Vector3f(position);
        Vector3f point2 = new Vector3f(position);
        Vector3f point3 = new Vector3f(position);

        new Matrix4f().translate(point2Orientation).scale(this.size).transformPosition(point2);
        new Matrix4f().translate(point3Orientation).scale(this.size).transformPosition(point3);

//        System.out.println("P2Z: " + point2.z);
//        System.out.println("P3Z: " + point3.z);

        float[] vertices = new float[9];

        vertices[0] = point2.x;
        vertices[1] = point2.y;
        vertices[2] = point2.z;
        vertices[3] = point1.x;
        vertices[4] = point1.y;
        vertices[5] = point1.z;
        vertices[6] = point3.x;
        vertices[7] = point3.y;
        vertices[8] = point3.z;

        this.setColor(color);

        this.setVertices(vertices);
    }
}
