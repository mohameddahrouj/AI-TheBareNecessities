/**
 * 
 * Represents search nodes for different algorithms
 * Includes: State 
 * 			 Cost to get to that state
 * 			 State's parent node
 * 
 * @author Mohamed Dahrouj
 * 
 */
public class SearchNode
{
	private SearchNode parent;
	private State curState;
	private double cost;    // cost to get to this state
	private double hCost; 	// heuristic cost
	private double fCost; 	// function cost
	
	// Constructor for the root
	public SearchNode(State s)
	{
		curState = s;
		parent = null;
		cost = 0;
		hCost = 0;
		fCost = 0;
	}

	// Constructor for all other SearchNodes
	// prev = parent node
	// s = state
	// Used for A*:
    //             c = g(n) cost to get to this node
	//             h = h(n) cost to get to this node
	public SearchNode(SearchNode prev, State s, double c, double h)
	{
		parent = prev;
		curState = s;
		cost = c;
		hCost = h;
		fCost = cost + hCost;
	}

	public State getCurrentState()
	{
		return curState;
	}

	public SearchNode getParent()
	{
		return parent;
	}

	//g(n) cost for A*
	public double getCost()
	{
		return cost;
	}

	//h(n) cost for A*
	public double getHCost()
	{
		return hCost;
	}

	//f(n) cost for A*
	public double getFCost()
	{
		return fCost;
	}
}
