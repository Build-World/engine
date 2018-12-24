package com.buildworld.game.world;

import com.buildworld.engine.graphics.game.GameItem;
import com.buildworld.engine.graphics.materials.Material;
import com.buildworld.engine.graphics.mesh.Mesh;
import com.buildworld.engine.graphics.mesh.meshes.CubeMesh;
import com.buildworld.engine.graphics.textures.Texture;
import com.buildworld.engine.utils.SimplexNoise;
import org.joml.Vector2f;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class World {

    private final int worldSize = 64;
    public final int worldHeight = 256;
    private final boolean generateSurfaceLayerOnly = true;

    private HashMap<Integer, HashMap<Integer, HashMap<Integer, GameItem>>> map;

    public World() throws Exception {
        int dimension = worldSize * 16;
        map = new HashMap<>(); // x, z, y

        float reflectance = 1f;
        Texture texture = new Texture("D:\\Programming\\Projects\\Build-World\\engine\\resources/textures/grassblock.png");
        Material material = new Material(texture, reflectance);
        Mesh cube = new CubeMesh().make(material);

        float blockScale = 0.5f;

        SimplexNoise noise = new SimplexNoise();

        for (int i = 0; i < dimension; i++) {
            for (int j = 0; j < dimension; j++) {
                int height = (int) ((noise.eval(i / (float) worldSize, j / (float) worldSize) + 1f) * 16f);
                for (int w = 0; w < height; w++) {
                    if (generateSurfaceLayerOnly && w != height - 1)
                        continue;
                    GameItem gameItem = new GameItem(cube);
                    gameItem.setScale(blockScale);
                    int x = i - (dimension / 2);
                    int y = w;
                    int z = j - (dimension / 2);
                    gameItem.setPosition(x, y, z);
                    setBlock(x, y, z, gameItem);
                }
            }
        }
    }

    public GameItem getBlock(int x, int y, int z) {
        try {
            return map.get(x).get(z).get(y);
        } catch (Exception e) {
            return null;
        }
    }

    public void setBlock(int x, int y, int z, GameItem gameItem) throws Exception {
        if (y < 0)
            throw new Exception("cant have a block below 0 in the height axis");
        // Puts a block into the map but if the hashmaps dont exist it will create it
        map.computeIfAbsent(x, k -> new HashMap<>()).computeIfAbsent(z, l -> new HashMap<>()).put(y, gameItem);
    }

    public boolean isAir(int x, int y, int z) {
        if (getBlock(x, y, z) != null)
            return true;
        return false;
    }

    public GameItem[] getRegion(int x, int y, int z, int radius) {
        int diameter = radius * 2 + 1;
        diameter = worldHeight;
        List<GameItem> region = new ArrayList<>();
        for (int i = radius * -1; i <= radius; i++) {
            for (int j = radius * -1; j <= radius; j++) {
                for (int k = 0; k < diameter; k++) {
                    GameItem gameItem = getBlock(x + i, k, z + j);
                    if (gameItem != null) {
                        region.add(gameItem);
                    }
                }
            }
        }
        return region.toArray(new GameItem[0]);
    }

    public GameItem[] getRegion(int x1, int y1, int z1, int x2, int y2, int z2) {
        List<GameItem> region = new ArrayList<>();

        int xs = Math.min(x1, x2);
        int ys = Math.min(y1, y2);
        int zs = Math.min(z1, z2);

        for (int i = xs; i <= Math.max(x1, x2); i++) {
            for (int j = zs; j <= Math.max(z1, z2); j++) {
                for (int k = ys; k <= Math.max(y1, y2); k++) {
                    GameItem gameItem = getBlock(i, k, j);
                    if (gameItem != null) {
                        region.add(gameItem);
                    }
                }
            }
        }

        return region.toArray(new GameItem[0]);
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

    public GameItem[] getMovedRegion(int x1, int y1, int z1, int x2, int y2, int z2, int radius) {
        return getMovedRegion(getMovedRegionCoords(x1, z1, x2, z2, radius), y1, y2);
    }

    public GameItem[] getMovedRegion(Vector2f[] coords, int y1, int y2) {
        List<GameItem> region = new ArrayList<>();

        for (Vector2f coord : coords)
        {
            for (int k = y1; k < y2; k++) {
                GameItem gameItem = getBlock((int)coord.x(), k, (int)coord.y());
                if (gameItem != null) {
                    region.add(gameItem);
                }
            }
        }

        return region.toArray(new GameItem[0]);
    }


}
