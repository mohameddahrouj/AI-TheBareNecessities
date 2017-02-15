import java.util.Scanner;

/**
 * Prompts user to enter puzzle information
 * Runner class for assignment
 */
@SuppressWarnings("resource")
public class Runner
{	
	private static String problemType;
	private static String CTP = "CTP";
	private static String SMP = "SMP";
	
	private static String searchType;
	private static String BFS = "BFS";
	private static String DFS = "DFS";
	private static String ASTAR = "A*";
	private static String heuristic;
	
	//If applicable
	private static int rows;
	private static int cols;
	
	private static int[] initSMPState;
	private static int[] initCTPTimes;
	private static int[] goalSMPState;
	
	public static void main(String[] args)
	{
		System.out.println("Welcome to Assignment #1");
		System.out.println("All Rights Reserved- Mohamed Dahrouj(100951843)\n");
		getProblemType();
		//SMP
		getBoardSizeIfApplicable();
		getSMPBoardIfApplicable();
		getSMPGoalBoardIfApplicable();
		//CTP
		getCTPTimesIfApplicable();
		//Search
		getSearchType();
		getAStarHeuristicIfApplicable();

		if (problemType.equals(SMP)) //Run SMP
		{

			if (searchType.equals(DFS)) //Depth First Search
			{
				DFSearch.search(initSMPState, goalSMPState, rows, cols);
			}
			else if (searchType.equals(BFS)) //Breadth First Search
			{
				BFSearch.search(initSMPState, goalSMPState, rows, cols);
			}
			else if (searchType.equals(ASTAR)) //A* Search
			{
				AStarSearch.search(heuristic, goalSMPState, initSMPState, rows, cols);
			}
		}

		else if (problemType.equals(CTP)) //Run CTP
		{
			if (searchType.equals(DFS)) //DFS
			{
				DFSearch.search(initCTPTimes);
			}
			else if (searchType.equals(BFS)) //BFS
			{
				BFSearch.search(initCTPTimes);
			}
			else if (searchType.equals(ASTAR)) //A* Search
			{
				AStarSearch.search(heuristic, initCTPTimes);
			}
		}
	}
	
	public static void getProblemType(){
		System.out.print("Enter Problem Mode (CTP/SMP): ");
		Scanner problemMode = new Scanner(System.in);
		String gameType = problemMode.nextLine();
		if(gameType.equals(CTP)){
			System.out.println();
			System.out.println("Commodity Transportation Problem selected");
		}
		if(gameType.equals(SMP)){
			System.out.println();
			System.out.println("Space Management Problem Transportation Problem selected");
		}
		problemType = gameType;
	}
	
	public static void getBoardSizeIfApplicable(){
		
		if(problemType.equals(SMP)){
			System.out.print("Please specify the configuration (rows,cols): ");
			Scanner scanner = new Scanner(System.in);
			String boardSize = scanner.nextLine();
			
			String[] sizeArray = boardSize.split(",");
			rows = Integer.parseInt(sizeArray[0]);
			cols = Integer.parseInt(sizeArray[1]);
			//scanner.close();
		}
	}
	
	// Helper method to build initial SMP board
	private static void getSMPBoardIfApplicable()
	{
		if(problemType.equals(SMP)){
			System.out.print("Please specify the initial board (0-1-2-...x): ");
			Scanner scanner = new Scanner(System.in);
			String board = scanner.nextLine();
			String[] boardArray = board.split("-");
			
			initSMPState = new int[boardArray.length];
			for (int i = 0; i < boardArray.length; i++)
			{
				initSMPState[i] = Integer.parseInt(boardArray[i]);
			}
		}
	}
	
	// Helper method to build initial SMP goal board
	private static void getSMPGoalBoardIfApplicable()
	{
		if(problemType.equals(SMP)){
			System.out.print("Please specify the goal board (0-1-2-...x):    ");
			Scanner scanner = new Scanner(System.in);
			String board = scanner.nextLine();
			String[] boardArray = board.split("-");
			
			goalSMPState = new int[boardArray.length];
			for (int i = 0; i < boardArray.length; i++)
			{
				goalSMPState[i] = Integer.parseInt(boardArray[i]);
			}
		}
	}
	
	// Helper method to define times for CTP board
	private static void getCTPTimesIfApplicable()
	{
		if(problemType.equals(CTP)){
			System.out.print("Please specify the initial times for the six people (1-3-5-...13): ");
			Scanner scanner = new Scanner(System.in);
			String times = scanner.nextLine();
			String[] timeArray = times.split("-");
			
			initCTPTimes = new int[timeArray.length];
			for (int i = 0; i < timeArray.length; i++)
			{
				initCTPTimes[i] = Integer.parseInt(timeArray[i]);
			}
		}
	}
	
	private static void getSearchType()
	{
			System.out.print("Please specify the algorithm for the probelm (BFS|DFS|A*): ");
			Scanner scanner = new Scanner(System.in);
			String search = scanner.nextLine();
			
			searchType = search;
	}
	
	private static void getAStarHeuristicIfApplicable()
	{
		if(searchType.equals("A*")){
			System.out.print("Please specify A* Heuristic (MANHATTAN|OUTOFPLACE|AVERAGE): ");
			Scanner scanner = new Scanner(System.in);
			String hrstc = scanner.nextLine();
			
			heuristic = hrstc;
			scanner.close();
		}
	}
}
