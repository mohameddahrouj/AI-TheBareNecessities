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
	private int PUZZLE_SIZE = 9;

	private int[] goal;
	private int[] currentBoard;
	
	private int rows;
	private int cols;

	// Constructor
	// board = representation for the new state to be constructed
	// n,m board size
	public SMPState(int[] board, int[] goal, int rows, int cols)
	{
		this.currentBoard = board;
		this.goal = goal;
		this.rows = rows;
		this.cols = cols;
		//Calculate puzzle size
		PUZZLE_SIZE = rows*cols;
	}

	@Override
	public double findCost()
	{
		return 1;
	}

	//Set the 'tiles out of place' distance for the current board
	// Used for A* - h1(n) value
	@Override
	public int getOutOfPlace()
	{
		int outOfPlace = 0;
		for (int i = 0; i < currentBoard.length; i++)
		{
			if (currentBoard[i] != goal[i])
			{
				outOfPlace++;
			}
		}
		return outOfPlace;
	}

	
	// Set the Manhattan Distance for the current board
	// Used for A* - h2(n) value
	@Override
	public int getManDist()
	{
		int value = 0;
		
		for (int i = 0; i < currentBoard.length; i++)
		{
			if(this.currentBoard[i] != i)
			{
				int col = i % cols;
				int goalCol = this.currentBoard[i] % cols;
				
				int row = i / this.cols;
				int goalRow = this.currentBoard[i] / cols;
				
				if(row != goalRow)
					value++;
				if(col != goalCol)
					value++;
			}
		}
		return value;
	}
	
	//Set the 'average' of h1 and h2 for the current board
	// Used for A* - h3(n) value
	@Override
	public int getAverageHeuristic()
	{
		return (getOutOfPlace() + getManDist())/2;
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
		s.add((new SMPState(copy, goal, rows, cols)));
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

	public int getTimeTaken(){
		return 0;
	}

	@Override
	public boolean isGoal()
	{
		if (Arrays.equals(currentBoard, goal))
		{
			return true;
		}
		return false;
	}

	/**
	 * Method to print out the current state. Prints the puzzle board for any size.
	 */
	@Override
	public void printState()
	{
		//Convert 1D array to 2D array
		int twoDArray[][] = new int[rows][cols];
		for(int i=0; i<rows;i++){
		   for(int j=0;j<cols;j++){
			   twoDArray[i][j] = currentBoard[j%cols+i*cols];
		   }
		}
		
		for(int[] row : twoDArray) {
			for (int index : row) {
				System.out.print(index);
				System.out.print("  ");
			}
			System.out.println();
		}
	}

	@Override
	public void setStateValue(int stateValue) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public State getPreviousState() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void setPreviousState(State State) {
		// TODO Auto-generated method stub
		
	}

}