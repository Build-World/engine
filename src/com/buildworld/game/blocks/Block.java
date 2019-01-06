package com.buildworld.game.blocks;

import com.buildworld.engine.graphics.game.GameItem;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.mesh.meshes.CubeMesh;
import com.buildworld.game.blocks.properties.BlockPropertyService;
import com.buildworld.game.blocks.properties.IBlockProperty;
import com.buildworld.game.blocks.types.IBlockType;
import com.buildworld.game.world.areas.BlockChunk;
import com.buildworld.game.world.areas.Chunk;
import com.shawnclake.morgencore.core.component.services.Services;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

abstract public class Block extends GameItem {

    public String namespace;
    public String name;
    public Material material;
    public IBlockType type;
    private Chunk chunk;
    public List<IBlockProperty> blockProperties;

    public static Map<Material, Mesh> matMeshes = new HashMap<>();

    public static Mesh getMesh(Material material) throws Exception
    {
        if(matMeshes.containsKey(material))
        {
            return matMeshes.get(material);
        } else {
            Mesh mesh = new CubeMesh().make(material);
            matMeshes.put(material, mesh);
            if(matMeshes.size() > 4096)
            {
                System.out.println("Block meshes exceeding limits");
            }
            return mesh;
        }
    }

    public Block(String namespace, String name, Material material) throws Exception {
        this(namespace, name, getMesh(material), null, null);
        this.material = material;
    }

    public Block(String namespace, String name, Material material, Chunk chunk) throws Exception {
        this(namespace, name, getMesh(material), null, chunk);
        this.material = material;
    }

    public Block(String namespace, String name, Material material, IBlockType type) throws Exception {
        this(namespace, name, getMesh(material), type, null);
        this.material = material;
    }

    public Block(String namespace, String name, Material material, IBlockType type, Chunk chunk) throws Exception {
        this(namespace, name, getMesh(material), type, chunk);
        this.material = material;
    }

    private Block(String namespace, String name, Mesh mesh, IBlockType type, Chunk chunk) throws Exception {
        super();
        this.setMesh(mesh);
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.chunk = chunk;
        this.blockProperties = new ArrayList<>();
        this.setScale(0.5f);

        create();
        ready();
    }

    public Block(Block original) throws Exception
    {
        this(original.namespace, original.name, original.material, original.type);
    }

    abstract public Block copy() throws Exception;

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

    public Chunk getChunk() {
        return chunk;
    }

    public void setChunk(Chunk chunk) {
        this.chunk = chunk;
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

    public void updateNeighbors() throws Exception {
        chunk.getRegion().getWorld().updateBlockNeighbors(getPosition());
    }

    public void updateNeighbors(Block ignore) throws Exception {
        chunk.getRegion().getWorld().updateBlockNeighbors(getPosition(), ignore);
    }

    public BlockChunk getNeighbors() throws Exception {
        return chunk.getRegion().getWorld().getBlockNeighbors(getPosition());
    }

}
