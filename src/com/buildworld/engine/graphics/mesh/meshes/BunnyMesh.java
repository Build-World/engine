package com.buildworld.engine.graphics.mesh.meshes;

import com.buildworld.engine.graphics.mesh.IMesh;

public class BunnyMesh implements IMesh {

    public static final String path = "C:\\Users\\using\\Desktop\\shawn\\build-world\\engine\\resources/models/bunny.obj";

    @Override
    public String getPath() {
        return path;
    }
}
