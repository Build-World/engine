package com.buildworld.game.world.generators.tests;

import com.buildworld.game.blocks.tests.MyBlock;
import com.buildworld.game.world.generators.Biome;

public class MyBiome extends Biome {

    public MyBiome() throws Exception {
        setRock(new MyBlock());
        setCore(new MyBlock());
        setCrust(new MyBlock());
        setSurface(new MyBlock());
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getKey() {
        return null;
    }

    @Override
    public String getName() {
        return null;
    }

}
