package com.buildworld.game.items;

import com.shawnclake.morgencore.core.component.services.Services;

public class Item {

    public String namespace;
    public String name;

    public void register()
    {
        Services.getService(ItemService.class).add(this);
    }
}
