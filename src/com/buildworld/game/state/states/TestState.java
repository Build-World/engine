package com.buildworld.game.state.states;

import com.buildworld.game.state.State;

public class TestState implements State {
    @Override
    public void input() {

    }

    @Override
    public void update(float delta) {
        System.out.println("ticked with delta: " + delta);
    }

    @Override
    public void render(float alpha) {
        System.out.println("drew with alpha: " + alpha);
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}
