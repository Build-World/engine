package com.buildworld.game.world.areas;

import com.buildworld.engine.graphics.game.Renderable;
import com.buildworld.game.blocks.Block;
import com.buildworld.game.voxels.RenderableVoxelPool;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class RenderChunk {

    public static final int size = Chunk.size / Chunk.renderChunksPerSide;
    private static final int poolSize = size * size * World.worldHeight / (World.worldHeight / 4);

    private ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, ConcurrentHashMap<Integer, Renderable>>> map;
    private RenderableVoxelPool pool;
    private Vector2f offset;
    private Chunk chunk;
    private boolean requiresRebuild = true;

    public RenderChunk(Chunk chunk, Vector2f offset) {
        pool = new RenderableVoxelPool(poolSize);
        map = new ConcurrentHashMap<>();
        this.offset = new Vector2f(offset);
        this.chunk = chunk;
    }

    public Set<Renderable> getBuffer() {
        return pool.getUsed();
    }

    public boolean isRequiresRebuild() {
        return requiresRebuild;
    }

    public void setRequiresRebuild(boolean requiresRebuild) {
        this.requiresRebuild = requiresRebuild;
    }

    public Vector2f getOffset() {
        return offset;
    }

    public Chunk getChunk() {
        return chunk;
    }

    public void build() throws Exception
    {
        if(!requiresRebuild)
        {
            return;
        }

        pool = new RenderableVoxelPool(poolSize);
        map = new ConcurrentHashMap<>();

        int xStart = (int)offset.x * size;
        int zStart = (int)offset.y * size;

        for(int i = xStart; i < (xStart + size); i++)
        {
            for(int j = zStart; j < (zStart + size); j++)
            {
                for(int k = 0; k < World.worldHeight; k++)
                {
//                    System.out.println(i+":"+j+":"+k);
                    Block block = chunk.getBlock(i,k,j);
                    int neighborCount = 0;
                    try {
                        neighborCount = block.getChunk().getRegion().getWorld().getBlockNeighborCount(block);
                    } catch (Exception e) {
                    }
                    if(block != null && neighborCount < 6)
                    {
                        Renderable renderable = pool.use(block, 0.5f);
                        map.computeIfAbsent(i, p -> new ConcurrentHashMap<>()).computeIfAbsent(j, l -> new ConcurrentHashMap<>()).put(k, renderable);
                    }
                }
            }
        }
    }
}
