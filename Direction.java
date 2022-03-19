// This class represents a move on the reversi board.
import java.util.ArrayList;
public class Direction {
    public ArrayList<int[]> dirList = new ArrayList<>(); // An ArrayList that will consists of vectors that contain information about the direction of a move.
    public int x; // x - coordinate
    public int y; // y - coordinate

    /**
     * Constructor
     * @param x is the given x - coordinate of a move
     * @param y is the given y - coordinate of a move
     */
    public Direction(int x, int y) {
        this.x = x;
        this.y = y;
        dirList.clear();  // list that contains vectors with directions
    }

    /**
     * This function adds a vector to the our ArrayList
     * @param dir is the given list we want to add
     */
    void fillDirList (int[] dir){
        dirList.add(dir);
    }

    /**
     * Getter for dirList
     * @return dirList
     */
    public ArrayList<int[]> getDirList() {
        return dirList;
    }
}
