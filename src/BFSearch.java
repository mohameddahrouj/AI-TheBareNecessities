import java.util.*;

/**
 * Generic Breadth First Search Algorithm
 * Supports CTP and SMP
 * 
 * @author Mohamed Dahrouj
 */
public class BFSearch
{
	/**
	 * Initialize SMP
	 */
	public static void search(int[] board, int[] goal, int n, int m)
	{
		SearchNode root = new SearchNode(new SMPState(board, goal, n, m, false));
		Queue<SearchNode> queue = new LinkedList<SearchNode>();
		HashSet<Integer> visitedStates = new HashSet<>();

		queue.add(root);
		visitedStates.add(root.getCurrentState().hashCode());
		performSearch(queue, visitedStates);
	}

	/**
	 * Initialize CTP
	 */
	public static void search(int[] times)
	{
		SearchNode root = new SearchNode(new CTPState(times));
		Queue<SearchNode> queue = new LinkedList<SearchNode>();
		HashSet<Integer> visitedStates = new HashSet<>();
		
		visitedStates.add(root.getCurrentState().hashCode());
		queue.add(root);

		performSearch(queue, visitedStates);
	}

	/**
	 * BFS Algorithm
	 * Search space = queue
	 */
	public static void performSearch(Queue<SearchNode> q, HashSet<Integer> visitedStates)
	{
		//Number of iterations
		int searchCount = 1;

		while (!q.isEmpty())
		{
			SearchNode tempNode = (SearchNode) q.poll();

			if (!tempNode.getCurrentState().isGoal())
			{
				// generate tempNode's direct children
				ArrayList<State> tempChildren = tempNode.getCurrentState().generateChildren(); 
				
				/*
				 * 1. Loop through the children
				 * 2. Make them SearchNodes
				 * 3. Check if they've already been evaluated
				 * 4. If they have not, add them to the queue
				 */
				for (int i = 0; i < tempChildren.size(); i++)
				{
					// Second param adds the cost of the new node to the current cost total in the SearchNode
					SearchNode newNode = new SearchNode(tempNode, tempChildren.get(i), tempNode.getCost() + tempChildren.get(i).findCost(), 0);
					int hashCode = newNode.getCurrentState().hashCode();
					if(!visitedStates.contains(hashCode))
					{
						q.add(newNode);
						visitedStates.add(hashCode);
					}
					
				}
				
				searchCount++;
			}
			else{ // Goal state has been found - print the path
				  // Track path using stack
				
				Stack<SearchNode> solutionPath = new Stack<SearchNode>();
				solutionPath.push(tempNode);
				tempNode = tempNode.getParent();
 
				while (tempNode.getParent() != null)
				{
					solutionPath.push(tempNode);
					tempNode = tempNode.getParent();
				}
				solutionPath.push(tempNode);

				// Size of stack before looping through and emptying it
				int loopSize = solutionPath.size();

				for (int i = 0; i < loopSize; i++)
				{
					tempNode = solutionPath.pop();
					tempNode.getCurrentState().printState();
					System.out.println();
					System.out.println();
				}
				System.out.println("BFS cost: " + tempNode.getCost());
				System.out.println("Total nodes processed: "	+ searchCount);

				System.exit(0);
			}
		}
		
		//If all else fails
		System.out.println("Error, could not perform BFS!");
	}
}
