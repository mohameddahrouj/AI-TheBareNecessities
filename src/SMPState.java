import java.util.ArrayList;
import java.util.Arrays;

/**
 * 
 * Represented by 1D array
 * 0 represents the hole and is treated differently when generating children nodes.
 * The hole is not considered a tile
 * 
 * @author Mohamed Dahrouj
 * 
 */
public class SMPState implements State
{
	//Default 3x3
	private int PUZZLE_SIZE = 9;
	
	//A* Heuristics
	private int outOfPlace = 0;
	private int manDist = 0;
	private int averageHeuristic = 0;

	private int[] GOAL;
	private int[] currentBoard;
	
	private int rows;
	private int cols;

	// Constructor
	// board = representation for the new state to be constructed
	// n,m board size
	public SMPState(int[] board, int rows, int cols)
	{
		currentBoard = board;
		this.rows = rows;
		this.cols = cols;
		//Calculate puzzle size if different than 3x3
		PUZZLE_SIZE = rows*cols;
		
		//Initialize GOAL board
		if (rows==2 && cols==4){
			GOAL = new int[]{1, 2, 3, 4, 5, 6, 7, 0};
		}
		if (rows==3 && cols==3){
			GOAL = new int[]{1, 2, 3, 8, 0, 4, 7, 6, 5};
		}
		if (rows==2 && cols==5){
			GOAL = new int[]{1, 2, 3, 4, 5, 6, 7, 8, 9, 0};
		}
		setOutOfPlace();
		setManDist();
		setAverageHeuristic();
	}

	@Override
	public double findCost()
	{
		return 1;
	}

	//Set the 'tiles out of place' distance for the current board
	// Used for A* - h1(n) value
	private void setOutOfPlace()
	{
		for (int i = 0; i < currentBoard.length; i++)
		{
			if (currentBoard[i] != GOAL[i])
			{
				outOfPlace++;
			}
		}
	}

	
	// Set the Manhattan Distance for the current board
	// Used for A* - h2(n) value
	private void setManDist()
	{
		// linearly search the array independent of the nested for's below
		int index = -1;

		// just keeps track of where we are on the board (relatively, can't use
		// 0 so these
		// values need to be shifted to the right one place)
		for (int y = 0; y < rows; y++)
		{
			for (int x = 0; x < cols; x++)
			{
				index++;

				// sub 1 from the val to get the index of where that value
				// should be
				int val = (currentBoard[index] - 1);

				/*
				 * If we're not looking at the hole. The hole will be at
				 * location -1 since we subtracted 1 before to turn val into the
				 * index
				 */
				if (val != -1)
				{
					// Horizontal offset, mod the tile value by the horizontal
					// dimension
					int horiz = val % rows;
					// Vertical offset, divide the tile value by the vertical
					// dimension
					int vert = val / cols;

					manDist += Math.abs(vert - (y)) + Math.abs(horiz - (x));
				}
				// If we are looking at the hole, skip it
			}
		}
	}
	
	//Set the 'average' of h1 and h2 for the current board
	// Used for A* - h3(n) value
	private void setAverageHeuristic()
	{
		averageHeuristic = (outOfPlace + manDist)/2;
	}

	// Find and return index of hole
	private int getHole()
	{
		int holeIndex = -1;

		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			if (currentBoard[i] == 0)
				holeIndex = i;
		}
		return holeIndex;
	}

	// Return the h1(n) value
	public int getOutOfPlace()
	{
		return outOfPlace;
	}

	// Return the h2(n) value
	public int getManDist()
	{
		return manDist;
	}
	
	// Return the h3(n) value
	public int getAverageHeuristic()
	{
		return averageHeuristic;
	}
	
	public int[] getCurBoard()
	{
		return currentBoard;
	}

	// Copy method
	private int[] copyBoard(int[] state)
	{
		int[] copy = new int[PUZZLE_SIZE];
		for (int i = 0; i < PUZZLE_SIZE; i++)
		{
			copy[i] = state[i];
		}
		return copy;
	}

	/**
	 * @return an ArrayList containing all of the children for that state
	 */
	@Override
	public ArrayList<State> generateChildren()
	{
		ArrayList<State> children = new ArrayList<State>();
		children.addAll(generateAllChessHorseMoves());
		children.addAll(generateAllZeroMoves());
		return children;
	}
	
	//Generate horse moves for non-blank tiles
	private ArrayList<State> generateAllChessHorseMoves() {

        ArrayList<State> children = new ArrayList<State>();
        ArrayList<Integer> positions = new ArrayList<>();

        for (int i = 0; i < PUZZLE_SIZE ; i++) {

            if(i != getHole()){
	        	int currentPosistion = i;
	            if (isValidColumn(currentPosistion, Direction.Left, 1)) {
	                positions.add(currentPosistion - 1 - (cols * 2)); // down 2, 1 left
	                positions.add(currentPosistion - 1 + (cols * 2)); // up 2, 1 left
	            }
	
	            if (isValidColumn(currentPosistion, Direction.Right, 1)) {
	                positions.add(currentPosistion + 1 - (cols * 2)); // down 2, 1 right
	                positions.add(currentPosistion + 1 + (cols * 2)); // up 2, 1 right
	            }
	
	            if (isValidColumn(currentPosistion, Direction.Left, 2)) {
	                positions.add(currentPosistion - 2 - cols); // left 2, 1 down
	                positions.add(currentPosistion - 2 + cols); // left 2, 1 up
	            }
	
	            if (isValidColumn(currentPosistion , Direction.Right, 2)) {
	                positions.add(currentPosistion + 2 - cols); // right 2, 1 down
	                positions.add(currentPosistion + 2 + cols); // right 2, 1 up
	            }
	
	            for (int position: positions) {
	                if(isValidPosition(position) && position != getHole()){
	                	swapAndStore(position, i, children);
	                }
	            }
	            positions.clear();
            }
        }

        return children;
    }

	//Generate horizontal, vertical and diagonal moves
    private ArrayList<State> generateAllZeroMoves()
    {
    	ArrayList<State> children = new ArrayList<State>();
        int hole = getHole();
        ArrayList<Integer> positions = new ArrayList<>();

        if(isValidColumn(hole, Direction.Left,1))
        {
            positions.add(hole -1);
            positions.add(hole -1 + cols); // leftUpHorizontal
            positions.add(hole -1 - cols); // leftDownHorizontal
        }

        if(isValidColumn(hole, Direction.Right,1))
        {
            positions.add(hole +1);
            positions.add(hole +1 + cols); // rightUpHorizontal
            positions.add(hole +1 - cols); // rightDownHorizontal
        }

        positions.add(hole + cols); // up
        positions.add(hole - cols); // down

        for (int position: positions) {
            if(isValidPosition(position)){
            	swapAndStore(position, hole, children);
            }
        }

        return children;
    }

    private  boolean isValidPosition(int position)
    {
        return position < PUZZLE_SIZE && position >=0 ;
    }

    private  boolean isValidColumn(int position,Direction direction, int move)
    {
        int value = position % cols;
        if(direction == Direction.Left)
            value = value - move;
        else
            value = value + move;
        return value < cols && value >=0 ;
    }
	
	/*
	 * Switches the tiles at indices d1 and d2, in a copy of the current board
	 * creates a new state based on this new board and pushes into list
	 */
	private void swapAndStore(int p1, int p2, ArrayList<State> s)
	{
		int[] copy = copyBoard(currentBoard);
		int temp = copy[p1];
		copy[p1] = currentBoard[p2];
		copy[p2] = temp;
		s.add((new SMPState(copy, rows, cols)));
	}
	
	public int hashCode()
	{
		long result = 0;
		
		for(int i=0; i< PUZZLE_SIZE; i++)
		{
			result += Math.pow(10, i) * this.currentBoard[PUZZLE_SIZE -i -1];
		}
		
		return Long.valueOf(result).hashCode();
	}


	@Override
	public boolean isGoal()
	{
		if (Arrays.equals(currentBoard, GOAL))
		{
			return true;
		}
		return false;
	}

	/**
	 * Method to print out the current state. Prints the puzzle board.
	 */
	@Override
	public void printState()
	{
		if(rows ==3 && cols==3){
			System.out.println(currentBoard[0] + " | " + currentBoard[1] + " | " + currentBoard[2]);
			System.out.println("---------");
			System.out.println(currentBoard[3] + " | " + currentBoard[4] + " | " + currentBoard[5]);
			System.out.println("---------");
			System.out.println(currentBoard[6] + " | " + currentBoard[7] + " | " + currentBoard[8]);
		}
		if(rows ==2 && cols==4){
			System.out.println(currentBoard[0] + " | " + currentBoard[1] + " | " + currentBoard[2] + " | " + currentBoard[3]);
			System.out.println("-------------");
			System.out.println(currentBoard[4] + " | " + currentBoard[5] + " | " + currentBoard[6] + " | " + currentBoard[7]);
		}
		if(rows ==2 && cols==5){
			System.out.println(currentBoard[0] + " | " + currentBoard[1] + " | " + currentBoard[2] + " | " + currentBoard[3]+ " | " + currentBoard[4]);
			System.out.println("-----------------");
			System.out.println(currentBoard[5] + " | " + currentBoard[6] + " | " + currentBoard[7] + " | " + currentBoard[8]+ " | " + currentBoard[9]);
		}
		
	}
	
	// Equals method to compare two states
	@Override
	public boolean equals(State s)
	{
		if (Arrays.equals(currentBoard, ((SMPState) s).getCurBoard()))
		{
			return true;
		}
		else
			return false;

	}

}