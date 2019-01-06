package com.buildworld.game.world.areas;

import com.buildworld.game.blocks.Block;
import com.buildworld.game.world.maps.types.HeightMap;
import com.buildworld.game.world.interfaces.IArea;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.HashMap;

public class Chunk implements IArea {

    public static final int size = 31;

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Block>>> map;

    private HeightMap heightMap;

    private Vector2f location;

    private Region region;

    public Region getRegion() {
        return region;
    }

    @Override
    public Vector2f getLocation2D() {
        return location;
    }

    public void setLocation(Vector2f location) {
        this.location = location;
    }

    public Chunk() {
        map = new HashMap<>();
    }

    public Chunk(Region region) {
        this();
        this.region = region;
    }

    public HeightMap getHeightMap() {
        return heightMap;
    }

    public void setHeightMap(HeightMap heightMap) {
        this.heightMap = heightMap;
    }

    public void setRegion(Region region) {
        this.region = region;
    }

    public void setBlock(int x, int y, int z, Block block) throws Exception
    {
        if(x < 0 || x >= size || z < 0 || z >= size || y < 0 || y > World.worldHeight)
        {
            throw new Exception("Out of chunk bounds");
        }

        // Puts a block into the map but if the hashmaps dont exist it will create it
        block.setPosition(x,y,z);
        block.setChunk(this);
        map.computeIfAbsent(x, k -> new HashMap<>()).computeIfAbsent(z, l -> new HashMap<>()).put(y, block);
    }

    public void setBlock(Vector3f coordinate, Block block) throws Exception
    {
        setBlock((int)coordinate.x, (int)coordinate.y, (int)coordinate.z, block);
    }

    public Block getBlock(int x, int y, int z) throws Exception
    {
        if(x < 0 || x >= size || z < 0 || z >= size || y < 0 || y > World.worldHeight)
        {
            throw new Exception("Out of chunk bounds");
        }

        try {
            return map.get(x).get(z).get(y);
        } catch (Exception e) {
            return null;
        }
    }

    public Block getBlock(Vector3f coordinate) throws Exception
    {
        return getBlock((int)coordinate.x, (int)coordinate.y, (int)coordinate.z);
    }

    public boolean isAir(int x, int y, int z) throws Exception {
        if(x < 0 || x >= size || z < 0 || z >= size || y < 0 || y > World.worldHeight)
        {
            throw new Exception("Out of chunk bounds");
        }

        return getBlock(x, y, z) == null;
    }

    public boolean isAir(Vector3f coordinate) throws Exception
    {
        return isAir((int)coordinate.x, (int)coordinate.y, (int)coordinate.z);
    }
}
