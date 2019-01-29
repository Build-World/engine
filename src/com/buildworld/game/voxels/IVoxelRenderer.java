package com.buildworld.game.voxels;

import com.buildworld.engine.graphics.game.Renderable;

import java.util.Set;

public interface IVoxelRenderer {

    Set<Renderable> getRenderables() throws Exception;
    String getUniqueVoxelRendererKey();

}
