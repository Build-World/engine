package com.buildworld.engine.graphics.shadow;

import static org.lwjgl.opengl.GL11.*;
import static org.lwjgl.opengl.GL13.*;

import com.buildworld.engine.graphics.Window;
import com.buildworld.engine.graphics.loaders.assimp.StaticMeshesLoader;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.shaders.ShaderProgram;
import com.buildworld.engine.utils.FileUtils;

public class ShadowTestRenderer {

    private ShaderProgram testShaderProgram;

    private Mesh quadMesh;

    public void init(Window window) throws Exception {
        setupTestShader();
    }

    private void setupTestShader() throws Exception {
        testShaderProgram = new ShaderProgram();
        testShaderProgram.createVertexShader(FileUtils.loadResource("/shaders/test_vertex.vs"));
        testShaderProgram.createFragmentShader(FileUtils.loadResource("/shaders/test_fragment.fs"));
        testShaderProgram.link();

        for (int i = 0; i < ShadowRenderer.NUM_CASCADES; i++) {
            testShaderProgram.createUniform("texture_sampler[" + i + "]");
        }

        quadMesh = StaticMeshesLoader.load("/models/quad.obj", "")[0];
    }

    public void renderTest(ShadowBuffer shadowMap) {
        testShaderProgram.bind();

        testShaderProgram.setUniform("texture_sampler[0]", 0);

        glActiveTexture(GL_TEXTURE0);
        glBindTexture(GL_TEXTURE_2D, shadowMap.getDepthMapTexture().getIds()[0]);

        quadMesh.render();

        testShaderProgram.unbind();
    }

    public void cleanup() {
        if (testShaderProgram != null) {
            testShaderProgram.cleanup();
        }
    }
}
