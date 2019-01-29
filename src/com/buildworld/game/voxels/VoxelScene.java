package com.buildworld.game.voxels;

import com.buildworld.engine.graphics.game.Scene;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

public class VoxelScene extends Scene {

    private HashMap<String, IVoxelRenderer> voxelRenderers;
    private Set<IVoxelRenderer> remove;
    private Set<IVoxelRenderer> add;

    public VoxelScene() {
        super();
        this.voxelRenderers = new HashMap<>();
        this.remove = new HashSet<>();
        this.add = new HashSet<>();
    }

    public HashMap<String, IVoxelRenderer> getVoxelRenderers() {
        return voxelRenderers;
    }

    public Set<IVoxelRenderer> getRemove() {
        return remove;
    }

    public Set<IVoxelRenderer> getAdd() {
        return add;
    }

    public void remove(IVoxelRenderer voxelRenderer)
    {
        if(voxelRenderers.remove(voxelRenderer.getUniqueVoxelRendererKey()) != null)
        {
            remove.add(voxelRenderer);
        }
    }

    public void add(IVoxelRenderer voxelRenderer)
    {
        if(!voxelRenderers.containsKey(voxelRenderer.getUniqueVoxelRendererKey()))
        {
            voxelRenderers.put(voxelRenderer.getUniqueVoxelRendererKey(), voxelRenderer);
            add.add(voxelRenderer);
        }
    }

    public void remove(Set<? extends IVoxelRenderer> voxelRenderer)
    {
        for(IVoxelRenderer renderer : voxelRenderer)
        {
            remove(renderer);
        }
    }

    public void add(Set<? extends IVoxelRenderer> voxelRenderer)
    {
        for(IVoxelRenderer renderer : voxelRenderer)
        {
            add(renderer);
        }
    }

    public void replace(Set<? extends IVoxelRenderer> newVoxelRenderers)
    {
        Set<IVoxelRenderer> requiresRemoval = new HashSet<>();

        for(IVoxelRenderer voxelRenderer : voxelRenderers.values())
        {
            if(!newVoxelRenderers.contains(voxelRenderer))
            {
                requiresRemoval.add(voxelRenderer);
            }
        }

        for(IVoxelRenderer voxelRenderer : requiresRemoval)
        {
            remove(voxelRenderer);
        }

        for(IVoxelRenderer voxelRenderer : newVoxelRenderers)
        {
            add(voxelRenderer);
        }
    }

    public void bake() throws Exception
    {
        for(IVoxelRenderer voxelRenderer : remove)
        {
            removeGameItems(voxelRenderer.getRenderables());
        }

        for(IVoxelRenderer voxelRenderer : add)
        {
            setGameItems(voxelRenderer.getRenderables());
        }

        remove.clear();
        add.clear();
    }
}
