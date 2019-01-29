package com.buildworld.engine.graphics.pooling;

import com.buildworld.engine.graphics.game.Renderable;
import com.buildworld.engine.graphics.mesh.Mesh;
import org.joml.Quaternionf;
import org.joml.Vector3f;

import java.util.HashSet;
import java.util.Set;
import java.util.Stack;

public class RenderablePool {

    private Set<Renderable> used;
    private Stack<Renderable> free;

    private int size;
    private int tolerance = 16;
    private int rateControl = 0;

    public RenderablePool(int size) {
        this.size = size;
        used = new HashSet<>(size);
        free = new Stack<>();
        for(int i = 0; i < size; i++)
        {
            free.push(new Renderable());
        }
    }

    public Set<Renderable> getUsed() {
        return used;
    }

    public Stack<Renderable> getFree() {
        return free;
    }

    public void free(Renderable renderable)
    {
        if(used.remove(renderable) && free.size() < tolerance)
        {
            free.add(renderable);
        }

        freePoolRebalance();
    }

    protected void freePoolRebalance()
    {
        rateControl--;
        if(rateControl < (tolerance * -1))
        {
            tolerance--;
            rateControl /= 2;
        }
    }

    public Renderable use(Mesh[] mesh, Vector3f position, float scale, Quaternionf rotation, boolean selected, boolean frustumCulling, boolean insideFrustrum, int textPos)
    {
        Renderable pooled = free.pop();

        pooled.setPosition(position.x, position.y, position.z);
        pooled.setScale(scale);
        pooled.setMeshes(mesh);

        pooled.setRotation(rotation);
        pooled.setSelected(selected);
        pooled.setTextPos(textPos);

        pooled.setDisableFrustumCulling(frustumCulling);
        pooled.setInsideFrustum(insideFrustrum);

        used.add(pooled);

        usePoolRebalance();

        return pooled;
    }

    public Renderable use(Mesh[] mesh, Vector3f position, float scale, boolean selected)
    {
        Renderable pooled = free.pop();

        pooled.setPosition(position.x, position.y, position.z);
        pooled.setScale(scale);
        pooled.setMeshes(mesh);
        pooled.setSelected(selected);

        used.add(pooled);

        usePoolRebalance();

        return pooled;
    }


    public Renderable use(Mesh[] mesh, Vector3f position, float scale)
    {
        Renderable pooled = free.pop();

        pooled.setPosition(position.x, position.y, position.z);
        pooled.setScale(scale);
        pooled.setMeshes(mesh);

        used.add(pooled);

        usePoolRebalance();

        return pooled;
    }

    public Renderable use(Renderable renderable)
    {
        Renderable pooled = free.pop();

        pooled.setPosition(renderable.getPosition().x, renderable.getPosition().y, renderable.getPosition().z);
        pooled.setScale(renderable.getScale());
        pooled.setDisableFrustumCulling(renderable.isDisableFrustumCulling());
        pooled.setInsideFrustum(renderable.isInsideFrustum());
        pooled.setMeshes(renderable.getMeshes());
        pooled.setRotation(renderable.getRotation());
        pooled.setSelected(renderable.isSelected());
        pooled.setTextPos(renderable.getTextPos());

        used.add(pooled);

        usePoolRebalance();

        return pooled;
    }

    protected void usePoolRebalance()
    {
        if(free.size() < 1)
        {
            free.push(new Renderable());
        }

        rateControl++;
        if(rateControl > tolerance)
        {
            tolerance++;
            rateControl /= 2;
        }
    }
}
