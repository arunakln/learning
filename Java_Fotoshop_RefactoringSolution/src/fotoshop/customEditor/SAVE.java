
package fotoshop.customEditor;

import fotoshop.base.ImageManager;
import fotoshop.command.Command;

/**
 * Class to save the user edited image to physical location
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class SAVE extends Command {
    
    // Contructors
    public SAVE(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    } 
    
    public SAVE(){}
    
    /**
     * Saves the user edited image to the physical location
     * @param imgManager holds the current image and its current state
     * @return false as user want to continue editing
     */
    @Override
    public boolean execute(ImageManager imgManager) {

        if (!hasSecondWord()) {
            // if there is no second word, we don't know where to save...
            System.out.println("save where?");
            return false;
        }
  
        String outputName = getSecondWord();
        boolean status = imgManager.save(outputName);
        
        return false;
    }
    

}
