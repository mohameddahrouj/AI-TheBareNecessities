import java.util.ArrayList;
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
	 * A* algorithm for SMP A*Search
	 */
	public static void search(int[] board, String heuristic, int n, int m)
	{
		SearchNode root = new SearchNode(new SMPState(board, n, m));
		Queue<SearchNode> q = new LinkedList<SearchNode>();
		q.add(root);

		// counter for number of iterations
		int searchCount = 1;

		while (!q.isEmpty())
		{
			SearchNode tempNode = (SearchNode) q.poll();

			if (!tempNode.getCurrentState().isGoal())
			{
				// generate tempNode's immediate possibile moves
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
						double h = ((SMPState) tempSuccessors.get(i)).getOutOfPlace();
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
						double h = ((SMPState) tempSuccessors.get(i)).getManDist();
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
						double h = ((SMPState) tempSuccessors.get(i)).getAverageHeuristic();
						checkedNode = new SearchNode(prev, s, c, h);
					}

					// Check for repeats before adding the new node
					if (!wasAlreadyEvaluated(checkedNode))
					{
						nodeSuccessors.add(checkedNode);
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

	// Returns true if SearchNode was evaluated, false if it hasn't
	private static boolean wasAlreadyEvaluated(SearchNode n)
	{
		boolean visited = false;
		SearchNode checkNode = n;

		// While n's parent isn't null, check to see if it's equal to the node
		// we're looking for.
		while (n.getParent() != null && !visited)
		{
			if (n.getParent().getCurrentState().equals(checkNode.getCurrentState()))
			{
				visited = true;
			}
			n = n.getParent();
		}

		return visited;
	}

}
