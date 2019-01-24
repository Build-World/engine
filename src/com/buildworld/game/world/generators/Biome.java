package com.buildworld.game.world.generators;

import com.buildworld.engine.interfaces.IKeyNameDescibe;
import com.buildworld.engine.utils.noise.SimplexNoise;
import com.buildworld.game.blocks.Block;
import com.buildworld.game.world.areas.*;
import com.buildworld.game.world.interfaces.IGenerate;
import com.buildworld.game.world.maps.types.FillHeightMap;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.List;

abstract public class Biome implements IGenerate, IKeyNameDescibe {

    private int surfaceThickness = 1;
    private int crustThickness = 3;
    private int coreThickness = 1;

    private int hydration = 1;
    private int floral = 1;

    private float temperature = 0;
    private float temperatureRange = 0;
    private float percipitation = 0;
    private float percipitationRange = 0;

    /**
     * The higher this value is, the less likely that it will be chosen.
     * If this value is 0, it has a normal chance of being chosen
     * If the value is < 0, it will be prioritized over other biomes
     */
    private float baseFrequency = 0f;

    private float innerBlendSquashingFactor = 0f;

    private Block surface;
    private Block crust;
    private Block rock;
    private Block core;
    private Block water;

//    private ArrayList<Floral> florals;


    public Biome() {
    }

    public int getSurfaceThickness() {
        return surfaceThickness;
    }

    public void setSurfaceThickness(int surfaceThickness) {
        this.surfaceThickness = surfaceThickness;
    }

    public int getCrustThickness() {
        return crustThickness;
    }

    public void setCrustThickness(int crustThickness) {
        this.crustThickness = crustThickness;
    }

    public int getCoreThickness() {
        return coreThickness;
    }

    public void setCoreThickness(int coreThickness) {
        this.coreThickness = coreThickness;
    }

    public int getHydration() {
        return hydration;
    }

    public void setHydration(int hydration) {
        this.hydration = hydration;
    }

    public int getFloral() {
        return floral;
    }

    public void setFloral(int floral) {
        this.floral = floral;
    }

    public float getTemperature() {
        return temperature;
    }

    public void setTemperature(float temperature) {
        this.temperature = temperature;
    }

    public float getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(float temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public float getPercipitation() {
        return percipitation;
    }

    public void setPercipitation(float percipitation) {
        this.percipitation = percipitation;
    }

    public float getPercipitationRange() {
        return percipitationRange;
    }

    public void setPercipitationRange(float percipitationRange) {
        this.percipitationRange = percipitationRange;
    }

    public Block getSurface() {
        return surface;
    }

    public void setSurface(Block surface) {
        this.surface = surface;
    }

    public Block getCrust() {
        return crust;
    }

    public void setCrust(Block crust) {
        this.crust = crust;
    }

    public Block getRock() {
        return rock;
    }

    public void setRock(Block rock) {
        this.rock = rock;
    }

    public Block getCore() {
        return core;
    }

    public void setCore(Block core) {
        this.core = core;
    }

    public Block getWater() {
        return water;
    }

    public void setWater(Block water) {
        this.water = water;
    }

    public float getBaseFrequency() {
        return baseFrequency;
    }

    public void setBaseFrequency(float baseFrequency) {
        this.baseFrequency = baseFrequency;
    }

    public void fillChunk(Chunk chunk, ChunkGrid chunkGrid, FillHeightMap heightMap, int seaLevel) throws Exception {
        chunk.setBiome(this);

        int xOffset = (int) chunk.getLocation2D().x * Chunk.size;
        int zOffset = (int) chunk.getLocation2D().y * Chunk.size;

        boolean biomeNorthDiffers = false;
        boolean biomeSouthDiffers = false;
        boolean biomeEastDiffers = false;
        boolean biomeWestDiffers = false;

        boolean biomeNorthEastDiffers = false;
        boolean biomeNorthWestDiffers = false;
        boolean biomeSouthEastDiffers = false;
        boolean biomeSouthWestDiffers = false;

        if(chunkGrid.getNorth() != null && !chunkGrid.getNorth().getBiome().getName().equals(this.getName()))
            biomeNorthDiffers = true;
        if(chunkGrid.getSouth() != null && !chunkGrid.getSouth().getBiome().getName().equals(this.getName()))
            biomeSouthDiffers = true;
        if(chunkGrid.getEast() != null && !chunkGrid.getEast().getBiome().getName().equals(this.getName()))
            biomeEastDiffers = true;
        if(chunkGrid.getWest() != null && !chunkGrid.getWest().getBiome().getName().equals(this.getName()))
            biomeWestDiffers = true;

        if(chunkGrid.getNorthEast() != null && !chunkGrid.getNorthEast().getBiome().getName().equals(this.getName()))
            biomeNorthEastDiffers = true;
        if(chunkGrid.getNorthWest() != null && !chunkGrid.getNorthWest().getBiome().getName().equals(this.getName()))
            biomeNorthWestDiffers = true;
        if(chunkGrid.getSouthEast() != null && !chunkGrid.getSouthEast().getBiome().getName().equals(this.getName()))
            biomeSouthEastDiffers = true;
        if(chunkGrid.getSouthWest() != null && !chunkGrid.getSouthWest().getBiome().getName().equals(this.getName()))
            biomeSouthWestDiffers = true;

        SimplexNoise noise = new SimplexNoise(chunk.getRegion().getWorld().getSeed(), chunk.getAbsBlockOffset());
        noise.setScaling(8, 32);
        noise.setFeatureSize(1f);
        noise.setSpreadFactor(1f);

        List<Float> northChance = new ArrayList<>(Chunk.size);
        List<Float> southChance = new ArrayList<>(Chunk.size);
        List<Float> eastChance = new ArrayList<>(Chunk.size);
        List<Float> westChance = new ArrayList<>(Chunk.size);

        List<Float> multiNorthChance = new ArrayList<>(Chunk.size);
        List<Float> multiSouthChance = new ArrayList<>(Chunk.size);
        List<Float> multiEastChance = new ArrayList<>(Chunk.size);
        List<Float> multiWestChance = new ArrayList<>(Chunk.size);

        while(multiNorthChance.size() < Chunk.size) multiNorthChance.add(0f);
        while(multiSouthChance.size() < Chunk.size) multiSouthChance.add(0f);
        while(multiEastChance.size() < Chunk.size) multiEastChance.add(0f);
        while(multiWestChance.size() < Chunk.size) multiWestChance.add(0f);

        // Moving along Z axis
        for(int i = 0; i < Chunk.size; i++)
        {
            if(biomeWestDiffers)
                westChance.add(noise.gen(xOffset, zOffset + i));
            if(biomeEastDiffers)
                eastChance.add(noise.gen(xOffset + Chunk.size, zOffset + i));

            if(biomeSouthWestDiffers)
                multiWestChance.set(i, noise.gen(xOffset, zOffset + i) - i - innerBlendSquashingFactor);
            if(biomeSouthEastDiffers)
                multiEastChance.set(i, noise.gen(xOffset + Chunk.size, zOffset + i) - i - innerBlendSquashingFactor);

            if(biomeNorthWestDiffers)
                multiWestChance.set(Chunk.size - 1 - i, noise.gen(xOffset, zOffset + Chunk.size - 1 - i) - i - innerBlendSquashingFactor);
            if(biomeNorthEastDiffers)
                multiEastChance.set(Chunk.size - 1 - i, noise.gen(xOffset + Chunk.size, zOffset + Chunk.size - 1 - i) - i - innerBlendSquashingFactor);
        }

        // Moving along X axis
        for(int i = 0; i < Chunk.size; i++)
        {
            if(biomeSouthDiffers)
                southChance.add(noise.gen(xOffset + i, zOffset));
            if(biomeNorthDiffers)
                northChance.add(noise.gen(xOffset + i, zOffset + Chunk.size));

            if(biomeSouthWestDiffers)
                multiSouthChance.set(i, noise.gen(xOffset + i, zOffset) - i - innerBlendSquashingFactor);
            if(biomeSouthEastDiffers)
                multiSouthChance.set(Chunk.size - 1 - i, noise.gen(xOffset + Chunk.size - 1 - i, zOffset) - i - innerBlendSquashingFactor);

            if(biomeNorthWestDiffers)
                multiNorthChance.set(i, noise.gen(xOffset + i, zOffset) - i - innerBlendSquashingFactor);
            if(biomeNorthEastDiffers)
                multiNorthChance.set(Chunk.size - 1 - i, noise.gen(xOffset + Chunk.size - 1 - i, zOffset + Chunk.size) - i - innerBlendSquashingFactor);
        }

        for (int i = 0; i < Chunk.size; i++) {
            for (int j = 0; j < Chunk.size; j++) {

                Block currentCore = getCore();
                Block currentRock = getRock();
                Block currentCrust = getCrust();
                Block currentSurface = getSurface();
                Block currentWater = getWater();

                if(biomeSouthWestDiffers && (j < multiSouthChance.get(i) || i < multiWestChance.get(j)))
                {
                    currentCore = chunkGrid.getSouthWest().getBiome().getCore();
                    currentRock = chunkGrid.getSouthWest().getBiome().getRock();
                    currentCrust = chunkGrid.getSouthWest().getBiome().getCrust();
                    currentSurface = chunkGrid.getSouthWest().getBiome().getSurface();
                    currentWater = chunkGrid.getSouthWest().getBiome().getWater();
                }

                if(biomeSouthEastDiffers && (j < multiSouthChance.get(i) || i > (Chunk.size - multiEastChance.get(j))))
                {
                    currentCore = chunkGrid.getSouthEast().getBiome().getCore();
                    currentRock = chunkGrid.getSouthEast().getBiome().getRock();
                    currentCrust = chunkGrid.getSouthEast().getBiome().getCrust();
                    currentSurface = chunkGrid.getSouthEast().getBiome().getSurface();
                    currentWater = chunkGrid.getSouthEast().getBiome().getWater();
                }

                if(biomeNorthWestDiffers && (j > (Chunk.size - multiNorthChance.get(i)) || i < multiWestChance.get(j)))
                {
                    currentCore = chunkGrid.getNorthWest().getBiome().getCore();
                    currentRock = chunkGrid.getNorthWest().getBiome().getRock();
                    currentCrust = chunkGrid.getNorthWest().getBiome().getCrust();
                    currentSurface = chunkGrid.getNorthWest().getBiome().getSurface();
                    currentWater = chunkGrid.getNorthWest().getBiome().getWater();
                }

                if(biomeNorthEastDiffers && (j > (Chunk.size - multiNorthChance.get(i)) || i > (Chunk.size - multiEastChance.get(j))))
                {
                    currentCore = chunkGrid.getNorthEast().getBiome().getCore();
                    currentRock = chunkGrid.getNorthEast().getBiome().getRock();
                    currentCrust = chunkGrid.getNorthEast().getBiome().getCrust();
                    currentSurface = chunkGrid.getNorthEast().getBiome().getSurface();
                    currentWater = chunkGrid.getNorthEast().getBiome().getWater();
                }

                if(biomeNorthDiffers && j > (Chunk.size - northChance.get(i)))
                {
                    currentCore = chunkGrid.getNorth().getBiome().getCore();
                    currentRock = chunkGrid.getNorth().getBiome().getRock();
                    currentCrust = chunkGrid.getNorth().getBiome().getCrust();
                    currentSurface = chunkGrid.getNorth().getBiome().getSurface();
                    currentWater = chunkGrid.getNorth().getBiome().getWater();
                }

                if(biomeEastDiffers && i > (Chunk.size - eastChance.get(j)))
                {
                    currentCore = chunkGrid.getEast().getBiome().getCore();
                    currentRock = chunkGrid.getEast().getBiome().getRock();
                    currentCrust = chunkGrid.getEast().getBiome().getCrust();
                    currentSurface = chunkGrid.getEast().getBiome().getSurface();
                    currentWater = chunkGrid.getEast().getBiome().getWater();
                }

                if(biomeSouthDiffers && j < southChance.get(i))
                {
                    currentCore = chunkGrid.getSouth().getBiome().getCore();
                    currentRock = chunkGrid.getSouth().getBiome().getRock();
                    currentCrust = chunkGrid.getSouth().getBiome().getCrust();
                    currentSurface = chunkGrid.getSouth().getBiome().getSurface();
                    currentWater = chunkGrid.getSouth().getBiome().getWater();
                }

                if(biomeWestDiffers && i < westChance.get(j))
                {
                    currentCore = chunkGrid.getWest().getBiome().getCore();
                    currentRock = chunkGrid.getWest().getBiome().getRock();
                    currentCrust = chunkGrid.getWest().getBiome().getCrust();
                    currentSurface = chunkGrid.getWest().getBiome().getSurface();
                    currentWater = chunkGrid.getWest().getBiome().getWater();
                }

                int top;
                for (int k = (World.worldHeight - 1); k >= 0; k--) {
                    if (heightMap.get(i + xOffset, j + zOffset, k) == 1) {
                        top = k;
                    } else {
                        continue;
                    }

                    float gen = noise.gen(i,j);

                    int rockThickness = top + 1 - getCoreThickness() - getCrustThickness() - getSurfaceThickness();

                    if (rockThickness < 0) {
                        System.out.println("The top is at: " + top);
                        throw new Exception("Thickness is too much for generated height. Cannot continue.");
                    }

                    for (int w = 0; w < getCoreThickness(); w++) {
                        if (heightMap.get(i + xOffset, j + zOffset, w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, currentCore.copy());
                    }

                    int thickness = getCoreThickness();

                    for (int w = thickness; w < (thickness + rockThickness); w++) {
                        if (heightMap.get(i + xOffset, j + zOffset, w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, currentRock.copy());
                    }

                    thickness += rockThickness;

                    for (int w = thickness; w < (thickness + getCrustThickness()); w++) {
                        if (heightMap.get(i + xOffset, j + zOffset, w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, currentCrust.copy());
                    }

                    thickness += getCrustThickness();

                    for (int w = thickness; w < (thickness + getSurfaceThickness()); w++) {
                        if (heightMap.get(i + xOffset, j + zOffset, w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, currentSurface.copy());
                    }

                    if(top < seaLevel)
                    {
                        for(int w = seaLevel; w > top; w--)
                        {
                            chunk.setBlock(i,w,j, currentWater.copy());
                        }
                    }

                    break;
                }
            }
        }
    }

    @Override
    public void generate() {

    }
}
