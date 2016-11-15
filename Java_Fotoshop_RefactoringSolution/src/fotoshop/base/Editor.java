
package fotoshop.base;

import fotoshop.MessageQueue.PrintQueue;
import fotoshop.command.Command;
import fotoshop.command.CommandWords;
import fotoshop.command.Parser;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author ad543
 * This is the base abstract class for different the versions of image editor
 * It can load the images from a physical location into cache 
 * and do modifications or apply filters and save it.
 */
public abstract class Editor {
    
    /**
     * Internationalisation messages
     */
    
    
    private final Parser parser; 
    
    public static CommandWords commandWords;
    
    private ImageManager imgManager;
    
    public static MessageBundle msgBundle;
    
        /**
     * Creates an editor where the images can be edited, apply filters 
     * and save to a physical location. 
     *
     * @param language The language to use
     * @param country The country
     * @see java.util.Locale
     */
    
    @SuppressWarnings("OverridableMethodCallInConstructor")
    public Editor(String language, String country) {        
        commandWords = getCommandWords();
        // internationalise
        msgBundle = MessageBundle.getInstance(language, country, getEditorPackageName());
        parser = new Parser(getEditorPackageName());  
        imgManager = new ImageManager();
    }
    
     /**
     * Main edit routine. Loops until the end of the editing session.
     */
    public void edit() {
        PrintQueue.printlnMessages(getWelcomeMessage());
        imgManager.printCurrentImageStatus();
   
        // Enter the main command loop.  Here we repeatedly read commands and
        // execute them until the editing session is over.
        boolean finished = false;
        while (!finished) {
            Command command = parser.getCommand();
            finished = processCommand(command);
        }
        PrintQueue.printlnMessage(msgBundle.getMessage("exitMessage"));
    }
    
        /**
     * Given a command, process (that is: execute) the command.
     *
     * @param command The command to be processed.
     * @return true If the command ends the image editing, false otherwise.
     */
    private boolean processCommand(Command command) {
        
        if (command.isUnknown()) {
            PrintQueue.printlnMessage(msgBundle.getMessage("dontknowcommand"));
            return false;
        }
        
        return command.execute(imgManager);
    }
    
    /**
     * Abstract method to get the name of the editor as this is a base class 
     * to extend different editors
     */
    protected abstract String getEditorName();
    
     /**
     * Abstract method to get the version of the editor as this is a base class 
     * to extend different editors
     */
    protected abstract String getEditorVersion();
    
     /**
     * Abstract method to get the package name of the editor
     */    
    protected abstract String getEditorPackageName();
    
    /**
     * Abstract method to get custom welcome messages
     */    
    protected abstract List<String> getWelcomeMessage();
    
    /**
     * Abstract method to get custom command words
     */ 
    protected abstract CommandWords getCommandWords();
}
