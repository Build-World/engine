package com.buildworld.game.world.maps;

import com.buildworld.engine.utils.noise.INoise;

public class GenerationMap2D {

    private float[][] map;
    private int x, z;
    private INoise noise;

    public GenerationMap2D(int x, int z) {
        this.x = x;
        this.z = z;
        this.map = new float[x][z];
    }

    public float[][] getMap() {
        return map;
    }

    public void setMap(float[][] map) {
        this.map = map;
    }

    public int getX() {
        return x;
    }

    public int getZ() {
        return z;
    }

    public INoise getNoise() {
        return noise;
    }

    public void setNoise(INoise noise) {
        this.noise = noise;
    }

    public void initializeWithNoise()
    {
        for(int i = 0; i < x; i++)
        {
            for(int j = 0; j < z; j++)
            {
                set(i,j,noise.gen((float)i,(float)j));
            }
        }
    }

    public float get(int x, int z)
    {
        return map[x][z];
    }

    public float get(int flatIndex)
    {
        if(flatIndex == 0)
            return get(0,0);
        int ix = flatIndex / x;
        int iz = flatIndex % z;
        return get(ix, iz);
    }

    public void set(int x, int z, float y)
    {
        map[x][z] = y;
    }

    public void set(int flatIndex, float y)
    {
        if(flatIndex == 0)
            set(0,0,y);
        int ix = flatIndex / x;
        int iz = flatIndex % z;
        set(ix, iz, y);
    }

}
