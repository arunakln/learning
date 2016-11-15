/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoshop.v1;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import javafx.collections.ObservableList;
import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.WritableImage;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javax.imageio.ImageIO;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * This is a class to manage the loading and saving of multiple images.
 * This class also holds the cache for multiple images where images can be 
 * get and put for displaying in multiple tabs.
 */
public class ImageManager {
    private FileChooser fileChooser;
    private Map<String, MyColorImage> imageMap = new LinkedHashMap<String, MyColorImage>();
    
    // constructor
    public ImageManager(){
        fileChooser = new FileChooser();
        initExtensionFilters();
    }
    
    /**
     * Opens a file chooser open dialog. Returns the
     * selected file or null if no file was selected.
     * @param stage The parent stage of the file chooser.
     * @return The chosen file. Null if no file was selected.
     */
    public File chooseFile(Stage stage)
    {
            return fileChooser.showOpenDialog(stage);
    }

    /**
     * Opens a file chooser save dialog. Returns the selected
     * file or null if no file was selected.The status of the 
     * image loading will be displayed in the console window.
     * @param stage The parent stage of the file chooser.
     * @return The chosen file. Null if no file was selected.
     */
    public void saveFile(Stage stage, WritableImage image)
    {
        fileChooser.setTitle("Save Image");

        File file = fileChooser.showSaveDialog(stage);
        if (file != null) {
            try {
                ImageIO.write(SwingFXUtils.fromFXImage(image,
                        null), "png", file);
                
                TextAreaContent.appendConsoleText("Image successfully saved ");
            } catch (IOException ex) {
                TextAreaContent.appendConsoleText("Some while saving file \n  " + ex.getMessage());
            }
        }            
    }
    
    /**
     * Loads an image from a given file and returns the image. The status of the 
     * image loading will be displayed in the console window.
     * @param imageFile The file of the image to be loaded.
     * @return An image opened from the given file.
     * @throws Exception
     */
    public Image loadImage(File imageFile) throws Exception
    {
            if(imageFile != null)
            {
                    BufferedImage bufferedImage = ImageIO.read(imageFile);
                    Image image = SwingFXUtils.toFXImage(bufferedImage, null);
                    imageMap.put(imageFile.getName(),new MyColorImage(image, imageFile.getName()));
                    TextAreaContent.appendConsoleText(imageFile.getName() + " - file loaded successfully");
                    return image;
            }
            else
            {
                    TextAreaContent.appendConsoleText("Problem while loading file " + imageFile.getName());
                    return null;
            }
    }
    
     /**
     * Add the given filter name to the associated image
     * @param tabID Unique ID to identify an image in cache.
     * @param filtername filter name to add to an image in cache.
     */
    public void addFilterToImage(String tabID, String filterName){
        MyColorImage colorImage= getColorImageByID(tabID);
        colorImage.addFilter(filterName);
    }
    
     /**
     * Gets the list of filters applied on an image.
     * @param tabID Unique ID to identify an image in cache.
     * @return list of filters applied on an image.
     */
    public List<String> getImageFilterList(String tabID){
        MyColorImage colorImage= getColorImageByID(tabID);        
        return colorImage.getAppliedFilters();
    }
    
        
     /**
     * Remove an filter from an image using index (filter stored as arraylist)
     * @param tabID Unique ID to identify an image in cache.
     * @param index the index of filter to remove
     */
    public void removeImageFilterByIndex(String tabID, int index){
        MyColorImage colorImage= getColorImageByID(tabID);
        colorImage.removeFilter(index);
    }
    
     /**
     * Gets the colourImage from cache by unique ID
     * @param tabID Unique ID to identify an image in cache.
     * @return returns the colour image and its filters.
     */
    public MyColorImage getColorImageByID(String tabID){
        MyColorImage colorImage= imageMap.get(tabID);
        if(colorImage == null){            
            imageMap.put(tabID, new MyColorImage());
        }
        
        return imageMap.get(tabID);
    }

    /**
     * Initializes the extensions filters used by the file chooser
     * and adds the filters to the file chooser.
     */
    private void initExtensionFilters()
    {
            FileChooser.ExtensionFilter extFilterJPG = new FileChooser.ExtensionFilter("JPG files (*.jpg)", "*.JPG");
            FileChooser.ExtensionFilter extFilterPNG = new FileChooser.ExtensionFilter("PNG files (*.png)", "*.PNG");
            FileChooser.ExtensionFilter extFilterGIF = new FileChooser.ExtensionFilter("GIF files (*.gif)", "*.GIF");
            FileChooser.ExtensionFilter extFilterJPEG = new FileChooser.ExtensionFilter("JPEG files (*.jpeg)", "*.JPEG");
            fileChooser.getExtensionFilters().addAll(extFilterJPG, extFilterPNG, extFilterGIF, extFilterJPEG);
    }
    
    private List<String> getCurrentImageFilter(String tabID){
         MyColorImage img = imageMap.entrySet()
                     .stream()
                     .filter(e -> e.getKey() == tabID)
                     .map(Map.Entry:: getValue)
                     .findFirst()
                     .orElse(null);
         
         if(img != null){
             return img.getAppliedFilters();
         }
         else {
             return new ArrayList<String>();
         }
             
         
    }
}
