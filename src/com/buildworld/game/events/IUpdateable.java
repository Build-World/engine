package com.buildworld.game.events;

import com.buildworld.game.blocks.Block;

public interface IUpdateable {
    void update(Block updater) throws Exception;
}
