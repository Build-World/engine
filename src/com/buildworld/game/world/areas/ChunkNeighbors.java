package com.buildworld.game.world.areas;

public class ChunkNeighbors {

    private Chunk target;
    private Chunk north;
    private Chunk south;
    private Chunk east;
    private Chunk west;

    public ChunkNeighbors(Chunk target, Chunk north, Chunk south, Chunk east, Chunk west) {
        this.target = target;
        this.north = north;
        this.south = south;
        this.east = east;
        this.west = west;
    }

    public Chunk getTarget() {
        return target;
    }

    public void setTarget(Chunk target) {
        this.target = target;
    }

    public Chunk getNorth() {
        return north;
    }

    public void setNorth(Chunk north) {
        this.north = north;
    }

    public Chunk getSouth() {
        return south;
    }

    public void setSouth(Chunk south) {
        this.south = south;
    }

    public Chunk getEast() {
        return east;
    }

    public void setEast(Chunk east) {
        this.east = east;
    }

    public Chunk getWest() {
        return west;
    }

    public void setWest(Chunk west) {
        this.west = west;
    }
}
