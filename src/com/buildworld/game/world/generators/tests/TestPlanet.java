package com.buildworld.game.world.generators.tests;

import com.buildworld.game.world.generators.Planet;
import com.buildworld.game.world.maps.types.FillHeightMap;
import org.joml.Vector2f;

public class TestPlanet {
    public static void main(String[] args) throws Exception
    {
        Planet planet = new MyPlanet(420);
        FillHeightMap fillHeightMap = planet.generateChunk(new Vector2f(0,0));




        for(int i = 0; i < fillHeightMap.getX(); i++)
        {
            for(int k = (fillHeightMap.getY() - 1); k >= 0; k--)
            {
                String line = String.format("%1$3s", k) + ": ";
                for(int j = 0; j < fillHeightMap.getZ(); j++)
                {
                    int result = (int)fillHeightMap.get(i,j,k);
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
    }
}
