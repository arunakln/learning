package fotoshop.base;

import java.awt.Color;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * @version 2015.11.07 
 * 
 *A class to handle all the command actions or filter actions 
 * like rot90, mono etc. manipulations on an image. 
 */

public class CommandActionManager {
    
    // Default constructor
    public CommandActionManager(){
        
    }
    
     /**
     * Undo the filter command rot90.Rotate the image 90 degree counter clockwise.
     * @param imgManager - object which holds the current image and state.
     * @return the status of image processing
     */    
    public boolean undoRot90(ImageManager imgManager){
        
        ColorImage currImage = imgManager.getCurrentImage();
        if(currImage == null){
            //System.out.println("rot90 - Image null");
            return false;
        }
        int height = currImage.getHeight();
        int width = currImage.getWidth();
        ColorImage rotImage = new ColorImage(height, width, currImage.getImageName()
                                                    , currImage.getImageFilter());
        for (int y=0; y<height; y++) { // in the rotated image
            for (int x=0; x<width; x++) {
                Color pix = currImage.getPixel(x,y);
                rotImage.setPixel(y,width-x-1, pix);
            }
        }
        
        imgManager.setCurrentImage(rotImage);
       return true;
    }
    
    /**
     * Rotate the image to 90 degree clockwise.
     * @param imgManager - object which holds the current image and state.
     * @return the status of image processing
     */
    public boolean rot90(ImageManager imgManager) {
        
        // R90 = [0 -1, 1 0] rotates around origin
        // (x,y) -> (-y,x)
        // then transate -> (height-y, x)
        ColorImage currImage = imgManager.getCurrentImage();
        if(currImage == null){
            //System.out.println("rot90 - Image null");
            return false;
        }
        int height = currImage.getHeight();
        int width = currImage.getWidth();
        ColorImage rotImage = new ColorImage(height, width, currImage.getImageName()
                                                , currImage.getImageFilter());
        for (int y=0; y<height; y++) { // in the rotated image
            for (int x=0; x<width; x++) {
                Color pix = currImage.getPixel(x,y);
                rotImage.setPixel(height-y-1,x, pix);
            }
        }
       
        imgManager.setCurrentImage(rotImage);

        return true;
    }
    
     /**
     * Converts the current image to monochrome.
     * @param imgManager - object which holds the current image and state.
     * @return the status of image processing
     */
    
    public boolean mono(ImageManager imgManager){
        
        ColorImage currImage = imgManager.getCurrentImage();
        if(currImage == null){
            //System.out.println("mono - Image null");
            return false;
        }
        //Graphics2D g2 = currentImage.createGraphics();
        int height = currImage.getHeight();
        int width = currImage.getWidth();
        for (int y=0; y<height; y++) {
            for (int x=0; x<width; x++) {
                Color pix = currImage.getPixel(x, y);
                int lum = (int) Math.round(0.299*pix.getRed()
                                         + 0.587*pix.getGreen()
                                         + 0.114*pix.getBlue());
                currImage.setPixel(x, y, new Color(lum, lum, lum));
            }
        }
        
        imgManager.setCurrentImage(currImage);
        
        return true;
    }
    
}
