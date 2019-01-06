package com.buildworld.game.world.areas;

import com.buildworld.game.world.WorldState;

import java.util.ArrayList;

public class Galaxy {

    ArrayList<World> worlds = new ArrayList<>();

    public World add() throws Exception
    {
        World world = new World(8, WorldState.UNLOADED);
        worlds.add(world);
        return world;
    }

}
