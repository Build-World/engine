package com.buildworld.game.state.states;

import com.buildworld.engine.MouseInput;
import com.buildworld.engine.graphics.*;
import com.buildworld.engine.graphics.mesh.CubeMesh;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.utils.PerlinNoise;
import com.buildworld.engine.utils.SimplexNoise;
import com.buildworld.game.GameItem;
import com.buildworld.game.state.State;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.lwjgl.glfw.GLFW.*;

public class GameState implements State {

    private static final float MOUSE_SENSITIVITY = 0.2f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private List<GameItem> gameItems = new ArrayList<>();

    private static final float CAMERA_POS_STEP = 0.2f;

    public GameState() {
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f(0f, 0f, 0f);
    }

    @Override
    public void init(Window window) throws Exception {
        renderer.init(window);

        Texture texture = new Texture("D:\\Programming\\Projects\\Build-World\\engine\\resources/textures/grassblock.png");
        Mesh mesh = new CubeMesh(texture);

        SimplexNoise noise = new SimplexNoise();

        int dimension = 256;
        float feature = 32f;

        for(int i = 0; i < dimension; i++)
        {
            for(int j = 0; j < dimension; j++)
            {
                int height = (int)((noise.eval(i/feature,j/feature) + 1f) * 16f);
                for(int w = 0; w < height; w++)
                {
                    if(w != height - 1)
                        continue;
                    GameItem gameItem = new GameItem(mesh);
                    gameItem.setPosition(i,w,j);
                    gameItems.add(gameItem);
                }
            }

        }

//        GameItem gameItem1 = new GameItem(mesh);
//        gameItem1.setPosition(0, 0, -2);
//        GameItem gameItem2 = new GameItem(mesh);
//        gameItem2.setPosition(1f, 1f, -2);
//        GameItem gameItem3 = new GameItem(mesh);
//        gameItem3.setPosition(0, 0, -3f);
//        GameItem gameItem4 = new GameItem(mesh);
//        gameItem4.setPosition(1f, 0, -3f);
//        gameItems = new GameItem[]{gameItem1, gameItem2, gameItem3, gameItem4};
    }

    @Override
    public void load() {

    }

    @Override
    public void ready() {

    }

    @Override
    public void input(Window window, MouseInput mouseInput) {
        cameraInc.set(0, 0, 0);
        if (window.isKeyPressed(GLFW_KEY_W)) {
            cameraInc.z = -1;
        } else if (window.isKeyPressed(GLFW_KEY_S)) {
            cameraInc.z = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_A)) {
            cameraInc.x = -1;
        } else if (window.isKeyPressed(GLFW_KEY_D)) {
            cameraInc.x = 1;
        }
        if (window.isKeyPressed(GLFW_KEY_Z)) {
            cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_X)) {
            cameraInc.y = 1;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput) {
        // Update camera position
        camera.movePosition(cameraInc.x * CAMERA_POS_STEP, cameraInc.y * CAMERA_POS_STEP, cameraInc.z * CAMERA_POS_STEP);

        // Update camera based on mouse
        if (mouseInput.isRightButtonPressed()) {
            Vector2f rotVec = mouseInput.getDisplVec();
            camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        }

//        gameItems.remove(gameItems.size()-1);

    }

    @Override
    public void render(Window window) {
        renderer.render(window, camera, gameItems.toArray(new GameItem[0]));
    }

    @Override
    public void cleanup() {
        renderer.cleanup();
        for (GameItem gameItem : gameItems) {
            gameItem.getMesh().cleanUp();
        }
    }

    @Override
    public void enter() {

    }

    @Override
    public void exit() {

    }
}