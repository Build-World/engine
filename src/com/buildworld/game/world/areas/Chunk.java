package com.buildworld.game.world.areas;

import com.buildworld.game.blocks.Block;
import com.buildworld.game.world.generators.Biome;
import com.buildworld.game.world.maps.types.HeightMap;
import com.buildworld.game.world.interfaces.IArea;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Chunk implements IArea {

    public static final int size = 33; // must be a number divisble by renderChunksPerSide.

    public static final int renderChunksPerSide = 3;

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Block>>> map;

    private HashMap<Integer, HashMap<Integer, RenderChunk>> renderChunks;

    private Biome biome;

    private HeightMap heightMap;

    private Vector2f location;

    private Region region;

    public static Vector2f sGetAbsBlockOffset(Vector2f regionOffset, Vector2f chunkOffset)
    {
        return new Vector2f(regionOffset).mul(Region.size).mul(Chunk.size).add(new Vector2f(chunkOffset).mul(Chunk.size));
    }

    public Vector2f getAbsBlockOffset()
    {
        return sGetAbsBlockOffset(this.getRegion().getLocation2D(), this.getLocation2D());
    }

    public static Vector2f sGetRelativeBlockOffset(Vector2f chunkOffset)
    {
        return new Vector2f(chunkOffset).mul(Chunk.size);
    }

    public Vector2f getRelativeBlockOffset()
    {
        return sGetRelativeBlockOffset(this.getLocation2D());
    }

    public Region getRegion() {
        return region;
    }

    @Override
    public Vector2f getLocation2D() {
        return location;
    }

    public void setLocation2D(Vector2f location) {
        this.location = location;
    }

    public Chunk() {
        map = new HashMap<>();
        renderChunks = new HashMap<>();
        for(int i = 0; i < renderChunksPerSide; i++)
        {
            for(int j = 0; j < renderChunksPerSide; j++)
            {
                renderChunks.computeIfAbsent(i, k -> new HashMap<>()).put(j, new RenderChunk(this, new Vector2f(i,j)));
            }
        }
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

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    public void build() throws Exception
    {
        for(int i = 0; i < renderChunksPerSide; i++) {
            for (int j = 0; j < renderChunksPerSide; j++) {
                renderChunks.get(i).get(j).build();
            }
        }
    }

    public List<Block> getChunkBuffer() throws Exception
    {
        List<Block> buffer = new ArrayList<>();
        for(int i = 0; i < renderChunksPerSide; i++) {
            for (int j = 0; j < renderChunksPerSide; j++) {
                buffer.addAll(renderChunks.get(i).get(j).getBuildBuffer());
            }
        }
        return buffer;
    }

    public RenderChunk getRenderChunkContainingBlock(int x, int z)
    {
        int xOffset = x / RenderChunk.size;
        int zOffset = z / RenderChunk.size;

        return renderChunks.get(x).get(z);
    }

    public RenderChunk getRenderChunkContainingBlock(Block block)
    {
        return getRenderChunkContainingBlock((int)block.getChunkCoordinate().x, (int)block.getChunkCoordinate().z);
    }

    public void alertRenderChunk(int x, int z)
    {
        //getRenderChunkContainingBlock(x,z).setRequiresRebuild(true);
    }

    public void alertRenderChunk(Block block)
    {
        alertRenderChunk((int)block.getChunkCoordinate().x, (int)block.getChunkCoordinate().z);
    }

    public void setBlock(int x, int y, int z, Block block) throws Exception
    {
        if(x < 0 || x >= size || z < 0 || z >= size || y < 0 || y > World.worldHeight)
        {
            throw new Exception("Out of chunk bounds");
        }

        // Sets the block coordinate relative to this chunk
        block.setChunkCoordinate(x,y,z);

        // Sets the absolute position of this block for rendering purposes
        Vector2f offset = getAbsBlockOffset();
        block.setPosition(offset.x+x,y,offset.y+z);

        block.setChunk(this);

        // Puts a block into the map but if the hashmaps dont exist it will create it
        map.computeIfAbsent(x, k -> new HashMap<>()).computeIfAbsent(z, l -> new HashMap<>()).put(y, block);

        // Alerts the applicable render chunk so that it knows it needs to rebuild itself.
        alertRenderChunk(x,z);
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
