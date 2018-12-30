package com.buildworld.game.blocks.types;

public class Entity implements IBlockType {

    private static Entity instance;

    @Override
    public String getDescription() {
        return "Blocks which are interactable";
    }

    public static IBlockType make() {
        if(instance == null)
            instance = new Entity();
        return instance;
    }

    @Override
    public String getKey() {
        return "entity";
    }

    @Override
    public String getName() {
        return "Entity";
    }

}
