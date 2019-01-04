package com.buildworld.game.world.utils;

import org.joml.Vector2f;
import org.joml.Vector3f;

public class Directions {
    public static final Vector3f NORTH = new Vector3f(0f,0f,1f);
    public static final Vector3f SOUTH = new Vector3f(0f,0f,-1f);
    public static final Vector3f EAST = new Vector3f(1f,0f,0f);
    public static final Vector3f WEST = new Vector3f(-1f,0f,0f);
    public static final Vector3f UP = new Vector3f(0f,1f,0f);
    public static final Vector3f DOWN = new Vector3f(0f,-1f,0f);

    public static final Vector2f NORTH2D = new Vector2f(0f,1f);
    public static final Vector2f SOUTH2D = new Vector2f(0f,-1f);
    public static final Vector2f EAST2D = new Vector2f(1f,0f);
    public static final Vector2f WEST2D = new Vector2f(-1f,0f);
}
