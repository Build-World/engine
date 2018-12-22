package com.buildworld.game;

public class GameTime {

    private long lastTime = System.nanoTime();
    private double desired_ticks = 40.0;
    private double desired_fps = 60.0;
    private double ns = 1000000000 / desired_ticks;
    private double fps = 1000000000 / desired_fps;
    private double tick_delta = 0;
    private double fps_delta = 0;

    // For the debugging output
    private long timer = System.currentTimeMillis();
    private int frames = 0;
    private int updates = 0;

    public void update()
    {
        long now = System.nanoTime();
        tick_delta += (now - lastTime) / ns;
        fps_delta += (now - lastTime) / fps;

        lastTime = now;
    }

    public boolean isTick()
    {
        if(tick_delta >= 1)
            return true;
        return false;
    }

    public void tick()
    {
        tick_delta--;
        updates++;
    }

    public boolean isDraw()
    {
        if(fps_delta >= 1)
            return true;
        return false;
    }

    public void draw()
    {
        frames++;
        fps_delta = 0;
    }

    public void printDebug()
    {
        if (System.currentTimeMillis() - timer > 1000) {
            timer += 1000;
            System.out.println(updates + " UPS, " + frames + " FPS");
            updates = 0;
            frames = 0;
        }
    }

}
