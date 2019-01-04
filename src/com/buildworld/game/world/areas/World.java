package com.buildworld.game.world.areas;

import com.buildworld.game.blocks.Block;
import com.buildworld.game.events.IUpdateable;
import com.buildworld.game.world.utils.Directions;
import com.buildworld.game.world.interfaces.IArea;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World implements IArea {

    public static final int worldHeight = 512;

    // Size in terms of regions inside of the world. If this is not set or set to -1, then the world is infinite
    //   otherwise a looping world will be created.
    private int size;

    private HashMap<Integer, HashMap<Integer, Region>> map;

    // TODO: These may no longer be required
    private List<Block> added = new ArrayList<>();
    private List<Block> removed = new ArrayList<>();

    // The location of the world within the galaxy
    // This may need to be a 3D vector to represent 3D space in a galaxy
    private Vector2f location;

    // The galaxy this world belongs to.
    // The galaxy should represent a server
    private Galaxy galaxy;

    @Override
    public Vector2f getLocation2D() {
        return location;
    }

    public World(int size) throws Exception {
        map = new HashMap<>(); // x, z, y
        this.size = size;
    }

    public World(Vector2f location, int size) throws Exception {
        this(size);
        this.location = location;
    }

    /**
     * Sets a region using x,z region offset coordinates.
     * Each coordinate does not correspond to a block, but rather a region.
     *
     * @param x
     * @param z
     * @param region
     * @throws Exception
     */
    public void setRegion(int x, int z, Region region) throws Exception {
        if (x >= size || z >= size) {
            throw new Exception("Out of world bounds");
        }

        // Puts a block into the map but if the hashmaps dont exist it will create it
        region.setWorld(this);
        region.setLocation(new Vector2f(x, z));
        map.computeIfAbsent(x, k -> new HashMap<>()).put(z, region);
    }

    public void setRegion(Vector2f coordinate, Region region) throws Exception {
        setRegion((int) coordinate.x, (int) coordinate.y, region);
    }

    public Region getRegion(int x, int z) throws Exception {
        if (x >= size || z >= size) {
            throw new Exception("Out of world bounds");
        }

        try {
            return map.get(x).get(z);
        } catch (Exception e) {
            return null;
        }
    }

    public Region getRegion(Vector2f coordinate) throws Exception {
        return getRegion((int) coordinate.x, (int) coordinate.y);
    }

    public boolean isRegion(int x, int z) throws Exception {
        if (x >= size || z >= size) {
            throw new Exception("Out of region bounds");
        }

        return getRegion(x, z) == null;
    }

    public boolean isRegion(Vector2f coordinate) throws Exception {
        return isRegion((int) coordinate.x, (int) coordinate.y);
    }

    public Region getContainingRegion(Block block) throws Exception {
        return getContainingRegion((int) block.getPosition().x, (int) block.getPosition().z);
    }

    public Region getContainingRegion(Vector2f coordinate) throws Exception {
        return getContainingRegion((int) coordinate.x, (int) coordinate.y);
    }

    public Region getContainingRegion(int x, int z) throws Exception {
        int regionLength = Region.size * Chunk.size;

        int regionX = x / regionLength;
        int regionZ = z / regionLength;

        return getRegion(regionX, regionZ);
    }

    public Chunk getContainingChunk(Block block) throws Exception {
        return getContainingChunk((int) block.getPosition().x, (int) block.getPosition().z);
    }

    public Chunk getContainingChunk(Vector2f coordinate) throws Exception {
        return getContainingChunk((int) coordinate.x, (int) coordinate.y);
    }

    public Chunk getContainingChunk(int x, int z) throws Exception {
        int regionLength = Region.size * Chunk.size;

        int chunkX = (x % regionLength) / Chunk.size;
        int chunkZ = (z % regionLength) / Chunk.size;

        return getContainingRegion(x, z).getChunk(chunkX, chunkZ);
    }

    public Block getBlock(int x, int y, int z) throws Exception {
        return getContainingChunk(x, z).getBlock(x % Chunk.size, y, z % Chunk.size);
    }

    public Block getBlock(Vector3f coordinate) throws Exception {
        return getBlock((int) coordinate.x, (int) coordinate.y, (int) coordinate.z);
    }

    public void setBlock(int x, int y, int z, Block block) throws Exception {
        getContainingChunk(x, z).setBlock(x % Chunk.size, y, z % Chunk.size, block);
        added.add(block);
    }

    public void setBlock(Vector3f coordinate, Block block) throws Exception {
        setBlock((int) coordinate.x, (int) coordinate.y, (int) coordinate.z, block);
    }

    public boolean isAir(int x, int y, int z) throws Exception {
        return getBlock(x, y, z) == null;
    }

    public boolean isAir(Vector3f coordinate) throws Exception {
        return isAir((int) coordinate.x, (int) coordinate.y, (int) coordinate.z);
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
     * @param coordinate
     * @param direction
     * @return
     * @throws Exception
     */
    public Block getBlockNeighbor(Vector3f coordinate, Vector3f direction) throws Exception {
        return getBlock(new Vector3f(coordinate).add(direction));
    }

    /**
     * Update the 6 neighboring blocks to a specific block coordinate.
     * This will not update the target coordinate
     *
     * @param coordinate
     * @throws Exception
     */
    public void updateBlockNeighbors(Vector3f coordinate) throws Exception {
        BlockChunk blockChunk = getBlockNeighbors(coordinate);
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
     * @param coordinate
     * @param ignore
     * @throws Exception
     */
    public void updateBlockNeighbors(Vector3f coordinate, Block ignore) throws Exception {
        BlockChunk blockChunk = getBlockNeighbors(coordinate);
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
     * @param coordinate
     * @return
     * @throws Exception
     */
    public BlockChunk getBlockNeighbors(Vector3f coordinate) throws Exception {
        return getBlockNeighbors(getBlock(coordinate));
    }

    /**
     * Returns the 6 neighbors of a given block including north, south, east, west, up and down
     *
     * @param block
     * @return
     * @throws Exception
     */
    public BlockChunk getBlockNeighbors(Block block) throws Exception {
        BlockChunk blockChunk = new BlockChunk(block);
        blockChunk.setNorth(getBlockNeighbor(block, Directions.NORTH));
        blockChunk.setSouth(getBlockNeighbor(block, Directions.SOUTH));
        blockChunk.setEast(getBlockNeighbor(block, Directions.EAST));
        blockChunk.setWest(getBlockNeighbor(block, Directions.WEST));
        blockChunk.setUp(getBlockNeighbor(block, Directions.UP));
        blockChunk.setDown(getBlockNeighbor(block, Directions.DOWN));
        return blockChunk;
    }

    public Block[] getRegion(int x, int y, int z, int radius) throws Exception {
        int diameter = radius * 2 + 1;
        List<Block> region = new ArrayList<>();
        for (int i = radius * -1; i <= radius; i++) {
            for (int j = radius * -1; j <= radius; j++) {
                for (int k = 0; k < y; k++) {
                    Block block = getBlock(x + i, k, z + j);
                    if (block != null) {
                        region.add(block);
                    }
                }
            }
        }
        return region.toArray(new Block[0]);
    }

    public Block[] getRegion(int x1, int y1, int z1, int x2, int y2, int z2) throws Exception {
        List<Block> region = new ArrayList<>();

        int xs = Math.min(x1, x2);
        int ys = Math.min(y1, y2);
        int zs = Math.min(z1, z2);

        for (int i = xs; i <= Math.max(x1, x2); i++) {
            for (int j = zs; j <= Math.max(z1, z2); j++) {
                for (int k = ys; k <= Math.max(y1, y2); k++) {
                    Block block = getBlock(i, k, j);
                    if (block != null) {
                        region.add(block);
                    }
                }
            }
        }

        return region.toArray(new Block[0]);
    }

    /**
     * Returns a list of coordinates consumed by the (X2,Z2) region which were not consumed by the (X1,Z1) region.
     * All regions must be odd number sizes, this is enforced by passing in a radius.
     * Algorithm is more efficient with smaller moves, and its worst cases are:
     * [abs(x2-x1) >= (radius * 2 + 1), abs(z2-z1) >= (radius * 2 + 1)]
     * Algorithm only iterates for each extra consumed coordinate.
     *
     * @param x1     x1
     * @param z1     z1
     * @param x2     x2
     * @param z2     z2
     * @param radius The distance from the center of a region to one edge
     * @return List of Vector2f coordinate pairs. X->X, Y->Z
     */
    public Vector2f[] getMovedRegionCoords(int x1, int z1, int x2, int z2, int radius) {
        // If there is no movement, return an empty array
        if (x1 == x2 && z1 == z2)
            return new Vector2f[0];

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
        Vector2f p1 = new Vector2f(x1, z1);
        Vector2f p2 = new Vector2f(x2, z2);

        Vector2f flip = new Vector2f(p2).sub(p1);

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
                Vector2f reverseFlip = new Vector2f(0, 0);

                Vector2f p4 = new Vector2f(p3).add(radius - flip.x() + 1, -radius).add(i, j);

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
                Vector2f reverseFlip = new Vector2f(0, 0);

                Vector2f p4 = new Vector2f(p3).add(-radius, radius - flip.y() + 1).add(i, j);

                if (xFlip)
                    reverseFlip.x = (p4.x() - p1.x()) * 2;
                if (zFlip)
                    reverseFlip.y = (p4.y() - p1.y()) * 2;

                p4.sub(reverseFlip);

                coords.add(p4);
            }
        }

        return coords.toArray(new Vector2f[0]);
    }

    public Vector2f[] flipMovedRegionCoords(Vector2f[] coords, int x1, int z1, int radius, boolean xFlip, boolean zFlip) {
        Vector2f[] flippedCoords = new Vector2f[coords.length];

        int diameter = radius * 2 + 1;

        Vector2f p1 = new Vector2f(x1, z1);

        for (int i = 0; i < coords.length; i++) {
            Vector2f reverseFlip = new Vector2f(0, 0);

            if (xFlip)
                reverseFlip.x = (coords[i].x() - p1.x()) * 2;
            if (zFlip)
                reverseFlip.y = (coords[i].y() - p1.y()) * 2;

            flippedCoords[i] = new Vector2f(coords[i]).sub(reverseFlip);
        }

        return flippedCoords;
    }

    public Vector2f[] translateRegionCoords(Vector2f[] coords, Vector2f offset) {
        Vector2f[] translatedCoords = new Vector2f[coords.length];

        for (int i = 0; i < coords.length; i++) {
            translatedCoords[i] = new Vector2f(coords[i]).add(offset);
        }

        return translatedCoords;
    }

    public Block[] getMovedRegion(int x1, int y1, int z1, int x2, int y2, int z2, int radius) throws Exception {
        return getMovedRegion(getMovedRegionCoords(x1, z1, x2, z2, radius), y1, y2);
    }

    public Block[] getMovedRegion(Vector2f[] coords, int y1, int y2) throws Exception {
        List<Block> region = new ArrayList<>();

        for (Vector2f coord : coords) {
            for (int k = y1; k < y2; k++) {
                Block block = getBlock((int) coord.x(), k, (int) coord.y());
                if (block != null) {
                    region.add(block);
                }
            }
        }

        return region.toArray(new Block[0]);
    }

    public Block[] getUpdatedInRange(int x, int z, int radius) {
        if (added.size() < 1)
            return new Block[0];

        List<Block> region = new ArrayList<>();
        for (Block block : added) {
            int gix = (int) block.getPosition().x;
            int giz = (int) block.getPosition().z;
            if (gix >= (x - radius) && gix <= (x + radius) && giz >= (z - radius) && giz <= (z + radius)) {
                region.add(block);
            }
        }
        this.added.clear();
        return region.toArray(new Block[0]);
    }


}
