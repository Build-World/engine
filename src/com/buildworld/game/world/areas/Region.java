package com.buildworld.game.world.areas;

import com.buildworld.game.world.interfaces.IArea;
import org.joml.Vector2f;

import java.util.HashMap;

public class Region implements IArea {

    public static final int size = 9;

    private HashMap<Integer, HashMap<Integer, Chunk>> map;

    private Vector2f location;
    private World world;

    public World getWorld() {
        return world;
    }

    @Override
    public Vector2f getLocation2D() {
        return null;
    }

    public void setLocation(Vector2f location) {
        this.location = location;
    }

    public Region() {
        map = new HashMap<>();
    }

    public Region(Vector2f location) {
        this();
        this.location = location;
    }

    public void setWorld(World world) {
        this.world = world;
    }

    public void setChunk(int x, int z, Chunk chunk) throws Exception
    {
        if(x < 0 || x >= size || z < 0 || z >= size)
        {
            throw new Exception("Out of region bounds");
        }

        // Puts a block into the map but if the hashmaps dont exist it will create it
        chunk.setRegion(this);
        chunk.setLocation(new Vector2f(x,z));
        map.computeIfAbsent(x, k -> new HashMap<>()).put(z, chunk);
    }

    public void setChunk(Vector2f coordinate, Chunk chunk) throws Exception {
        setChunk((int)coordinate.x, (int)coordinate.y, chunk);
    }

    public Chunk getChunk(int x, int z) throws Exception
    {
        if(x < 0 || x >= size || z < 0 || z >= size)
        {
            throw new Exception("Out of region bounds");
        }

        try {
            return map.get(x).get(z);
        } catch (Exception e) {
            return null;
        }
    }

    public Chunk getChunk(Vector2f coordinate) throws Exception {
        return getChunk((int)coordinate.x, (int)coordinate.y);
    }

    public boolean isChunk(int x, int z) throws Exception
    {
        if(x < 0 || x >= size || z < 0 || z >= size)
        {
            throw new Exception("Out of chunk bounds");
        }

        return getChunk(x, z) == null;
    }

    public boolean isChunk(Vector2f coordinate) throws Exception
    {
        return isChunk((int)coordinate.x, (int)coordinate.y);
    }


}
