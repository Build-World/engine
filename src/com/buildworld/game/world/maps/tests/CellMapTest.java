package com.buildworld.game.world.maps.tests;

import com.buildworld.engine.utils.noise.SimplexNoise;
import com.buildworld.game.world.areas.Chunk;
import com.buildworld.game.world.maps.types.CaveMap;
import org.joml.Vector2f;

import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class CellMapTest {

    //CellMap cellMap = new Cell
    public static void main(String[] args)
    {
        CaveMap caveMap = new CaveMap(Chunk.size * 4 + 4, Chunk.size * 4 + 4, 42, 84, new Vector2f(0f,0f));
        long start = System.nanoTime();
        caveMap.generate();
        long end = System.nanoTime();
        System.out.println(Arrays.deepToString(caveMap.getMap()));
        System.out.println("Time: " + (TimeUnit.NANOSECONDS.toMillis(end - start)) + "ms");

        for(int i = 0; i < caveMap.getX(); i++)
        {
            for(int k = (caveMap.getY() - 1); k >= 0; k--)
            {
                String line = "";
                for(int j = 0; j < caveMap.getZ(); j++)
                {
                    int result = (int)caveMap.get(i,j,k);
                    if(result == 0)
                    {
                        line += " ";
                    } else {
                        line += "#";
                    }
                }
                System.out.println(line);
            }
            System.out.println();
        }

        benchmark();


    }

    public static void benchmark()
    {
        SimplexNoise noise = new SimplexNoise();
        double max = 0, min = 0;

        for(int i = 0; i < 10000; i++)
        {
            for(int j = 0; j < 10000; j++)
            {
                //double gen = FastSimplexNoise.noise(i / 24f, j / 24f);
                double gen = noise.gen(i, j);
                if(gen > max)
                    max = gen;
                if(gen < min)
                    min = gen;
            }
        }

        System.out.println("Max: " + max);
        System.out.println("Min: " + min);
    }
}
