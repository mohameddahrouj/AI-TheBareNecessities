import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
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
	public static void search(String heuristic, int[] board, int[] goal, int n, int m)
	{
		SearchNode root = new SearchNode(new SMPState(board, goal, n, m, true));
		Queue<SearchNode> q = new LinkedList<SearchNode>();
		HashSet<Integer> visitedStates = new HashSet<>();
		
		visitedStates.add(root.getCurrentState().hashCode());
		q.add(root);

		performSearch(heuristic, q, visitedStates, false);
	}
	
	/**
	 * Initialize CTP
	 */
	public static void search(String heuristic, int[] times)
	{
		SearchNode root = new SearchNode(new CTPState(times));
		Queue<SearchNode> q = new LinkedList<SearchNode>();
		HashSet<Integer> visitedStates = new HashSet<>();
		
		visitedStates.add(root.getCurrentState().hashCode());
		q.add(root);

		performSearch(heuristic, q, visitedStates, true);
	}
	
	/**
	 * A* algorithm
	 */
	public static void performSearch(String heuristic, Queue<SearchNode> q, HashSet<Integer> visitedStates, boolean isCTP)
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
					// make the node
					if (heuristic == "OUTOFPLACE")
					{
						//Create new SearchNode with tempNode as the parent,
						//tempNode's cost in addition to the new cost for temp state
						//and the Manhattan h(n) value
						SearchNode prev = tempNode;
						State s = tempSuccessors.get(i);
						double c = tempNode.getCost() + tempSuccessors.get(i).findCost();
						double h = tempSuccessors.get(i).getOutOfPlace();
						checkedNode = new SearchNode(prev, s, c, h);
					}
					if (heuristic == "MANHATTAN")
					{
						//Create new SearchNode with tempNode as the parent,
						//tempNode's cost in addition to the new cost for temp state
						//and the Manhattan h(n) value
						SearchNode prev = tempNode;
						State s = tempSuccessors.get(i);
						double c = tempNode.getCost() + tempSuccessors.get(i).findCost();
						double h = tempSuccessors.get(i).getManDist();
						checkedNode = new SearchNode(prev, s, c, h);
					}
					else
					{
						//Create new SearchNode with tempNode as the parent,
						//tempNode's cost in addition to the new cost for temp state
						//and the Average h(n) value
						SearchNode prev = tempNode;
						State s = tempSuccessors.get(i);
						double c = tempNode.getCost() + tempSuccessors.get(i).findCost();
						double h = tempSuccessors.get(i).getAverageHeuristic();
						checkedNode = new SearchNode(prev, s, c, h);
					}

					// Check for repeats before adding the new node
					if(!isCTP){
						int hashCode = checkedNode.getCurrentState().hashCode();
						if(!visitedStates.contains(hashCode))
						{
							nodeSuccessors.add(checkedNode);
							visitedStates.add(hashCode);
						}
					}
					else{
						if (!checkRepeats(checkedNode))
						{
							nodeSuccessors.add(checkedNode);
						}
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
				int timeTaken = 0;

				for (int i = 0; i < loopSize; i++)
				{
					tempNode = solutionPath.pop();
					tempNode.getCurrentState().printState();
					// Gets the time taken for CTP
					if(isCTP){
						timeTaken = timeTaken + tempNode.getCurrentState().getTimeTaken();
					}
					System.out.println();
					System.out.println();
				}
				if(isCTP){
					System.out.println("Total time taken to cross: " + timeTaken);
				}
				System.out.println("A* cost: " + tempNode.getCost());
				System.out.println("Total nodes processed: " + searchCount);

				System.exit(0);
			}
		}
		
		System.out.println("Error, could not perform A*!");

	}

	/*
	 * Helper method to check to see if a SearchNode has already been evaluated.
	 */
	private static boolean checkRepeats(SearchNode n)
	{
		boolean repeated = false;
		SearchNode checkNode = n;

		while (n.getParent() != null && !repeated)
		{
			if (n.getParent().getCurrentState().equals(checkNode.getCurrentState()))
			{
				repeated = true;
			}
			n = n.getParent();
		}

		return repeated;
	}
}
