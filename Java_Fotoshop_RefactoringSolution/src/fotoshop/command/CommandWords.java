package fotoshop.command;

import fotoshop.base.Editor;
import java.util.MissingResourceException;

/**
 * This class is derived from the "World of Zuul" application,
 * @author Michael Kolling and David J. Barnes, ad543
 * version 2006.03.30
 * This class holds an enumeration of all command words known to the editor.
 * It is used to recognise commands as they are typed in.
 *
 * @version 2015.11.07
 */

public abstract class CommandWords
{


    /**
     * Constructor - initialise the command words.
     */
    public CommandWords()
    {
        // nothing to do at the moment...
    }

    /**
     * Check whether a given String is a valid command word. 
     * @param cmdString user command input
     * @return true if a given string is a valid command,
     * false if it isn't.
     */
    protected boolean isCommand(String cmdString)
    {
        try{
            for(String command : getCustomCommandWords()) {
            if (command.equals(Editor.msgBundle.getMessage(cmdString)))
                return true;
            }
            // if we get here, the string was not found in the commands
            return false;
        }
        catch(MissingResourceException ex){
            return false;
        }
    }
    
    /**
     * Abstract method to get the custom command words
     * @return list of localized command words
     */
    public abstract String[] getCustomCommandWords();
}
