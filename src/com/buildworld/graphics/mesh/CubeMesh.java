package com.buildworld.graphics.mesh;

import com.buildworld.graphics.Directions;
import com.buildworld.graphics.Faces;
import com.buildworld.graphics.colors.RGBAColor;
import org.joml.Vector3f;

public class CubeMesh extends Mesh {

    private SquareMesh squareTop;
    private SquareMesh squareBottom;
    private SquareMesh squareLeft;
    private SquareMesh squareRight;
    private SquareMesh squareFront;
    private SquareMesh squareBack;

    public CubeMesh(Vector3f position) {
        squareFront = new SquareMesh(new Vector3f(position), Faces.front, new RGBAColor(1, 0f, 0f));
        squareBack = new SquareMesh(new Vector3f(position).add(Directions.back), Faces.back, new RGBAColor(0.5f, 1, 0.5f));

        squareLeft = new SquareMesh(new Vector3f(position), Faces.left, new RGBAColor(0f, 0, 1f));
        squareRight = new SquareMesh(new Vector3f(position).add(Directions.right), Faces.right, new RGBAColor(0.5f, 0f, 0.5f));


        squareTop = new SquareMesh(new Vector3f(position).add(Directions.up), Faces.top, new RGBAColor(0.5f, 0.5f, 0.5f));
        squareBottom = new SquareMesh(new Vector3f(position), Faces.bottom, new RGBAColor(0, 0.5f, 0.5f));
    }
}
