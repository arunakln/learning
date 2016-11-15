
package fotoshop.customEditor;

import fotoshop.base.ImageManager;
import fotoshop.command.Command;


/**
 * Class to handle the loading of images in cache.
 * As this class is not undoable it extends command abstract class.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class OPEN extends Command {

    public OPEN(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    public OPEN(){}
    
    /**
     * Loads the given user input file to  cache.
     * @param imgManager holds the current image and its current state
     * @return boolean status to say whether to quit the editor or not
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        
        boolean wantToQuit = false;
        if (!hasSecondWord()) {
            // if there is no second word, we don't know what to open...
            System.out.println("open what?");
            return wantToQuit;
        }
  
        String inputName = getSecondWord();
         boolean status = imgManager.loadImage(inputName);
        
        if (status == false) {
            imgManager.printHelp();            
        } else {             
            System.out.println("Loaded " + inputName);
        }
        
        return wantToQuit;
    }
    


}
