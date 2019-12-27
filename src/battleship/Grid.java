/**
 * 
 */
package battleship;

import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

import types.Square;

/**
 * @author youss
 *
 */
public class Grid {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Scanner sc = new Scanner(System.in);
		Grid g = new Grid();
		System.out.println(g.toString());
		int[] ships = new int[] { 5, 4, 3, 3, 2 }; //TODO - Class for the Ships
		g.setup(ships);
		g.getField()[0][0] = Square.SHOT;
		System.out.println(g.toString());
		
		while(!g.check()) {
			
			System.out.println("Choix position tir x :");
			int x = sc.nextInt();
			System.out.println("Choix position tir y : ");
			int y = sc.nextInt();
			x = (x>=0 && x<10) ? x :0;
			y = (y>=0 && y<10) ? y :0;	
			g.shot(x, y);
			System.out.println(g.toString());
		}
		System.out.println("Game over !");
		
	}

	private static final int GRID_LENGTH = 10;
	private static final int GRID_WIDTH = 10;

	private Square[][] field;

	
	/**
	 * Getter for the field
	 *@return Returns a copy of the field
	 */
	public Square[][] getField() {
		return Arrays.stream(field).map(Square[]::clone).toArray(Square[][]::new);
	}

	public Grid() {
		field = new Square[GRID_LENGTH][GRID_WIDTH];
		initGrid();
	}


	private enum orientation {
		VERTICAL, HORIZONTAL
	}

	   /**
	   * Place a piece of ship
	   * @param x This is the x coordinate
	   * @param x  This is the y coordinate
	   */
	private void placePiece(int x, int y) {

		field[x][y]= Square.OCCUPIED;

	}

   /**
    * Overriding toString()
    * Returns a String representing the field
    * X = Missed Shot
    * O = Occupied Square
    * E = Empty Square
    * D = Destroyed Piece of Ship
   */
	@Override
	public String toString() {
		StringBuilder b = new StringBuilder();

		for (int i = 0; i < GRID_WIDTH; i++) {
			for (int j = 0; j < GRID_LENGTH; j++) {
				if (field[i][j]== Square.EMPTY) {
					b.append(".  ");
				}

				else if (field[i][j] == Square.OCCUPIED) {
					b.append("O  ");
				}

				else if (field[i][j] == Square.SHOT)
					b.append("X  ");

				else {
					b.append("D  ");
				}
			}

			b.append("\n");
		}

		return b.toString();
	}

	private void initGrid() {
		for (int i = 0; i < GRID_LENGTH; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				field[i][j] = Square.EMPTY;
			}
		}
	}

	private boolean isSquareAvailable(int x, int y) {
		if (field[x][y] == Square.EMPTY)
			return true;
		else
			return false;
	}

	   /**
	   * Place the ship on the field with the orientation o
	   * On the position (x,y)
	   */
	private boolean placeShip(int shiplength, orientation o, int x, int y) {
		int deltaX, deltaY;
		int currentX = x;
		int currentY = y;
		if (o == orientation.VERTICAL) {
			deltaX = 0;
			deltaY = 1;
		}

		else { // o == orientation.HORIZONTAL
			deltaX = 1;
			deltaY = 0;
		}

		for (int i = 0; i < shiplength; i++) {
			if (!isSquareAvailable(currentX, currentY))
				return false;
			else {
				currentX = currentX + deltaX;
				currentY = currentY + deltaY;
			}

			if (currentX >= GRID_LENGTH || currentY >= GRID_WIDTH)
				return false;

		}
		currentX = x;
		currentY = y;// reset initial point
		for (int j = 0; j < shiplength; j++) {

			placePiece(currentX, currentY);

			currentX = currentX + deltaX;
			currentY = currentY + deltaY;

		}

		return true;
	}

   /**
    * Returns a random orientation
   */
	private orientation pickRandomOrientation(Random rand) {
		if (rand.nextBoolean())
			return orientation.HORIZONTAL;
		else
			return orientation.VERTICAL;
	}

	   /**
	    * sets the field by placing the ships
	    * @param ships This is an array containing the lengths of the ships
	   */
	public void setup(int[] ships) {
		initGrid();
		boolean out = false;
		Random rand = new Random(System.currentTimeMillis());
		int[] listX = Utils.getRandomRange(GRID_LENGTH); //Generates a random sequence of numbers from 0 to 9 with no duplicates
		int[] listY = Utils.getRandomRange(GRID_WIDTH);
		for (int shiplength : ships) {
			orientation o = pickRandomOrientation(rand);
			for (int x : listX) {
				out = false;
				for (int y : listY) {
					if (placeShip(shiplength, o, x, y)) {
						out = true;
						break;
					}
				}
				if (out)
					break;
			}
		}
	}
	
   /**
    * Checks if the game is over
   */
	public boolean check() {
		int nbDestroyed = 0;
		for (int i = 0; i < GRID_LENGTH; i++) {
			for (int j = 0; j < GRID_WIDTH; j++) {
				if (field[i][j] == Square.OCCUPIED)
					return false;

				else if (field[i][j] == Square.DESTROYED)
					nbDestroyed++;
			}
		}
		return nbDestroyed > 0;
	}

   /**
    * Shots on a square at position (x,y)
    * @return Square returns the state of the square shot
   */
	public Square shot(int x, int y) {
		if (field[x][y] == Square.OCCUPIED)
			field[x][y]= Square.DESTROYED;
		else if (field[x][y] == Square.EMPTY)
			field[x][y] =  Square.SHOT;
		
		return field[x][y];
	}
		

}
