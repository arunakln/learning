
package fotoshop.base;


import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.stream.Collectors;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * Class to hold the list of filter and its  details applied on an Image
 */
public class ImageFilter {
    
    private List<String> filterList = new ArrayList<String>();
    
    // Constructor
    public ImageFilter(){
        
    }
    
     /**
     * Add the filter to the current image
     *
     * @param filterName name of the filter to add
     */
    public void addFilter(String filterName){
        this.filterList.add(filterName);
    }
    
    /**
     * Get the list of applied filter on an image
     * @return filterList return the list of filters applied on an image.
     */
    public List<String> getAppliedFilters(){
        return filterList;
    }
    
    /**
     * Remove the filter element based on index.
     * @param index index of the filter to be removed
     */
    public void removeFilter(int index){
        this.filterList.remove(index);
    }
    
    /**
     * Get the list of image filter message details to print
     * @return list of image filter message details
     */
    public List<String> getAppliedFilterDetails(){
        List<String> msg = new LinkedList<String>();
        msg.add("");
        msg.add("");
        msg.add( filterList.stream()
                .collect(Collectors.joining(" \n")));
        return msg;
    }
}
