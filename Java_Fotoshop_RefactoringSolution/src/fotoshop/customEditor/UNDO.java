/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoshop.customEditor;

import fotoshop.base.ImageManager;
import fotoshop.command.Command;
import fotoshop.command.UndoableCommand;
import fotoshop.factory.CommandActionFactory;
import java.util.List;

/**
 * Class that handles the undo request from the user. When the user requests undo
 * it look for last filter applied on the image. If it is an undoable command 
 * then it look for the related command class and executes undo, else it will go back
 * the filter list check for the latest undoable filter and does the undo operation.
 * once the filter is undone, then that undone filter and also its successor filters 
 * will be removed from the filter list. 
 * 
 * eg. If the filter list has got the open, rot90 and save commands, 
 * then it undo the undoable command rot90 and removes both rot90 and save from 
 * the filter list.
 * 
 * Currently only commands rot90 and flipH are undoable.
 * 
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 */
public class UNDO extends Command{
    
    private CommandActionFactory factory = new CommandActionFactory(); // factory to the requested command class
    
    //Constructors
    public UNDO(String firstWord, String secondWord, String thirdWord) {
        super(firstWord, secondWord, thirdWord);
    } 
    
    public UNDO(){}
    
    /**
     * Checks for the undoable filter command from the applied image filter list
     * and get the command class and executes undo. Once undone, the undone filter 
     * and also the successor filter commands (if present) will be removed from the filter list
     * @param imgManager holds the current image and its current state
     * @return false as user want to continue editing 
     */
    @Override
    public boolean execute(ImageManager imgManager) {    
        boolean wantToQuit = false;
        List<String> filterList = imgManager.getCurrentImage().getAppliedImageFilters();
        
        if(!filterList.isEmpty())
        {
            int lastFilterIndex = filterList.size() - 1;
            String lastFilterString = filterList.get(lastFilterIndex);
            System.out.println("Current filter is : " + lastFilterString);
            
            String cmdString = (this.getClass().getPackage().getName() + "."+lastFilterString.toUpperCase());
            
            Command cmd = (Command) factory.getCommandAction(cmdString);
            
            if(cmd.IsUndoableCommand()){
                UndoableCommand undocmd = (UndoableCommand) factory.getCommandAction(cmdString);
                if(cmd !=null){
                    boolean status = undocmd.undo(imgManager);
                    
                    if(status == true){
                        imgManager.getCurrentImage().removeFilter(lastFilterIndex);
                    }                
                }
                else{
                    System.out.println("Unknown Command " + lastFilterString);
                }
            }            
            
        }
        else
        {
            System.out.println("No filters applied to the image");
        }        
        return wantToQuit;
    }
    
}
