// This class represents the Reversi board
import java.util.ArrayList;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

public class State {

	public Point[][] Board = new Point[8][8];  // We make a 2d Array as our board. Each tile on the board is a Point.
	public int sum;  // sum is used when we calculate the value of each move

    /**
     * This function creates our initial game board.
     * When the game begins the player who plays with black has two disks set at tiles (4,4) and (3,3).
     * The player who plays with white has two disks set at tiles (3,4) and (4,3).
     * Every other tile is empty.
     * We consider that in this state sum is 0 because it is the initial state and therefore it is not beneficial
     * to a particular player.
     */
	public void initialState() {
	    sum = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Board[i][j] = new Point(i, j);
            }
        }
        Board[3][4].setColour(Point.B);
		Board[4][3].setColour(Point.B);
		Board[4][4].setColour(Point.W);
		Board[3][3].setColour(Point.W);
	}

    /**
     * This function prints the Reversi board and it resembles the actual game board as much as possible.
     */
    public void printBoard() {
        // We use PrintStream in order to print the unicode characters
        PrintStream out = new PrintStream(System.out, true, StandardCharsets.UTF_8);
        System.out.println("  A  B C D  E F G  H");  // In the top horizontal line we put letters from A to H
        // to differentiate the columns and refer to them for the coordinates.
        for (int i = 0; i < 8; i++) {
            System.out.print((i+1) + " ");  // In the first vertical line we print the number of each row (1 to 8)
            for (int j = 0; j < 8; j++) {
                out.print(Board[i][j].getColour());  // We print the appropriate char depending on the colour of each point
                System.out.print(" ");
            }
            System.out.println();
        }
    }

    /**
     * This function creates a copy of the given board.
     * @param board is the board we are copying to the new board
     * @return the new board - copy we created (newBoard)
     */
    public State CopyBoard(State board) {
	    State newBoard = new State(); // Create a new object for our copy
	    newBoard.sum = board.sum;  // The copy's sum (current value of the board) is the same as the board we are copying from
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // Each tile in our copy is a point which for a specific set of (x,y) coordinates
                // has the same colour as the parameter board's tile.
                newBoard.Board[i][j] = new Point(board.Board[i][j].getX(), board.Board[i][j].getY(), board.Board[i][j].getColour());
            }
        }
        return newBoard;
    }

    /**
     * This function finds the valid moves a player can choose from.
     * The available moves depend on the colour the player is playing with.
     * @param colour is the player's colour.
     * @return the ArrayList that contains the valid moves the player with the given colour can choose from
     */
    public ArrayList<Direction> findValidMoves(char colour) {
        ArrayList<Direction> movesList = new ArrayList<>();  // The ArrayList in which we will add the valid moves
        // We will search for valid moves by checking every tile on the board
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                Point position = Board[i][j];  // The current tile
                if (position.getColour() == Point.E) { // If the current tile is empty we will begin to examine the surrounding tiles
                    for (int row = -1; row <= 1; row++) {
                        for (int col = -1; col <=1; col++) {
                            // When row and col are zero we our at the current tile
                            if (row == 0 && col == 0)
                                continue;

                            // If we start to examine points with coordinates outside of the board we move on
                            try{
                                // Find a neighboring tile using col and row
                                // The neighbor will be a tile above, bellow or diagonal to our current position
                                Point neighbor = Board[position.getX() + row][position.getY() + col];
                                // If that tile is not empty and the disk on it is not ours - if we found an opponent
                                if ((neighbor.getColour() != Point.E) && (neighbor.getColour() != colour)) {
                                    int swappedPieces = 1;  // We found a disk we might be able to swap

                                    // Start checking tiles in that direction
                                    do{
                                        // neighbor is now the previous neighbor's neighbor in the same direction
                                        neighbor = Board[neighbor.getX() + row][neighbor.getY() + col];
                                        // If the neighboring tile's piece has the same colour we do
                                        if (neighbor.getColour() == colour) {
                                            boolean exists = false; // We use exists to determine whether or not we already added this tile to our movesList
                                            for (Direction d : movesList) {  // for every tile in our movesList
                                                // Check if it is the same as the tile we are trying to add
                                                if (d.x == position.getX() && d.y == position.getY()) {
                                                    // If it is
                                                    int[] list = {row, col, swappedPieces}; // Create a vector that contains row and col to determine
                                                    // our direction and how many pieces we can swap
                                                    d.fillDirList(list); // Add that vector to our tile's list of vectors.
                                                    exists = true;
                                                    break;
                                                }
                                            }
                                            // If we haven't added this tile to our movesList we add it
                                            if (!exists){
                                                Direction d = new Direction(position.getX(), position.getY());
                                                int[] list = {row, col, swappedPieces}; // A vector that contains information about our direction and
                                                // the number of disks we can swap
                                                d.fillDirList(list); // Add that vector to our tile's list of vectors.
                                                movesList.add(d);
                                            }
                                        }
                                        swappedPieces++; // Increase by one in case we can make one more move towards that direction
                                    }while (neighbor.getColour() != Point.E && neighbor.getColour() != colour);
                                    // Do the above while the neighbor is an opponent
                                }
                            } catch (IndexOutOfBoundsException e){
                                // continue to the next loop
                            }
                        }
                    }
                }
            }
        }
        return movesList;
    }

    /**
     * This function evaluates the current board. It is used to determine whether a move is beneficial or not.
     * @param tempBoard is the given board we are evaluating
     */
    public void evaluate(State tempBoard) {
        sum = 0; // The value of the given board is 0, since we haven't made any calculations yet.
        // We construct a priority board using positional strategy
        // Each tile has a value based on how beneficial making a move there would be for us
        // The corners are the best, the 4 tiles at the center are considered neutral,
        // Tiles adjacent to the corners are worth less and the square whose corners are B2, G2, B7, G7 creates a
        // danger zone which consist of it's corners and the tiles on it's borders
        // Lastly, the tiles surrounding the center are good since they allow a player mobility and the rest of
        // the tiles are also worth an alright value.
        int[][] priorityBoard = { {1000, -300, 120,  80,  80, 120, -300, 1000},
                                  {-300, -500, -45, -60, -60, -45, -500, -300},
                                  { 120,  -45,  15,  10,  10,  15,  -45,  120},
                                  {  80,  -60,  10,  20,  20,  10,  -60,   80},
                                  {  80,  -60,  10,  20,  20,  10,  -60,   80},
                                  { 120,  -45,  15,  10,  10,  15,  -45,  120},
                                  {-300, -500, -45, -60, -60, -45, -500, -300},
                                  {1000, -300, 120,  80,  80, 120, -300, 1000} };

        int b = 0;
        int w = 0;
        for (int x = 0; x < 8; x++) {
            for (int y = 0; y < 8; y++) {
                if (tempBoard.Board[x][y].getColour() == Point.B) {
                    b += priorityBoard[x][y]; // Add the value of the point where there is a black disk to b
                }
                else if (tempBoard.Board[x][y].getColour() == Point.W) {
                    w += priorityBoard[x][y]; // Add the value of the point where there is a white disk to w
                }
            }
        }

        b += findValidMoves(Point.B).size(); // Add the number of valid moves the player who plays with black can make
        w += findValidMoves(Point.W).size(); // Add the number of valid moves the player who plays with white can make

        // sum is the value of the board
        if (b+w == 0)
            sum = 0;
        else
            sum = (b - w)/(b + w);
    }

    /**
     * This function presents the board after a move has been made
     * @param tempBoard is the given board in which we want to make the move
     * @param d contains all the necessary information to make the desired move
     * @param colour is the colour the player who makes the move has
     * @return the new state
     */
    public State makeMove(State tempBoard, Direction d, char colour) {
        tempBoard.Board[d.x][d.y].setColour(colour); // Set the desired colour to the first tile

        // If there are more tiles we would like to swap
        for (int[] i : d.getDirList()) {
            for (int j = 1; j <= i[2]; j++) {
                int mx = d.x + j*i[0]; // find the x - coordinate of the tile who's colour we want to swap
                int my = d.y + j*i[1]; // find the y - coordinate of the tile who's colour we want to swap
                tempBoard.Board[mx][my].swapColour(); // Swap the colour of the desired tile
            }
        }
        evaluate(tempBoard);  // Evaluate the current board
        return tempBoard;
    }

    /**
     * Getter
     * @return the object of this class
     */
    State get() {
	    return this;
    }

    /**
     * This function returns an ArrayList of all the boards we would have if we made each valid move
     * (One board for each possible move)
     * @param colour is the colour of the player that is currently playing
     * @return the ArrayList of the boards we would have
     */
    public ArrayList<State> getChildren(char colour) {
        ArrayList<State> children = new ArrayList<>(); // Create an ArrayList for the boards

        // For each valid move our player can make
        for (Direction d : findValidMoves(colour))
        {
            children.add(makeMove(CopyBoard(get()), d, colour)); // Add the board we create using the makeMove function
        }
        return children;
    }

    /**
     * A String function that shows the current score
     * @return the score
     */
    public String getScore() {
        int w = 0;
        int b = 0;
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                if (Board[i][j].getColour() == Point.W)
                    w++; // Count how many white disks we have
                else if (Board[i][j].getColour() == Point.B)
                    b++; // Count how many black disks we have
            }
        }

        return "The score is: Black: " + b + " White: " + w;
    }

    /**
     * This function is true when the game is over.
     * The game is over when the board is full or when neither player can move
     * @return the boolean variable that determines whether the game is over
     */
    public boolean isTerminal() {
        boolean gameOver = true; // We assume the game is over, initially
        for (int i = 0; i < 8; i++) {
            for (int j = 0; j < 8; j++) {
                // If there is at least one empty tile the game is not over
                if (Board[i][j].getColour() == Point.E) {
                    gameOver = false;
                    break;
                }
            }
            // If there is at least one empty tile we don't need to check for more, the game will continue
            if (!gameOver)
                break;
        }

        // If there are empty tiles we must check whether or not at least one of the players can make a move
        if (!gameOver) {
            // If neither of the players can move, we have reached a dead end and the game is over
            if (findValidMoves(Point.W).size() == 0 && findValidMoves(Point.B).size() == 0)
                gameOver = true;
        }
        return gameOver;
    }
}