package com.buildworld.game.world.generators;

import com.buildworld.engine.interfaces.IKeyNameDescibe;
import com.buildworld.engine.utils.brushes.BoxBlur;
import com.buildworld.engine.utils.noise.archive.SimplexNoise;
import com.buildworld.game.blocks.Block;
import com.buildworld.game.world.maps.types.FillHeightMap;
import com.buildworld.game.world.maps.types.HeightMap;
import com.buildworld.game.world.interfaces.IGenerate;
import com.buildworld.game.world.areas.Chunk;
import com.buildworld.game.world.areas.Region;
import com.buildworld.game.world.areas.World;
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

//    public Chunk populateChunk(Region region, FillHeightMap heightMap) throws Exception {
//
//        for (int i = 0; i < Chunk.size; i++) {
//            for (int j = 0; j < Chunk.size; j++) {
//                int height = (int)heightMap.get(i, j);
//
//                int rockThickness = height - coreThickness - crustThickness - surfaceThickness;
//
//                if (rockThickness < 0)
//                    throw new Exception("Thickness is too much for generated height. Cannot continue.");
//
//                for (int w = 0; w < coreThickness; w++) {
//                    chunk.setBlock(i, w, j, core.copy());
//                }
//
//                int thickness = coreThickness;
//
//                for (int w = thickness; w < (thickness + rockThickness); w++) {
//                    chunk.setBlock(i, w, j, rock.copy());
//                }
//
//                thickness += rockThickness;
//
//                for (int w = thickness; w < (thickness + crustThickness); w++) {
//                    chunk.setBlock(i, w, j, crust.copy());
//                }
//
//                thickness += crustThickness;
//
//                for (int w = thickness; w < (thickness + surfaceThickness); w++) {
//                    chunk.setBlock(i, w, j, surface.copy());
//                }
//
//            }
//        }
//
//        return chunk;
//    }

//    public Region generate() throws Exception {
//        return generateRegion();
//    }


}
