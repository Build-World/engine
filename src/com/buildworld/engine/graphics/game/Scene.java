package com.buildworld.engine.graphics.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.buildworld.engine.graphics.lights.SceneLight;
import com.buildworld.engine.graphics.mesh.Mesh;

public class Scene {

    private Map<Mesh, List<GameItem>> meshMap;
    
    private SkyBox skyBox;
    
    private SceneLight sceneLight;

    public Scene() {
        meshMap = new HashMap<>();
    }
    
    public Map<Mesh, List<GameItem>> getGameMeshes() {
        return meshMap;
    }

    public void setGameItems(GameItem[] gameItems) {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            addGameItem(gameItems[i]);
        }
    }

    public void removeGameItems(GameItem[] gameItems)
    {
        int numGameItems = gameItems != null ? gameItems.length : 0;
        for (int i=0; i<numGameItems; i++) {
            removeGameItem(gameItems[i]);
        }
    }

    public void removeGameItem(GameItem gameItem)
    {
        Mesh mesh = gameItem.getMesh();
        List<GameItem> list = meshMap.get(mesh);
        if(list != null)
        {
            list.remove(gameItem);
        }
    }

    public void addGameItem(GameItem gameItem)
    {
        Mesh mesh = gameItem.getMesh();
        List<GameItem> list = meshMap.get(mesh);
        if ( list == null ) {
            list = new ArrayList<>();
            meshMap.put(mesh, list);
        }
        list.add(gameItem);
    }

    public SkyBox getSkyBox() {
        return skyBox;
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
    
}
