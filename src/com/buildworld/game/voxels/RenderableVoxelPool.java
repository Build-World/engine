package com.buildworld.game.voxels;

import com.buildworld.engine.graphics.game.Renderable;
import com.buildworld.engine.graphics.pooling.RenderablePool;
import com.buildworld.game.blocks.Block;

public class RenderableVoxelPool extends RenderablePool {

    public RenderableVoxelPool(int size) {
        super(size);
    }

    public Renderable use(IVoxel voxel, float scale) throws Exception
    {
        // TODO: The other properties on the pooled renderable need to be reset to default or set via parameters
        return use(voxel.getMesh(), voxel.getWorldPosition(), scale);
    }
}
