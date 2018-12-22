package com.buildworld.graphics;

import org.joml.Vector3f;

public class Faces {

    public static Vector3f[] front = {Directions.up, Directions.right, Directions.down, Directions.left};
    public static Vector3f[] back = {Directions.up, Directions.right, Directions.down, Directions.left};

    public static Vector3f[] right = {Directions.back, Directions.up, Directions.front, Directions.down};
    public static Vector3f[] left = {Directions.back, Directions.up, Directions.front, Directions.down};

    public static Vector3f[] top = {Directions.back, Directions.right, Directions.front, Directions.left};
    public static Vector3f[] bottom = {Directions.back, Directions.right, Directions.front, Directions.left};
}
