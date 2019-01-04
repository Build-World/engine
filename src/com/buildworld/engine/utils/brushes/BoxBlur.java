package com.buildworld.engine.utils.brushes;

import com.buildworld.game.world.maps.types.HeightMap;
import com.shawnclake.morgencore.core.component.Numbers;

//http://www.jhlabs.com/ip/blurring.html

public class BoxBlur {

    public static HeightMap blur(HeightMap input, int radius, int iterations, int x, int y, int z)
    {
        if(iterations < 1)
            return input;

        HeightMap outputHalf = blur(input, radius, x,y,z);
        HeightMap outputFull = blur(outputHalf, radius, z,y,x);

        for(int i = 0; i < (iterations-1); i++)
        {
            outputHalf = blur(outputFull, radius, x,y,z);
            outputFull = blur(outputHalf, radius, z,y,x);
        }

        return outputFull;
    }

    private static HeightMap blur(HeightMap input, int radius, int x, int y, int z) // x,y,z are the width, height, and depth bounds of a chunk
    {
        /*
        x -> width
        z -> height
        y -> Possible data size (in this case possible heights are from 0 to world height)
        input -> in
        output -> out
        height -> ta,tr,tg,tb
         */

        HeightMap output = new HeightMap(input.getX(), input.getZ());

        int widthMinus1 = x - 1;
        int tableSize = 2 * radius + 1;
        int[] divide = new int[y * tableSize];

        for ( int i = 0; i < y*tableSize; i++ )
            divide[i] = i/tableSize;

        int inIndex = 0;

        for ( int j = 0; j < z; j++ ) {
            int outIndex = j;
            int height = 0;

            for ( int i = -radius; i <= radius; i++ ) {
                height += input.get(inIndex + (int)Numbers.clamp(i, 0, x - 1));
            }

            for ( int k = 0; k < x; k++ ) {
                output.set(outIndex, divide[height]);

                int i1 = k+radius+1;
                if ( i1 > widthMinus1 )
                    i1 = widthMinus1;
                int i2 = k-radius;
                if ( i2 < 0 )
                    i2 = 0;
                int h1 = (int)input.get(inIndex+i1);
                int h2 = (int)input.get(inIndex+i2);

                height += h1 - h2;

                outIndex += z;
            }
            inIndex += x;
        }

        return output;
    }

}
