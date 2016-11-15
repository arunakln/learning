package fotoshop.command;

import fotoshop.base.CommandActionManager;
import fotoshop.base.ImageManager;

/**
 * This class is taken from the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 *
 * This class holds information about a command that was issued by the user.
 * A command currently consists of two strings: a command word and a second
 * word (for example, if the command was "take map", then the two strings
 * obviously are "take" and "map").
 * 
 * The way this is used is: Commands are already checked for being valid
 * command words. If the user entered an invalid command (a word that is not
 * known) then the command word is <null>.
 *
 * If the command had only one word, then the second word is <null>.
 * 
 * @author  Michael Kolling, David J. Barnes and ad543
 * @version 2015.11.07
 */

public abstract class Command
{
    private String commandWord;
    private String secondWord;
    private String thirdWord;
    private boolean isUndoableCommand = false;
    protected CommandActionManager cmdAction = new CommandActionManager();

    /**
     * Create a command object. First and second word must be supplied, but
     * either one (or both) can be null.
     * @param firstWord The first word of the command. Null if the command
     *                  was not recognised.
     * @param secondWord The second word of the command.
     * @param thirdWord The second word of the command.
     */
    public Command(String firstWord, String secondWord, String thirdWord)
    {
        commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = secondWord;

    }
    
    /**
     * Default constructor
     */
    public Command()
    {
        this("","","");

    }
    
        /**
     * Add command words to the command
     * @param firstWord command word 
     * @param secondWord
     * @param thirdWord
     */
    public void addCommandWords(String firstWord, String secondWord, String thirdWord) {
        commandWord = firstWord;
        this.secondWord = secondWord;
        this.thirdWord = thirdWord;

    }

    /**
     * Return the command word (the first word) of this command. If the
     * command was not understood, the result is null.
     * @return The command word.
     */
    public String getCommandWord()
    {
        return commandWord;
    }

    /**
     * @return The second word of this command. Returns null if there was no
     * second word.
     */
    public String getSecondWord()
    {
        return secondWord;
    }

    /**
     * @return The third word of this command. Returns null if there was no
     * third word.
     */
    public String getThirdWord()
    {
        return secondWord;
    }
    
    /**
     * @return true if this command was not understood.
     */
    public boolean isUnknown()
    {
        return (commandWord == null);
    }

    /**
     * @return true if the command has a second word.
     */
    public boolean hasSecondWord()
    {
        return (secondWord != null);
    }
    
    /**
     * @return true if the command has a third word.
     */
    public boolean hasThirdWord()
    {
        return (thirdWord != null);
    }
    
     /**
     * @return returns status whether a command is undoable
     */
    public boolean IsUndoableCommand(){
        return isUndoableCommand;
    }
    
    /**
     * @param status set whether a command is undoable or not
     */
    public void setIsUndoableCommand(boolean status){
        this.isUndoableCommand = status;
    }
    
     /**
      * Abstract class to handle the command action request from the user.
     * @param imgManager passes the current image and its status to command actions
     * @return return boolean status whether to continue or quit the editor
     */
    public abstract boolean execute(ImageManager imgManager);
    
}

