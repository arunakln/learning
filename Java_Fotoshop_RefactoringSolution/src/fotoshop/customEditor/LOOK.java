
package fotoshop.customEditor;


import fotoshop.base.ImageManager;
import fotoshop.command.Command;

/**
 * Class to handle look command. This prints out the current image details
 * and its current filter details.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class LOOK extends Command {
    
    // constructor intializes parent class
    public LOOK(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
       
    } 
    public LOOK(){}
    
    /**
     * Prints the current images details and its filter details
     * @param imgManager holds the current image and current state
     * @return boolean status to say whether to quit the editor or not
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        boolean wantToQuit = false;
        
        imgManager.printCurrentImageStatus();
        
        return wantToQuit;
    }

    
}
