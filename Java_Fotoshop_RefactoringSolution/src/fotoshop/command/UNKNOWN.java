
package fotoshop.command;

import fotoshop.MessageQueue.PrintQueue;
import fotoshop.base.ColorImage;
import fotoshop.base.Editor;
import fotoshop.base.ImageManager;

/**
 *
 * @author ad543
 * Executed when the user command is unknown
 */
public class UNKNOWN extends Command {
    
    // Constructor
    public UNKNOWN(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    }
    
    /**
     * notifies user that the command entered is unknown
     * @param imgManager holds the current image and its status
     * @return boolean status to say whether to quit the editor or not
     */

    @Override
    public boolean execute(ImageManager imgManager) {
        PrintQueue.printlnMessage(Editor.msgBundle.getMessage("UnknownCommand"));
        return false;
    }    

}
