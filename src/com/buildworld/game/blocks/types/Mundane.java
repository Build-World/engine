package com.buildworld.game.blocks.types;

public class Mundane implements IBlockType {
    private static Mundane instance;

    @Override
    public String getDescription() {
        return "Blocks which exist only as scenic objects.";
    }

    public static IBlockType make() {
        if(instance == null)
            instance = new Mundane();
        return instance;
    }

    @Override
    public String getKey() {
        return "mundane";
    }

    @Override
    public String getName() {
        return "Mundane";
    }
}
