
package fotoshop.customEditor;


import fotoshop.base.ImageManager;
import fotoshop.command.Command;

/**
 * Class to print help messages about commands to users. 
 * As this class cannot be undone it extends command abstract class.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class HELP extends Command{
    
    // Constructor
    public HELP(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
        
    } 
    public HELP(){}

    /**
     * print the help messages to the user
     * @param imgManager holds current image and its current state
     * @return boolean status to say whether to quit the editor or not
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        boolean wantToQuit = false;
        imgManager.printHelp();
        
        return wantToQuit;
    }

}
