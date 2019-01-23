package com.buildworld.game;

import com.buildworld.engine.io.MouseInput;
import com.buildworld.engine.graphics.Window;
import com.buildworld.game.blocks.properties.BlockPropertyService;
import com.buildworld.game.blocks.BlockService;
import com.buildworld.game.events.UpdateService;
import com.buildworld.game.items.ItemService;
import com.buildworld.game.mod.ModLoader;
import com.buildworld.game.mod.ModService;
import com.buildworld.game.state.*;
import com.shawnclake.morgencore.core.component.services.Services;


public class Game {

    public static final int TARGET_FPS = 160;
    public static final int TARGET_TICKS = 40;
    public static final String name = "Build World";
    public static final String path = "D:\\Programming\\Projects\\Build-World";

    private double lastFps;

    private int fps;

    private boolean running = true;

    private final GameTime gameTime;

    private final Window window;

    private final MouseInput mouseInput;

    private final ModLoader modLoader;

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
        Window.WindowOptions opts = new Window.WindowOptions();

        opts.cullFace = true;
        opts.showFps = true;

        window = new Window(name, 640, 480, vSync, opts);
        gameTime = new GameTime(TARGET_TICKS, TARGET_FPS);
        mouseInput = new MouseInput();
        modLoader = new ModLoader(path + "\\mods");
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

        new UpdateService();

        new BlockService();
        new BlockPropertyService();
        new ItemService();

        new ModService();
        modLoader.onBoot(window);
    }

    // Loads features and services
    // Loads blocks, items, mods, etc.
    public void load() throws Exception
    {
        modLoader.onLoad();
    }

    // After loading is done, this method is called to get things ready to begin looping and ticking
    public void ready() throws Exception
    {
        Services.getService(GameStateService.class).ready();
        modLoader.onReady();
    }

    // Called when we are ready to begin looping
    public void play() throws Exception
    {
        modLoader.onPlay();

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
            this.tick(this.gameTime.getDelta());
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
    public void input() throws Exception
    {
        mouseInput.input(window);
        Services.getService(GameStateService.class).input(window, mouseInput);
    }

    public void draw() throws Exception
    {
        Services.getService(GameStateService.class).render(window);
        modLoader.onDraw();
        window.update();
    }

    public void draw(float alpha) throws Exception
    {
        Services.getService(GameStateService.class).render(window);
        modLoader.onDraw();
        window.update();
    }

    public void tick() throws Exception
    {
        Services.getService(GameStateService.class).update(1f, mouseInput);
        modLoader.onTick();
        Services.getService(UpdateService.class).trigger();
    }

    public void tick(float delta) throws Exception
    {
        Services.getService(GameStateService.class).update(delta, mouseInput);
        modLoader.onTick();
        Services.getService(UpdateService.class).trigger();
    }

}
