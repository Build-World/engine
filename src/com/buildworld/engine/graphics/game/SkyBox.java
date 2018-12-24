package com.buildworld.engine.graphics.game;

import com.buildworld.engine.graphics.materials.Material;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.mesh.OBJLoader;
import com.buildworld.engine.graphics.textures.Texture;

public class SkyBox extends GameItem {

    public SkyBox(String objModel, String textureFile) throws Exception {
        super();
        Mesh skyBoxMesh = OBJLoader.loadMesh(objModel);
        Texture skyBoxtexture = new Texture(textureFile);
        skyBoxMesh.setMaterial(new Material(skyBoxtexture, 0.0f));
        setMesh(skyBoxMesh);
        setPosition(0, 0, 0);
    }
}
