package com.buildworld.engine.graphics.mesh.meshes;

import com.buildworld.engine.graphics.mesh.IMesh;
import com.buildworld.game.Game;

public class BunnyMesh implements IMesh {

    public static final String path = Game.path + "\\engine\\resources/models/bunny.obj";

    @Override
    public String getPath() {
        return path;
    }
}
