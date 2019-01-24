package com.buildworld.engine.graphics.animations;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.buildworld.engine.graphics.game.GameItem;
import com.buildworld.engine.graphics.mesh.Mesh;
import org.joml.Matrix4f;

public class AnimGameItem extends GameItem {

    private Map<String, Animation> animations;

    private Animation currentAnimation;

    public AnimGameItem(Mesh[] meshes, Map<String, Animation> animations) {
        super(meshes);
        this.animations = animations;
        Optional<Map.Entry<String, Animation>> entry = animations.entrySet().stream().findFirst();
        currentAnimation = entry.isPresent() ? entry.get().getValue() : null;
    }

    public Animation getAnimation(String name) {
        return animations.get(name);
    }

    public Animation getCurrentAnimation() {
        return currentAnimation;
    }

    public void setCurrentAnimation(Animation currentAnimation) {
        this.currentAnimation = currentAnimation;
    }
}
