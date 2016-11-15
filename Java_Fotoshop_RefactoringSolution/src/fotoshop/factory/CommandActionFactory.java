
package fotoshop.factory;

import fotoshop.command.Command;

/**
 * Factory method to get the respective command class based on the user command request 
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class CommandActionFactory {
    
    // Constructor
    public CommandActionFactory(){
        
    }
    
    /**
     * get the command object based on the command string
     * @param CommandString user command string
     * @return the respective command object
     */
    
    public Command getCommandAction(String CommandString){
        try {                
                Command cmd = (Command) Class.forName(CommandString).newInstance();
               
                return cmd;
           }
            catch (ClassNotFoundException  
                   | InstantiationException 
                   | IllegalAccessException 
                   | SecurityException  
                   | IllegalArgumentException 
                   e ) 
            { 
                System.out.println("problem while instantiating" );
                return null;
            } 

    }
    
    
}
