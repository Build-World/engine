package com.buildworld.game.world;

import com.buildworld.game.world.areas.*;
import com.buildworld.game.world.generators.Biome;
import com.buildworld.game.world.generators.Planet;
import com.buildworld.game.world.maps.types.*;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

public class WorldController {

    private World world;
    private Planet planetGenerator;
    private List<Biome> biomes;

    public WorldController(World world, Planet planet) throws Exception {
        this.biomes = new ArrayList<>();
        this.world = world;
        if (world.getWorldState().equals(WorldState.UNLOADED)) {
            world.load();
        }
        this.planetGenerator = planet;
        this.planetGenerator.setSeed(world.getSeed());
    }

    public List<Biome> getBiomes() {
        return biomes;
    }

    protected Biome evaluateMostLikelyBiome(float averageTemperature, float averagePercipitation) throws Exception {
        Biome bestBiome = null;
        float bestLikliness = 0;
        for (Biome testing : getBiomes()) {
            float myLiklyness = testing.getBaseFrequency();
            float temperatureDiff = Math.abs(testing.getTemperature() - averageTemperature);
            float percipitationDiff = Math.abs(testing.getPercipitation() - averagePercipitation);

            // Because the peripitation is a value between 0 and 1, the diff needs to be scaled up to be somewhat
            //   as effective as the temperature
            percipitationDiff *= 40;

            // Decreases likliness by how far off the difference between the average and desired temp is
            if (temperatureDiff > testing.getTemperatureRange()) {
                myLiklyness += 3 * (temperatureDiff - testing.getTemperatureRange());
                temperatureDiff = testing.getTemperatureRange();
            }
            myLiklyness += temperatureDiff;

            // Decreases likliness by how far off the difference between the average and desired percipitation is
            if (percipitationDiff > testing.getPercipitationRange()) {
                myLiklyness += 3 * (percipitationDiff - testing.getPercipitationRange());
                percipitationDiff = testing.getPercipitationRange();
            }
            myLiklyness += percipitationDiff;

            // If this is the first iteration it sets the biome right in
            if (bestBiome == null) {
                bestBiome = testing;
                bestLikliness = myLiklyness;
            }

            // Only if a biome is more likely (denoted by a smaller likliness value) will it be set in
            // If two biomes have the same likliness, this will always use the first one found
            // This is to produce repeatable results
            if (myLiklyness < bestLikliness) {
                bestBiome = testing;
                bestLikliness = myLiklyness;
            }
        }

        // This will be null if no biomes are registered
        if (bestBiome == null) {
            throw new Exception("There must be at least one biome registered for the generator to function");
        }

        System.out.println("Likliness: " + bestLikliness);

        return bestBiome;
    }

    protected Chunk generateChunk(Region region, Vector2f chunkOffset, FillHeightMap heightMap, TemperatureMap temperatureMap, PercipitationMap percipitationMap) throws Exception {
        // Creating the chunk
        Chunk chunk = new Chunk();
        chunk.setRegion(region);
        chunk.setLocation2D(chunkOffset);

        // Calculates the average temperature and average percipitation of this chunk
        Vector2f blockOffset = chunk.getRelativeBlockOffset();
        float averageTemperature = 0, averagePercipitation = 0;
        for (int i = (int) blockOffset.x; i < (blockOffset.x + Chunk.size); i++) {
            for (int j = (int) blockOffset.y; j < (blockOffset.y + Chunk.size); j++) {
                averageTemperature += temperatureMap.get(i, j);
                averagePercipitation += percipitationMap.get(i, j);
            }
        }

        averageTemperature /= (Chunk.size * Chunk.size);
        averagePercipitation /= (Chunk.size * Chunk.size);

        // Determines the best fit biome for this chunk
        Biome biome = evaluateMostLikelyBiome(averageTemperature, averagePercipitation);

        // Getting our chunk neighbors
        ChunkGrid chunkGrid = region.getWorld().getChunkGrid(chunk);

        // Fills the biome with blocks, floral, structures, metals, loot, mobs, animals, etc.
        biome.fillChunk(chunk, chunkGrid, heightMap, planetGenerator.getSeaLevel());

        return chunk;
    }

    protected Region generateRegion(Vector2f regionOffset) throws Exception {
        // Generating maps
        FillHeightMap fillHeightMap = planetGenerator.generateHeightMap(regionOffset);
        TemperatureMap temperatureMap = planetGenerator.generateTemperatureMap(regionOffset);
        PercipitationMap percipitationMap = planetGenerator.generatePercipitationMap(regionOffset);
        FertilityMap fertilityMap = planetGenerator.generateFertilityMap(regionOffset);
        AirQualityMap airQualityMap = planetGenerator.generateAirQualityMap(regionOffset);

        // Creating region and applying maps to it
        Region region = new Region(RegionState.GENERATING, regionOffset);
        region.setWorld(world);
        world.setRegion(regionOffset, region);
        region.setTemperatureMap(temperatureMap);
        region.setPercipitationMap(percipitationMap);
        region.setFertilityMap(fertilityMap);
        region.setAirQualityMap(airQualityMap);

        // Generating each chunk in the region
        for (int i = 0; i < Region.size; i++) {
            for (int j = 0; j < Region.size; j++) {
                Chunk chunk = generateChunk(region, new Vector2f(i, j), fillHeightMap, temperatureMap, percipitationMap);
                region.setChunk(i, j, chunk);
            }
        }

        for (int i = 0; i < Region.size; i++) {
            for (int j = 0; j < Region.size; j++) {
                region.getChunk(i,j).build();
            }
        }

        return region;
    }

    public void unloadRegion(Vector2f regionOffset) throws Exception {
        Region region = world.getRegion(regionOffset);
        if (region == null) {
            throw new Exception("Cannot unload a non existent region");
        } else if (region.getState().equals(RegionState.LOADED)) {
            region.setState(RegionState.UNLOADED);
            region.unload();
            world.unloadRegion(regionOffset);
        }

    }

    public Region loadRegion(Vector2f regionOffset) throws Exception {
        Region region = world.getRegion(regionOffset);

        if (region == null) {
            region = generateRegion(regionOffset);
            //region.setLocation2D(regionOffset);
            region.setState(RegionState.LOADED);
        } else if (region.getState().equals(RegionState.UNLOADED)) {
            region.load();
            region.setWorld(world);
            region.setLocation2D(regionOffset);
        }

        return region;
    }

    public boolean isRegionLoaded(Vector2f regionOffset) throws Exception {
        return world.getRegion(regionOffset) == null;
    }
}
