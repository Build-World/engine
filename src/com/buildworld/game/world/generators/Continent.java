package com.buildworld.game.world.generators;

import com.buildworld.engine.interfaces.IKeyNameDescibe;
import com.buildworld.game.world.interfaces.IGenerate;
import com.buildworld.game.world.maps.types.FillHeightMap;

abstract public class Continent implements IGenerate, IKeyNameDescibe {

    public void generate(FillHeightMap fillHeightMap)
    {

    }

    @Override
    public void generate() {

    }
}
