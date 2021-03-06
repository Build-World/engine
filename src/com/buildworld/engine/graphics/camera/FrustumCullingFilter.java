package com.buildworld.engine.graphics.camera;

import java.util.List;
import java.util.Map;

import com.buildworld.engine.graphics.game.Renderable;
import com.buildworld.engine.graphics.mesh.Mesh;
import org.joml.FrustumIntersection;
import org.joml.Matrix4f;
import org.joml.Vector3f;

public class FrustumCullingFilter {

    private final Matrix4f prjViewMatrix;

    private final FrustumIntersection frustumInt;

    public FrustumCullingFilter() {
        prjViewMatrix = new Matrix4f();
        frustumInt = new FrustumIntersection();
    }

    public void updateFrustum(Matrix4f projMatrix, Matrix4f viewMatrix) {
        // Calculate projection view matrix
        prjViewMatrix.set(projMatrix);
        prjViewMatrix.mul(viewMatrix);
        // Update frustum intersection class
        frustumInt.set(prjViewMatrix);
    }

    public void filter(Map<? extends Mesh, List<Renderable>> mapMesh) {
        for (Map.Entry<? extends Mesh, List<Renderable>> entry : mapMesh.entrySet()) {
            List<Renderable> renderables = entry.getValue();
            filter(renderables, entry.getKey().getBoundingRadius());
        }
    }

    public void filter(List<Renderable> renderables, float meshBoundingRadius) {
        float boundingRadius;
        Vector3f pos;
        for (Renderable renderable : renderables) {
            if (!renderable.isDisableFrustumCulling()) {
                boundingRadius = renderable.getScale() * meshBoundingRadius;
                pos = renderable.getPosition();
                renderable.setInsideFrustum(insideFrustum(pos.x, pos.y, pos.z, boundingRadius));
            }
        }
    }

    public boolean insideFrustum(float x0, float y0, float z0, float boundingRadius) {
        return frustumInt.testSphere(x0, y0, z0, boundingRadius+1);
    }
}
