import java.util.ArrayList;

/**
 * State interface
 * @author Mohamed Dahrouj
 */
public interface State
{
	// determine if current state is goal
	boolean isGoal();

	// generate children to the current state
	ArrayList<State> generateChildren();

	// determine cost from initial state to THIS state
	// cost to come to this state
	double findCost();
	
	//A* Heuristics
	//h1(n) value
	int getOutOfPlace();
	
	//h2(n) value
	int getManDist();
	
	//h3(n) value
	int getAverageHeuristic();

	// print the current state
	public void printState();
	
	// Time taken  
	public int getTimeTaken();

	// compare the actual state data
	public boolean equals(State s);
}
