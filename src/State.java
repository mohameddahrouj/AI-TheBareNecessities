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

	// print the current state
	public void printState();

	// compare the actual state data
	public boolean equals(State s);
}
