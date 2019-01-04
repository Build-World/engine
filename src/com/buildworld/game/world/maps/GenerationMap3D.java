package com.buildworld.game.world.maps;

import com.buildworld.engine.utils.noise.INoise;

public class GenerationMap3D {

    private float[][][] map;
    private int x, y, z;
    private INoise noise;

    public GenerationMap3D(int x, int z, int y) {
        this.x = x;
        this.y = y;
        this.z = z;
        this.map = new float[x][z][y];
    }

    public float[][][] getMap() {
        return map;
    }

    public void setMap(float[][][] map) {
        this.map = map;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
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
                for(int k = 0; k < y; k++)
                {
                    set(i,j,k,noise.gen((float)i,(float)j));
                }

            }
        }
    }

    public float get(int x, int z, int y)
    {
        return map[x][z][y];
    }

    public float get(int flatIndex)
    {
        if(flatIndex == 0)
            return get(0,0,0);
        int zy = z*y;
        int ix = flatIndex / zy;
        int remains = flatIndex % zy;
        int iz = remains / z;
        int iy = remains % y;
        return get(ix, iz, iy);
    }

    public void set(int x, int z, int y, float w)
    {
        map[x][z][y] = w;
    }

    public void set(int flatIndex, float w)
    {
        if(flatIndex == 0)
            set(0,0,0, w);
        int zy = z*y;
        int ix = flatIndex / zy;
        int remains = flatIndex % zy;
        int iz = remains / z;
        int iy = remains % y;
        set(ix, iz, iy, w);
    }
}
