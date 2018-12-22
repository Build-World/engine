package com.buildworld;

import com.buildworld.graphics.Renderer;
import org.lwjgl.*;
import org.lwjgl.glfw.*;
import org.lwjgl.opengl.*;
import org.lwjgl.system.*;

import java.nio.*;

import static org.lwjgl.glfw.Callbacks.*;
import static org.lwjgl.glfw.GLFW.*;
import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.system.MemoryStack.*;
import static org.lwjgl.system.MemoryUtil.*;

public class Main {

    // The window handle
    private long window;

    public void run() {
        System.out.println("Hello LWJGL " + Version.getVersion() + "!");

        //Start graphics in their own thread.
        Thread thread = new Thread(new Renderer(), "Renderer");
        thread.start();

        //Start a thread to run game logic.
        //Thread gameThread = new Thread();
        //gameThread.start();
    }

    public static void main(String[] args) {
        new Main().run();
    }

}
