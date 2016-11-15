
package fotoshop.customEditor;

import fotoshop.base.ImageManager;
import fotoshop.command.Command;


/**
 *
 * @author ad543
 */
public class QUIT extends Command {
    
    public QUIT(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    } 
    
    public QUIT(){}
    
    /**
     * Helps quit the editor.
     * @param imgManager holds the current image and its current state
     * @return true as we want to quit editor
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        if (hasSecondWord()) {
            System.out.println("Quit what?");
            return false;
        } else {
            return true;  // signal that we want to quit
        }
    }
}
