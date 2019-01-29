package com.buildworld.engine.graphics.game;

import java.util.*;

import com.buildworld.engine.graphics.lights.SceneLight;
import com.buildworld.engine.graphics.mesh.InstancedMesh;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.particles.IParticleEmitter;
import com.buildworld.engine.graphics.weather.Fog;

public class Scene {

    private final Map<Mesh, List<Renderable>> meshMap;

    private final Map<InstancedMesh, List<Renderable>> instancedMeshMap;

    private SkyBox skyBox;

    private SceneLight sceneLight;

    private Fog fog;

    private boolean renderShadows;

    private IParticleEmitter[] particleEmitters;

    public Scene() {
        meshMap = new HashMap<>();
        instancedMeshMap = new HashMap<>();
        fog = Fog.NOFOG;
        renderShadows = true;
    }

    public Map<Mesh, List<Renderable>> getGameMeshes() {
        return meshMap;
    }

    public Map<InstancedMesh, List<Renderable>> getGameInstancedMeshes() {
        return instancedMeshMap;
    }

    public boolean isRenderShadows() {
        return renderShadows;
    }

    public void setGameItem(Renderable renderable)
    {
        for (Mesh mesh : renderable.getMeshes()) {
            boolean instancedMesh = mesh instanceof InstancedMesh;
            List<Renderable> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
            if (list == null) {
                list = new ArrayList<>();
                if (instancedMesh) {
                    instancedMeshMap.put((InstancedMesh)mesh, list);
                } else {
                    meshMap.put(mesh, list);
                }
            }
            list.add(renderable);
        }
    }

    public void removeGameItem(Renderable renderable)
    {
        for (Mesh mesh : renderable.getMeshes()) {
            boolean instancedMesh = mesh instanceof InstancedMesh;
            List<Renderable> list = instancedMesh ? instancedMeshMap.get(mesh) : meshMap.get(mesh);
            if(list != null)
                list.remove(renderable);
        }
    }

    public void setGameItems(Renderable[] renderables) {
        // Create a map of meshes to speed up rendering
        int numGameItems = renderables != null ? renderables.length : 0;
        for (int i = 0; i < numGameItems; i++) {
            setGameItem(renderables[i]);
        }
    }

    public void removeGameItems(Renderable[] renderables) {
        // Create a map of meshes to speed up rendering
        int numGameItems = renderables != null ? renderables.length : 0;
        for (int i = 0; i < numGameItems; i++) {
            removeGameItem(renderables[i]);
        }
    }

    public <T extends Renderable> void setGameItems(List<T> renderables) {
        for(Renderable renderable : renderables)
        {
            setGameItem(renderable);
        }
    }

    public <T extends Renderable> void removeGameItems(List<T> renderables) {
        for(Renderable renderable : renderables)
        {
            removeGameItem(renderable);
        }
    }

    public <T extends Renderable> void setGameItems(Set<T> renderables) {
        for(Renderable renderable : renderables)
        {
            setGameItem(renderable);
        }
    }

    public <T extends Renderable> void removeGameItems(Set<T> renderables) {
        for(Renderable renderable : renderables)
        {
            removeGameItem(renderable);
        }
    }

    public void cleanup() {
        for (Mesh mesh : meshMap.keySet()) {
            mesh.cleanUp();
        }
        for (Mesh mesh : instancedMeshMap.keySet()) {
            mesh.cleanUp();
        }
        if (particleEmitters != null) {
            for (IParticleEmitter particleEmitter : particleEmitters) {
                particleEmitter.cleanup();
            }
        }
    }

    public SkyBox getSkyBox() {
        return skyBox;
    }

    public void setRenderShadows(boolean renderShadows) {
        this.renderShadows = renderShadows;
    }

    public void setSkyBox(SkyBox skyBox) {
        this.skyBox = skyBox;
    }

    public SceneLight getSceneLight() {
        return sceneLight;
    }

    public void setSceneLight(SceneLight sceneLight) {
        this.sceneLight = sceneLight;
    }

    /**
     * @return the fog
     */
    public Fog getFog() {
        return fog;
    }

    /**
     * @param fog the fog to set
     */
    public void setFog(Fog fog) {
        this.fog = fog;
    }

    public IParticleEmitter[] getParticleEmitters() {
        return particleEmitters;
    }

    public void setParticleEmitters(IParticleEmitter[] particleEmitters) {
        this.particleEmitters = particleEmitters;
    }

}
