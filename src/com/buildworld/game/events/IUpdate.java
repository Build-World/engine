package com.buildworld.game.events;

public interface IUpdate extends IUpdateable {
    void update();
    boolean requiresUpdates();
}
