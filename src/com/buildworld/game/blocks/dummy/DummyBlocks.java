package com.buildworld.game.blocks.dummy;

import com.buildworld.game.blocks.Block;

public class DummyBlocks {

    public static Block[][] getBlocks(int rows, int cols)
    {
        Block blocka = new DummyBlock();
        Block blockb = new DummyBlock();

        blocka.color.setRed(1.0f);
        blockb.color.setGreen(1.0f);

        Block[][] blocks = new Block[rows][cols];

        for(int i = 0; i < rows; i++)
        {
            for(int j=0; j<cols;j++)
            {
                int decider = (i % 2 + j) % 2;
                if(decider == 1)
                    blocks[i][j] = blocka;
                else
                    blocks[i][j] = blockb;
            }
        }

        return blocks;
    }

}
