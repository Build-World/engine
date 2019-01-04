package com.buildworld.game.world.areas;

import java.util.ArrayList;

public class Galaxy {

    ArrayList<World> worlds = new ArrayList<>();

    public World add() throws Exception
    {
        World world = new World(8);
        worlds.add(world);
        return world;
    }

}
