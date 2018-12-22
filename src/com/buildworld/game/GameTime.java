package com.buildworld.game;

import java.util.logging.Level;
import java.util.logging.Logger;

public class GameTime {

    private Timer timer;
    private int desired_ticks, desired_fps;
    private float delta;
    private float accumulator = 0f;
    private float interval;
    private float alpha;
    private long debug_timer = System.currentTimeMillis();

    public GameTime(int desired_ticks, int desired_fps) {
        timer = new Timer();
        timer.init();
        this.desired_fps = desired_fps;
        this.desired_ticks = desired_ticks;
        interval = 1f / this.desired_ticks;
    }

    public float getAlpha() {
        return alpha;
    }

    public float getDelta() {
        return delta;
    }

    public float getAccumulator() {
        return accumulator;
    }

    public float getInterval() {
        return interval;
    }

    public void update()
    {
        /* Get delta time and update the accumulator */
        delta = timer.getDelta();
        accumulator += delta;
    }

    public boolean isTick()
    {
        return accumulator >= interval;
    }

    public void postTick()
    {
        timer.updateUPS();
        accumulator -= interval;
    }

    public void preDraw()
    {
        /* Calculate alpha value for interpolation */
        alpha = accumulator / interval;
    }

    public void postDraw()
    {
        timer.updateFPS();

        /* Update timer */
        timer.update();
    }

    public void sync()
    {
        this.syncHelper(desired_fps);
    }

    /**
     * Synchronizes the game at specified frames per second.
     *
     * @param fps Frames per second
     */
    public void syncHelper(int fps) {
        double lastLoopTime = timer.getLastLoopTime();
        double now = timer.getTime();
        float targetTime = 1f / fps;

        while (now - lastLoopTime < targetTime) {
            Thread.yield();

            /* This is optional if you want your game to stop consuming too much
             * CPU but you will loose some accuracy because Thread.sleep(1)
             * could sleep longer than 1 millisecond */
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(Game.class.getName()).log(Level.SEVERE, null, ex);
            }

            now = timer.getTime();
        }
    }

    public void printDebug()
    {
        if (System.currentTimeMillis() - debug_timer > 1000) {
            debug_timer += 1000;
            System.out.println(timer.getUPS() + " UPS, " + timer.getFPS() + " FPS");
        }
    }
}
