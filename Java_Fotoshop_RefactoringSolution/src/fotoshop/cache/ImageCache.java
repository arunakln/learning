
package fotoshop.cache;

import fotoshop.base.ColorImage;
import fotoshop.base.Editor;
import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.imageio.ImageIO;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * Class to load and cache multiple images. It implements the interface ICache. 
 */
public class ImageCache implements ICache{
    
    private ColorImage img;
    private String fileLocation = System.getProperty("user.dir");
    private Map cache = new LinkedHashMap();
    
    //Constructor
    public ImageCache(){
        
    }
    
    //Initializes the class with the file location of images to load
    public ImageCache(String fileLocation){
        this.fileLocation = fileLocation;
    }

    /**
     * Get the color image from cache based on key which is the filename
     * @param key 
     * @return returns the ColorImage from the cache
     */ 
    
    @Override
    public ColorImage get(String key) {
        Object img = cache.get(key);
        if (img == null) { 
            put(key);            
        }
        return (ColorImage) img;
    }

     /**
     * Load the image from the physical location and put the color image in cache
     * @param key 
     * @return returns the ColorImage from the cache
     */ 
    @Override
    public boolean put(String fileName) {
        
        addImage(fileName);
        
        if(this.img != null){
            cache.put(fileName, this.img);
            return true;
        }
        return false;
    }
    
     /**
     * private method to load the image from the physical location
     * @param fileName name of the file to get from location 
     */ 
    
    private void addImage(String fileName){
     try {
            this.img = new ColorImage(ImageIO.read(new File(fileName))); 
            this.img.setImageName(fileName);
        } catch (IOException e) {
            System.out.println(Editor.msgBundle.getMessage("cannotFindImage") + fileName);
            System.out.println(Editor.msgBundle.getMessage("cwdIs") + fileLocation);
            
        }        
    }
    
}
