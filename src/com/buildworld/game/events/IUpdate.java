package com.buildworld.game.events;

public interface IUpdate extends IUpdateable {
    void update() throws Exception;
    boolean requiresUpdates() throws Exception;
}
