package com.buildworld.game.world.maps.types;

import com.buildworld.engine.utils.noise.SimplexNoise;
import com.buildworld.game.world.maps.CellMap;
import com.shawnclake.morgencore.core.component.Numbers;
import org.joml.Vector2f;


public class CaveMap extends CellMap {

    private SimplexNoise noise;
    private Vector2f blockOffset;
    private int featureSize = 1;

    public CaveMap(int x, int z, int y, int seed, Vector2f blockOffset) {
        super(x, z, y);
        this.blockOffset = blockOffset;
        noise = new SimplexNoise(seed);
        noise.setFeatureSize(0.540540f);
    }

    @Override
    protected float getChance(int x, int z, int y) {
        return (float)Numbers.scale(noise.gen((x + blockOffset.x) / featureSize, y, (z + blockOffset.y) / featureSize), -1, 1, 0, 1);
    }

    @Override
    protected int getOutOfBoundsCell(int x, int z, int y) {
        return 1;
    }
}
