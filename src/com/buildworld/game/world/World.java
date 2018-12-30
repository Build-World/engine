package com.buildworld.game.world;

import com.buildworld.game.blocks.Block;
import com.buildworld.game.events.IUpdate;
import com.buildworld.game.events.UpdateService;
import com.shawnclake.morgencore.core.component.services.Services;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World {

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, Block>>> map;

    private List<Block> added = new ArrayList<>();
    private List<Block> removed = new ArrayList<>();

    public World() throws Exception {
        map = new HashMap<>(); // x, z, y
    }

    public Block getBlock(int x, int y, int z) {
        try {
            return map.get(x).get(z).get(y);
        } catch (Exception e) {
            return null;
        }
    }
    
    public Block getBlock(Vector3f coordinate)
    {
        return getBlock((int)coordinate.x, (int)coordinate.y, (int)coordinate.z);
    }

    public void setBlock(int x, int y, int z, Block block) throws Exception {
        if (y < 0)
            throw new Exception("cant have a block below 0 in the height axis");
        if(block instanceof IUpdate)
        {
            if(((IUpdate)block).requiresUpdates())
            {
                Services.getService(UpdateService.class).add((IUpdate)block);
            }
        }

        // Puts a block into the map but if the hashmaps dont exist it will create it
        block.setPosition(x,y,z);
        map.computeIfAbsent(x, k -> new HashMap<>()).computeIfAbsent(z, l -> new HashMap<>()).put(y, block);
        added.add(block);
    }
    
    public void setBlock(Vector3f coordinate, Block block) throws Exception
    {
        setBlock((int)coordinate.x, (int)coordinate.y, (int)coordinate.z, block);
    }

    public boolean isAir(int x, int y, int z) {
        return getBlock(x, y, z) == null;
    }
    
    public boolean isAir(Vector3f coordinate)
    {
        return isAir((int)coordinate.x, (int)coordinate.y, (int)coordinate.z);
    }

    public Block getNeighbor(Vector3f coordinate, Vector3f direction)
    {
        return getBlock(new Vector3f(coordinate).add(direction));
    }

    public BlockChunk getNeighbors(Vector3f coordinate)
    {
        BlockChunk blockChunk = new BlockChunk(getBlock(coordinate));
        blockChunk.setNorth(getNeighbor(coordinate, Directions.NORTH));
        blockChunk.setSouth(getNeighbor(coordinate, Directions.SOUTH));
        blockChunk.setEast(getNeighbor(coordinate, Directions.EAST));
        blockChunk.setWest(getNeighbor(coordinate, Directions.WEST));
        blockChunk.setUp(getNeighbor(coordinate, Directions.UP));
        blockChunk.setDown(getNeighbor(coordinate, Directions.DOWN));
        return blockChunk;
    }

    public Block[] getRegion(int x, int y, int z, int radius) {
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

    public Block[] getRegion(int x1, int y1, int z1, int x2, int y2, int z2) {
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
     *      [abs(x2-x1) >= (radius * 2 + 1), abs(z2-z1) >= (radius * 2 + 1)]
     *      Algorithm only iterates for each extra consumed coordinate.
     * @param x1 x1
     * @param z1 z1
     * @param x2 x2
     * @param z2 z2
     * @param radius The distance from the center of a region to one edge
     * @return List of Vector2f coordinate pairs. X->X, Y->Z
     */
    public Vector2f[] getMovedRegionCoords(int x1, int z1, int x2, int z2, int radius) {
        // If there is no movement, return an empty array
        if(x1 == x2 && z1 == z2)
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

    public Vector2f[] flipMovedRegionCoords(Vector2f[] coords, int x1, int z1, int radius, boolean xFlip, boolean zFlip)
    {
        Vector2f[] flippedCoords = new Vector2f[coords.length];

        int diameter = radius * 2 + 1;

        Vector2f p1 = new Vector2f(x1, z1);

        for(int i = 0; i < coords.length; i++)
        {
            Vector2f reverseFlip = new Vector2f(0, 0);

            if (xFlip)
                reverseFlip.x = (coords[i].x() - p1.x()) * 2;
            if (zFlip)
                reverseFlip.y = (coords[i].y() - p1.y()) * 2;

            flippedCoords[i] = new Vector2f(coords[i]).sub(reverseFlip);
        }

        return flippedCoords;
    }

    public Vector2f[] translateRegionCoords(Vector2f[] coords, Vector2f offset)
    {
        Vector2f[] translatedCoords = new Vector2f[coords.length];

        for(int i = 0; i < coords.length; i++) {
            translatedCoords[i] = new Vector2f(coords[i]).add(offset);
        }

        return translatedCoords;
    }

    public Block[] getMovedRegion(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
        return getMovedRegion(getMovedRegionCoords(x1, z1, x2, z2, radius), y1, y2);
    }

    public Block[] getMovedRegion(Vector2f[] coords, int y1, int y2) {
        List<Block> region = new ArrayList<>();

        for (Vector2f coord : coords)
        {
            for (int k = y1; k < y2; k++) {
                Block block = getBlock((int)coord.x(), k, (int)coord.y());
                if (block != null) {
                    region.add(block);
                }
            }
        }

        return region.toArray(new Block[0]);
    }

    public Block[] getUpdatedInRange(int x, int z, int radius)
    {
        if(added.size() < 1)
            return new Block[0];

        List<Block> region = new ArrayList<>();
        for(Block block : added)
        {
            int gix = (int)block.getPosition().x;
            int giz = (int)block.getPosition().z;
            if(gix >= (x - radius) && gix <= (x + radius) && giz >= (z - radius) && giz <= (z + radius))
            {
                region.add(block);
            }
        }
        this.added.clear();
        return region.toArray(new Block[0]);
    }


}
