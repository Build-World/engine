package com.buildworld.game.blocks;

import com.buildworld.graphics.colors.RGBAColor;
import com.shawnclake.morgencore.core.component.services.Services;

abstract public class Block {

    public String namespace;
    public String name;
    public RGBAColor color = new RGBAColor();
    public Material material = new Material();

    public void register()
    {
        Services.getService(BlockService.class).add(this);
    }


}
