package com.buildworld.game;

import com.buildworld.engine.io.MouseInput;
import com.buildworld.engine.graphics.Window;
import com.buildworld.game.blocks.BlockService;
import com.buildworld.game.items.ItemService;
import com.buildworld.game.state.*;
import com.buildworld.game.state.states.GameState;
import com.shawnclake.morgencore.core.component.services.Services;


public class Game {

    public static final int TARGET_FPS = 160;
    public static final int TARGET_TICKS = 40;
    public static final String name = "Build World";

    private boolean running = true;

    private final GameTime gameTime;

    private final Window window;

    private final MouseInput mouseInput;

    public static void run() throws Exception
    {
        Game game = new Game();
        game.boot();
        game.load();
        game.ready();
        game.play();
        game.dispose();
    }

    public Game() {
        boolean vSync = false;
        window = new Window(name, 640, 480, vSync);
        gameTime = new GameTime(TARGET_TICKS, TARGET_FPS);
        mouseInput = new MouseInput();
    }

    public void dispose() {
        /* Set empty state to trigger the exit method in the current state */
        Services.getService(GameStateService.class).cleanup();
    }

    // Called when first launching app and sets up objects and instances
    // In other words, it gets the engine ready to work
    public void boot() throws Exception
    {
        window.init();
        mouseInput.init(window);

        new GameStatesService();
        new GameStateService();

        State state = new GameState();
        state.init(window);
        Services.getService(GameStatesService.class).add(state);

        new BlockService();
        new ItemService();
    }

    // Loads features and services
    // Loads blocks, items, mods, etc.
    public void load()
    {
        Services.getService(GameStateService.class).change(GameState.class);
    }

    // After loading is done, this method is called to get things ready to begin looping and ticking
    public void ready()
    {
        Services.getService(GameStateService.class).ready();
    }

    // Called when we are ready to begin looping
    public void play() throws Exception
    {
        while(running && !window.windowShouldClose())
        {
            this.loop();
        }
    }

    // Loop controls the game flow
    public void loop() throws Exception
    {
        this.input();

        this.gameTime.update();

        while(this.gameTime.isTick())
        {
            this.tick();
            this.gameTime.postTick();
        }

        this.gameTime.preDraw();
        this.draw(this.gameTime.getAlpha());
        this.gameTime.postDraw();

        this.gameTime.printDebug();

        if ( !window.isvSync() ) {
            this.gameTime.sync();
        }
    }

    // Handle user input
    public void input()
    {
        mouseInput.input(window);
        Services.getService(GameStateService.class).input(window, mouseInput);
    }

    public void draw()
    {
        Services.getService(GameStateService.class).render(window);
        window.update();
    }

    public void draw(float alpha)
    {
        Services.getService(GameStateService.class).render(window);
        window.update();
    }

    public void tick() throws Exception
    {
        Services.getService(GameStateService.class).update(1f, mouseInput);
    }

    public void tick(float delta) throws Exception
    {
        Services.getService(GameStateService.class).update(delta, mouseInput);
    }

}
