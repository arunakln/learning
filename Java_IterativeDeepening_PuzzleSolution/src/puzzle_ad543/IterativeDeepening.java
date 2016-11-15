package puzzle_ad543;

import java.util.ArrayList;
import java.util.LinkedList;

/**
 *
 * @author Aruna Duraisingm - ad543
 * Date 18/04/2016
 * 
 * The Class finds a solution to the search problem for 11 tile puzzle using iterative deepening algorithm.
 * Using initial state configuration board, it is expanded using the depth first limited search 
 * criteria. From the initial board it will check for the possible moves and put in stack
 * and expand tree one by one till it reaches the max iterative length. 
 * Once the solution is found then it breaks from all the loop and print the solution path in
 * the file and go back to the next problem. 
 */
public class IterativeDeepening {
    private int maxDepth;
    private SquareBoard initState; // Board holds inital configuration state
    private SquareBoard goalState;// Board holds goal state configuration
    private ArrayList<String> visitedNodes = null; // Array list to store the visited nodes.
    private LinkedList<SquareBoard> goalPath = new LinkedList<SquareBoard>(); // List to save the closed path
    private ArrayList<SquareBoard> tmpPath = new ArrayList<SquareBoard>();
    private String fileName;
    private boolean solnFound = false; // Flag to check whether solution is found.
    
    // Construct and initializes the board
    public IterativeDeepening(String initState, String goalState, int maxDepth, String fileName){
        this.initState = new SquareBoard(initState);
        this.goalState = new SquareBoard(goalState);
        this.maxDepth = maxDepth; 
        this.fileName = fileName;
        this.solnFound = false;
        solvePuzzle();
    }
    
    // Splove the puzze by iterative deepening algorithm
    private void solvePuzzle(){        
        goalPath.add(initState);        
        for (int i = 0; i < maxDepth; i++)
        {   visitedNodes = new ArrayList<String>(); // initialize the visited nodes list
            if(solnFound) break; // if solution found break the loop          
            depthFirstLimitedSearch(0,i, this.initState);// perform depth first limited search.
        }        
    }
   
    // Depth first limited search algorithm - perform recursive loops  
    // to expand the branch and check whether goal state is reached.
    // Parameters Depth- Holds the current depth of the tree.
    // maxDepth - holds the maximum iterations it can perform.
    // currState - Holds the current state of the board.
   private void depthFirstLimitedSearch(int depth, int maxDepth, SquareBoard currState){
        
        if (maxDepth<=0) return; // If the expansion of node reaches the max depth return.
        
        String node = currState.arrayToString(); // converts current state to string
        
        // If the current node is already visited then return.
        if(visitedNodes.contains(node))
        {
            return; // node already visited
        }    
        
        // Once the node is expanded and if it is not visited, then add to the visited node list.
        visitedNodes.add(node);     
        //currState.printMatrix();
        
        //Check if the expanded child node is equal to the goal state.
        if(currState.checkEqual(this.goalState)){  
            System.out.println("Found a solution at depth " + depth + ": for file : "+ fileName);
            for (int i = 0; i < depth; i++) {
                //tmpPath.get(i).printMatrix(); 
                goalPath.add(tmpPath.get(i));
            }
            
            // Once the solution if found print the solution to the file.
            FileManager f = new FileManager();
            String fullpath = f.createFile(this.fileName);            
            f.addNodeToFile(fullpath, goalPath); 
            solnFound = true; // set solution found flag to true.
            //System.exit(0);
        }              
        
        // get all the possible actions can perform on the current state based on
        // the operator '_'
        ArrayList<Integer> moves = currState.nextPossibleMoves();
        
        // Exapnd the tree by calling recursively.
        for (int i = 0;i < moves.size(); i++) {
            int move = (Integer) moves.get(i);            
            SquareBoard newBoard = SquareBoard.nextConfig(currState, move);
            tmpPath.add(depth, newBoard);
            depthFirstLimitedSearch(depth+1, maxDepth-1, newBoard);
        }        
    }    
}

