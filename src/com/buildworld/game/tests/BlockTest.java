package com.buildworld.game.tests;

import com.buildworld.game.blocks.Block;
import com.buildworld.game.blocks.dummy.DummyBlocks;

public class BlockTest {

    public void test() {
        int rows = 10;
        int cols = 10;

        Block[][] blocks = DummyBlocks.getBlocks(rows, cols);

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                System.out.println("i:" + i + " j:" + j + " R:" + blocks[i][j].color.getRed() + " G:" + blocks[i][j].color.getGreen() + " B:" + blocks[i][j].color.getBlue());
            }
        }
    }

    public static void main(String[] args)
    {
        new BlockTest().test();
    }


}
