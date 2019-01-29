package com.buildworld.engine.graphics.mesh;

import com.buildworld.engine.graphics.loaders.assimp.StaticMeshesLoader;
import com.buildworld.game.Game;

import java.util.HashMap;
import java.util.Map;

public class MeshCache {
    private static MeshCache INSTANCE;

    private Map<String, Mesh[]> meshMap;

    private MeshCache() {
        meshMap = new HashMap<>();
    }

    public Map<String, Mesh[]> getMeshMap() {
        return meshMap;
    }

    public static synchronized MeshCache getInstance() {
        if ( INSTANCE == null ) {
            INSTANCE = new MeshCache();
        }
        return INSTANCE;
    }

    public Mesh[] getMesh(String namespace, String name) throws Exception {
        String fullname = namespace + "." + name;
        Mesh[] mesh = meshMap.get(fullname);
        if ( mesh == null || mesh.length < 1) {
            throw new Exception("Mesh `" + fullname + "` is not loaded");
        }
        return mesh;
    }

    public boolean isMeshLoaded(String namespace, String name)
    {
        try {
            getMesh(namespace, name);
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void loadMesh(String modelName, String resourcePath, String namespace, String name) throws Exception
    {
        String fullname = namespace + "." + name;
        if(meshMap.containsKey(fullname))
        {
            return;
        }
        meshMap.put(fullname, StaticMeshesLoader.load(Game.path + resourcePath + "/" + modelName, resourcePath));
        System.out.println("Loaded mesh for " + fullname);
    }
}
