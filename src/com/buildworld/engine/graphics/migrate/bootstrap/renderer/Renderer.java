package com.buildworld.engine.graphics.migrate.bootstrap.renderer;

public interface Renderer {
    void init();
    void clear();
    void begin();
    void end();
    void flush();
    void dispose();
    void setupShaderProgram();
    void specifyVertexAttributes();
}
