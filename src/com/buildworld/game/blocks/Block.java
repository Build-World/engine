package com.buildworld.game.blocks;

import com.buildworld.game.interfaces.Colorable;
import com.buildworld.graphics.colors.RGBAColor;
import com.shawnclake.morgencore.core.component.services.Services;

abstract public class Block implements Colorable {

    public String namespace;
    public String name;
    public RGBAColor color = new RGBAColor();

    public void register()
    {
        Services.getService(BlockService.class).add(this);
    }


}
