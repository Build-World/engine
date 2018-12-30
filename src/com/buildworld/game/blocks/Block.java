package com.buildworld.game.blocks;

import com.buildworld.engine.graphics.game.GameItem;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.mesh.meshes.CubeMesh;
import com.buildworld.game.blocks.properties.BlockPropertyService;
import com.buildworld.game.blocks.properties.IBlockProperty;
import com.buildworld.game.blocks.types.IBlockType;
import com.buildworld.game.blocks.types.Mundane;
import com.shawnclake.morgencore.core.component.services.Services;

import java.util.ArrayList;
import java.util.List;

abstract public class Block extends GameItem {

    public String namespace;
    public String name;
    public Material material;
    public IBlockType type;
    public List<IBlockProperty> blockProperties;


    public Block(String namespace, String name, Material material) throws Exception {
        super(new CubeMesh().make(material));
        this.material = material;
        this.namespace = namespace;
        this.name = name;
        this.type = Mundane.make();
        this.blockProperties = new ArrayList<>();
        this.setScale(0.5f);

        create();
        ready();
    }

    public Block(String namespace, String name, Material material, IBlockType type) throws Exception {
        super(new CubeMesh().make(material));
        this.material = material;
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.blockProperties = new ArrayList<>();
        this.setScale(0.5f);

        create();
        ready();
    }

    public Block(String namespace, String name, Mesh mesh, IBlockType type) throws Exception {
        super();
        this.setMesh(mesh);
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.blockProperties = new ArrayList<>();
        this.setScale(0.5f);

        create();
        ready();
    }

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
    }

    public Material getMaterial() {
        return material;
    }

    public IBlockType getType() {
        return type;
    }

    public List<IBlockProperty> getBlockProperties() {
        return blockProperties;
    }

    public  <T extends IBlockProperty> boolean hasBlockProperty(Class<T> property)
    {
        for(IBlockProperty prop : getBlockProperties())
        {
            if(prop.getClass() == property)
                return true;
        }
        return false;
    }

    public <T extends IBlockProperty> T getBlockProperty(Class<T> property) throws Exception
    {
        for(IBlockProperty prop : getBlockProperties())
        {
            if(prop.getClass() == property)
                return (T)prop;
        }
        throw new Exception("Block property does not exist on block");
    }

    protected void create() {
        for (IBlockProperty property : Services.getService(BlockPropertyService.class).getMandatory()) {
            if (property.isTypesWhitelisted() && property.getWhitelist().stream().noneMatch(getType().getClass()::isInstance))
                continue;
            blockProperties.add(property.make());
        }
    }

    protected void ready() throws Exception {}

    public void register() {
        Services.getService(BlockService.class).add(this);
    }

}
