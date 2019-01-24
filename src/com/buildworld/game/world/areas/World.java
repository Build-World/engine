package com.buildworld.game.world.areas;

import com.buildworld.engine.interfaces.IPersist;
import com.buildworld.game.blocks.Block;
import com.buildworld.game.events.IUpdateable;
import com.buildworld.game.world.WorldState;
import com.buildworld.game.world.utils.Directions;
import com.buildworld.game.world.interfaces.IArea;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World implements IArea, IPersist {

    public static final int worldHeight = 512;

    // Size in terms of regions inside of the world. If this is not set or set to -1, then the world is infinite
    //   otherwise a looping world will be created.
    private int size;

    private HashMap<Integer, HashMap<Integer, Region>> map;

    private List<Block> added = new ArrayList<>();
    private List<Block> removed = new ArrayList<>();

    // The location of the world within the galaxy
    // This may need to be a 3D vector to represent 3D space in a galaxy
    private Vector2f location;

    private WorldState worldState;

    private int seed;

    // The galaxy this world belongs to.
    // The galaxy should represent a server
    private Galaxy galaxy;

    @Override
    public Vector2f getLocation2D() {
        return location;
    }

    public World(int size, WorldState worldState) throws Exception {
        map = new HashMap<>(); // x, z, y
        this.size = size;
        this.worldState = worldState;
    }

    public World(int size, WorldState worldState, Vector2f location2D) throws Exception {
        this(size, worldState);
        this.location = location2D;
    }

    public WorldState getWorldState() {
        return worldState;
    }

    public void setWorldState(WorldState worldState) {
        this.worldState = worldState;
    }

    public Galaxy getGalaxy() {
        return galaxy;
    }

    public void setGalaxy(Galaxy galaxy) {
        this.galaxy = galaxy;
    }

    public int getSize() {
        return size;
    }

    public HashMap<Integer, HashMap<Integer, Region>> getMap() {
        return map;
    }

    public List<Block> getAdded() {
        return added;
    }

    public List<Block> getRemoved() {
        return removed;
    }

    public Vector2f getLocation() {
        return location;
    }

    public int getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    /**
     * Sets a region using x,z region offset coordinates.
     * Each coordinate does not correspond to a block, but rather a region.
     *
     * @param regionX
     * @param regionZ
     * @param region
     * @throws Exception
     */
    public void setRegion(int regionX, int regionZ, Region region) throws Exception {
        if (regionX >= size || regionZ >= size) {
            throw new Exception("Out of world bounds");
        }

        // Puts a block into the map but if the hashmaps dont exist it will create it
        region.setWorld(this);
        region.setLocation2D(new Vector2f(regionX, regionZ));
        map.computeIfAbsent(regionX, k -> new HashMap<>()).put(regionZ, region);
    }

    /**
     * Sets a region into the given region coordinates
     * The coordinate is not a block coordinate, it is a region coordinate
     * @param regionCoordinate
     * @param region
     * @throws Exception
     */
    public void setRegion(Vector2f regionCoordinate, Region region) throws Exception {
        setRegion((int) regionCoordinate.x, (int) regionCoordinate.y, region);
    }

    /**
     * Returns a region given the x,z region coordinates
     * @param regionX
     * @param regionZ
     * @return
     * @throws Exception
     */
    public Region getRegion(int regionX, int regionZ) throws Exception {
        if (regionX >= size || regionZ >= size) {
            throw new Exception("Out of world bounds");
        }

        try {
            return map.get(regionX).get(regionZ);
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * Returns a region given region coordinates
     * @param regionCoordinate
     * @return
     * @throws Exception
     */
    public Region getRegion(Vector2f regionCoordinate) throws Exception {
        return getRegion((int) regionCoordinate.x, (int) regionCoordinate.y);
    }

    /**
     * Unloads a region from the world
     * @param regionX
     * @param regionZ
     * @return
     * @throws Exception
     */
    public void unloadRegion(int regionX, int regionZ) throws Exception {
        if (regionX >= size || regionZ >= size) {
            throw new Exception("Out of world bounds");
        }

        try {
            map.get(regionX).remove(regionZ);
        } catch (Exception e) {
        }
    }

    /**
     * Unloads a region from the world
     * @param regionCoordinate
     * @return
     * @throws Exception
     */
    public void unloadRegion(Vector2f regionCoordinate) throws Exception {
        unloadRegion((int) regionCoordinate.x, (int) regionCoordinate.y);
    }

    /**
     * Returns whether or not a region exists at the given region coordinates
     * @param regionX
     * @param regionZ
     * @return
     * @throws Exception
     */
    public boolean isRegion(int regionX, int regionZ) throws Exception {
        if (regionX >= size || regionZ >= size) {
            throw new Exception("Out of region bounds");
        }

        return getRegion(regionX, regionZ) == null;
    }

    /**
     * Determines whether or not a region exists at a given region coordinates
     * @param regionCoordinate
     * @return
     * @throws Exception
     */
    public boolean isRegion(Vector2f regionCoordinate) throws Exception {
        return isRegion((int) regionCoordinate.x, (int) regionCoordinate.y);
    }

    /**
     * Gets the region which contains the given block
     * @param block
     * @return
     * @throws Exception
     */
    public Region getContainingRegion(Block block) throws Exception {
        return getContainingRegion((int) block.getPosition().x, (int) block.getPosition().z);
    }

    /**
     * Returns the region which contains the given block coordinates
     * @param blockCoordinate
     * @return
     * @throws Exception
     */
    public Region getContainingRegion(Vector2f blockCoordinate) throws Exception {
        return getContainingRegion((int) blockCoordinate.x, (int) blockCoordinate.y);
    }

    /**
     * Returns the region which contains the given block coordinates
     * @param blockX
     * @param blockZ
     * @return
     * @throws Exception
     */
    public Region getContainingRegion(int blockX, int blockZ) throws Exception {
        int regionLength = Region.size * Chunk.size;

        int regionX = blockX / regionLength;
        int regionZ = blockZ / regionLength;

        return getRegion(regionX, regionZ);
    }

    /**
     * Returns the chunk containing the given block
     * @param block
     * @return
     * @throws Exception
     */
    public Chunk getContainingChunk(Block block) throws Exception {
        return getContainingChunk((int) block.getPosition().x, (int) block.getPosition().z);
    }

    /**
     * Returns the chunk containing the given block coordinates
     * @param blockCoordinate
     * @return
     * @throws Exception
     */
    public Chunk getContainingChunk(Vector2f blockCoordinate) throws Exception {
        return getContainingChunk((int) blockCoordinate.x, (int) blockCoordinate.y);
    }

    /**
     * Returns the chunk containing the given block coordinates
     * @param blockX
     * @param blockZ
     * @return
     * @throws Exception
     */
    public Chunk getContainingChunk(int blockX, int blockZ) throws Exception {
        int regionLength = Region.size * Chunk.size;

        int chunkX = (blockX % regionLength) / Chunk.size;
        int chunkZ = (blockZ % regionLength) / Chunk.size;

        return getContainingRegion(blockX, blockZ).getChunk(chunkX, chunkZ);
    }

    /**
     * Returns the block at the given block coordinates
     * @param blockX
     * @param blockY
     * @param blockZ
     * @return
     * @throws Exception
     */
    public Block getBlock(int blockX, int blockY, int blockZ) throws Exception {
        return getContainingChunk(blockX, blockZ).getBlock(blockX % Chunk.size, blockY, blockZ % Chunk.size);
    }

    /**
     * Returns the block at the given block coordinate
     * @param blockCoordinate
     * @return
     * @throws Exception
     */
    public Block getBlock(Vector3f blockCoordinate) throws Exception {
        return getBlock((int) blockCoordinate.x, (int) blockCoordinate.y, (int) blockCoordinate.z);
    }

    /**
     * Sets a block at the given block coordinate
     * @param blockX
     * @param blockY
     * @param blockZ
     * @param block
     * @throws Exception
     */
    public void setBlock(int blockX, int blockY, int blockZ, Block block) throws Exception {
        Chunk chunk = getContainingChunk(blockX, blockZ);
        block.setChunk(chunk);
        chunk.setBlock(blockX % Chunk.size, blockY, blockZ % Chunk.size, block);
        added.add(block);
    }

    /**
     * Sets a block at the given block coordinate
     * @param blockCoordinate
     * @param block
     * @throws Exception
     */
    public void setBlock(Vector3f blockCoordinate, Block block) throws Exception {
        setBlock((int) blockCoordinate.x, (int) blockCoordinate.y, (int) blockCoordinate.z, block);
    }

    /**
     * Returns whether a block exists at the given block coordinates. If not, it is air
     * @param blockX
     * @param blockY
     * @param blockZ
     * @return
     * @throws Exception
     */
    public boolean isAir(int blockX, int blockY, int blockZ) throws Exception {
        return getBlock(blockX, blockY, blockZ) == null;
    }

    /**
     * Returns whether a block exists at the given block coordinates. If not, it is air
     * @param blockCoordinate
     * @return
     * @throws Exception
     */
    public boolean isAir(Vector3f blockCoordinate) throws Exception {
        return isAir((int) blockCoordinate.x, (int) blockCoordinate.y, (int) blockCoordinate.z);
    }

    public Chunk getChunkNeighbor(Chunk chunk, Vector2f direction) throws Exception
    {
        Vector2f blockOffset = new Vector2f(chunk.getAbsBlockOffset());
        blockOffset.add(new Vector2f(direction).mul(Chunk.size));
        try {
            return getContainingChunk(blockOffset);
        } catch(Exception e)
        {
            return null;
        }
    }

    public ChunkNeighbors getChunkNeighbors(Chunk chunk) throws Exception
    {
        Chunk north = getChunkNeighbor(chunk, Directions.NORTH2D);
        Chunk south = getChunkNeighbor(chunk, Directions.SOUTH2D);
        Chunk east = getChunkNeighbor(chunk, Directions.EAST2D);
        Chunk west = getChunkNeighbor(chunk, Directions.WEST2D);
        return new ChunkNeighbors(chunk, north, south, east, west);
    }

    public ChunkGrid getChunkGrid(Chunk chunk) throws Exception
    {
        Chunk north = getChunkNeighbor(chunk, Directions.NORTH2D);
        Chunk south = getChunkNeighbor(chunk, Directions.SOUTH2D);
        Chunk east = getChunkNeighbor(chunk, Directions.EAST2D);
        Chunk west = getChunkNeighbor(chunk, Directions.WEST2D);
        Chunk northEast = getChunkNeighbor(chunk, new Vector2f(Directions.NORTH2D).add(Directions.EAST2D));
        Chunk northWest = getChunkNeighbor(chunk, new Vector2f(Directions.NORTH2D).add(Directions.WEST2D));
        Chunk southEast = getChunkNeighbor(chunk, new Vector2f(Directions.SOUTH2D).add(Directions.EAST2D));
        Chunk southWest = getChunkNeighbor(chunk, new Vector2f(Directions.SOUTH2D).add(Directions.WEST2D));
        return new ChunkGrid(chunk, north, south, east, west, northEast, northWest, southEast, southWest);
    }

    /**
     * Get the neighboring block to the specified block in the given direction
     *
     * @param block
     * @param direction
     * @return
     * @throws Exception
     */
    public Block getBlockNeighbor(Block block, Vector3f direction) throws Exception {
        return getBlockNeighbor(block.getPosition(), direction);
    }

    /**
     * Get the neighboring block to the specified coordinate in the given direction
     *
     * @param blockCoordinate
     * @param direction
     * @return
     * @throws Exception
     */
    public Block getBlockNeighbor(Vector3f blockCoordinate, Vector3f direction) throws Exception {
        return getBlock(new Vector3f(blockCoordinate).add(direction));
    }

    /**
     * Update the 6 neighboring blocks to a specific block coordinate.
     * This will not update the target coordinate
     *
     * @param blockCoordinate
     * @throws Exception
     */
    public void updateBlockNeighbors(Vector3f blockCoordinate) throws Exception {
        BlockNeighbors blockChunk = getBlockNeighbors(blockCoordinate);
        if (blockChunk.getNorth() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getNorth()).update(blockChunk.getTarget());
        if (blockChunk.getSouth() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getSouth()).update(blockChunk.getTarget());
        if (blockChunk.getEast() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getEast()).update(blockChunk.getTarget());
        if (blockChunk.getWest() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getWest()).update(blockChunk.getTarget());
        if (blockChunk.getUp() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getUp()).update(blockChunk.getTarget());
        if (blockChunk.getDown() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getDown()).update(blockChunk.getTarget());
    }

    /**
     * Update the 6 neighboring blocks to a specific block.
     * This will not update the target coordinate
     *
     * @param block
     * @throws Exception
     */
    public void updateBlockNeighbors(Block block) throws Exception {
        updateBlockNeighbors(block.getPosition());
    }

    /**
     * Updates all of the neighbors to a specified coordinate, but does not update the specified coordinate or the
     * passed ignore block.
     *
     * @param blockCoordinate
     * @param ignore
     * @throws Exception
     */
    public void updateBlockNeighbors(Vector3f blockCoordinate, Block ignore) throws Exception {
        BlockNeighbors blockChunk = getBlockNeighbors(blockCoordinate);
        if (ignore != blockChunk.getNorth() && blockChunk.getNorth() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getNorth()).update(blockChunk.getTarget());
        if (ignore != blockChunk.getSouth() && blockChunk.getSouth() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getSouth()).update(blockChunk.getTarget());
        if (ignore != blockChunk.getEast() && blockChunk.getEast() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getEast()).update(blockChunk.getTarget());
        if (ignore != blockChunk.getWest() && blockChunk.getWest() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getWest()).update(blockChunk.getTarget());
        if (ignore != blockChunk.getUp() && blockChunk.getUp() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getUp()).update(blockChunk.getTarget());
        if (ignore != blockChunk.getDown() && blockChunk.getDown() instanceof IUpdateable)
            ((IUpdateable) blockChunk.getDown()).update(blockChunk.getTarget());
    }

    /**
     * Updates all of the neighbors to a specified block, but does not update the specified block or the
     * passed ignore block.
     *
     * @param block
     * @param ignore
     * @throws Exception
     */
    public void updateBlockNeighbors(Block block, Block ignore) throws Exception {
        updateBlockNeighbors(block.getPosition(), ignore);
    }

    /**
     * Returns the 6 neighbors of a given coordinate including north, south, east, west, up and down
     *
     * @param blockCoordinate
     * @return
     * @throws Exception
     */
    public BlockNeighbors getBlockNeighbors(Vector3f blockCoordinate) throws Exception {
        return getBlockNeighbors(getBlock(blockCoordinate));
    }

    /**
     * Returns the 6 neighbors of a given block including north, south, east, west, up and down
     *
     * @param block
     * @return
     * @throws Exception
     */
    public BlockNeighbors getBlockNeighbors(Block block) throws Exception {
        BlockNeighbors blockChunk = new BlockNeighbors(block);
        blockChunk.setNorth(getBlockNeighbor(block, Directions.NORTH));
        blockChunk.setSouth(getBlockNeighbor(block, Directions.SOUTH));
        blockChunk.setEast(getBlockNeighbor(block, Directions.EAST));
        blockChunk.setWest(getBlockNeighbor(block, Directions.WEST));
        blockChunk.setUp(getBlockNeighbor(block, Directions.UP));
        blockChunk.setDown(getBlockNeighbor(block, Directions.DOWN));
        return blockChunk;
    }

    public int getBlockNeighborCount(Block block) throws Exception {
        BlockNeighbors blockChunk = getBlockNeighbors(block);
        int count = 0;
        if(blockChunk.getNorth() != null)
            count++;
        if(blockChunk.getSouth() != null)
            count++;
        if(blockChunk.getEast() != null)
            count++;
        if(blockChunk.getWest() != null)
            count++;
        if(blockChunk.getUp() != null)
            count++;
        if(blockChunk.getDown() != null)
            count++;
        return count;
    }

    /**
     * Gets a cube region with a specified center point and extending in all directions by the radius amount
     * @param blockX
     * @param blockY
     * @param blockZ
     * @param radius
     * @return
     * @throws Exception
     */
    public List<Block> getRegion(int blockX, int blockY, int blockZ, int radius) throws Exception {
        int diameter = radius * 2 + 1;
        int start = radius * -1; // radius * -1
        int yStart = (start < 0) ? 0 : start;
        List<Block> region = new ArrayList<>();
        for (int i = start; i <= radius; i++) {
            for (int j = start; j <= radius; j++) {
                for (int k = yStart; k < radius; k++) {
                    try {
                        Block block = getBlock(blockX + i, blockY + k, blockZ + j);
                        if (block != null && getBlockNeighborCount(block) < 6) {
                            region.add(block);
                        }
                    } catch(Exception e) {
                    }
                }
            }
        }
        return region;
    }

    /**
     * Returns a cube region of blocks bounded by the two diagonal points
     * @param blockX1
     * @param blockY1
     * @param blockZ1
     * @param blockX2
     * @param blockY2
     * @param blockZ2
     * @return
     * @throws Exception
     */
    public List<Block> getRegion(int blockX1, int blockY1, int blockZ1, int blockX2, int blockY2, int blockZ2) throws Exception {
        List<Block> region = new ArrayList<>();

        int xs = Math.min(blockX1, blockX2);
        int ys = Math.min(blockY1, blockY2);
        int zs = Math.min(blockZ1, blockZ2);

        for (int i = xs; i <= Math.max(blockX1, blockX2); i++) {
            for (int j = zs; j <= Math.max(blockZ1, blockZ2); j++) {
                for (int k = ys; k <= Math.max(blockY1, blockY2); k++) {
                    try {
                        Block block = getBlock(i, k, j);
                        if (block != null && getBlockNeighborCount(block) < 6) {
                            region.add(block);
                        }
                    } catch(Exception e) {
                    }
                }
            }
        }

        return region;
    }

    /**
     * Returns a list of coordinates consumed by the (X2,Z2) region which were not consumed by the (X1,Z1) region.
     * All regions must be odd number sizes, this is enforced by passing in a radius.
     * Algorithm is more efficient with smaller moves, and its worst cases are:
     * [abs(x2-x1) >= (radius * 2 + 1), abs(z2-z1) >= (radius * 2 + 1)]
     * Algorithm only iterates for each extra consumed coordinate.
     *
     * @param blockX1     x1
     * @param blockZ1     z1
     * @param blockX2     x2
     * @param blockZ2     z2
     * @param radius The distance from the center of a region to one edge
     * @return List of Vector2f coordinate pairs. X->X, Y->Z
     */
    public List<Vector2f> getMovedRegionCoords(int blockX1, int blockZ1, int blockX2, int blockZ2, int radius) {
        // If there is no movement, return an empty array
        if (blockX1 == blockX2 && blockZ1 == blockZ2)
            return new ArrayList<>();

        List<Vector2f> coords = new ArrayList<>();

        // diameter represents the side length of a square
        int diameter = radius * 2 + 1;

        // whether we are flipping on the X and/or Z axis to get our L to the top right quadrant
        boolean xFlip = false, zFlip = false;

        /*
         * VECTOR LIST:
         * P1: The original point
         * P2: The point we are moving to
         * P3: The point where P2 maps to the top right quadrant, if P2 is already in the top right, then P3==P2
         * P4: A coordinate
         * flip: The absolute values of the difference of (P2 - P1)
         * reverseFlip: Maps P4 from the top right quadrant to its expected quadrant
         */
        Vector2f p1 = new Vector2f(blockX1, blockZ1);
        Vector2f p2 = new Vector2f(blockX2, blockZ2);

        Vector2f flip = new Vector2f(p2).sub(p1);

        // Create these now to reduce overhead of garbage collection
        Vector2f reverseFlip = new Vector2f();
        Vector2f p4 = new Vector2f();

        if (flip.x() < 0) {
            xFlip = true;
            flip.x = Math.abs(flip.x);
        }

        if (flip.y() < 0) {
            zFlip = true;
            flip.y = Math.abs(flip.y);
        }

        Vector2f p3 = new Vector2f(p1).add(flip);

        // Gets the coordinates of the rectangle on the right of the square
        // Only runs if there is movement on the X axis
        for (int i = 0; i < flip.x; i++) {
            for (int j = 0; j < diameter; j++) {
                reverseFlip.set(0f,0f);

                p4.set(p3).add(radius - flip.x() + 1, -radius).add(i, j);

                if (xFlip)
                    reverseFlip.x = (p4.x() - p1.x()) * 2;
                if (zFlip)
                    reverseFlip.y = (p4.y() - p1.y()) * 2;

                p4.sub(reverseFlip);

                coords.add(p4);
            }
        }

        // Gets the coordinates of the rectangle on the top of the square
        // Only runs if there is movement on the Z axis
        for (int i = 0; i < (diameter - flip.x); i++) {
            for (int j = 0; j < flip.y; j++) {
                reverseFlip.set(0f,0f);

                p4.set(p3).add(-radius, radius - flip.y() + 1).add(i, j);

                if (xFlip)
                    reverseFlip.x = (p4.x() - p1.x()) * 2;
                if (zFlip)
                    reverseFlip.y = (p4.y() - p1.y()) * 2;

                p4.sub(reverseFlip);

                coords.add(p4);
            }
        }

        return coords;
    }

    /**
     * Returns a list of coordinates consumed by the (X2, Y@, Z2) region which were not consumed by the (X1,Y1,Z1) region.
     * All regions must be odd number sizes, this is enforced by passing in a radius.
     * Algorithm is more efficient with smaller moves, and its worst cases are:
     * [abs(x2-x1) >= (radius * 2 + 1), abs(y2-y1) >= (radius * 2 + 1), abs(z2-z1) >= (radius * 2 + 1)]
     * Algorithm only iterates for each extra consumed coordinate.
     *
     * @param blockX1
     * @param blockY1
     * @param blockZ1
     * @param blockX2
     * @param blockY2
     * @param blockZ2
     * @param radius
     * @return
     */
//    public List<Vector3f> getMovedRegionCoords(int blockX1, int blockY1, int blockZ1, int blockX2, int blockY2, int blockZ2, int radius) {
//        // If there is no movement, return an empty array
//        if (blockX1 == blockX2 && blockY1 == blockY2 && blockZ1 == blockZ2)
//            return new ArrayList<>();
//
//        List<Vector3f> coords = new ArrayList<>();
//
//        // diameter represents the side length of a square
//        int diameter = radius * 2 + 1;
//
//        // whether we are flipping on the X and/or Z axis to get our L to the top right quadrant
//        boolean xFlip = false, yFlip=false, zFlip = false;
//
//        /*
//         * VECTOR LIST:
//         * P1: The original point
//         * P2: The point we are moving to
//         * P3: The point where P2 maps to the top right quadrant, if P2 is already in the top right, then P3==P2
//         * P4: A coordinate
//         * flip: The absolute values of the difference of (P2 - P1)
//         * reverseFlip: Maps P4 from the top right quadrant to its expected quadrant
//         */
//        Vector3f p1 = new Vector3f(blockX1, blockY1, blockZ1);
//        Vector3f p2 = new Vector3f(blockX2, blockY2, blockZ2);
//
//        Vector3f flip = new Vector3f(p2).sub(p1);
//
//        // Create these now to reduce overhead of garbage collection
//        Vector3f reverseFlip = new Vector3f();
//        Vector3f p4 = new Vector3f();
//
//        if (flip.x() < 0) {
//            xFlip = true;
//            flip.x = Math.abs(flip.x);
//        }
//
//        if (flip.y() < 0) {
//            yFlip = true;
//            flip.y = Math.abs(flip.y);
//        }
//
//        if (flip.z() < 0) {
//            zFlip = true;
//            flip.z = Math.abs(flip.z);
//        }
//
//        Vector3f p3 = new Vector3f(p1).add(flip);
//
//        // Gets the coordinates of the prism on the right of the cube
//        // Only runs if there is movement on the X axis
//        for (int i = 0; i < flip.x; i++) {
//            for (int j = 0; j < diameter; j++) {
//                for(int k = 0; k < diameter; k++)
//                {
//                    reverseFlip.set(0f,0f,0f);
//                    p4.set(p3).add(radius - flip.x() + 1, -radius, -radius).add(i, j, k);
//
//                    if (xFlip)
//                        reverseFlip.x = (p4.x() - p1.x()) * 2;
//                    if (yFlip)
//                        reverseFlip.y = (p4.y() - p1.y()) * 2;
//                    if (zFlip)
//                        reverseFlip.z = (p4.z() - p1.z()) * 2;
//
//                    p4.sub(reverseFlip);
//
//                    coords.add(p4);
//                }
//            }
//        }
//
//        // Gets the coordinates of the prism on the top of the cube
//        // Only runs if there is movement on the Y axis
//        for (int i = 0; i < (diameter - flip.x); i++) {
//            for (int j = 0; j < flip.y; j++) {
//                for(int k = 0; k < diameter; k++)
//                {
//                    reverseFlip.set(0f,0f,0f);
//
//                    p4.set(p3).add(-radius, radius - flip.y() + 1, -radius).add(i, j, k);
//
//                    if (xFlip)
//                        reverseFlip.x = (p4.x() - p1.x()) * 2;
//                    if (yFlip)
//                        reverseFlip.y = (p4.y() - p1.y()) * 2;
//                    if (zFlip)
//                        reverseFlip.z = (p4.z() - p1.z()) * 2;
//
//                    p4.sub(reverseFlip);
//
//                    coords.add(p4);
//                }
//
//            }
//        }
//
//        // Gets the coordinates of the prism on the back of the cube
//        // Only runs if there is movement on the Z axis
//        for (int i = 0; i < (diameter - flip.x); i++) {
//            for (int j = 0; j < (diameter - flip.y); j++) {
//                for(int k = 0; k < flip.z; k++)
//                {
//                    reverseFlip.set(0f,0f,0f);
//
//                    p4.set(p3).add(-radius, -radius, radius - flip.z() + 1).add(i, j, k);
//
//                    if (xFlip)
//                        reverseFlip.x = (p4.x() - p1.x()) * 2;
//                    if (yFlip)
//                        reverseFlip.y = (p4.y() - p1.y()) * 2;
//                    if (zFlip)
//                        reverseFlip.z = (p4.z() - p1.z()) * 2;
//
//                    p4.sub(reverseFlip);
//
//                    coords.add(p4);
//                }
//
//            }
//        }
//
//        return coords;
//    }
    public List<Vector3f> getMovedRegionCoords(int blockX1, int blockY1, int blockZ1, int blockX2, int blockY2, int blockZ2, int radius) {
        // If there is no movement, return an empty array
        if (blockX1 == blockX2 && blockY1 == blockY2 && blockZ1 == blockZ2)
            return new ArrayList<>();

        List<Vector3f> coords = new ArrayList<>();

        // diameter represents the side length of a square
        int diameter = radius * 2 + 1;

        // whether we are flipping on the X and/or Z axis to get our L to the top right quadrant
        boolean xFlip = false, yFlip=false, zFlip = false;

        /*
         * VECTOR LIST:
         * P1: The original point
         * P2: The point we are moving to
         * P3: The point where P2 maps to the top right quadrant, if P2 is already in the top right, then P3==P2
         * P4: A coordinate
         * flip: The absolute values of the difference of (P2 - P1)
         * reverseFlip: Maps P4 from the top right quadrant to its expected quadrant
         */
        Vector3f p1 = new Vector3f(blockX1, blockY1, blockZ1);
        Vector3f p2 = new Vector3f(blockX2, blockY2, blockZ2);

        Vector3f flip = new Vector3f(p2).sub(p1);

        if (flip.x() < 0) {
            xFlip = true;
            flip.x = Math.abs(flip.x);
        }

        if (flip.y() < 0) {
            yFlip = true;
            flip.y = Math.abs(flip.y);
        }

        if (flip.z() < 0) {
            zFlip = true;
            flip.z = Math.abs(flip.z);
        }

        Vector3f p3 = new Vector3f(p1).add(flip);

        // Gets the coordinates of the prism on the right of the cube
        // Only runs if there is movement on the X axis
        for (int i = 0; i < flip.x; i++) {
            for (int j = 0; j < diameter; j++) {
                for(int k = 0; k < diameter; k++)
                {
                    Vector3f reverseFlip = new Vector3f(0, 0, 0);

                    Vector3f p4 = new Vector3f(p3).add(radius - flip.x() + 1, -radius, -radius).add(i, j, k);

                    if (xFlip)
                        reverseFlip.x = (p4.x() - p1.x()) * 2;
                    if (yFlip)
                        reverseFlip.y = (p4.y() - p1.y()) * 2;
                    if (zFlip)
                        reverseFlip.z = (p4.z() - p1.z()) * 2;

                    p4.sub(reverseFlip);

                    coords.add(p4);
                }
            }
        }

        // Gets the coordinates of the prism on the top of the cube
        // Only runs if there is movement on the Y axis
        for (int i = 0; i < (diameter - flip.x); i++) {
            for (int j = 0; j < flip.y; j++) {
                for(int k = 0; k < diameter; k++)
                {
                    Vector3f reverseFlip = new Vector3f(0, 0, 0);

                    Vector3f p4 = new Vector3f(p3).add(-radius, radius - flip.y() + 1, -radius).add(i, j, k);

                    if (xFlip)
                        reverseFlip.x = (p4.x() - p1.x()) * 2;
                    if (yFlip)
                        reverseFlip.y = (p4.y() - p1.y()) * 2;
                    if (zFlip)
                        reverseFlip.z = (p4.z() - p1.z()) * 2;

                    p4.sub(reverseFlip);

                    coords.add(p4);
                }

            }
        }

        // Gets the coordinates of the prism on the back of the cube
        // Only runs if there is movement on the Z axis
        for (int i = 0; i < (diameter - flip.x); i++) {
            for (int j = 0; j < (diameter - flip.y); j++) {
                for(int k = 0; k < flip.z; k++)
                {
                    Vector3f reverseFlip = new Vector3f(0, 0, 0);

                    Vector3f p4 = new Vector3f(p3).add(-radius, -radius, radius - flip.z() + 1).add(i, j, k);

                    if (xFlip)
                        reverseFlip.x = (p4.x() - p1.x()) * 2;
                    if (yFlip)
                        reverseFlip.y = (p4.y() - p1.y()) * 2;
                    if (zFlip)
                        reverseFlip.z = (p4.z() - p1.z()) * 2;

                    p4.sub(reverseFlip);

                    coords.add(p4);
                }

            }
        }

        return coords;
    }

    /**
     * Flips a set of coordinates around the point x1,z1
     * @param blockCoordinates
     * @param blockX1
     * @param blockZ1
     * @param xFlip
     * @param zFlip
     * @return
     */
    public List<Vector2f> flipMovedRegionCoords(List<Vector2f> blockCoordinates, int blockX1, int blockZ1, boolean xFlip, boolean zFlip) {
        List<Vector2f> flippedCoords = new ArrayList<>(blockCoordinates.size());

        Vector2f p1 = new Vector2f(blockX1, blockZ1);
        Vector2f reverseFlip = new Vector2f(0f,0f);

        for(Vector2f blockCoordinate : blockCoordinates)
        {
            reverseFlip.set(0f,0f);

            if (xFlip)
                reverseFlip.x = (blockCoordinate.x() - p1.x()) * 2;
            if (zFlip)
                reverseFlip.y = (blockCoordinate.y() - p1.y()) * 2;

            flippedCoords.add(new Vector2f(blockCoordinate).sub(reverseFlip));
        }

        return flippedCoords;
    }

    /**
     * Flips a set of coordinates around the point x1,y1,z1
     * @param blockCoordinates
     * @param blockX1
     * @param blockY1
     * @param blockZ1
     * @param xFlip
     * @param yFlip
     * @param zFlip
     * @return
     */
    public List<Vector3f> flipMovedRegionCoords(List<Vector3f> blockCoordinates, int blockX1, int blockY1, int blockZ1, boolean xFlip, boolean yFlip, boolean zFlip) {
        List<Vector3f> flippedCoords = new ArrayList<>(blockCoordinates.size());

        Vector3f p1 = new Vector3f(blockX1, blockY1, blockZ1);

        Vector3f reverseFlip = new Vector3f(0, 0, 0);

        for(Vector3f blockCoordinate : blockCoordinates)
        {
            reverseFlip.set(0f,0f,0f);

            if (xFlip)
                reverseFlip.x = (blockCoordinate.x() - p1.x()) * 2;
            if(yFlip)
                reverseFlip.y = (blockCoordinate.y() - p1.y()) * 2;
            if (zFlip)
                reverseFlip.z = (blockCoordinate.z() - p1.z()) * 2;

            flippedCoords.add(new Vector3f(blockCoordinate).sub(reverseFlip));
        }

        return flippedCoords;
    }

    /**
     * Translates a set of coords by a specified offset
     * @param blockCoordinates
     * @param blockOffset
     * @return
     */
    public List<Vector2f> translateRegionCoords(List<Vector2f> blockCoordinates, Vector2f blockOffset) {
        List<Vector2f> translatedCoords = new ArrayList<>(blockCoordinates.size());

        for(Vector2f blockCoordinate : blockCoordinates)
        {
            translatedCoords.add(new Vector2f(blockCoordinate).add(blockOffset));
        }

        return translatedCoords;
    }

    /**
     * Translates a set of coords by a specified offset
     * @param blockCoordinates
     * @param blockOffset
     * @return
     */
    public List<Vector3f> translateRegionCoords(List<Vector3f> blockCoordinates, Vector3f blockOffset) {
        List<Vector3f> translatedCoords = new ArrayList<>(blockCoordinates.size());

        for(Vector3f blockCoordinate : blockCoordinates)
        {
            translatedCoords.add(new Vector3f(blockCoordinate).add(blockOffset));
        }

        return translatedCoords;
    }

    /**
     * Returns a list of blocks based on the moved regions denoted by the two center points
     * @param blockX1
     * @param blockY1
     * @param blockZ1
     * @param blockX2
     * @param blockY2
     * @param blockZ2
     * @param radius
     * @return
     * @throws Exception
     */
    public List<Block> getMovedRegion(int blockX1, int blockY1, int blockZ1, int blockX2, int blockY2, int blockZ2, int radius) throws Exception {
        return getMovedRegion(getMovedRegionCoords(blockX1, blockY1, blockZ1, blockX2, blockY2, blockZ2, radius));
    }

    /**
     * Takes a list of coords and turns it into a list of blocks
     * @param blockCoordinates
     * @return
     * @throws Exception
     */
    public List<Block> getMovedRegion(List<Vector3f> blockCoordinates) throws Exception {
        List<Block> region = new ArrayList<>();

        for (Vector3f coord : blockCoordinates) {
            try {
                Block block = getBlock((int) coord.x(), (int) coord.y(), (int) coord.z());
                if (block != null && getBlockNeighborCount(block) < 6) {
                    region.add(block);
                }
            } catch(Exception e) {
            }
        }

        return region;
    }

    /**
     * Removes blocks which would be visually hidden as they have neighboring blocks on all 6 sides
     * @param blocks
     * @return
     * @throws Exception
     */
    public List<Block> removeHiddenBlocks(List<Block> blocks) throws Exception
    {
        List<Block> region = new ArrayList<>();
        for(Block block : blocks)
        {
            if (block != null && getBlockNeighborCount(block) < 6) {
                region.add(block);
            }
        }
        return region;
    }

    /**
     * Returns a list of new blocks that exist in a given range for a given center point and radius
     * @param blockX
     * @param blockY
     * @param blockZ
     * @param radius
     * @return
     */
    public List<Block> getUpdatedInRange(int blockX, int blockY, int blockZ, int radius) {
        if (added.size() < 1)
            return new ArrayList<>();

        List<Block> region = new ArrayList<>();
        for (Block block : added) {
            int gix = (int) block.getPosition().x;
            int giz = (int) block.getPosition().z;
            int giy = (int) block.getPosition().y;
            if (gix >= (blockX - radius) && gix <= (blockX + radius) && giz >= (blockZ - radius) && giz <= (blockZ + radius) && giy >= (blockY - radius) && giy <= (blockY + radius)) {
                region.add(block);
            }
        }
        this.added.clear();
        return region;
    }

    /**
     * Load a persisted world
     * @throws Exception
     */
    @Override
    public void load() throws Exception {
        // TODO: Load in the seed, and all the regions (but leave the regions unloaded)
    }

    /**
     * Persist this world
     * @throws Exception
     */
    @Override
    public void unload() throws Exception {
        // TODO
    }
}
