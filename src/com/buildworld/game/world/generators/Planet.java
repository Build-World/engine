package com.buildworld.game.world.generators;

import com.buildworld.engine.interfaces.IKeyNameDescibe;
import com.buildworld.engine.utils.brushes.BoxBlur;
import com.buildworld.engine.utils.noise.SimplexNoise;
import com.buildworld.game.world.areas.Chunk;
import com.buildworld.game.world.areas.Region;
import com.buildworld.game.world.areas.World;
import com.buildworld.game.world.interfaces.IGenerate;
import com.buildworld.game.world.maps.types.*;
import org.joml.Vector2f;

import java.util.concurrent.TimeUnit;

abstract public class Planet implements IGenerate, IKeyNameDescibe {

    /**
     * The sea level of the planet
     * <p>
     * Lower values result in quicker generation
     * Higher values result in more detailed and interesting generation
     * <p>
     * This value should not be set below 64 as it would become far less interesting
     * This value should not be set above 256 as this could result in terrain generating very close to the world height limit
     * <p>
     * 5th stage: This value is added to the 4th stage to offset the final values vertically. 4th + 5th
     */
    private int seaLevel = 128;

    /**
     * 4th stage: This value is multiplied to the 3rd stage. 3rd * 4th
     * <p>
     * This value must be set in conjunction with the magnitude factor.
     * <p>
     * The range of terrain height variances can be estimated as:
     * Non-Inclusive Lower bound: ((-1 + magnitudeFactor) ^ stretchFactor) * heightFactor
     * Non-Inclusive Upper bound: ((1 + magnitudeFactor) ^ stretchFactor) * heightFactor
     */
    private int heightFactor = 64; // Multiplies generated height

    /**
     * 2nd stage: This value is added to the generated noise. noise + 2nd
     * <p>
     * Generated noise is returned in the range: (-1, 1)
     * <p>
     * This value is extremely touchy and any value higher than 2 will have substantial effect on the height map
     * For magnitudeFactor >= 1:
     * No concave generation will occur. (Generation will always be above sealevel)
     * <p>
     * For magnitudeFactor <= -1:
     * No convex generation will occur. (Generation will always be below sealevel)
     * <p>
     * For magnitudeFactor > -1 && magnitudeFactor < 1:
     * Both convex and concave generation will occur. (Generation can be above or below sea level)
     * <p>
     * Any value between [0.5, 1.5] is probably most effective
     * <p>
     * Negative values will create concave generation rather than convex generation
     * <p>
     * Negative values >= -2 will create substantial pit generation
     */
    private float magnitudeFactor = 0f;  // 2nd stage: This value is added to the generated noise value: (-1, 1)

    /**
     * 3rd stage: This value is used as the exponent to the 2nd stage. Math.pow(2nd, 3rd).
     * <p>
     * For magnitudeFactor >= 2:
     * This has a dramatic stretching effect for values > 1, and a dramatic compressing effect for values < 1
     * <p>
     * For magnitudeFactor > 0 && magnitudeFactor < 2:
     * This will have a stretching effect for some values and a compressing effect for other values
     * <p>
     * For magnitudeFactor <= 0:
     * This will have a modest stretching effect for values < 1, and a modest compressing effect for values > 1
     */
    private float stretchFactor = 1f;

    /**
     * Dictates the rate at which featureNoise changes sizes
     * Smaller values will generate more eratic terrain
     * Larger values will generate smoother terrain
     * Sort of defines the resolution of the generated noise
     * Overall, this value has a relatively subtle effect on generation
     * default: 16
     */
    private float frequencyNoiseFeatureSize = 16f;

    /**
     * Defines how spread out the frequency noise shall be
     * Essentially stretches the generated noise without modifying the resolution of the generated noise
     * Smaller values will generate eratic terrain
     * Larger values will generate smoother terrain
     * Overall, this value has a slightly more than subtle effect on generation
     * <p>
     * Values smaller than 1 are not recommended
     * <p>
     * default: 1
     */
    private float frequencyNoiseSpreadFactor = 1f;

    /**
     * HeightNoise provides a basis layer of noise on which we can apply transformations
     * Defines the resolution of the HeightNoise
     * Smaller values will generate eratic terrain
     * Larger values will generate smoother terrain
     * Overall, this value has a medium effect on generation
     * default: 2
     */
    private float heightNoiseFeatureSize = 2f;

    /**
     * Defines how spread out the height noise shall be
     * Essentially stretches the generated noise without modifying the resolution of the generated noise
     * Smaller values will generate eratic terrain
     * Larger values will generate smoother terrain
     * Overall, this value has a large effect on generation
     * <p>
     * Values smaller than 1 are not recommended
     * <p>
     * default: 4
     */
    private float heightNoiseSpreadFactor = 4f;

    /**
     * Defines how spread out the feature noise shall be
     * Essentially stretches the generated noise without modifying the resolution of the generated noise
     * Smaller values will generate eratic terrain
     * Larger values will generate smoother terrain
     * Overall, this value has a substantial effect on generation
     * <p>
     * Values smaller than 1 are not recommended
     * <p>
     * default: 2
     */
    private float featureNoiseSpreadFactor = 2f;

    /**
     * Scales the frequency noise when applying it to the feature noise
     * Smaller values will generate smoother terrain
     * Larger values will generate eratic terrain
     * Overall, this value has a medium effect on generation
     * <p>
     * A value of 0 will essentially turn off the frequency noise effect
     * <p>
     * default: 1
     */
    private float featureNoiseFeatureSizeScale = 1f;

    /**
     * Added to the frequency noise after scaling it before applying to th feature noise
     * Smaller values will generate eratic terrain
     * Larger values will generate smoother terrain
     * Overall, this value has a substantial effect on generation
     * <p>
     * Values smaller than 10 will generate very condensed terrain
     * <p>
     * This essentially defines the size of biomes and oceans
     * <p>
     * default: 18
     */
    private float featureNoiseFeatureSizeModifier = 18f;

    /**
     * Defines the % of generation that should become land vs ocean
     * A value of -1 will generate ocean only
     * A value of 1 will generate land only
     * A value of 0 will bias land and ocean equally.
     * <p>
     * This value should be between -1 and 1. Values outside of this range will create extremely eratic generation
     * <p>
     * default: 0
     */
    private float landToOceanRatio = 0f;

    /**
     * The % chance that caves that would generate through the surface layer will actually penetrate
     * This value should be between 0 and 1.
     * <p>
     * A value of 1 will make caves always pertrude
     * A value of 0 will make caves never pertrude
     * <p>
     * Caves will never pertrude below the sea level.
     * <p>
     * Lower values means caves will only pertrude if they are close to but above sea level
     * Larger values will allow caves to pertrude at greater elevations
     * <p>
     * This value does not effect a caves ability to pertrude on the horizontals axis
     * <p>
     * default: 0.1
     */
    private float cavePertrusionThreshhold = 0.1f;

    /**
     * The thickness of cave pertrusion or potential cave pertrusion cover
     *
     * A value of 1 will always cover the vertical areas of a pertrusion, but horizontal axis pertrusions will still be probable
     *
     * A value greater than 1 will reduce the probability of a horizontal pertrusion while also reducing the size of a cave
     *
     * A value of 0 is essentially the same as setting the cave pertrusion threshhold to 0 and will completely disable the covering of pertrusions
     */
    private int cavePertrusionThickness = 1;

    /**
     * Defines the minimum surface thickness.
     * <p>
     * If this value is greater than 0, the cavePertrusionThreshhold is ignored and no caves will pertrude through the surface
     * <p>
     * default: 0
     */
    private int minimumSurfaceThickness = 0;

    /**
     * The seed provides repeatable generation
     */
    private int seed;

    public Planet(int seed) {
        this.seed = seed;
    }

    public int getSeaLevel() {
        return seaLevel;
    }

    public void setSeaLevel(int seaLevel) {
        this.seaLevel = seaLevel;
    }

    public int getHeightFactor() {
        return heightFactor;
    }

    public void setHeightFactor(int heightFactor) {
        this.heightFactor = heightFactor;
    }

    public float getMagnitudeFactor() {
        return magnitudeFactor;
    }

    public void setMagnitudeFactor(float magnitudeFactor) {
        this.magnitudeFactor = magnitudeFactor;
    }

    public float getStretchFactor() {
        return stretchFactor;
    }

    public void setStretchFactor(float stretchFactor) {
        this.stretchFactor = stretchFactor;
    }

    public float getFrequencyNoiseFeatureSize() {
        return frequencyNoiseFeatureSize;
    }

    public void setFrequencyNoiseFeatureSize(float frequencyNoiseFeatureSize) {
        this.frequencyNoiseFeatureSize = frequencyNoiseFeatureSize;
    }

    public float getFrequencyNoiseSpreadFactor() {
        return frequencyNoiseSpreadFactor;
    }

    public void setFrequencyNoiseSpreadFactor(float frequencyNoiseSpreadFactor) {
        this.frequencyNoiseSpreadFactor = frequencyNoiseSpreadFactor;
    }

    public float getHeightNoiseFeatureSize() {
        return heightNoiseFeatureSize;
    }

    public void setHeightNoiseFeatureSize(float heightNoiseFeatureSize) {
        this.heightNoiseFeatureSize = heightNoiseFeatureSize;
    }

    public float getHeightNoiseSpreadFactor() {
        return heightNoiseSpreadFactor;
    }

    public void setHeightNoiseSpreadFactor(float heightNoiseSpreadFactor) {
        this.heightNoiseSpreadFactor = heightNoiseSpreadFactor;
    }

    public float getFeatureNoiseSpreadFactor() {
        return featureNoiseSpreadFactor;
    }

    public void setFeatureNoiseSpreadFactor(float featureNoiseSpreadFactor) {
        this.featureNoiseSpreadFactor = featureNoiseSpreadFactor;
    }

    public float getFeatureNoiseFeatureSizeScale() {
        return featureNoiseFeatureSizeScale;
    }

    public void setFeatureNoiseFeatureSizeScale(float featureNoiseFeatureSizeScale) {
        this.featureNoiseFeatureSizeScale = featureNoiseFeatureSizeScale;
    }

    public float getFeatureNoiseFeatureSizeModifier() {
        return featureNoiseFeatureSizeModifier;
    }

    public void setFeatureNoiseFeatureSizeModifier(float featureNoiseFeatureSizeModifier) {
        this.featureNoiseFeatureSizeModifier = featureNoiseFeatureSizeModifier;
    }

    public float getLandToOceanRatio() {
        return landToOceanRatio;
    }

    public void setLandToOceanRatio(float landToOceanRatio) {
        this.landToOceanRatio = landToOceanRatio;
    }

    public float getCavePertrusionThreshhold() {
        return cavePertrusionThreshhold;
    }

    public void setCavePertrusionThreshhold(float cavePertrusionThreshhold) {
        this.cavePertrusionThreshhold = cavePertrusionThreshhold;
    }

    public int getCavePertrusionThickness() {
        return cavePertrusionThickness;
    }

    public void setCavePertrusionThickness(int cavePertrusionThickness) {
        this.cavePertrusionThickness = cavePertrusionThickness;
    }

    public int getMinimumSurfaceThickness() {
        return minimumSurfaceThickness;
    }

    public void setMinimumSurfaceThickness(int minimumSurfaceThickness) {
        this.minimumSurfaceThickness = minimumSurfaceThickness;
    }

    public int getSeed() {
        return seed;
    }

    public Region generateRegion() throws Exception {
        Region region = new Region();

        return region;
    }

    public FillHeightMap generateChunk(Vector2f regionOffset) throws Exception {
        long start = System.nanoTime();
        Vector2f blockPos = new Vector2f(regionOffset).mul(Region.size).mul(Chunk.size);
        int regionLength = Region.size * Chunk.size;

        FillHeightMap fillHeightMap = new FillHeightMap(regionLength, regionLength, seaLevel * 2);

        HeightMap heightMap = new HeightMap(regionLength, regionLength);
        FeatureMap featureMap = new FeatureMap(regionLength, regionLength);
        FrequencyMap frequencyMap = new FrequencyMap(regionLength, regionLength);

        // the +4's are due to the 2 long boundares along all edges of the generated cell map
        CaveMap caveMap = new CaveMap(regionLength + 4, regionLength + 4, (seaLevel * 2) + 4, seed, blockPos);

        SimplexNoise frequencyNoise = new SimplexNoise(seed);
        frequencyNoise.setFeatureSize(frequencyNoiseFeatureSize);
        frequencyNoise.setSpreadFactor(frequencyNoiseSpreadFactor);
        frequencyMap.setNoise(frequencyNoise);
        frequencyMap.initializeWithNoise();

        caveMap.generate();

        SimplexNoise heightNoise = new SimplexNoise(seed);
        heightNoise.setFeatureSize(heightNoiseFeatureSize);
        heightNoise.setSpreadFactor(heightNoiseSpreadFactor);
        heightMap.setNoise(heightNoise);
        heightMap.initializeWithNoise();

        SimplexNoise featureNoise = new SimplexNoise(seed);
        featureNoise.setSpreadFactor(featureNoiseSpreadFactor);

        long check1 = System.nanoTime();

        for (int i = 0; i < regionLength; i++) {
            for (int j = 0; j < regionLength; j++) {
                featureNoise.setFeatureSize(((frequencyMap.get(i, j) + landToOceanRatio) * featureNoiseFeatureSizeScale) + featureNoiseFeatureSizeModifier);
                featureMap.set(i, j, featureNoise.gen(i, j));
                float input = heightMap.get(i, j) + 1;
                int output = (int) ((Math.pow((input + magnitudeFactor), stretchFactor)) * heightFactor * featureMap.get(i, j)) + seaLevel;
                if (output > (seaLevel * 2 - 1)) {
                    throw new Exception("2*sealevel is not a sufficient optimization");
                }
                for (int k = 0; k < output; k++) {
                    int isBlock = (int) caveMap.get(i + 2, j + 2, k);
                    // k -> 1, output -> 3, min -> 2    3-1 == 1
                    if ((output - k) <= minimumSurfaceThickness) {
                        fillHeightMap.set(i, j, k, 1);
                    } else if (k < (output - cavePertrusionThickness)) {
                        fillHeightMap.set(i, j, k, isBlock);
                    } else {
                        if (k < seaLevel) {
                            fillHeightMap.set(i, j, k, 1);
                        } else if (Math.abs(featureMap.get(i, j)) < cavePertrusionThreshhold) {
                            fillHeightMap.set(i, j, k, 0);
                        } else {
                            fillHeightMap.set(i, j, k, 1);
                        }
                    }
                }
            }
        }

        long check2 = System.nanoTime();

//        System.out.println("cave/height/freq: " + (TimeUnit.NANOSECONDS.toMillis(check1 - start)) + "ms");
//        System.out.println("feature/fill: " + (TimeUnit.NANOSECONDS.toMillis(check2 - check1)) + "ms");
//        System.out.println("total: " + (TimeUnit.NANOSECONDS.toMillis(check2 - start)) + "ms");

        return fillHeightMap;
    }

    public HeightMap smoothing(HeightMap heightMap) {
        return BoxBlur.blur(heightMap, 3, 2, Chunk.size, World.worldHeight, Chunk.size);
    }

    @Override
    public void generate() {

    }

}
