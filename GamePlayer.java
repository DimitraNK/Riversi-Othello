// This class represents a player that plays Reversi
import java.util.Random;
import java.util.ArrayList;

public class GamePlayer {
    private int maxDepth;  // maxDepth is the maximum depth MiniMax will reach
    char playerColour;  // playerColour is the colour the player plays with

    /**
     * Constructor for our player
     * @param maxDepth is the given value for the maximum depth MiniMax will reach
     * @param playerColour is the player's colour
     * */
    public GamePlayer(int maxDepth, char playerColour) {
        this.maxDepth = maxDepth;
        this.playerColour = playerColour;
    }

    /**
     * Minimax Algorithm
     * @param board is the given board we will initiate MiniMax for
     * @return the current state depending on who is playing
     */
    public State MiniMax(State board) {
        // If the player who's colour is black is playing then we want to Maximize the heuristics value
        if (playerColour == Point.B) {
            return max(board.CopyBoard(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
        // If the player who's colour is white is playing then we want to Minimize the heuristics value
        else {
            return min(board.CopyBoard(board), 0, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }
    }

    // The max and min functions are called interchangingly until maxDepth is reached

    /**
     * This function is called when we Maximize
     * @param board is the given board
     * @param depth is the depth we are currently reaching
     * @param alpha is used for AB pruning
     * @param beta is used for AB pruning
     * @return max
     */
    public State max(State board, int depth, int alpha, int beta) {
        Random rand = new Random();  // Create a Random object
        State max;

        // If the state is terminal or after we reach maximum depth, then the board is returned.
        if((board.isTerminal()) || (depth == maxDepth)) {
            return board;
        }

        ArrayList<State> children = board.getChildren(Point.B); // Find the boards that derive from the valid moves
        // the player who plays with black can make
        if (children.isEmpty())
            max = board; // There are no possible moves
        else
            max = children.get(0); // Set max as the first board

        // for every board (child)
        for (State c : children) {
            // For each child min is called on a lower depth
            State tempState = min(c,depth + 1, alpha, beta);
            // The child with the greatest value is selected and returned
            if(tempState.sum > max.sum)
                max = c;
            // If we have the same value with the heuristic
            // and the random integer between 0 and 2 (excluded) we generated is 0
            else if (tempState.sum == max.sum && rand.nextInt(2) == 0)
                max = c;


            if (alpha < tempState.sum)
                alpha = tempState.sum;

            //AB Pruning
            if (alpha >= beta)
                break; // "Prune" the tree branch we don't need.
        }
        return max;
    }

    /**
     * This function is called when we Minimize
     * @param board is the given board
     * @param depth is the depth we are currently reaching
     * @param alpha is used for AB pruning
     * @param beta is used for AB pruning
     * @return min
     */
    public State min(State board, int depth , int alpha, int beta) {
        Random rand = new Random(); // Create a Random object
        State min;

        // If the state is terminal or after we reach maximum depth, then the board is returned.
        if((board.isTerminal()) || (depth == maxDepth)) {
            return board;
        }

        // Find the boards that derive from the valid moves the player who plays with white can make
        ArrayList<State> children = board.getChildren(Point.W);
        if (children.isEmpty())
            min = board; // There are no possible moves
        else
            min = children.get(0); // Set min as the first board

        // for every board (child)
        for (State c : children) {
            // For each child max is called on the next depth
            State tempState = max(c,depth + 1, alpha, beta);
            // The child with the smallest value is selected and returned
            if(tempState.sum < min.sum)
                min = c;
            // If we have the same value with the heuristic and the random integer between 0 and 2 (excluded) we generated is 0
            else if (tempState.sum == min.sum && rand.nextInt(2) == 0)
                min = c;


            if (beta > tempState.sum)
                beta = tempState.sum;

            //AB Pruning
            if (alpha >= beta)
                break; // "Prune" the tree branch we don't need.
        }
        return min;
    }
}
