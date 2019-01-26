package com.buildworld.game.world.areas;

import com.buildworld.game.world.WorldState;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Galaxy {

    private List<World> worlds = Collections.synchronizedList(new ArrayList<>());

    public List<World> getWorlds() {
        return worlds;
    }

    public World add() throws Exception
    {
        World world = new World(8, WorldState.UNLOADED);
        worlds.add(world);
        return world;
    }

}
