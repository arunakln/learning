
package fotoshop.base;

import fotoshop.MessageQueue.PrintQueue;
import fotoshop.cache.ICache;
import fotoshop.cache.ImageCache;
import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;
import javax.imageio.ImageIO;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07
 * Class to manage create, read, save and cache images. 
 * This class holds the currently editing image and its current state which is passed 
 * to various command actions for manipulation. This class uses cache
 * to load and get images, so faster and easier access. This cache may not be effective 
 * when handling single image but be useful for multiple image editing. 
 */

public class ImageManager {
    
    
    protected ColorImage currentImage = null;
    private ICache cache = new ImageCache();
    
    //Constructor
    public ImageManager(){
        
    }
    
     /**
     * Load an image to cache
     * @param name The name of the image file
     * @return status return the success status of loading
     */
    public boolean loadImage(String name) {        
       
        boolean status = cache.put(name);
        
        if(status == true){
            currentImage = cache.get(name);
        }        
        return status;
    }
    
     /**
     * Save an image file to the physical location
     * @param outputName The name of the image file to save as
     * @return status return the success status of saving
     */
    public boolean save(String outputName){
        try {
            File outputFile = new File(outputName);
            ImageIO.write(currentImage, "jpg", outputFile);
            PrintQueue.printlnMessage(Editor.msgBundle.getMessage("ImageSavedTo") + outputName);
        } catch (IOException e) {
            System.out.println(e.getMessage());
            printHelp();
            return false;
        }
        return true;
    }    
   
     /**
     * Print the help message string
     * @return status return the success status
     */
    public boolean printHelp(){
        PrintQueue.printlnMessage(Editor.msgBundle.getMessage("usingFotoshop"));
        PrintQueue.printlnMessage("");
        PrintQueue.printlnMessage(Editor.msgBundle.getMessage("commandwords"));
        
        if(Editor.commandWords != null){
            for(String cmd : Editor.commandWords.getCustomCommandWords()){
                PrintQueue.printMessage(Editor.msgBundle.getMessage(cmd) + "\t");
            }
        }      
        PrintQueue.printlnMessage("");
        return true;
    }
    
     /**
     * get the list of current image message details to print
     * @return List list of image message details
     */
    public void printCurrentImageStatus(){
        
        PrintQueue.printMessage(Editor.msgBundle.getMessage("currImage"));
        if(currentImage == null)
             PrintQueue.printlnMessage("null");
        else
            PrintQueue.printlnMessage(currentImage.getImageName());            
        
        PrintQueue.printMessage(Editor.msgBundle.getMessage("filtersapplied"));  
        
        
        if(currentImage != null)
        PrintQueue.printMessage(currentImage.getAppliedImageFilters().stream()
                .collect(Collectors.joining(" \t")));  
        PrintQueue.printlnMessage(""); 
        
    }
    
    /**
     * Get the height of currently editing image
     * @return height of current image
     */
    public int getHeight(){
        return this.currentImage.getHeight();
    }
    
     /**
     * Get the width of currently editing image
     * @return width of current image
     */
    public int getWidth(){
        return this.currentImage.getWidth();
    }
    
     /**
     * set the currently modified/edited image to the current image
     * @param modifiedImage  edited image by various command action filters.
     */
    public void setCurrentImage(ColorImage modifiedImage){
         this.currentImage  = modifiedImage;
    }  
    
     /**
     * Get the currently editing image
     * @return returns currently editing image
     */
    public ColorImage getCurrentImage(){
         return this.currentImage;
    }  
    
}
