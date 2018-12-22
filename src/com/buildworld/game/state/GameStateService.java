package com.buildworld.game.state;

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

    @Override
    public void input() {
        currentState.input();
    }

    @Override
    public void update() {
        currentState.update();
    }

    @Override
    public void update(float delta) {
        currentState.update(delta);
    }

    @Override
    public void render() {
        currentState.render();
    }

    @Override
    public void render(float alpha) {
        currentState.render(alpha);
    }

    @Override
    public void enter() {
        currentState.enter();
    }

    @Override
    public void exit() {
        currentState.exit();
    }
}
