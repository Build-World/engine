package com.buildworld.engine.graphics.particles;

import java.util.List;

import com.buildworld.engine.graphics.game.GameItem;

public interface IParticleEmitter {

    void cleanup();
    
    Particle getBaseParticle();
    
    List<GameItem> getParticles();
}
