import java.util.ArrayList;
import java.util.HashSet;
import java.util.Stack;

/**
 * Generic Depth First Search Algorithm
 * Supports CTP and SMP
 * 
 * @author Mohamed Dahrouj
 */
public class DFSearch
{
	/**
	 * Initialize SMP
	 */
	public static void search(int[] board, int[] goal, int n, int m)
	{
		SearchNode root = new SearchNode(new SMPState(board, goal, n, m));
		Stack<SearchNode> stack = new Stack<SearchNode>();

		HashSet<Integer> visitedStates = new HashSet<>();
		stack.add(root);
		visitedStates.add(root.getCurrentState().hashCode());

		performSearch(stack, visitedStates, false);
	}

	/**
	 * Initialize CTP
	 */
	public static void search(int[] times)
	{
		SearchNode root = new SearchNode(new CTPState(times));
		Stack<SearchNode> stack = new Stack<SearchNode>();
 
		HashSet<Integer> visitedStates = new HashSet<>();
		stack.add(root);
		visitedStates.add(root.getCurrentState().hashCode());

		performSearch(stack, visitedStates, true);
	}

	/**
	 * DFS Algorithm
	 * Search space = s
	 */
	public static void performSearch(Stack<SearchNode> s, HashSet<Integer> visitedStates, boolean isCTP)
	{
		//Number of iterations
		int searchCount = 1;

		while (!s.isEmpty())
		{
			SearchNode tempNode = (SearchNode) s.pop();

			if (!tempNode.getCurrentState().isGoal())
			{
				// generate tempNode's immediate successors
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
					SearchNode newNode = new SearchNode(tempNode, tempChildren.get(i), tempNode.getCost()	+ tempChildren.get(i).findCost(), 0);
					
					if(!isCTP){
						int hashCode = newNode.getCurrentState().hashCode();
						if(!visitedStates.contains(hashCode))
						{
							s.push(newNode);
							visitedStates.add(hashCode);
						}
					}
					
					else{
						if (!wasAlreadyEvaluated(newNode))
						{
							s.push(newNode);
						}
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
				System.out.println("DFS cost: " + tempNode.getCost());
				System.out.println("The nodes processed: "	+ searchCount);

				System.exit(0);
			}
		}

		//If all else fails
		System.out.println("Error, could not perform DFS!");
	}
	
	// Returns true if SearchNode was evaluated, false if it hasn't
	private static boolean wasAlreadyEvaluated(SearchNode node)
	{
		boolean visited = false;
		SearchNode checkNode = node;

		while (node.getParent() != null && !visited)
		{
			if (node.getParent().getCurrentState().equals(checkNode.getCurrentState()))
			{
				visited = true;
			}
			node = node.getParent();
		}

		return visited;
	}
}
