package com.buildworld.game.voxels;

import com.buildworld.engine.graphics.game.Renderable;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.game.world.areas.Chunk;
import org.joml.Vector3f;

public interface IVoxel {

    String getName();
    String getNamespace();
    String getResourcePath();
    String getModelFilename();
    Chunk getChunk();
    void setChunk(Chunk chunk);
    Vector3f getChunkPosition();
    void setChunkPosition(Vector3f position);
    void setChunkPosition(int x, int y, int z);
    Vector3f getWorldPosition();
    void setWorldPosition(Vector3f position);
    void setWorldPosition(int x, int y, int z);
    Mesh[] getMesh() throws Exception;
    Renderable getRenderable() throws Exception;

}
