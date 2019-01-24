package com.buildworld.game.world.areas;

import com.buildworld.game.blocks.Block;
import com.buildworld.game.world.interfaces.IArea;
import org.joml.Vector2f;
import org.joml.Vector3f;

public class BlockNeighbors implements IArea {

    private Block target;
    private Block north;
    private Block south;
    private Block east;
    private Block west;
    private Block up;
    private Block down;

    public BlockNeighbors(Block target, Block north, Block south, Block east, Block west, Block up, Block down) {
        this.target = target;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
        this.up = up;
        this.down = down;
    }

    public BlockNeighbors(Block target) {
        this.target = target;
    }

    public Block getNorth() {
        return north;
    }

    public void setNorth(Block north) {
        this.north = north;
    }

    public Block getSouth() {
        return south;
    }

    public void setSouth(Block south) {
        this.south = south;
    }

    public Block getEast() {
        return east;
    }

    public void setEast(Block east) {
        this.east = east;
    }

    public Block getWest() {
        return west;
    }

    public void setWest(Block west) {
        this.west = west;
    }

    public Block getUp() {
        return up;
    }

    public void setUp(Block up) {
        this.up = up;
    }

    public Block getDown() {
        return down;
    }

    public void setDown(Block down) {
        this.down = down;
    }

    public Block getTarget() {
        return target;
    }

    @Override
    public Vector2f getLocation2D() {
        return new Vector2f(target.getPosition().x, target.getPosition().z);
    }

    public Vector3f getLocation3D() {
        return new Vector3f(target.getPosition());
    }
}
