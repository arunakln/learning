
package fotoshop.customEditor;


import fotoshop.base.ImageManager;
import fotoshop.command.Command;
import fotoshop.command.Parser;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Class to handle the user scripts (which is present in a text file)
 * that contains the list of command to execute. It executes the commands line by line 
 * from the script file and edits the images. This would be helpful to execute 
 * large set of commands at once.
 * 
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class SCRIPT extends Command {
    
    public SCRIPT(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    } 
    
    public SCRIPT(){}
    
    /**
     * Execute the commands in script file line by line and edits the image.
     * @param imgManager holds the current image and its current state
     * @return true once script executed it quits.
     */
    @Override
    public boolean execute(ImageManager imgManager) {
        //boolean wantToQuit = false;
        
        if (!hasSecondWord()) {
            // if there is no second word, we don't know what to open...
            System.out.println("which script"); 
            return false;
        }
  
        String scriptName = getSecondWord();
        Parser scriptParser = new Parser("fotoshop.customEditor");
        try (FileInputStream inputStream = new FileInputStream(scriptName)) {
            scriptParser.setInputStream(inputStream);
            boolean finished = false;
            while (!finished) {
                try {
                    Command cmd = scriptParser.getCommand();
                    finished = cmd.execute(imgManager);
                } catch (Exception ex) {
                    return finished;
                }               
            }
            return finished;
        } 
        catch (FileNotFoundException ex) {
            System.out.println("Cannot find " + scriptName);
            return false;
        }
        catch (IOException ex) {
            throw new RuntimeException("Panic: script barfed!");
        }
    } 

}
