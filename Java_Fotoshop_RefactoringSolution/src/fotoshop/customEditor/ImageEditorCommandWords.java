
package fotoshop.customEditor;

import fotoshop.command.CommandWords;

/**
 * Class holds the custom localized command words.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class ImageEditorCommandWords extends CommandWords {
    
    // a constant array that holds all valid command words
    private final  String[] validCommands = {
        "open", "save", "look", "mono", "rot90", "help", "quit", "script"
    };
    
    /**
     * get the valid custom command words
     * @return list of valid custom command words.
     */
    @Override
    public String[] getCustomCommandWords() {
        return this.validCommands;
    }
}
