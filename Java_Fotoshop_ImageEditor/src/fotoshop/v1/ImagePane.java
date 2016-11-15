/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoshop.v1;

import java.io.File;
import java.util.List;
import javafx.geometry.Point3D;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.image.PixelWriter;
import javafx.scene.image.WritableImage;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.transform.Rotate;
import javafx.scene.transform.Scale;
import javafx.stage.Stage;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * This class defines the TabPane where the multiple images are displayed in tabs.
 */
public class ImagePane extends TabPane{
    
    private ImageManager imgManager;
    private Stage mainStage;
    
    // constructor
    public ImagePane (Stage mainStage){
        
        super();  
        this.imgManager = new ImageManager();
        this.mainStage = new Stage();
        initTab();
        
    }
    
     /**
     
     * Initializes the tab. An tab listener is added to tab change so it 
     * adds the selected image filter in filter history tab
     */
    public void initTab(){
        
        getSelectionModel().selectedItemProperty().addListener((ov, oldTab, newTab) -> {  
            displayCurrentTabHistory();
       });
    }
    
    /**
     * Adds a tab to the image pane. The tab contains
     * a scroll pane which contains the image opened
     * by the user.
     * @param imgView An image to be opened in a new tab.
     * @param tabName The name of the tab.
     */
    public void addTab(ImageView imgView, String tabName)
    {
            Tab newTab = new Tab();
            newTab.setText(tabName);
            newTab.setContent(createNewScrollPane(imgView));
            newTab.setId(tabName);
            getTabs().add(newTab);
            getSelectionModel().select(newTab); 

    }

    /**
     * Creates a new canvas, assigns the canvas an image
     * and adds the appropriate event handlers.
     * @param img The image to be displayed on the canvas.
     * @return The created canvas.
     */
    public StackPane createNewCanvas(ImageView imgView)
    {       
            StackPane stackPane = new StackPane();
            Canvas canvas = new Canvas(imgView.getImage().getWidth(), imgView.getImage().getHeight());
            GraphicsContext gc = canvas.getGraphicsContext2D();
            //gc.drawImage(imgView.getImage(), 0, 0);
            stackPane.getChildren().addAll(canvas, imgView);
            return stackPane;
    }

    /**
     * Creates a new scroll pane and adds the provided imageView
     * to it.
     * @param imgView image to be set on the scroll pane.
     * @return The created scroll pane.
     */
    public ScrollPane createNewScrollPane(ImageView imgView)
    {
            ScrollPane sp = new ScrollPane();
            sp.setContent(imgView);
            return sp;
    }
    
    /**
     * Returns the image from the active tab.
     * @return The image from the active tab.
     */
    public WritableImage getCurrentImage()
    {
            return ((ScrollPane)(getSelectionModel().getSelectedItem().getContent())).getContent().snapshot(null, null);
    }

    /**
     * Returns if there is an active tab or not.
     * @return A boolean representing if there is an active tab.
     */
    public boolean hasCurrentImage()
    {
            return getSelectionModel().getSelectedItem() != null;
    }
   
    /**
    * Returns the selected TabID which is the filename.
    * @return a string tabID which is a filename.
    */
    public String getSelectedTabID()
    {
        if ( getSelectionModel().getSelectedItem() != null)  
        {
            return getSelectionModel().getSelectedItem().getId();
        }
        return null;
    }
    
    /**
    * Adds the current tab filter history to the filter history tab textarea
    * when there is a tab change.
    */
    public void displayCurrentTabHistory(){
        TextAreaContent.clearFilterHistory();           

        List<String> filters = imgManager.getImageFilterList(getSelectedTabID());

        filters.stream().forEach((item) -> {
            TextAreaContent.appendFilterHistory(item);
        });        
    }
    
    /**
    * Adds the given filter name to the selected image
    * @param a string filter name to add to the selected image.
    */
    public void addFilter(String filterName){
        if(getSelectedTabID() != null){
            imgManager.addFilterToImage(getSelectedTabID(), filterName);
            TextAreaContent.appendFilterHistory(filterName);
        }        
    }
    
    /**
     * Uses the file manager to open an image. Any exception
     * is caught because we just want to show that an error
     * occurred when opening the image. This exception is displayed in the console
     * window textarea.
     */
    public void openImage()
    {
            try
            {
                final File imageFile = imgManager.chooseFile(this.mainStage);
                if(imageFile != null){
                    imgManager.loadImage(imageFile);                    
                    MyColorImage currImage = imgManager.getColorImageByID(imageFile.getName());
                    addTab(currImage.getImageView(), currImage.getImageName());
                    addFilter("open");             
                }
            }
            catch(Exception exception)
            {
                    TextAreaContent.appendConsoleText("Problem while opening image \n" + exception.getMessage()); 
            }

    }
    
    /**
     * Saves an image using the file manager. If no image
     * is currently open, then message will be displayed on the console window.
     */
    public void saveImage()
    {
            if(hasCurrentImage())
            {
                    imgManager.saveFile(mainStage, getCurrentImage()); 
                    addFilter("save");
            }
            else
            {
                TextAreaContent.appendConsoleText("There is no message to save");                    
            }
    }
    
    /**
     * Returns the currently selected image view (active tab)
     * @return The image from the active tab.
     */
    public ImageView getCurrentImageView(){
        if(getSelectedTabID() != null){
            MyColorImage colorImage = imgManager.getColorImageByID(getSelectedTabID());
            return colorImage.getImageView();
        }
        return null;
        
    }
    
    /**
     * Rotate the image to 90 degrees.
     * @param degrees angle to rotate the image
     * @param isUndo boolean to say whether this is an undo operation.
     */
    public void rotateImage(double degrees, boolean isUndo)    {
        MyColorImage colorImage = imgManager.getColorImageByID(getSelectedTabID());
        ImageView imgView = colorImage.getImageView();

        Rotate rotate = new Rotate();
        rotate.setAngle(degrees);
        rotate.setPivotX(imgView.getImage().getWidth()/2);
        rotate.setPivotY(imgView.getImage().getHeight()/2);
        
        imgView.getTransforms().add(rotate);            
        getSelectionModel().getSelectedItem().setContent(createNewScrollPane(imgView));        

        if (!isUndo)
        {
            addFilter("rot90");
            TextAreaContent.appendConsoleText("Rotated image to 90 degrees");
        }
        else{
            TextAreaContent.appendConsoleText("Undone rotate image command");
        }
    }
    
    /**
     * Filp the image vertically.     
     * @param isUndo boolean to say whether this is an undo operation.
     */
    public void flipV(boolean isUndo)    {
        MyColorImage colorImage = imgManager.getColorImageByID(getSelectedTabID());
        ImageView imgView = colorImage.getImageView();  
        
        imgView.setRotate(180);        
        flipH(-1, true);
        
        getSelectionModel().getSelectedItem().setContent(createNewScrollPane(imgView));        

        if (!isUndo)
        {
            
                addFilter("flipV");
                TextAreaContent.appendConsoleText("Image Fliped Vertically");
            
        }
        else{
            TextAreaContent.appendConsoleText("Undone FlipV Command");
        }
    }

    /**
     * Filp the image horizontally.     
     * @param isUndo boolean to say whether this is an undo operation.
     */
    public void flipH(double scaleX, boolean isUndo)    {
        MyColorImage colorImage = imgManager.getColorImageByID(getSelectedTabID());
        ImageView imgView = colorImage.getImageView();        
        imgView.setScaleX(scaleX);        
        getSelectionModel().getSelectedItem().setContent(createNewScrollPane(imgView));
        
        
        if (!isUndo)
        {
                addFilter("flipH");
                TextAreaContent.appendConsoleText("Image Fliped Horizontally");
            
        }
        else{
            TextAreaContent.appendConsoleText("Undone FlipH Command");
        }
    }
    
     /**
     * Undo the latest filter.It loops the image filter list in descending order 
     * and get the filter one by one and if filter is undoable it undo the filter.
     */
    public void undoFilter(){
        List<String> filterList = imgManager.getImageFilterList(getSelectedTabID());
        
        if(filterList.size() > 0){
            int lastFilterIndex = filterList.size() - 1;
            
            
            if(lastFilterIndex > 0)
            {
                String lastFilterString = filterList.get(lastFilterIndex);
                
                switch (lastFilterString.toLowerCase()) {
                    case "rot90":  
                        rotateImage(-90, true);
                        imgManager.removeImageFilterByIndex(getSelectedTabID(), lastFilterIndex);
                        displayCurrentTabHistory();
                        break;
                    case "flipH":  
                        flipH(-1, true);
                        imgManager.removeImageFilterByIndex(getSelectedTabID(), lastFilterIndex);
                        displayCurrentTabHistory();
                        break;                        
                    case "flipV":                             
                        flipV(true);
                        imgManager.removeImageFilterByIndex(getSelectedTabID(), lastFilterIndex);
                        displayCurrentTabHistory();
                        break;
                    default: imgManager.removeImageFilterByIndex(getSelectedTabID(), lastFilterIndex);
                             displayCurrentTabHistory();
                             break;
                }                    
                
            }
        }
        
        
    }
}
