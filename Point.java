// This class represent a point in the Reversi board.
public class Point{
    public int x, y;  // Coordinates of each point on the Reversi board
    char colour;      // Current colour of a point

    // Values for the colour of the tiles on the Reversi board
    public static final char W = '\u26AA';  // White circle
    public static final char B = '\u26AB';  // Black circle
    public static final char E = '\u2610';  // Empty tile

    /**
     * Constructor 1:
     * @param x is the given x - coordinate
     * @param y is the given y - coordinate
     * @param colour is the given colour
     */
    public Point(int x, int y, char colour){
        this.x = x;
        this.y = y;
		this.colour = colour;
    }

    /**
     * Constructor 2: with no parameters.
     * The point has default values for it's coordinates (x = 0, y = 0) and the tile
     * is empty.
     */
    public Point(){
        this.x = this.y = 0;
		this.colour = E;
    }

    /**
     * Constructor 3: The tile is empty but we have values for the point's coordinates
     * @param x is the given x - coordinate
     * @param y is the given y - coordinate
     */
    public Point(int x, int y){
        this.x = x;
        this.y = y;
        this.colour = E;
    }

    /**
     * Getter for x - coordinate
     * @return the x - coordinate of a given point
     */
    int getX(){
        return x;
    }

    /**
     * Getter for y - coordinate
     * @return the y - coordinate of a given point
     */
    int getY(){
        return y;
    }

    /**
     * Getter for the tile's colour
     * @return the colour of a given point
     */
    char getColour(){
        return colour;
    }

    /**
     * Setter for the tile's colour
     * @param colour is the colour we want to assign to the point
     */
    public void setColour(char colour){
        this.colour = colour;
    }

    /**
     * This function changes a point's colour to the opponent's colour
     */
    public void swapColour(){
        // if the point's colour is white then we swap to black
        if (colour == W){
            colour = B;
        }
        // if the point's colour is black then we swap to white
        else if (colour == B){
            colour = W;
        }
    }

}
