package com.buildworld.game.interactables;

import com.buildworld.engine.graphics.camera.Camera;
import com.buildworld.engine.graphics.game.Renderable;
import org.joml.Intersectionf;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class CameraBoxSelectionDetector {

    private final Vector3f max;

    private final Vector3f min;

    private final Vector2f nearFar;

    private Vector3f dir;

    public CameraBoxSelectionDetector() {
        dir = new Vector3f();
        min = new Vector3f();
        max = new Vector3f();
        nearFar = new Vector2f();
    }

    public void selectGameItem(Renderable[] renderables, Camera camera) {
        dir = camera.getViewMatrix().positiveZ(dir).negate();
        selectGameItem(renderables, camera.getPosition(), dir);
    }
    
    protected Renderable selectGameItem(Renderable[] renderables, Vector3f center, Vector3f dir) {
        Renderable selectedRenderable = null;
        float closestDistance = Float.POSITIVE_INFINITY;

        for (Renderable renderable : renderables) {
            renderable.setSelected(false);
            min.set(renderable.getPosition());
            max.set(renderable.getPosition());
            min.add(-renderable.getScale(), -renderable.getScale(), -renderable.getScale());
            max.add(renderable.getScale(), renderable.getScale(), renderable.getScale());
            if (Intersectionf.intersectRayAab(center, dir, min, max, nearFar) && nearFar.x < closestDistance) {
                closestDistance = nearFar.x;
                selectedRenderable = renderable;
            }
        }

        if (selectedRenderable != null) {
            selectedRenderable.setSelected(true);
        }
        return selectedRenderable;
    }
}
