package com.buildworld.game;

import com.buildworld.game.blocks.BlockService;
import com.buildworld.game.items.ItemService;

import static org.lwjgl.glfw.GLFW.*;

public class Game {

    private GameTime gameTime = new GameTime();
    private long window;
    private boolean running = true;

    public static Game run()
    {
        Game game = new Game();
        game.boot();
        game.load();
        game.ready();
        game.play();
        return game;
    }

    // Called when first launching app and sets up objects and instances
    // In other words, it gets the engine ready to work
    public void boot()
    {
        new BlockService();
        new ItemService();
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
        this.gameTime.update();

        this.input();

        while(this.gameTime.isTick())
        {
            this.tick();
            this.gameTime.tick();
        }

        if(this.gameTime.isDraw())
        {
            this.draw();
            this.gameTime.draw();
        }

        this.gameTime.printDebug();

        /*if(glfwWindowShouldClose(window)){
            running = false;
        }*/

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

    }

    // CPU based world updates
    public void tick()
    {

    }

}
