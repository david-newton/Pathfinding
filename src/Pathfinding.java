import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Scanner;

/**
 * An implementation of the A* Algorithm to navigate a square grid environment.
 * 
 * @author David Newton
 *
 */
public class Pathfinding 
{
	//can alter grid_size without loss of functionality.
	private static final int GRID_SIZE = 15; 

	public static void main(String[] args) 
	{
		Scanner input;
		
		//CREATE ENVIROMENT
		//Only generates new environment once per run.
		//User can specify multiple paths in same environment.
		int enviroment[][] = new int[GRID_SIZE][GRID_SIZE];
		int startingRow, startingCol, endingRow, endingCol;
		//String sRow, sCol, eRow, eCol;
			
		for(int i = 0; i < GRID_SIZE;i++)//each col
		{		
			for(int j = 0; j < GRID_SIZE; j++)//each row
			{		
				double d = Math.random();
				if (d < 0.9) 
					enviroment[i][j] = 0;	
				else 
					enviroment[i][j] = 1;//10% of the time node defined as unpathable(1)									
			}	
		}
		
		boolean keepPathfinding = true;
		
		do{
			
		
		//DISPLAY GENERATED ENVIROMENT
		System.out.println("----------------------------------------------");
		System.out.println("Generated Enviroment[0 = Pathable Node, 1 = Unpathable Node]:");
		System.out.println("----------------------------------------------");
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < GRID_SIZE; i++) 
		{
			 for (int j = 0; j < GRID_SIZE; j++) 
			   {
			       sb.append(enviroment[i][j] + " ");
			   }
			   sb.append("\n");
		}
		System.out.print(sb.toString());
		System.out.print("----------------------------------------------\n");
		
		
		//USER INPUT
		input = new Scanner(System.in);
		
		
		
		System.out.println("[From Top to Bottom: Rows 0-" + (GRID_SIZE - 1) + "]");
		System.out.println("[From Left to Right: Columns 0-" + (GRID_SIZE - 1) + "]");
		System.out.println();
		//GET STARTING NODE COORDINATES FROM USER
		System.out.println("Starting Node Selection");
		System.out.print("----------------------------------------------\n");
		do
		{
			do
			{	//TODO - UPDATE VALIDATION METHOD FOR NUMBERFORMATEXCEPTION	
				System.out.print("Enter starting row: ");
				try{
				startingRow = Integer.parseInt(input.nextLine().trim());
				}catch(Exception e){//catch numberformatex
					
					startingRow = -1;
				}
			    
				if (!(startingRow >= 0 && startingRow < GRID_SIZE + 1))
				{
					System.out.println("Invalid Row!");
				}
				
			}while(!(startingRow >= 0 && startingRow < GRID_SIZE + 1));
			
			do
			{
				System.out.print("Enter starting column: ");
				startingCol = Integer.parseInt(input.nextLine().trim());
				
				if (!(startingCol >= 0 && startingCol < GRID_SIZE + 1))
				{
					System.out.println("Invalid Row!");
				}
				
			}while(!(startingCol >= 0 && startingCol < GRID_SIZE + 1));
			
			if(enviroment[startingRow][startingCol] != 0)
			{
				System.out.println("Invalid Node: Unpathable.");
			}
		}while(enviroment[startingRow][startingCol] != 0);
		
		System.out.println();
		
		//GET ENDING NODE COORDINATES FROM USER
		System.out.println("Ending Node:");
		do
		{
			do
			{		
				System.out.print("Enter ending row: ");
				endingRow = Integer.parseInt(input.nextLine().trim());
			
				if (!(endingRow >= 0 && endingRow < GRID_SIZE + 1))
				{
					System.out.println("Invalid Row!");
				}
				
			}while(!(endingRow >= 0 && endingRow < GRID_SIZE + 1));
			
			do
			{
				System.out.print("Enter ending column: ");
				endingCol = Integer.parseInt(input.nextLine().trim());
				
				if (!(endingCol >= 0 && endingCol < GRID_SIZE + 1))
				{
					System.out.println("Invalid Row!");
				}
				
			}while(!(endingCol >= 0 && endingCol < GRID_SIZE + 1));
			
			if(enviroment[endingRow][endingCol] != 0)
			{
				System.out.println("Invalid Node: Unpathable.");
			}
		}while(enviroment[endingRow][endingCol] != 0);
		System.out.println();		
		
		
		//CREATE START/END NODES
		Node start = new Node(startingRow, startingCol, enviroment[startingRow][startingCol]);
		Node end = new Node(endingRow, endingCol, enviroment[endingRow][endingCol]);
		
		//SET VALUES FOR START NODE
		start.setG(0);//know start has g = 0
		start.setH(getHeuristic(start, end));
		start.setF();
		
		//RUN A*
		findPath(start,end,enviroment);
		
		System.out.println();
		
		//DETERMINE IF USER WISHES TO FIND NEW PATH
		String newPath;
		do
		{
			System.out.print("Find another path? [y/n]: ");
			newPath = input.nextLine().trim();
			System.out.println();
			
		}while(!newPath.matches("^y|Y|n|N$"));
		
		if(newPath.matches("^n|N$")) keepPathfinding = false;
		
		}while(keepPathfinding);
		
		System.out.println("Good-Bye!");
		input.close();
	}
	public static void findPath(Node s, Node e, int[][] env)
	{
		boolean continueSearching 		= true;
		
		PriorityQueue<Node> openList 	= new PriorityQueue<Node>();
		LinkedList<Node> closedList 	= new LinkedList<Node>();
		ArrayList<Node> path			= new ArrayList<Node>();//stores the path in an arrList
		String pathDisplay[][] 			= new String[GRID_SIZE][GRID_SIZE];//stores the environment + path for display
		
		openList.add(s);
		int gVal;
		
		while(continueSearching)
		{
			//if nothing left in open list, no path.
			if(openList.isEmpty())
			{
				System.out.println("No Path Exists!");
				continueSearching = false;
				break;
			}
			
			//pop node from openlist
			Node node = openList.remove();
			
			//IF NODE EQUALS GOAL THEN SEARCH IS OVER
			if(node.equals(e))
			{
				continueSearching = false;
				
				//builds path by iterating backwards through each node's parent
				path.add(e);
				while(!node.equals(s))
				{
					path.add(node.getParent());
					node = node.getParent();				
				}
				
				//reverses path since we built it from goal to start
				Collections.reverse(path);
				
				//BUILD GRAPHICAL DISPLAY OF PATH
				System.out.println();
				for (int i = 0; i < GRID_SIZE; i++) 
				{
				    for (int j = 0; j < GRID_SIZE; j++) 
				    {
				        pathDisplay[i][j] = Integer.toString(env[i][j]);
				    }
				    
				}
				
				//for each node in path mark with X on display
				for (Node nd : path) 
				{
					int row = nd.getRow();
					int col = nd.getCol();
					pathDisplay[row][col] = "X"; 
				}
				pathDisplay[s.getRow()][s.getCol()] = "S";
				pathDisplay[e.getRow()][e.getCol()] = "G";
				
				//DISPLAY FOUND PATH
				System.out.println("----------------------------------------------");
				System.out.println("Path Found! [S = Start, G = Goal, X = Path Taken]:");
				System.out.println("----------------------------------------------");
				StringBuffer sb = new StringBuffer();
				for (int i = 0; i < GRID_SIZE; i++) 
				{
				    for (int j = 0; j < GRID_SIZE; j++) 
				    {
				        sb.append(pathDisplay[i][j] + " ");
				    }
				    sb.append("\n");
				}
				System.out.print(sb.toString());
				System.out.println("----------------------------------------------\n");
				
			}
			//IF OPEN LIST NOT EMPTY AND WORKING NODE IS NOT GOAL THEN GENERATE NEIGHBORS
			else
			{
				//SEARCH SURROUNDING NODES FOR VALID NEIGHBORS
				for(int i = node.getRow() - 1; i <= node.getRow() + 1; i++)
				{
					for(int k = node.getCol() - 1; k <= node.getCol() + 1; k++)
					{	
						//not current node AND inside environment AND node is pathable
						if((i != node.getRow() || k != node.getCol()) && i >= 0 && i < GRID_SIZE && k >= 0 && k < GRID_SIZE &&  env[i][k] != 1)
						{
							Node anotherNode = new Node(i,k,0);
							anotherNode.setParent(node);
							gVal = 10;
							//unless diag neighbor (distance = 14)
							if(Math.abs(i - node.getRow()) + Math.abs(k - node.getCol()) == 2)
							{
								gVal = 14;
							}
							//UPDATE G
							anotherNode.setG(node.getG() + gVal);
							//GET H
							anotherNode.setH(getHeuristic(anotherNode, e));
							//SET F
							anotherNode.setF();
							
							//make sure node wasnt previously visted
							if(!closedList.contains(anotherNode))
							{
								//if NOT already in openlist just add node
								if(!openList.contains(anotherNode))
								{
									openList.add(anotherNode);
								}
								//if already in openList, compare new G
								else
								{
									Iterator<Node> itr = openList.iterator();
									while(itr.hasNext())
									{
										Node temp = itr.next();
										if(temp.equals(anotherNode))
										{
											//if better g found, then update
											if(anotherNode.getG() < temp.getG())
											{
												temp.setG(anotherNode.getG());
												temp.setParent(node);
												openList.remove(anotherNode);
												openList.add(temp);
												break;
												
											}											
										}
									}									
								}								
							}
						}
					}
				}
				closedList.add(node);				
			}
		}	
	}
	
	/**
	 * Uses the "Manhattan" method to return heuristic value of a node.
	 * 
	 * 
	 * @param start The starting node.
	 * @param end The goal node.
	 * @return the heuristic value of a node
	 */
	public static int getHeuristic(Node start, Node end)
	{
		int rowDifference = (Math.abs(start.getRow() - end.getRow())) * 10; //* 10 due to move cost
		int colDifference = (Math.abs(start.getCol() - end.getCol())) * 10;
		int hVal = rowDifference + colDifference;
		return hVal;
	}
}