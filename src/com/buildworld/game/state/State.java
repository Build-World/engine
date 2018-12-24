package com.buildworld.game.state;

import com.buildworld.engine.io.MouseInput;
import com.buildworld.engine.graphics.Window;

public interface State {
    void init(Window window) throws Exception;

    void load();

    void ready();

    void enter();

    void exit();

    void input(Window window, MouseInput mouseInput);

    void update(float interval, MouseInput mouseInput) throws Exception;

    void render(Window window);

    void cleanup();
}
