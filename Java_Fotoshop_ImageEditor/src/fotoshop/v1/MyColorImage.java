
package fotoshop.v1;

import java.util.ArrayList;
import java.util.List;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * 
 * This class define the image object which will be displayed in the tab.
 */
public class MyColorImage {
    private List<String> filterList = new ArrayList<String>();
    private String ImageName;
    private ImageView imageView;
    
    //constructor
    public MyColorImage(){
    
    }
    
     /**
     * Constructor to initialize class with given image.   
     * @param image loaded image to initialize.
     */
    public MyColorImage(Image image, String imageName){
        this.imageView = new ImageView(image);
        this.ImageName = imageName;
    }
    
    /**
     * add the give imageView to the class.
     * @param image image to add
     */
    public void addImageView(Image image, String imageName){
        if(image != null)
        {
            this.imageView = new ImageView(image);
            this.ImageName = imageName;
        }
    }
    
     /**
     * get the current imageView
     * @return return the current imageview
     */
    
    public ImageView getImageView(){
        return this.imageView;
    }
    
    /**
     * Get the image filename.
     * @return the filename.
     */
    public String getImageName(){
        return this.ImageName;
    }
    
    /**
     * Get the list of filters applied on an image.
     * @return the list of filters applied on an object.
     */
    public List<String> getAppliedFilters(){
        return this.filterList;
    }
    
    /**
     * Add an filter to the current image
     * @param filtername - the filter name applied on an image
     */ 
    public void addFilter(String filtername){
        if(filtername != null)
            this.filterList.add(filtername);
    }
    
    /**
     * Remove filter from an image based on index.
     * @param index - removes the given index element from the filter list
     */ 
    public void removeFilter(int index){
        if(index >=0 )
            this.filterList.remove(index);
    }
    
    /**
     * Remove filter from an image based on index.
     * @param index - removes the given index element from the filter list
     */ 
    public void setImageView(ImageView modifiedImage){
        this.imageView = modifiedImage;
    }
}
