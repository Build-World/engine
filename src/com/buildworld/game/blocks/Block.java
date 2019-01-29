package com.buildworld.game.blocks;

import com.buildworld.engine.graphics.game.Renderable;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.mesh.MeshCache;
import com.buildworld.game.blocks.properties.BlockPropertyService;
import com.buildworld.game.blocks.properties.IBlockProperty;
import com.buildworld.game.blocks.types.IBlockType;
import com.buildworld.game.voxels.IVoxel;
import com.buildworld.game.world.areas.BlockNeighbors;
import com.buildworld.game.world.areas.Chunk;
import com.shawnclake.morgencore.core.component.services.Services;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.List;

abstract public class Block implements IVoxel {

    public String namespace;
    public String name;
    public IBlockType type;
    private Chunk chunk;
    private Vector3f chunkPosition;
    private Vector3f worldPosition;
    public List<IBlockProperty> blockProperties;

    abstract public void initialize() throws Exception;

    abstract protected void ready() throws Exception;


    public Block(String namespace, String name) throws Exception {
        this(namespace, name, null, null);
    }

    public Block(String namespace, String name, Chunk chunk) throws Exception {
        this(namespace, name, null, chunk);
    }

    public Block(String namespace, String name, IBlockType type) throws Exception {
        this(namespace, name, type, null);
    }

    private Block(String namespace, String name, IBlockType type, Chunk chunk) throws Exception {
        this.namespace = namespace;
        this.name = name;
        this.type = type;
        this.chunk = chunk;
        this.blockProperties = new ArrayList<>();
        this.worldPosition = new Vector3f();
        this.chunkPosition = new Vector3f();

        initialize();
        loadMesh();
        initializeProperties();
        register();
        ready();
    }

    public Block(Block original) throws Exception {
        this(original.namespace, original.name, original.type);
    }

    public void register() {
        Services.getService(BlockService.class).add(this);
    }

    public void loadMesh() throws Exception
    {
        MeshCache.getInstance().loadMesh(getModelFilename(), getResourcePath(), getNamespace(), getName());
    }

    public Mesh[] getMesh() throws Exception
    {
        return MeshCache.getInstance().getMesh(getNamespace(), getName());
    }

    @Override
    public Renderable getRenderable() throws Exception {
        Renderable renderable = new Renderable(getMesh());
        renderable.setScale(0.5f);
        renderable.setPosition(worldPosition.x, worldPosition.y, worldPosition.z);
        return renderable;
    }

    @Override
    public Vector3f getChunkPosition() {
        return chunkPosition;
    }

    @Override
    public void setChunkPosition(Vector3f position) {
        chunkPosition.set(position);
    }

    @Override
    public Vector3f getWorldPosition() {
        return worldPosition;
    }

    @Override
    public void setWorldPosition(int x, int y, int z) {
        worldPosition.set(x, y, z);
    }

    @Override
    public void setWorldPosition(Vector3f position) {
        worldPosition.set(position);
    }

    public void setChunkPosition(int x, int y, int z) {
        chunkPosition.x = x;
        chunkPosition.y = y;
        chunkPosition.z = z;
    }

    abstract public Block copy() throws Exception;

    public String getNamespace() {
        return namespace;
    }

    public String getName() {
        return name;
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

    public <T extends IBlockProperty> boolean hasBlockProperty(Class<T> property) {
        for (IBlockProperty prop : getBlockProperties()) {
            if (prop.getClass() == property)
                return true;
        }
        return false;
    }

    public <T extends IBlockProperty> T getBlockProperty(Class<T> property) throws Exception {
        for (IBlockProperty prop : getBlockProperties()) {
            if (prop.getClass() == property)
                return (T) prop;
        }
        throw new Exception("Block property does not exist on block");
    }

    protected void initializeProperties() {
        for (IBlockProperty property : Services.getService(BlockPropertyService.class).getMandatory()) {
            if (property.isTypesWhitelisted() && property.getWhitelist().stream().noneMatch(getType().getClass()::isInstance))
                continue;
            blockProperties.add(property.make());
        }
    }

    public void updateNeighbors() throws Exception {
        chunk.getRegion().getWorld().updateBlockNeighbors(getWorldPosition());
    }

    public void updateNeighbors(Block ignore) throws Exception {
        chunk.getRegion().getWorld().updateBlockNeighbors(getWorldPosition(), ignore);
    }

    public BlockNeighbors getNeighbors() throws Exception {
        return chunk.getRegion().getWorld().getBlockNeighbors(getWorldPosition());
    }

}
