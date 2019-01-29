package com.buildworld.engine.graphics.particles;

import java.util.List;

import com.buildworld.engine.graphics.game.Renderable;

public interface IParticleEmitter {

    void cleanup();
    
    Particle getBaseParticle();
    
    List<Renderable> getParticles();
}
