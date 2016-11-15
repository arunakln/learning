
package fotoshop.customEditor;

import fotoshop.base.ImageManager;
import fotoshop.command.UndoableCommand;


/**
 * Class to handle Flip horizontal command action filter. 
 * As this class can be undoable it extends undoable abstract class.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class FLIPH  extends UndoableCommand{
    public FLIPH(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
        super.setIsUndoableCommand(true);
    } 
    
    // Constructor
    public FLIPH(){}

    
    /**
     * Execute the horizontal flip action on current image
     * @param imgManager holds the current image and its current state
     * @return false as user want to continue editing
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        System.out.println("Execute FlipH command action");
        return false;
    }
    
    /**
     * Undo the horizontal flip action on current image
     * @param imgManager holds the current image and its current state
     * @return false as user want to continue editing
     */
    @Override
    public boolean undo(ImageManager imgManager) {
        System.out.println("Undo FlipH command action");
        return true;
    }   

}
