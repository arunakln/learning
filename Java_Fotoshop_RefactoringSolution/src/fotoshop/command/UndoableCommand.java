
package fotoshop.command;

import fotoshop.base.ImageManager;

/**
 * Abstract Class to handle the undoable command. This extends the command class
 * Whichever command actions/ filters can be undone will extend this class.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public abstract class UndoableCommand extends Command {
    
    //constructor
    public UndoableCommand (String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public UndoableCommand(){
        
    }
    
    /**
     * Abstract method to hold the implementation of undo functionality
     * @param imgManager holds the current image and state
     * @return boolean status whether to quit or continue editing
     */
    public abstract boolean undo(ImageManager imgManager);
    
}
