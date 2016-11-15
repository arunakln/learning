package fotoshop.command;

import fotoshop.MessageQueue.PrintQueue;
import fotoshop.base.Editor;
import fotoshop.factory.CommandActionFactory;
import java.io.FileInputStream;
import java.util.Scanner;

/**
 * This class is taken from the "World of Zuul" application. 
 * "World of Zuul" is a very simple, text based adventure game.  
 * 
 * This parser reads user input and tries to interpret it as an "Adventure"
 * command. Every time it is called it reads a line from the terminal and
 * tries to interpret the line as a three word command. It returns the command
 * as an object of class Command.
 *
 * The parser has a set of known command words. It checks user input against
 * the known commands, and if the input is not one of the known commands, it
 * returns a command object that is marked as an unknown command.
 * As the call to UNKNOWN command class is manually printed it looks for the 
 * class in current package.  
 * 
 * @author  Michael Kolling and David J. Barnes 
 * Updated by ad543 - Aruna Duraisingam
 * version 2006.03.30
 * @version 2015.11.07
 */
public class Parser 
{
    private CommandWords commands;  // holds all valid command words
    private Scanner reader;         // source of command input
    private String packageName;
    private CommandActionFactory factory = new CommandActionFactory(); 

    /**
     * Create a parser to read from the terminal window.
     */
    public Parser(String packageName) 
    {
        this.commands = Editor.commandWords;
        reader = new Scanner(System.in);
        this.packageName = packageName;
    }
    
    /**
     * Manually set the command stream 
     * @param str input commands
     */
    public void setInputStream(FileInputStream str) { 
        reader = new Scanner(str);
    }
    
        /**
     * @return The next command action from the user
     */
    public Command getCommand() {
        String inputLine;   // will hold the full input line
        String word1 = null;
        String word2 = null;
        String word3 = null;

        PrintQueue.printMessage("> ");     // print prompt

        inputLine = reader.nextLine();

        try ( // Find up to two words on the line.
            // Note this construct will auto close the input 
            Scanner tokenizer = new Scanner(inputLine)) {
                if(tokenizer.hasNext()) {
                    word1 = tokenizer.next();      // get first word
                    if(tokenizer.hasNext()) {
                        word2 = tokenizer.next();      // get second word
                    }
                    if(tokenizer.hasNext()) {
                        word3 = tokenizer.next();      // get second word
                        // note: we just ignore the rest of the input line.
                    }
                }
        }


        if(commands.isCommand(word1)) {
            String cmdString = this.packageName+"."+ word1.toUpperCase();
            
            // Get the respective command action based on user command from factory
            Command cmd = factory.getCommandAction(cmdString);
            
            if(cmd != null)
            {
                cmd.addCommandWords(word1, word2, word3);
                return cmd;
            }
            else{
                return new UNKNOWN(word1, word2, word3);
            } 
        }
        else {
            return new UNKNOWN(null, word2, word3); 
        }
    }    

}
