package com.buildworld.game;

import com.buildworld.game.blocks.BlockService;
import com.buildworld.game.items.ItemService;
import com.buildworld.graphics.RenderService;
import com.buildworld.graphics.bootstrap.Window;
import com.buildworld.graphics.bootstrap.Renderer;
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

    private Renderer renderer = new Renderer();

    public static Game run()
    {
        Game game = new Game();
        game.boot();
        game.load();
        game.ready();
        game.play();
        game.dispose();
        return game;
    }

    /**
     * Releases resources that where used by the game.
     */
    public void dispose() {
        /* Dipose renderer */
        renderer.dispose();

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
        errorCallback = GLFWErrorCallback.createPrint();
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

        new BlockService();
        new ItemService();
        new RenderService();
    }

    // Loads features and services
    // Loads blocks, items, mods, etc.
    public void load()
    {

    }

    // After loading is done, this method is called to get things ready to begin looping and ticking
    public void ready()
    {

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

    }

    // ALL OpenGL shananigans should begin in here
    // This is called each frame
    // Mostly GPU bound
    public void draw()
    {
        //renderer.draw();
    }

    public void draw(float alpha)
    {

    }

    // CPU based world updates
    public void tick()
    {

    }

    public void tick(float delta)
    {

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
