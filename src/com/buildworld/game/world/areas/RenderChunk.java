package com.buildworld.game.world.areas;

import com.buildworld.game.blocks.Block;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class RenderChunk {

    public static final int size = Chunk.size / Chunk.renderChunksPerSide;

    private List<Block> buildBuffer;
    private Vector2f offset;
    private Chunk chunk;
    private boolean requiresRebuild = true;

    public RenderChunk(Chunk chunk, Vector2f offset) {
        buildBuffer = new ArrayList<>();
        this.offset = new Vector2f(offset);
        this.chunk = chunk;
    }

    public List<Block> getBuildBuffer() {
        return buildBuffer;
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

        buildBuffer.clear();

        int xStart = (int)offset.x * size;
        int zStart = (int)offset.y * size;

        for(int i = xStart; i < size; i++)
        {
            for(int j = zStart; j < size; j++)
            {
                for(int k = 0; k < World.worldHeight; k++)
                {
                    Block block = chunk.getBlock(i,j,k);
                    if(block != null && block.getChunk().getRegion().getWorld().getBlockNeighborCount(block) < 6)
                    {
                        buildBuffer.add(chunk.getBlock(i,j,k));
                    }
                }
            }
        }
    }

}
