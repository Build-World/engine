package com.buildworld.game.blocks.tests;

import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.mesh.meshes.CubeMesh;
import com.buildworld.engine.graphics.textures.Texture;
import com.buildworld.game.blocks.Block;
import com.buildworld.game.blocks.Material;
import com.buildworld.game.blocks.types.IBlockType;
import com.buildworld.game.blocks.types.Mundane;
import com.buildworld.game.world.areas.Chunk;
import com.buildworld.game.world.areas.World;

public class MyBlock extends Block {

    private static Material material;

    public MyBlock() throws Exception
    {
        super("buildworld.mods.core.blocks", "grass", makeMaterial(), Mundane.make());
    }

    public MyBlock(MyBlock original) throws Exception
    {
        this();
    }

    private static Material makeMaterial() throws Exception
    {
        if(material == null)
        {
            material = new Material(new Texture("C:\\Users\\using\\Desktop\\shawn\\build-world\\core\\src\\com\\buildworld\\mods\\core\\resources\\textures\\grassblock.png"));
        }
        return material;
    }

    @Override
    public Block copy() throws Exception {
        return new MyBlock(this);
    }
}
