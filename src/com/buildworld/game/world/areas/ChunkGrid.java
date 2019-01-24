package com.buildworld.game.world.areas;

public class ChunkGrid extends ChunkNeighbors {

    private Chunk northEast;
    private Chunk northWest;
    private Chunk southEast;
    private Chunk southWest;

    public ChunkGrid(Chunk target, Chunk north, Chunk south, Chunk east, Chunk west, Chunk northEast, Chunk northWest, Chunk southEast, Chunk southWest) {
        super(target, north, south, east, west);
        this.northEast = northEast;
        this.northWest = northWest;
        this.southEast = southEast;
        this.southWest = southWest;
    }

    public Chunk getNorthEast() {
        return northEast;
    }

    public void setNorthEast(Chunk northEast) {
        this.northEast = northEast;
    }

    public Chunk getNorthWest() {
        return northWest;
    }

    public void setNorthWest(Chunk northWest) {
        this.northWest = northWest;
    }

    public Chunk getSouthEast() {
        return southEast;
    }

    public void setSouthEast(Chunk southEast) {
        this.southEast = southEast;
    }

    public Chunk getSouthWest() {
        return southWest;
    }

    public void setSouthWest(Chunk southWest) {
        this.southWest = southWest;
    }
}
