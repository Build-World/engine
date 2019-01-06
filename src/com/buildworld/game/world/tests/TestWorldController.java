package com.buildworld.game.world.tests;

import com.buildworld.game.world.RegionState;
import com.buildworld.game.world.WorldController;
import com.buildworld.game.world.WorldState;
import com.buildworld.game.world.areas.Region;
import com.buildworld.game.world.areas.World;
import com.buildworld.game.world.generators.Planet;
import com.buildworld.game.world.generators.tests.MyPlanet;
import org.joml.Vector2f;

public class TestWorldController {

    public static void main(String[] args) throws Exception
    {
        Planet planetGenerator = new MyPlanet(42);
        World world = new World(64, WorldState.LOADED);
        world.setSeed(42);

        WorldController worldController = new WorldController(world, planetGenerator);
        Region region = worldController.loadRegion(new Vector2f(0,0));

        region.setState(RegionState.LOADED);
    }

}
