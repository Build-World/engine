package com.buildworld.game.world;

import com.buildworld.game.world.areas.Region;
import com.buildworld.game.world.areas.World;
import com.buildworld.game.world.generators.Biome;
import com.buildworld.game.world.generators.Planet;
import com.buildworld.game.world.maps.types.FillHeightMap;
import org.joml.Vector2f;

public class WorldController {

    private World world;
    private Planet planetGenerator;
    private Biome biome;

    public WorldController(World world, Planet planet) throws Exception {
        this.world = world;
        if(world.getWorldState().equals(WorldState.UNLOADED))
        {
            world.load();
        }
        this.planetGenerator = planet;
        this.planetGenerator.setSeed(world.getSeed());
    }

    public Biome getBiome() {
        return biome;
    }

    public void setBiome(Biome biome) {
        this.biome = biome;
    }

    protected Region generateRegion(Vector2f regionOffset) throws Exception
    {
        FillHeightMap fillHeightMap = planetGenerator.generateHeightMap(regionOffset);
        Region region = new Region(RegionState.GENERATING, regionOffset);
        biome.generate(fillHeightMap, region);
        return region;
    }

    public void unloadRegion(Vector2f regionOffset) throws Exception
    {
        Region region = world.getRegion(regionOffset);
        if(region == null)
        {
            throw new Exception("Cannot unload a non existent region");
        } else if(region.getState().equals(RegionState.LOADED))
        {
            region.setState(RegionState.UNLOADED);
            region.unload();
            world.unloadRegion(regionOffset);
        }

    }

    public Region loadRegion(Vector2f regionOffset) throws Exception
    {
        Region region = world.getRegion(regionOffset);

        if(region == null)
        {
            region = generateRegion(regionOffset);
            world.setRegion(regionOffset, region);
            region.setWorld(world);
            //region.setLocation(regionOffset);
            region.setState(RegionState.LOADED);
        } else if (region.getState().equals(RegionState.UNLOADED))
        {
            region.load();
            region.setWorld(world);
            region.setLocation(regionOffset);
        }

        return region;
    }

    public boolean isRegionLoaded(Vector2f regionOffset) throws Exception {
        return world.getRegion(regionOffset) == null;
    }
}
