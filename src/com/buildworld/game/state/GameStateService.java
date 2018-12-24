package com.buildworld.game.state;

import com.buildworld.engine.io.MouseInput;
import com.buildworld.engine.graphics.Window;
import com.shawnclake.morgencore.core.component.services.Service;
import com.shawnclake.morgencore.core.component.services.Services;

public class GameStateService extends Service implements State {
    /**
     * Current active state.
     */
    private State currentState;

    /**
     * Creates a state machine.
     */
    public GameStateService() {
        super();
    }

    /**
     * Changes the current state.
     *
     */
    public <T extends State> void change(Class<T> state) {
        if(currentState != null)
            currentState.exit();
        currentState = Services.getService(GameStatesService.class).getItem(state);
        if(currentState != null)
            currentState.enter();
    }

    public State getCurrentState() {
        return currentState;
    }

    @Override
    public void init(Window window) throws Exception {
        this.currentState.init(window);
    }

    @Override
    public void load() {
        this.currentState.load();
    }

    @Override
    public void ready() {
        this.currentState.ready();
    }

    @Override
    public void enter() {
        this.currentState.enter();
    }

    @Override
    public void exit() {
        this.currentState.exit();
    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        this.currentState.input(window, mouseInput);
    }

    @Override
    public void update(float interval, MouseInput mouseInput) throws Exception{
        this.currentState.update(interval, mouseInput);
    }

    @Override
    public void render(Window window) {
        this.currentState.render(window);
    }

    @Override
    public void cleanup() {
        this.currentState.cleanup();
    }
}
