
package fotoshop.customEditor;


import fotoshop.base.ImageManager;
import fotoshop.command.UndoableCommand;

/**
 * Class to handle rotate the image 90 degrees cloackwise. As this is an undoable
 * command it extends UndoableCommand abstract class
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class ROT90 extends UndoableCommand { 
  
    // Constructors
    public ROT90(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
        super.setIsUndoableCommand(true);
    } 
    
    public ROT90(){}

    /**
     * Executes the rotate 90 command
     * @param imgManager holds the current image and its current state
     * @return false as user want to continue editing
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        boolean wantToQuit = false;

        boolean status = cmdAction.rot90(imgManager);
        
        if (status == true)
        {
            imgManager.getCurrentImage().addFilter("rot90");
        }
       
        return wantToQuit;
    }
    
    public boolean undo(ImageManager imgManager){        
        return cmdAction.undoRot90(imgManager);         
    }

}
