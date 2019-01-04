package com.buildworld.engine.utils.noise;

/**
 * SimplexNoise With Fractal
 *
 * Parameters:
 * - Frequency(f)
 *     Low values will greatly increase the feature size. For example, f=0.005 is great for wide surface terrain
 *     f = 0.02 is great for sort of normal generation
 *     As f increases, the resolution of the generation increases and it becomes more random looking
 *     f = 0.08 is great for seeding initial 3D cellular automata algorithms
 */
public class SimplexNoise implements INoise {

    private FastNoise fastNoise;
    private float spreadFactor = 1f;

    public SimplexNoise() {
        fastNoise = new FastNoise();
        fastNoise.SetNoiseType(FastNoise.NoiseType.SimplexFractal);
        fastNoise.SetFrequency(0.02f);
        fastNoise.SetFractalType(FastNoise.FractalType.FBM);
        fastNoise.SetFractalOctaves(5);
        fastNoise.SetFractalLacunarity(2f);
        fastNoise.SetFractalGain(0.5f);
    }

    public SimplexNoise(int seed) {
        this();
        setSeed(seed);
    }

    public FastNoise getFastNoise() {
        return fastNoise;
    }

    public float getSpreadFactor() {
        return spreadFactor;
    }

    public void setSpreadFactor(float spreadFactor) {
        this.spreadFactor = spreadFactor;
    }

    /**
     * For size < 1, feature sizes become shrinked. ie. size = 0.5 will have feature sizes half as big
     * For size > 1, feature sizes become enlarged. ie. size = 2 will have feature sizes twice as big
     * @param size
     */
    public void setFeatureSize(float size)
    {
        fastNoise.SetFrequency(1f / (size * 50f));
    }

    @Override
    public float gen(float x, float y) {
        return fastNoise.GetNoise(x/spreadFactor,y/spreadFactor);
    }

    @Override
    public float gen(float x, float y, float z) {
        return fastNoise.GetNoise(x/spreadFactor,y/spreadFactor,z/spreadFactor);
    }

    @Override
    public float gen(float x, float y, float z, float w) {
        return fastNoise.GetSimplex(x/spreadFactor,y/spreadFactor,z/spreadFactor,w/spreadFactor);
    }

    @Override
    public void setSeed(int seed) {
        fastNoise.SetSeed(seed);
    }

    @Override
    public int getSeed() {
        return fastNoise.GetSeed();
    }
}
