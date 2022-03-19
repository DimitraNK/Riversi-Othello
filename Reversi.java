import java.util.Scanner;
import java.util.ArrayList;
public class Reversi{

	public static void main(String[] args){
		Scanner in = new Scanner(System.in);
		Scanner input = new Scanner(System.in);
		System.out.println("Do you want to play Reversi?");
		System.out.println("Press Y to play or anything else to exit the game...");
		String ans = input.nextLine();
		while (ans.equals("Y")) {
			int ans1;
			// Check whether or not the answer is one of the available choices
			do {
				System.out.println("Select from the following choices: ");
				System.out.println("Press 0 for Computer vs Computer");
				System.out.println("Press 1 for Computer vs Human");
				ans1 = in.nextInt();
			}while (ans1 != 0 && ans1 !=1);
			int ans2;
			// Check whether or not the answer is one of the available choices
			do {
				System.out.println("Select from the following choices: ");
				System.out.println("Press 1 for Easy (depth 3)");
				System.out.println("Press 2 for Intermediate (depth 6)");
				System.out.println("Press 3 for Advanced (depth 9)");
				System.out.println("Press 4 to set your own depth");
				ans2 = in.nextInt();
			}while (ans2 <= 0 || ans2 > 4);
			// Set the value of the depth variable
			int depth;
			if (ans2 == 1){
				depth = 3;
			}
			else if (ans2 == 2){
				depth = 6;
			}
			else if (ans2 == 3){
				depth = 9;
			}
			else{
				System.out.println("What depth do you choose?");
				depth = in.nextInt();
			}
			// Computer vs Human
			if (ans1 == 1) {
				System.out.println("What is your name?");
				String name = input.nextLine();
				System.out.println("Computer vs "+name+" game begins now!");

				int ans3;
				// Check whether or not the answer is one of the available choices
				do {
					System.out.println("Press 1 to play first");
					System.out.println("Press 2 to play second");
					ans3 = in.nextInt();
				} while (ans3 != 1 && ans3 != 2);

				GamePlayer Computer;
				GamePlayer Human;
				boolean turn; // Is true when the Human player plays

				if (ans3 == 1) {
					System.out.println("You are playing first with black!");
					Computer = new GamePlayer(depth, Point.W); // Initialize GamePlayer object for the Computer
					Human = new GamePlayer(depth, Point.B); // Initialize GamePlayer object for the Human
					turn = true; // Human plays first
				}
				else {
					System.out.println("You are playing second with white!");
					Computer = new GamePlayer(depth, Point.B); // Initialize GamePlayer object for the Computer
					Human = new GamePlayer(depth, Point.W); // Initialize GamePlayer object for the Human
					turn = false; // Computer plays first
				}

				State mainBoard = new State();
				mainBoard.initialState();
				System.out.println("~*~*~*~*~*~*~*~*~*~*");
				mainBoard.printBoard();
				System.out.println("~*~*~*~*~*~*~*~*~*~*");

				// While the game isn't over
				while(!mainBoard.isTerminal()) {
					// If it's Human player's turn to play
					if (turn){
						System.out.println(name + ", it's your turn to play!");
						ArrayList<Direction> HumanMoves = mainBoard.findValidMoves(Human.playerColour); // Find valid moves

						if (HumanMoves.size() > 0) {
							// Display valid moves
							System.out.println("Here are your choices: ");
							for (int i = 0; i < HumanMoves.size(); i++) {
								System.out.println("Option " + (i +1) + ": ("+ (char)(HumanMoves.get(i).x + 65) +", "+ (HumanMoves.get(i).y + 1) + ")");
							}
							System.out.println("Press the number of one of the given options");
							int choice = in.nextInt();
							Direction d = HumanMoves.get(choice-1);
							mainBoard.makeMove(mainBoard, d, Human.playerColour); // Make the selected move
						}
						else {
							System.out.println(name + ", you cannot move!");
						}
					}
					// If it's Computer's turn to play
					else {
						System.out.println("It's the Computer's turn to play!");
						mainBoard = Computer.MiniMax(mainBoard); // Call MiniMax for the computer
					}
					// Print the current board
					System.out.println("~*~*~*~*~*~*~*~*~*~*");
					mainBoard.printBoard();
					System.out.println(mainBoard.getScore());
					System.out.println("~*~*~*~*~*~*~*~*~*~*");
					turn = !turn; // next player
				}

			}
			// Computer vs Human
			else {
				System.out.println("Computer1 vs Computer2 game begins now!");
				GamePlayer Computer1 = new GamePlayer(depth, Point.B); // Initialize Computer1 with black
				GamePlayer Computer2 = new GamePlayer(depth, Point.W); // Initialize Computer1 with white
				boolean turn = true;

				System.out.println("Computer1 will be playing first with black!");
				System.out.println("Computer2 will be playing second with white!");

				State mainBoard = new State();
				mainBoard.initialState();
				System.out.println("~*~*~*~*~*~*~*~*~*~*");
				mainBoard.printBoard();
				System.out.println("~*~*~*~*~*~*~*~*~*~*");

				while(!mainBoard.isTerminal()){
					if (turn) {
						System.out.println("It's the Computer1's turn to play!");
						mainBoard = Computer1.MiniMax(mainBoard); // Call MiniMax for Computer1
					}
					else {
						System.out.println("It's the Computer2's turn to play!");
						mainBoard = Computer2.MiniMax(mainBoard); // Call MiniMax for Computer1
					}
					turn = !turn; // next player
					// Print the board
					System.out.println("~*~*~*~*~*~*~*~*~*~*");
					mainBoard.printBoard();
					System.out.println(mainBoard.getScore());
					System.out.println("~*~*~*~*~*~*~*~*~*~*");
				}

			}
			System.out.println("Game Over!!!");
			System.out.println("Do you want to play Reversi?");
			System.out.println("Press Y to play or anything else to exit the game...");
			ans = input.nextLine();
		}
		System.out.println("Goodbye!");
		in.close();
		input.close();
	}
}