package com.buildworld.game.blocks.properties;

import com.buildworld.engine.interfaces.IFactory;
import com.buildworld.engine.interfaces.IKeyNameDescibe;
import com.buildworld.engine.interfaces.IOptional;
import com.buildworld.game.blocks.types.IBlockType;

import java.util.ArrayList;

public interface IBlockProperty extends IKeyNameDescibe, IOptional, IFactory<IBlockProperty> {
    default boolean isTypesWhitelisted()
    {
        return false;
    }
    default ArrayList<IBlockType> getWhitelist()
    {
        return new ArrayList<>();
    }
}
