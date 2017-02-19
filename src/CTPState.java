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
	private Direction[] GOAL;

	// The current 4-bit representation of the state
	public Direction[] curState;
	
	private int[] times; 
	private int timeTaken;
	private int n;

	/**
	 * Default Constructor
	 */
	public CTPState(int[] times)
	{
		this.times = times;
		n = times.length;
		curState = new Direction[n];
		timeTaken = 0;
		
		//Everyone starts on the left hand side
		for (int i=0; i<n; i++)
		{
			curState[i]= Direction.Left;
		}
		
		//Initialize goal
		GOAL = new Direction[n];
		for (int i=0; i<n; i++)
		{
			GOAL[i]= Direction.Right;
		}
	}

	//Array containing a state, which has all peoples positions
	public CTPState(int[] times, Direction[] stateArr, int timeTaken)
	{
		this.times = times;
		n = times.length;
		curState = new Direction[n];
		this.timeTaken = timeTaken;
		//Everyone starts on the left hand side
		for (int i=0; i<n; i++)
		{
			curState[i]= stateArr[i];
		}
		
		//Initialize goal
		GOAL = new Direction[n];
		for (int i=0; i<n; i++)
		{
			GOAL[i]= Direction.Right;
		}
	}

	//Cost to come to this state
	@Override
	public double findCost()
	{
		return 1;
	}
	
	//Return the array of times corresponding to each person
	public int[] getTimes(){
		return times;
	}
	
	public Direction[] getCurState()
	{
		return curState;
	}
	
	public int getTimeTaken(){
		return timeTaken;
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
						
						successors.add(new CTPState(times, tempState, getMax(times[0], times[i])));
						tempState = Arrays.copyOf(curState, curState.length);// reset
					}
			}
			// going alone, if we didn't add anything
			tempState[0] = Direction.Right;
			successors.add(new CTPState(times, tempState, getMax(times[0], 0)));
			tempState = Arrays.copyOf(curState, curState.length);

		}
		// if person is on the right
		else
		{
			// he must select a person to take
			for (int i = 1; i < n; i++){
					// he must take a person with him to the left
					if (tempState[i] == Direction.Right)
					{
						tempState[0] = Direction.Left;
						tempState[i] = Direction.Left;
						successors.add(new CTPState(times, tempState, getMax(times[0], times[i])));
						tempState = Arrays.copyOf(curState, curState.length);
					}
			}
			// going alone
			tempState[0] = Direction.Left;
			successors.add(new CTPState(times, tempState, getMax(times[0], 0)));
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
	
	public int getMax(int num1, int num2){
	
		if(num1>num2){
			return num1;
		}
		return num2;
	
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
		for(int i=0; i<n; i++){
			System.out.println("Person" + Integer.toString(i+1)+ ": " + curState[i]);
		}
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

	@Override
	public int getOutOfPlace() {
		int numOutOfPlace = 0;
		
		for (Direction d: curState){
			if(d.equals(Direction.Left)){
				numOutOfPlace++;
			}
		}
		
		return numOutOfPlace;
	}

	@Override
	public int getManDist() {
		int time = 0;
		//go through all people except first who is the slowest
		for (int i = n - 1; i >= 1; i--){
			//Slowest person goes
			time += times[i];
			//Fastest person (first person) comes back
			time += times[0];
			//if last person in list
			if(i==1){
				time -= times[0];
			}
		}
		return time;
	}


	@Override
	public int getAverageHeuristic() {
		return (getOutOfPlace() + getManDist())/2;
	}
}
