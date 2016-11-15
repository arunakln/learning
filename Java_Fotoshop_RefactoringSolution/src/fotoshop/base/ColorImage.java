
package fotoshop.base;

import java.awt.*;
import java.awt.image.*;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;

/**
 * Extends standard BufferedImage class with convenience functions
 * for getting/setting pixel values using the standard Color class
 * and converting the raster to a standard 24-bit direct colour format.
 *
 * Based on class OFImage described in chapter 11 of the book
 * "Objects First with Java" by David J Barnes and Michael Kolling
 * (from 2nd edition onwards).
 *
 * @author  Michael Kolling, David J. Barnes, Peter Kenny
 * @version 1.0
 * 
 * Update by ad543 07 Nov 2015
 * This class been updated to handle the image details as well.
 */

public class ColorImage extends BufferedImage
{
  
    
    private String ImagePath = System.getProperty("user.dir");
    private ImageFilter filter = new ImageFilter();
    private String ImageName;
    
    /**
     * Create a ColorImage copied from a BufferedImage
     * Convert to 24-bit direct colour
     * @param image The image to copy
     */
    public ColorImage(BufferedImage image)
    {
        super(image.getWidth(), image.getHeight(), TYPE_INT_RGB);
        int width = image.getWidth();
        int height = image.getHeight();
        for (int y=0; y<height; y++)
            for (int x=0; x<width; x++)
                setRGB(x, y, image.getRGB(x,y));
    }

    /**
     * Create a ColorImage with specified size and 24-bit direct colour
     * @param width The width of the image
     * @param height The height of the image
     */
    public ColorImage(int width, int height)
    {
        super(width, height, TYPE_INT_RGB);
    }
    
        /**
     * Create a ColorImage with specified size and 24-bit direct colour 
     * along with the image details such as the file name and the filter applied to it.
     * @param width The width of the image
     * @param height The height of the image
     * @param fileName the name of the file (this would be the key for an image in cache,
     * @param filter defines the filter object on an image.
     */
    public ColorImage(int width, int height, String fileName, ImageFilter filter)
    {
        super(width, height, TYPE_INT_RGB);
        this.ImageName = fileName ;
        this.filter = filter;
    }

    /**
     * Set a given pixel of this image to a specified color.
     * The color is represented as an (r,g,b) value.
     * @param x The x position of the pixel
     * @param y The y position of the pixel
     * @param col The color of the pixel
     */
    public void setPixel(int x, int y, Color col)
    {
        int pixel = col.getRGB();
        setRGB(x, y, pixel);
    }

    /**
     * Get the color value at a specified pixel position
     * @param x The x position of the pixel
     * @param y The y position of the pixel
     * @return The color of the pixel at the given position
     */
    public Color getPixel(int x, int y)
    {
        int pixel = getRGB(x, y);
        return new Color(pixel);
    }
    
    /**
     * Get the image physical path
     * @return The image physical path
     */    
    public String getImagePath(){
        return this.ImagePath;
    }
    
    /**
     * Get the image full physical path including the Image name.
     * @return The image physical path including image name.
     */    
    public String getImageFullPath(){
        return (ImagePath + "\\" + this.ImageName);
    }
    
    /**
     * Get the list of filters applied on an image.
     * @return list of applied filters on an image
     */     
    public java.util.List<String> getAppliedImageFilters(){
        return filter.getAppliedFilters();
    }
    
    /**
     * Get the image filename.
     * @return the filename.
     */
    public String getImageName(){
        return this.ImageName;
    }
    
    /**
     * Get the image filter object on an image.
     * @return the image filter object.
     */
    public ImageFilter getImageFilter(){
        return this.filter;
    }
    
    /**
     * Add an filter to the current image
     * @param filtername - the filter name applied on an image
     */ 
    public void addFilter(String filtername){
        this.filter.addFilter(filtername);
    }
    
    /**
     * Remove filter from an image based on index.
     * @param index - removes the given index element from the filter list
     */ 
    public void removeFilter(int index){
        this.filter.removeFilter(index);
    }
    
    /**
     * Set the image name which is the filename
     * @param name - filename of an image.
     */ 
    public void setImageName (String name){
        this.ImageName = name;
    }
}
