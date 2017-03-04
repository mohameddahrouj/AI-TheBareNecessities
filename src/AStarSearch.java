import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Stack;

/**
 * Defines an A* search to be performed on a qualifying puzzle.
 * Currently supports SMP
 * 
 * @author Mohamed Dahrouj
 */
public class AStarSearch
{
	/**
	 * Initialize SMP
	 */
	public static void search(Heuristic heuristic, int[] board, int[] goal, int n, int m)
	{
		SearchNode root = new SearchNode(new SMPState(board, goal, n, m));
		Queue<SearchNode> q = new LinkedList<SearchNode>();
		HashSet<Integer> visitedStates = new HashSet<>();
		
		visitedStates.add(root.getCurrentState().hashCode());
		q.add(root);

		performSearch(heuristic, q, visitedStates);
	}
	
	/**
	 * Initialize CTP
	 */
	public static void search(ArrayList<Person> leftSide, Heuristic heuristic)
	{   
		SearchNode root = new SearchNode(new CTPState(leftSide, new ArrayList<Person>(), Direction.Left, 0, 0, null, heuristic));

		State finalState = performSearch(heuristic, root.getCurrentState());
		
        String solved = getPathToSuccess(finalState);
        
        System.out.println(solved);
	}
	
	/**
	 * A* algorithm
	 */
	public static void performSearch(Heuristic heuristic, Queue<SearchNode> q, HashSet<Integer> visitedStates)
	{
		// counter for number of iterations
		int searchCount = 1;

		while (!q.isEmpty())
		{
			SearchNode tempNode = (SearchNode) q.poll();

			if (!tempNode.getCurrentState().isGoal())
			{
				// generate tempNode's immediate possible moves
				ArrayList<State> tempSuccessors = tempNode.getCurrentState().generateChildren();
				ArrayList<SearchNode> nodeSuccessors = new ArrayList<SearchNode>();

				/*
				 * 1. Loop through the children
				 * 2. Make them SearchNodes
				 * 3. Check if they've already been evaluated
				 * 4. If they have not, add them to the queue
				 */
				for (int i = 0; i < tempSuccessors.size(); i++)
				{
					SearchNode checkedNode;
					//Create new SearchNode with tempNode as the parent,
					//tempNode's cost in addition to the new cost for temp state
					//and the Out of place h(n) value
					SearchNode prev = tempNode;
					State s = tempSuccessors.get(i);
					double c = tempNode.getCost() + tempSuccessors.get(i).findCost();
					
					// make the node
					if (heuristic.equals(Heuristic.OUTOFPLACE))
					{
						double h = tempSuccessors.get(i).getOutOfPlace();
						checkedNode = new SearchNode(prev, s, c, h);
					}
					if (heuristic.equals(Heuristic.MANHATTAN))
					{
						double h = tempSuccessors.get(i).getManDist();
						checkedNode = new SearchNode(prev, s, c, h);
					}
					else
					{
						double h = tempSuccessors.get(i).getAverageHeuristic();
						checkedNode = new SearchNode(prev, s, c, h);
					}

					// Check for repeats before adding the new node
					int hashCode = checkedNode.getCurrentState().hashCode();
					if(!visitedStates.contains(hashCode))
					{
						nodeSuccessors.add(checkedNode);
						visitedStates.add(hashCode);
					}

				}

				// Check to see if nodeSuccessors is empty. If it is, continue the loop from the top
				if (nodeSuccessors.size() == 0)
					continue;

				SearchNode lowestNode = nodeSuccessors.get(0);

				
				//This loop finds the lowest f(n) cost in a node, and then sets that node as the lowest.
				for (int i = 0; i < nodeSuccessors.size(); i++)
				{
					if (lowestNode.getFCost() > nodeSuccessors.get(i).getFCost())
					{
						lowestNode = nodeSuccessors.get(i);
					}
				}

				int lowestValue = (int) lowestNode.getFCost();

				// Adds any nodes that have that same lowest value.
				for (int i = 0; i < nodeSuccessors.size(); i++)
				{
					if (nodeSuccessors.get(i).getFCost() == lowestValue)
					{
						q.add(nodeSuccessors.get(i));
					}
				}

				searchCount++;
			}
			else //Goal has been found - print path
			{
				// Use a stack to track the path from the starting state to the goal state
				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
				solutionPath.push(tempNode);
				tempNode = tempNode.getParent();

				while (tempNode.getParent() != null)
				{
					solutionPath.push(tempNode);
					tempNode = tempNode.getParent();
				}
				solutionPath.push(tempNode);

				// Size of the stack before looping through and emptying it
				int loopSize = solutionPath.size();
				for (int i = 0; i < loopSize; i++)
				{
					tempNode = solutionPath.pop();
					tempNode.getCurrentState().printState();
					System.out.println();
					System.out.println();
				}
				System.out.println("A* cost: " + tempNode.getCost());
				System.out.println("Total nodes processed: " + searchCount);

				System.exit(0);
			}
		}
		
		System.out.println("Error, could not perform A*!");
	}

	public static State performSearch(Heuristic heuristic, State state) {

        if (state.isGoal()) {
            return state;
        }

        Comparator<State> comparator = new StateComparator();
        Queue<State> open = new PriorityQueue<>(10, comparator);
        HashSet<State> closed = new HashSet<>();
        open.add(state);

        while (!open.isEmpty()) {

            State currentState = open.poll();
            closed.add(currentState);

            for (State newState : currentState.generateChildren())
            {
                if (newState.isGoal()) {
                    return newState;
                }

                boolean inClosed = closed.contains(newState);
                if(!inClosed)
                {
                    boolean inOpen = open.contains(newState);
                    if(!inOpen)
                        open.add(newState);
                    else
                    {
                        State openState = getObject(open, newState);

                        if(newState.findCost() < openState.findCost())
                        {
                            openState.setStateValue((int)newState.findCost());
                            openState.setPreviousState(newState.getPreviousState());
                        }
                    }

                }

            }
        }

        return null;
    }
	
	//Used to print in backwards order
    private static State reverseStates(State state)
    {
    	State previousSystem = null;
        State currentSystem = state;

        while(currentSystem != null)
        {
            State temp =  currentSystem.getPreviousState();
            currentSystem.setPreviousState(previousSystem);
            previousSystem = currentSystem;
            currentSystem = temp;
        }

        return previousSystem;
    }


    public static String getPathToSuccess(State state)
    {
        StringBuilder builder = new StringBuilder();
        state = reverseStates(state);

        int count = 0;

        while(state != null)
        {
            builder.append(state.toString());
            state = state.getPreviousState();
            count++;
        }

        return builder.toString() + "Total Nodes Processed: " + count;
    }

     private static State getObject( Collection<State> states, State state)
     {
         for (State system: states) {
             if(state.equals(system))
                 return system;
         }

         return null;
     }
	
}
