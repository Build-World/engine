package com.buildworld;

import com.buildworld.game.Game;

import java.io.File;

public class Main {

    public static void main(String[] args) throws Exception {
        System.setProperty("org.lwjgl.librarypath", new File("lib/natives").getAbsolutePath());
        Game.run();
    }

}
