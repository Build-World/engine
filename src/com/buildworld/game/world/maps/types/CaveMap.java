package com.buildworld.game.world.maps.types;

import com.buildworld.engine.utils.noise.SimplexNoise;
import com.buildworld.game.world.maps.CellMap;
import com.shawnclake.morgencore.core.component.Numbers;
import org.joml.Vector2f;


public class CaveMap extends CellMap {

    private SimplexNoise noise;
    private Vector2f blockOffset;

    /**
     * Stretch Factor will remap coordinates to different areas of the noise map
     * Values > 1 will essentially stretch the noise map and map multiple coordinates to the same areas of the noise map
     * Values < 1 will produce unpredictable results as it will be skipping over parts of the noise map
     * Overall, this value has a large effect on generation
     * default: 1
     */
    private float stretchFactor = 1f;

    /**
     * Noise Feature Szze
     * Defines the resolution of the Cave Map Noise
     * Smaller values will generate eratic caves  - less cavernous
     * Larger values will generate smoother caves - much more cavernous
     * Overall, this value has a large effect on generation
     * default: 0.75
     */
    private float noiseFeatureSize = 0.75f;

    public CaveMap(int x, int z, int y, int seed, Vector2f blockOffset) {
        super(x, z, y);
        this.blockOffset = blockOffset;
        noise = new SimplexNoise(seed);
        //noise.setFeatureSize(0.540540f);
        noise.setFeatureSize(noiseFeatureSize);
    }

    public float getStretchFactor() {
        return stretchFactor;
    }

    public void setStretchFactor(float stretchFactor) {
        this.stretchFactor = stretchFactor;
    }

    public float getNoiseFeatureSize() {
        return noiseFeatureSize;
    }

    public void setNoiseFeatureSize(float noiseFeatureSize) {
        this.noiseFeatureSize = noiseFeatureSize;
    }

    @Override
    protected float getChance(int x, int z, int y) {
        return (float)Numbers.scale(noise.gen((x + blockOffset.x) / stretchFactor, y, (z + blockOffset.y) / stretchFactor), -1, 1, 0, 1);
    }

    @Override
    protected int getOutOfBoundsCell(int x, int z, int y) {
        return 1;
    }
}
