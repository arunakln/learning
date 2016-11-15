
package fotoshop.customEditor;

import fotoshop.base.Editor;
import fotoshop.command.CommandWords;
import java.util.LinkedList;
import java.util.List;

/**
 * This is the main class holds the custom editor which extends the base class 
 * for common functionality. This is a simple command line customer image editor 
 * where the images can be loaded, edited, apply filters and saved to a physical location.
 * 
 * The image can be edited based on the user command line input.
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class ImageEditor extends Editor {   
    
    private static final String editorName = "ImageEditor"  ; // custom editor name
    
    private static final String editorVersion = "version.1"  ; // custom editor version no.

    // constructor which initializes parent editor
    public ImageEditor(String language, String country) {
        super(language, country);
        
    }  
    
    // default constructor
    public ImageEditor() {
        this("en", "US");
    } 
    
    /**
     * Gets the current editor name
     * @return current editor name
     */
    @Override
    protected String getEditorName(){
        return editorName;
    }
    
    /**
     * get the current editor version
     * @return current editor version
     */
    @Override
    protected String getEditorVersion(){
        return editorVersion;
    }
    
    /**
     * get the current editor package name
     * @return current editor package name
     */
    
    @Override
    protected String getEditorPackageName(){
        
        return this.getClass().getPackage().getName();
    }
    
    /**
     * get the custom welcome messages
     * @return the list of welcome messages to print
     */
    @Override
    protected List<String> getWelcomeMessage(){
        java.util.List<String> msg = new LinkedList<String>();
        msg.add(msgBundle.getMessage("welcomeLine1"));
        msg.add(msgBundle.getMessage("welcomeLine2"));
        msg.add(msgBundle.getMessage("welcomeLine3"));
        msg.add("");
       return msg;
    }
    
    /**
     * Get localized custom command words object
     * @return localized custom command words object
     */
    @Override
    protected CommandWords getCommandWords(){
        return new ImageEditorCommandWords();
    }

}
