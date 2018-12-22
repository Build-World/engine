package com.buildworld.game;

import com.buildworld.game.blocks.BlockService;
import com.buildworld.game.items.ItemService;
import com.buildworld.game.state.*;
import com.buildworld.game.state.states.EmptyState;
import com.buildworld.game.state.states.TestState;
import com.buildworld.graphics.RenderService;
import com.buildworld.graphics.bootstrap.Window;
import com.buildworld.graphics.bootstrap.Renderer;
import com.shawnclake.morgencore.core.component.services.Services;
import org.lwjgl.glfw.GLFWErrorCallback;
import org.lwjgl.opengl.GL;

import static org.lwjgl.glfw.GLFW.glfwTerminate;
import static org.lwjgl.glfw.GLFW.glfwInit;
import static org.lwjgl.glfw.GLFW.glfwSetErrorCallback;


public class Game {

    public static final int TARGET_FPS = 60;
    public static final int TARGET_TICKS = 40;
    public static final String name = "Build World";

    private GameTime gameTime;

    /**
     * The error callback for GLFW.
     */
    private GLFWErrorCallback errorCallback;

    private boolean running = true;

    private Window window;

    private Renderer renderer;

    public static void run()
    {
        Game game = new Game();
        game.boot();
        game.load();
        game.ready();
        game.play();
        game.dispose();
    }

    public Game() {
        renderer = new Renderer();
    }

    /**
     * Releases resources that where used by the game.
     */
    public void dispose() {
        /* Dipose renderer */
        renderer.dispose();

        /* Set empty state to trigger the exit method in the current state */
        Services.getService(GameStateService.class).change(null);

        /* Release window and its callbacks */
        window.destroy();

        /* Terminate GLFW and release the error callback */
        glfwTerminate();
        errorCallback.free();
    }

    // Called when first launching app and sets up objects and instances
    // In other words, it gets the engine ready to work
    public void boot()
    {
        /* Set error callback */
        errorCallback = GLFWErrorCallback.createPrint(System.err);
        glfwSetErrorCallback(errorCallback);

        /* Initialize GLFW */
        if (!glfwInit()) {
            throw new IllegalStateException("Unable to initialize GLFW!");
        }

        /* Create GLFW window */
        window = new Window(640, 480, name, false);

        gameTime = new GameTime(TARGET_TICKS, TARGET_FPS);

        /* Initialize renderer */
        renderer.init();

        new GameStatesService();
        new GameStateService();

        new BlockService();
        new ItemService();
        new RenderService(renderer);
    }

    // Loads features and services
    // Loads blocks, items, mods, etc.
    public void load()
    {
        State state = new TestState();
        state.load();
        Services.getService(GameStatesService.class).add(state);

        state = new EmptyState();
        state.load();
        Services.getService(GameStatesService.class).add(state);

        Services.getService(GameStateService.class).change(TestState.class);
    }

    // After loading is done, this method is called to get things ready to begin looping and ticking
    public void ready()
    {
        Services.getService(GameStateService.class).ready();
    }

    // Called when we are ready to begin looping
    public void play()
    {
        while(running)
        {
            this.loop();
        }
    }

    // Loop controls the game flow
    public void loop()
    {
        if(window.isClosing()){
            running = false;
            window.destroy();
        }

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

        /* Update window to show the new screen */
        window.update();

        /* Synchronize if v-sync is disabled */
        if (!window.isVSyncEnabled()) {
            this.gameTime.sync();
        }

    }

    // Handle user input
    public void input()
    {
        Services.getService(GameStateService.class).input();
    }

    public void draw()
    {
        Services.getService(GameStateService.class).render();
    }

    public void draw(float alpha)
    {
        Services.getService(GameStateService.class).render(alpha);
    }

    // CPU based world updates
    public void tick()
    {
        Services.getService(GameStateService.class).update();
    }

    public void tick(float delta)
    {
        Services.getService(GameStateService.class).update(delta);
    }

    /**
     * Determines if the OpenGL context supports version 3.2.
     *
     * @return true, if OpenGL context supports version 3.2, else false
     */
    public static boolean isDefaultContext() {
        return GL.getCapabilities().OpenGL32;
    }

}
