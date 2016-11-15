
package puzzle_ad543;

import java.util.ArrayList;
import java.util.Arrays;

/**
 *
 * @author Aruna Duraisingam - ad543
 * Date - 17-Apr-2016
 * 
 * Class that hold the configuration/properties of the state.
 * It initializes the class and provides all possible moves for the given state
 * based on the operator '_'. Create new configurations based on the selected move.
 */
public class SquareBoard {
    private String state;
    private ArrayList moves;
    private char[] eleArray = new char[16];
    
    // Construction initializes the provided state configuration
    // Param - state - string that holds the board elements
    public SquareBoard(String state){
        this.state = state;
        initialize();        
    }
    // default constructor
    public SquareBoard(){              
    }
    
    // initialize the board. Convers string to single dimension array
    private void initialize(){
        try {
            if(this.state.length() != 16 )
                throw new Exception("Exception: Start state should be 16 size.");
            
            for(int i=0; i< this.state.length(); i++){
                char cha = this.state.charAt(i);
                eleArray[i] = cha;               
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    // function to convert array to string
    public String arrayToString(){
        return new String(eleArray);
    }
    
    // Moves the opertor '_' to the given index and creates a new square board.
    // param Currstate - current state of the board
    // moveIndex - location to move the operator.
    // returns new satate
    public static SquareBoard nextConfig(SquareBoard currState, int moveIndex){
        SquareBoard newBoard = new SquareBoard(currState.arrayToString());
        int index = new String(currState.eleArray).indexOf("_");
        newBoard.eleArray[index] = newBoard.eleArray[moveIndex];
        newBoard.eleArray[moveIndex] = '_';
        return newBoard;
    }
    
    // check whether the given two states are equal.
    public boolean checkEqual(SquareBoard goal) {        
        return Arrays.equals(eleArray, goal.eleArray);
    }
    
    // Function that provides the possible operator moves
    //based on the operator '_' 
    public ArrayList<Integer> nextPossibleMoves(){
        moves = new ArrayList<Integer>();
        int index=new String(eleArray).indexOf("_");      
        
        switch(index){
            case 0: 
                moves.add(1);
                moves.add(4);
                break;
            case 1:
                moves.add(5);
                moves.add(0);
                break;
            case 4:
                moves.add(0);
                moves.add(5);
                moves.add(8);
                break;
            case 5:
                moves.add(6);
                moves.add(4);
                moves.add(9);
                moves.add(1);
                break;
            case 6:
                moves.add(5);
                moves.add(7);
                moves.add(10);
                break;
            case 7: 
                moves.add(6);
                moves.add(11);
                break;
            case 8: 
                moves.add(4);
                moves.add(9);
                break;
            case 9:
                moves.add(5);
                moves.add(8);
                moves.add(10);
                moves.add(13);
                break;
            case 10:
                moves.add(6);
                moves.add(9);
                moves.add(11);
                moves.add(14);
                break;
            case 11:
                moves.add(7);
                moves.add(10);
                break;
            case 13:
                moves.add(9);
                moves.add(14);
                break;
            case 14:
                moves.add(10);
                moves.add(13);
                break;
        }        
        return moves;
    }    
    
    // Function that prints single dimension array to matrix
    public void printMatrix() {
        for (int i = 1; i <= 16; i++){
            if(i%4 ==0){
                System.out.println(eleArray[i-1]);
            }
            else{
                System.out.print(eleArray[i-1]);
            }
           if(i==16){
               System.out.println("\r\n");
           }
        }       
    }
    

}
