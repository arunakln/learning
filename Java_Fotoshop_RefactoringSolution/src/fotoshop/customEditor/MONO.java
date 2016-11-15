
package fotoshop.customEditor;

import fotoshop.base.ImageManager;
import fotoshop.command.Command;

/**
 * Class to handle MONO command. This converts the current colour image 
 * to monochrome image.
 * 
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class MONO extends Command{
    
    // constructor initializes the parent
    public MONO(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    } 
    
    public MONO(){}
    
    /**
     * Executes the monochrome conversion of current colour image.
     * @param imgManager holds the current image and its current state
     * @return boolean status to say whether to quit the editor or not
     */
    @Override
    public boolean execute(ImageManager imgManager) {
       boolean wantToQuit = false;
       
        boolean status = cmdAction.mono(imgManager);        

        if (status == true)
        {
            imgManager.getCurrentImage().addFilter("mono");
        }
        
        return wantToQuit;
    }
    
}
