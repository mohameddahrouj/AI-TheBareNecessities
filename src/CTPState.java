import java.util.ArrayList;
import java.util.Arrays;

/**
 * Commodity transportation problem
 * Invalid states are stored, not generated. Generated states are checked for
 * validity against the invalidStates 2D-array, and removed from the successors
 * array list if invalid.
 * State Representation:
 * A state is defined as a 4-bit string with Direction enums representing whether a person
 * is on the left or right of the river.
 * 
 * @author Mohamed Dahrouj
 * 
 */
public class CTPState implements State
{
	//Goal state- all people end up on the right side of the bridge
	private final Direction[] GOAL = new Direction[]{ Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right, Direction.Right };

	// The current 4-bit representation of the state
	public Direction[] curState;
	
	private int[] times; 
	private int n;

	/**
	 * Default Constructor
	 */
	public CTPState(int[] times)
	{
		//Everyone starts on the left hand side
		curState = new Direction[]{ Direction.Left, Direction.Left, Direction.Left, Direction.Left, Direction.Left, Direction.Left };
		this.times = times;
		n = times.length;
	}


	//Array containing a state, which has all six positions
	public CTPState(int[] times, Direction[] stateArr)
	{
		curState = new Direction[]{ stateArr[0], stateArr[1], stateArr[2], stateArr[3], stateArr[4], stateArr[5] };
		this.times = times;
		n = times.length;
	}

	//Cost to come to this state
	@Override
	public double findCost()
	{
		return 1;
	}
	
	//Return the aray of times corresponsding to each person
	public int[] getTimes(){
		return times;
	}
	
	public Direction[] getCurState()
	{
		return curState;
	}
	
	/**
	 * Generate all possible successors to the current state.
	 * Remove successor states that match a state description in the "invalid states" array
	 */
	@Override
	public ArrayList<State> generateChildren()
	{
		ArrayList<State> successors = new ArrayList<State>();
		Direction[] tempState = Arrays.copyOf(curState, curState.length);
		
		
		// P1 is on the left
		if (tempState[0] == Direction.Left)
		{
			for (int i = 1; i < n; i++){
				// he must take a person with him
				if (tempState[i] == Direction.Left)
				{
					tempState[0] = Direction.Right;
					tempState[i] = Direction.Right;
					successors.add(new CTPState(times, tempState));
					tempState = Arrays.copyOf(curState, curState.length);// reset
				}	
			}
			// going alone, if we didn't add anything
			tempState[0] = Direction.Right;
			successors.add(new CTPState(times, tempState));
			tempState = Arrays.copyOf(curState, curState.length);

		}
		// if person is on the right
		else
		{
			// he must select an person to take
			for (int i = 1; i < n; i++){
				// he must take a person with him to the left
				if (tempState[i] == Direction.Right)
				{
					tempState[0] = Direction.Left;
					tempState[i] = Direction.Left;
					successors.add(new CTPState(times, tempState));
					tempState = Arrays.copyOf(curState, curState.length);
				}
			}
			// going alone
			tempState[0] = Direction.Left;
			successors.add(new CTPState(times, tempState));
			tempState = Arrays.copyOf(curState, curState.length);

		}
		return successors;
	}

	
	// Check to see if the current state is the goal state.
	@Override
	public boolean isGoal()
	{
		if (Arrays.equals(curState, GOAL))
		{
			return true;
		}
		return false;
	}

	@Override
	public boolean equals(Object obj)
	{
		if (this == obj)
			return true;
		else if (obj == null)
			return false;
		else if (getClass() != obj.getClass())
			return false;
		CTPState other = (CTPState) obj;
		if (!curState.equals(other.curState))
			return false;
		return true;
	}

	/**
	 * Method to print out the current state. Prints the current position of
	 * each person.
	 */
	@Override
	public void printState()
	{
		System.out.println("Person 1: " + curState[0]);
		System.out.println("Person 2: " + curState[1]);
		System.out.println("Person 3: " + curState[2]);
		System.out.println("Person 4: " + curState[3]);
		System.out.println("Person 5: " + curState[4]);
		System.out.println("Person 6: " + curState[5]);
		
		/*String[] leftside = new String[]{"X","X","X","X","X","X"};
		String bridge = " <--------> ";
		String[] rightside = new String[]{"X","X","X","X","X","X"};
		
		for(int i=0; i<6; i++){
			if(curState[0].equals(Direction.Left)){
				leftside[i] = Integer.toString(i + 1);
			}
			System.out.print(leftside[i]);
		}
		System.out.print(bridge);
		for(int i=0; i<6; i++){
			if(curState[0].equals(Direction.Right)){
				rightside[i] = Integer.toString(i + 1);
			}
			System.out.print(rightside[i]);
		}*/
	}

	/**
	 * Overloaded equals method to compare two states.
	 * 
	 * @return true or false, depending on whether the states are equal
	 */
	@Override
	public boolean equals(State s)
	{
		if (Arrays.equals(curState, ((CTPState) s).getCurState()))
		{
			return true;
		}
		else
			return false;

	}
}
