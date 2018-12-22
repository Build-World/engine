package com.buildworld.game.state;

import com.buildworld.game.Game;

public interface State {
    /**
     * Handles input of the state.
     */
    void input();

    /**
     * Updates the state (fixed timestep).
     */
    default void update() {
        update(1f / Game.TARGET_TICKS);
    }

    /**
     * Updates the state (variable timestep)
     *
     * @param delta Time difference in seconds
     */
    void update(float delta);

    /**
     * Renders the state (no interpolation).
     */
    default void render() {
        render(1f);
    }

    /**
     * Renders the state (with interpolation).
     *
     * @param alpha Alpha value, needed for interpolation
     */
    void render(float alpha);

    default void load() {}

    default void ready() {}

    /**
     * Gets executed when entering the state, useful for initialization.
     */
    void enter();

    /**
     * Gets executed when leaving the state, useful for disposing.
     */
    void exit();
}
