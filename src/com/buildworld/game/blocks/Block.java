package com.buildworld.game.blocks;

import com.buildworld.engine.graphics.game.GameItem;
import com.buildworld.engine.graphics.mesh.meshes.CubeMesh;
import com.shawnclake.morgencore.core.component.services.Services;

abstract public class Block extends GameItem {

    public String namespace;
    public String name;
    public Material material;

    public Block(Material material, String namespace, String name) throws Exception {
        super(new CubeMesh().make(material));
        this.material = material;
        this.namespace = namespace;
        this.name = name;
        this.setScale(0.5f);
    }

    public void register()
    {
        Services.getService(BlockService.class).add(this);
    }
}
