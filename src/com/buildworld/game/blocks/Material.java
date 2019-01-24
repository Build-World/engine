package com.buildworld.game.blocks;

import com.buildworld.engine.graphics.textures.Texture;
import org.joml.Vector4f;

public class Material extends com.buildworld.engine.graphics.materials.Material {
    public Material() {
    }

    public Material(Vector4f colour, float reflectance) {
        super(colour, reflectance);
    }

    public Material(Texture texture) {
        super(texture);
    }

    public Material(Texture texture, float reflectance) {
        super(texture, reflectance);
    }

    public Material(Vector4f diffuseColour, Vector4f specularColour, float reflectance) {
        super(diffuseColour, specularColour, reflectance);
    }

    public Material(Vector4f diffuseColour, Vector4f specularColour, Texture texture, float reflectance) {
        super(diffuseColour, specularColour, texture, reflectance);
    }

    public static Material make(Texture texture)
    {
        return new Material(texture, 1f);
    }
}
