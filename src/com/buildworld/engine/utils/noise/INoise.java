package com.buildworld.engine.utils.noise;

public interface INoise {
    float gen(float x, float y);
    float gen(float x, float y, float z);
    float gen(float x, float y, float z, float w);
    void setSeed(int seed);
    int getSeed();
}
