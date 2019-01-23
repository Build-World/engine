package com.buildworld.game.world.generators;

import com.buildworld.engine.interfaces.IKeyNameDescibe;
import com.buildworld.game.blocks.Block;
import com.buildworld.game.world.RegionState;
import com.buildworld.game.world.areas.Chunk;
import com.buildworld.game.world.areas.Region;
import com.buildworld.game.world.areas.World;
import com.buildworld.game.world.interfaces.IGenerate;
import com.buildworld.game.world.maps.types.FillHeightMap;
import org.joml.Vector2f;

abstract public class Biome implements IGenerate, IKeyNameDescibe {

    private int surfaceThickness = 1;
    private int crustThickness = 3;
    private int coreThickness = 1;

    private int hydration = 1;
    private int floral = 1;

    private int temperature;
    private int temperatureRange;
    private int humidity;
    private int humidityRange;

    private Block surface;
    private Block crust;
    private Block rock;
    private Block core;

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

    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temperature) {
        this.temperature = temperature;
    }

    public int getTemperatureRange() {
        return temperatureRange;
    }

    public void setTemperatureRange(int temperatureRange) {
        this.temperatureRange = temperatureRange;
    }

    public int getHumidity() {
        return humidity;
    }

    public void setHumidity(int humidity) {
        this.humidity = humidity;
    }

    public int getHumidityRange() {
        return humidityRange;
    }

    public void setHumidityRange(int humidityRange) {
        this.humidityRange = humidityRange;
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

    public Region generateRegion(FillHeightMap fillHeightMap, Region region) throws Exception
    {
        for(int i = 0; i < Region.size; i++)
        {
            for(int j = 0; j < Region.size; j++)
            {
                region.setChunk(i, j, createFilledChunk(region, fillHeightMap, new Vector2f(i,j)));
            }
        }
        return region;
    }

    public Chunk createFilledChunk(Region region, FillHeightMap heightMap, Vector2f chunkOffset) throws Exception {

        Chunk chunk = new Chunk();
        chunk.setRegion(region);
        chunk.setLocation(chunkOffset);

        int xOffset = (int)chunkOffset.x * Chunk.size;
        int zOffset = (int)chunkOffset.y * Chunk.size;

        for (int i = 0; i < Chunk.size; i++) {
            for (int j = 0; j < Chunk.size; j++) {
                int top;
                for(int k = (World.worldHeight - 1); k >= 0; k--)
                {                    
                    if(heightMap.get(i + xOffset , j + zOffset,k) == 1) {
                        top = k;
                    } else {
                        continue;
                    }

                    int rockThickness = top + 1 - getCoreThickness() - getCrustThickness() - getSurfaceThickness();

                    if (rockThickness < 0)
                        throw new Exception("Thickness is too much for generated height. Cannot continue.");

                    for (int w = 0; w < getCoreThickness(); w++) {
                        if(heightMap.get(i + xOffset , j + zOffset,w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, getCore().copy());
                    }

                    int thickness = getCoreThickness();

                    for (int w = thickness; w < (thickness + rockThickness); w++) {
                        if(heightMap.get(i + xOffset , j + zOffset,w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, getRock().copy());
                    }

                    thickness += rockThickness;

                    for (int w = thickness; w < (thickness + getCrustThickness()); w++) {
                        if(heightMap.get(i + xOffset , j + zOffset,w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, getCrust().copy());
                    }

                    thickness += getCrustThickness();

                    for (int w = thickness; w < (thickness + getSurfaceThickness()); w++) {
                        if(heightMap.get(i + xOffset , j + zOffset,w) == 0)
                            continue;
                        chunk.setBlock(i, w, j, getSurface().copy());
                    }

                    break;
                }
            }
        }

        return chunk;
    }

    public Region generate(FillHeightMap fillHeightMap, Region region) throws Exception {
        return generateRegion(fillHeightMap, region);
    }

    @Override
    public void generate() {

    }
}
