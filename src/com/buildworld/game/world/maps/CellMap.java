package com.buildworld.game.world.maps;

/**
 *
 * Loosely based off of this article and adapted to 3D
 * https://gamedevelopment.tutsplus.com/tutorials/generate-random-cave-levels-using-cellular-automata--gamedev-9664
 */
abstract public class CellMap extends GenerationMap3D {

    private float bornAliveChance = 0.3f;
    private int deathLimit = 5;
    private int birthLimit = 5;
    private int steps = 3;

    public CellMap(int x, int z, int y) {
        super(x, z, y);
    }

    public float getBornAliveChance() {
        return bornAliveChance;
    }

    public void setBornAliveChance(float bornAliveChance) {
        this.bornAliveChance = bornAliveChance;
    }

    public int getDeathLimit() {
        return deathLimit;
    }

    public void setDeathLimit(int deathLimit) {
        this.deathLimit = deathLimit;
    }

    public int getBirthLimit() {
        return birthLimit;
    }

    public void setBirthLimit(int birthLimit) {
        this.birthLimit = birthLimit;
    }

    public int getSteps() {
        return steps;
    }

    public void setSteps(int steps) {
        this.steps = steps;
    }

    abstract protected float getChance(int x, int z, int y);
    abstract protected int getOutOfBoundsCell(int x, int z, int y);

    public void generate(){
        //Set up the map with random values
        generateInitialMap();
        //And now run the simulation for a set number of steps
        for(int i=0; i<steps; i++){
            simulate();
        }
    }

    protected void generateInitialMap(){
        for(int i=0; i<getX(); i++){
            for(int j=0; j<getZ(); j++){
                for(int k = 0; k<getY(); k++)
                {
                    if(getChance(i,j,k) < bornAliveChance){
                        set(i,j,k,1);
                    } else {
                        set(i,j,k,0);
                    }
                }
            }
        }
    }

    //Returns the number of cells in a ring around (x,y) that are alive.
    protected int countAliveNeighbours(int x, int z, int y){
        int count = 0;
        for(int i=-1; i<2; i++){
            for(int j=-1; j<2; j++){
                for(int k=-1; k<2; k++)
                {
                    //If we're looking at the middle point
                    if(i == 0 && j == 0 && k == 0){
                        //Do nothing, we don't want to add ourselves in!
                        continue;
                    }

                    int neighbour_x = x+i;
                    int neighbour_z = z+j;
                    int neighbour_y = y+k;

                    //In case the index we're looking at it off the edge of the map
                    if(neighbour_x < 0 || neighbour_z < 0 || neighbour_y < 0 || neighbour_x >= getX() || neighbour_z >= getZ() || neighbour_y >= getY()){
                        if(getOutOfBoundsCell(neighbour_x, neighbour_z, neighbour_y) == 1)
                        {
                            count = count + 1;
                        }
                    }
                    //Otherwise, a normal check of the neighbour
                    else if(get(neighbour_x, neighbour_z, neighbour_y) == 1){
                        count = count + 1;
                    }
                }
            }
        }
        return count;
    }

    protected void simulate(){
        float[][][] newMap = new float[getX()][getZ()][getY()];
        //Loop over each row and column of the map
        for(int i=0; i<getX(); i++){
            for(int j=0; j<getZ(); j++){
                for(int k = 0; k < getY(); k++)
                {
                    int nbs = countAliveNeighbours(i,j,k);
                    //The new value is based on our simulation rules
                    //First, if a cell is alive but has too few neighbours, kill it.
                    if(get(i,j,k) == 1){
                        if(nbs < deathLimit){
                            newMap[i][j][k] = 0;
                        }
                        else{
                            newMap[i][j][k] = 1;
                        }
                    } //Otherwise, if the cell is dead now, check if it has the right number of neighbours to be 'born'
                    else{
                        if(nbs == birthLimit){
                            newMap[i][j][k] = 1;
                        }
                        else{
                            newMap[i][j][k] = 0;
                        }
                    }
                }

            }
        }
        setMap(newMap);
    }


}
