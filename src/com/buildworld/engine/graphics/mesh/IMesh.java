package com.buildworld.engine.graphics.mesh;

import com.buildworld.engine.graphics.materials.Material;

public interface IMesh {

    String getPath();

    default Mesh make(Material material) throws Exception {
        Mesh mesh = OBJLoader.loadMesh(getPath());
        mesh.setMaterial(material);
        return mesh;
    }

    default Mesh make() throws Exception {
        return OBJLoader.loadMesh(getPath());
    }
}
