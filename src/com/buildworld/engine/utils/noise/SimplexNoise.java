package com.buildworld.engine.utils.noise;

import com.shawnclake.morgencore.core.component.Numbers;
import org.joml.Vector2f;
import org.joml.Vector4f;

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
    private Vector4f offset;

    private float scaleNoiseMin = 0;
    private float scaleNoiseMax = 0;

    public SimplexNoise() {
        offset = new Vector4f(0f,0f,0f,0f);
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

    public SimplexNoise(int seed, Vector4f offset) {
        this(seed);
        setOffset(offset);
    }

    public SimplexNoise(int seed, Vector2f offset) {
        this(seed);
        setOffset(offset);
    }

    public SimplexNoise(int seed, float x, float y) {
        this(seed);
        setOffset(x,y);
    }

    public void setOffset(float x, float y)
    {
        this.offset.x = x;
        this.offset.y = y;
    }

    public void setOffset(float x, float y, float z, float w)
    {
        this.offset.x = x;
        this.offset.y = y;
        this.offset.z = z;
        this.offset.w = w;
    }

    public void setOffset(Vector4f offset)
    {
        setOffset(offset.x, offset.y, offset.z, offset.w);
    }

    public void setOffset(Vector2f offset)
    {
        setOffset(offset.x, offset.y);
    }

    public Vector4f getOffset() {
        return offset;
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

    @Override
    public void setSeed(int seed) {
        fastNoise.SetSeed(seed);
    }

    @Override
    public int getSeed() {
        return fastNoise.GetSeed();
    }

    public float getScaleNoiseMin() {
        return scaleNoiseMin;
    }

    public void setScaleNoiseMin(float scaleNoiseMin) {
        this.scaleNoiseMin = scaleNoiseMin;
    }

    public float getScaleNoiseMax() {
        return scaleNoiseMax;
    }

    public void setScaleNoiseMax(float scaleNoiseMax) {
        this.scaleNoiseMax = scaleNoiseMax;
    }

    public void setScaling(float min, float max)
    {
        setScaleNoiseMin(min);
        setScaleNoiseMax(max);
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

    private float scaleNoise(float gen)
    {
        float scaled = gen;
        if(scaleNoiseMax != scaleNoiseMin)
        {
            scaled = (float)Numbers.scale(gen, -1, 1, scaleNoiseMin, scaleNoiseMax);
        }
        return scaled;
    }

    @Override
    public float gen(float x, float y) {
        float gen = fastNoise.GetNoise((x+offset.x)/spreadFactor,(y+offset.y)/spreadFactor);
        gen = scaleNoise(gen);
        return gen;
    }

    @Override
    public float gen(float x, float y, float z) {
        float gen = fastNoise.GetNoise((x+offset.x)/spreadFactor,(y+offset.y)/spreadFactor,(z+offset.z)/spreadFactor);
        gen = scaleNoise(gen);
        return gen;
    }

    @Override
    public float gen(float x, float y, float z, float w) {
        float gen = fastNoise.GetSimplex((x+offset.x)/spreadFactor,(y+offset.y)/spreadFactor,(z+offset.z)/spreadFactor,(w+offset.w)/spreadFactor);
        gen = scaleNoise(gen);
        return gen;
    }

}
