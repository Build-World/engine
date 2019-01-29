package com.buildworld.game.hud;

import com.buildworld.engine.graphics.Window;

public interface IHud {

    void init(Window window) throws Exception;
    void render(Window window);

}
