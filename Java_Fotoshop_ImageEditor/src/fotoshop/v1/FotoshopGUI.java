
package fotoshop.v1;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import javafx.application.Application;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Separator;
import javafx.scene.control.Slider;
import javafx.scene.control.SplitPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.ToolBar;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import javafx.stage.Modality;
import javafx.stage.Stage;

/*
 *
 *  @author ad543 - Aruna Duraisingam
 * This is a simple Image editor application named fotoshop editor. This editor 
 * can be used to maniputate multiple images which is displayed in tabs.
 * This class holds the main layout of the application. Using this image editor
 * one can load image, save image and do animated effects like rotate, flip, scale etc.
 *
 */

public class FotoshopGUI extends Application {
    
    private ImagePane   imagePane;
    private Stage         mainStage;
    
    /**
     * @param primaryStage holds the primary stage for the application layout.
     */
    @Override
    public void start(Stage primaryStage) {
        
        mainStage = primaryStage;        
        imagePane = new ImagePane(mainStage);
        
        MenuBar       menuBar     = new MenuBar();
        menuBar.getMenus().addAll(initMenus());
        
        ToolBar toolbar = new ToolBar();
        toolbar.setPrefHeight(30);
        toolbar.getItems().addAll(initToolBar());
        
        // Top
        
        VBox topMenu = new VBox();
        topMenu.setSpacing(0);       
        topMenu.getChildren().addAll(menuBar, toolbar);
        
        // Bottom
        HBox bottomBox = new HBox();        
        bottomBox.setAlignment(Pos.CENTER);
                
        Label bottomLabel = new Label("Version 1.0");
        bottomLabel.setAlignment(Pos.CENTER);
        bottomLabel.setTextAlignment(TextAlignment.CENTER);
        bottomLabel.getStyleClass().add("bottomLabel");
        
        bottomBox.getChildren().addAll(bottomLabel);
        
        // Slider Box
       
        Label opacityCaption = new Label("Opacity Level:");        
        Label scaleCaption = new Label("Scale Level:"); 
        
        VBox leftSliderBox = new VBox();
        leftSliderBox.setSpacing(20);       
        
        leftSliderBox.getChildren().addAll(opacityCaption, getOpacitySlider(), scaleCaption, getScaleSlider());
        
        leftSliderBox.getStyleClass().add("leftSliderBox");
        
       // filter history
        
        TabPane tbp = new TabPane();
        
        Tab htab = new Tab();
        htab.setText("Filter History");
        htab.setContent(TextAreaContent.getFilterHistoryTextArea());
        htab.setId("filterHistory");
        htab.setClosable(false);
        tbp.getTabs().add(htab);
        
        // console
        TabPane ctbp = new TabPane();
        Tab ctab = new Tab();
        ctab.setText("Console");
        ctab.setContent(TextAreaContent.getOutputTextArea());
        ctab.setId("console");
        ctab.setClosable(false);
        ctbp.getTabs().add(ctab);
        
        // Centre
        
        SplitPane hsp = new SplitPane();
        hsp.setOrientation(Orientation.HORIZONTAL);
        
        SplitPane vsp = new SplitPane();
        vsp.setOrientation(Orientation.VERTICAL);
        
        SplitPane main = new SplitPane();
        main.setOrientation(Orientation.VERTICAL);
       
        hsp.setDividerPositions(0.8f);
        vsp.setDividerPositions(0.5f, 0.5f);
        main.setDividerPositions(0.75f);
        
        main.getItems().addAll(imagePane, ctbp);
        vsp.getItems().addAll(leftSliderBox, tbp);
        hsp.getItems().addAll(main, vsp);  
        
        // Border Pane
        BorderPane root = new BorderPane();  
        
        root.setTop(topMenu);
        root.setCenter(hsp);
        root.setBottom(bottomBox);         
        
        root.getStyleClass().add("root");
        
        Scene scene = new Scene(root, 1200, 900);
        scene.getStylesheets().addAll(this.getClass().getResource("style.css").toExternalForm());
        mainStage.setTitle("Fotoshop Editor");
        mainStage.setScene(scene);
        mainStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }
    
    /**
     * Initializes the main menu 
     * @return List<Menu> returns the list of menu objects.
     */
    private List<Menu> initMenus()
    {
            final Menu fileMenu     = new Menu("File");
            fileMenu.getItems().addAll(initFileMenuItems());

            final Menu editMenu = new Menu("Edit");
            editMenu.getItems().addAll(initEditMenuItems());

            final Menu filterMenu = new Menu("Filter");
            filterMenu.getItems().addAll(initFilterMenuItems());

            final Menu helpMenu  = new Menu("Help");
            helpMenu.getItems().addAll(initHelpMenuItems());

            return new ArrayList<Menu>(Arrays.asList(fileMenu, editMenu, filterMenu, helpMenu));
    }

    /**
     * Initializes the menu items for the file menu.
     * @return A list containing the menu items for the file menu.
     */
    private List<MenuItem> initFileMenuItems()
    {
            //Opens an existing image.
            final MenuItem open = new MenuItem("Open");
            open.setOnAction((ae) -> imagePane.openImage());

            //Saves the image the user is currently working on.
            final MenuItem save = new MenuItem("Save");
            save.setOnAction((ae) -> imagePane.saveImage());

            //quit editor.
            final MenuItem quit = new MenuItem("Quit");
            quit.setOnAction((ae) -> System.exit(0));

            return new ArrayList<MenuItem>(Arrays.asList( open, save, quit));
    }

    /**
     * Initializes the menu items for the help menu.
     * @return A list containing the menu items for the help menu.
     */
    private List<MenuItem> initHelpMenuItems()
    {
            final MenuItem about = new MenuItem("About");
            about.setOnAction((ae) -> showHelp());

            return new ArrayList<MenuItem>(Arrays.asList(about));
    }

    /**
     * Initializes the menu items for the edit menu.
     * @return A list containing the menu items for the edit menu.
     */
    private List<MenuItem> initEditMenuItems()
    {
            final MenuItem undo = new MenuItem("Undo");
            undo.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                            imagePane.undoFilter();
                    }
            });


            return new ArrayList<MenuItem>(Arrays.asList(undo));
    }

    /**
     * Initializes the menu items for the filter menu.
     * @return A list containing the menu items for the filter menu.
     */
    private List<MenuItem> initFilterMenuItems()
    {
            final MenuItem rot90 = new MenuItem("rot90");
            rot90.setOnAction(new EventHandler<ActionEvent>() {
                    @Override public void handle(ActionEvent e) {
                        imagePane.rotateImage(90, false);
                    }
            });

//            final MenuItem flipH = new MenuItem("Flip Horizontally");
//            flipH.setOnAction((ae) -> imagePane.flipH(-1, false));
//
//            final MenuItem flipV = new MenuItem("Flip Vertically");
//            flipH.setOnAction((ae) -> imagePane.flipV(false));

            return new ArrayList<MenuItem>(Arrays.asList(rot90));
    }
    
    /**
     * Opens a modal dialog window with help text
     */    
    private void showHelp(){               
        Stage dialog = new Stage();
        dialog.setTitle("About Fotoshop Editor");
        dialog.initModality(Modality.APPLICATION_MODAL);
        
        VBox dialogVbox = new VBox(40);
        dialogVbox.getChildren().add(new Text("Fotoshop Image Editor"));
        dialogVbox.getChildren().add(getHelpText());
        dialogVbox.setAlignment(Pos.CENTER);
        dialogVbox.setPadding(new Insets(10));
        dialogVbox.getStyleClass().add("dialogVbox");
        //dialogVbox.setPrefWidth(400.00);
        
        ScrollPane sp = new ScrollPane();
        sp.setContent(dialogVbox);

        Scene dialogScene = new Scene(dialogVbox, 450, 300);
        dialog.setScene(dialogScene);
        dialog.show();
    }
    
    /**
     * Gets the help text to be displayed
     * @return returns th help text string
     */ 
    private Text getHelpText(){
            Text text = new Text();
            text.setWrappingWidth(400);
            text.setTextAlignment(TextAlignment.JUSTIFY);        

            String str = "Image editing encompasses the processes of altering images, "
                    + "whether they are digital photographs, traditional photochemical photographs, "
                    + "or illustrations. Traditional analog image editing is known as photo retouching, "
                    + "using tools such as an airbrush to modify photographs, or editing illustrations "
                    + "with any traditional art medium. Graphic software programs, which can be broadly "
                    + "grouped into vector graphics editors, raster graphics editors, and 3D modelers, "
                    + "are the primary tools with which a user may manipulate, enhance, and transform images. "
                    + "Many image editing programs are also used to render or create computer art from scratch.";
            text.setText(str);
            return text;
    }
    
    /**
     * Get opacity slider
     * @return the opacity slider object
     */ 
    private Slider getOpacitySlider(){
        final Slider opacityLevel = new Slider(0, 1, 1);
        opacityLevel.setShowTickMarks(true);
        opacityLevel.setShowTickLabels(true);
        opacityLevel.setMajorTickUnit(0.25f);
        opacityLevel.setBlockIncrement(0.1f); 
        opacityLevel.getStyleClass().add("opacityLevel");
        
        opacityLevel.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                ImageView imgView = imagePane.getCurrentImageView();
                    if(imgView != null)
                        imgView.setOpacity(new_val.doubleValue());                
            }
        });
        
        return opacityLevel;
    }
    
        /**
     * Get scale slider
     * @return the scale slider object
     */ 
    private Slider getScaleSlider(){
        final Slider scaling = new Slider (0, 1, 1);
        scaling.setShowTickMarks(true);
        scaling.setShowTickLabels(true);
        scaling.setMajorTickUnit(0.25f);
        scaling.setBlockIncrement(0.1f); 
       
        scaling.valueProperty().addListener(new ChangeListener<Number>() {
            public void changed(ObservableValue<? extends Number> ov,
                Number old_val, Number new_val) {
                
                ImageView imgView = imagePane.getCurrentImageView();
                if(imgView != null){
                    imgView.setScaleX(new_val.doubleValue());
                    imgView.setScaleY(new_val.doubleValue());
                }                 
            }
        });
        
        return scaling;
    }
    
    /**
     * Initializes the toolbar
     * @return list of nodes present in toolbar
     */ 
    private List<Node> initToolBar(){
        
        double iconWidth = 30.0; 
        double iconHeight = 30.0;
        
        Button openBtn = new Button();           
        ImageView openImg = new ImageView(new Image("/resources/Open-icon.png" , iconWidth, iconHeight, false, false));        
        openBtn.setGraphic(openImg);
        openBtn.setTooltip(new Tooltip("Open Folder"));
        openBtn.setOnAction((ae) -> imagePane.openImage());
        
        
        Button saveBtn = new Button();       
        ImageView saveImg = new ImageView(new Image("/resources/Floppy-Drive-icon1.png" , iconWidth, iconHeight, false, false));         
        saveBtn.setGraphic(saveImg);
        saveBtn.setTooltip(new Tooltip("Save File"));
        saveBtn.setOnAction((ae) -> imagePane.saveImage());
        
        Separator sp0 = new Separator();
        sp0.setOrientation(Orientation.VERTICAL);
        
        Button undoBtn = new Button();
        ImageView undoImg = new ImageView(new Image("/resources/undo.png" , iconWidth, iconHeight, false, false));         
        undoBtn.setGraphic(undoImg);
        undoBtn.setTooltip(new Tooltip("Undo"));
        undoBtn.setOnAction((ae) -> imagePane.undoFilter());
        
        Separator sp1 = new Separator();
        sp1.setOrientation(Orientation.VERTICAL);
        
        
        Button rot90Btn = new Button();
        ImageView rotImg = new ImageView(new Image("/resources/rot90.png" , iconWidth, iconHeight, false, false));         
        rot90Btn.setGraphic(rotImg);
        rot90Btn.setTooltip(new Tooltip("Rotate 90 degrees"));
        rot90Btn.setOnAction((ae) -> imagePane.rotateImage(90, false));
        
        Button flipHbtn = new Button();
        ImageView fliphImg = new ImageView(new Image("/resources/fliph.png" , iconWidth, iconHeight, false, false));         
        flipHbtn.setGraphic(fliphImg);
        flipHbtn.setTooltip(new Tooltip("Flip Horizontally"));
        flipHbtn.setOnAction((ae) -> imagePane.flipH(-1, false));
        
        Button flipVbtn = new Button();
        ImageView flipvImg = new ImageView(new Image("/resources/flipv.png" , iconWidth, iconHeight, false, false));         
        flipVbtn.setGraphic(flipvImg);
        flipVbtn.setTooltip(new Tooltip("Flip Vertically"));
        flipVbtn.setOnAction((ae) -> imagePane.flipV(false));
        
//        Separator sp2 = new Separator();
//        sp2.setOrientation(Orientation.VERTICAL);
        
//        Label displayLabel = new Label("Zoom");
//        ChoiceBox display = new ChoiceBox();
//        display.getItems().addAll("25%", "50%", "100%");
        
        Separator sp3 = new Separator();
        sp3.setOrientation(Orientation.VERTICAL);
        
        Button helpBtn = new Button();
        ImageView helpImg = new ImageView(new Image("/resources/help3.png" , iconWidth, iconHeight, false, false));         
        helpBtn.setGraphic(helpImg);
        helpBtn.setTooltip(new Tooltip("Help"));
        helpBtn.setOnAction((ae) -> showHelp());
        
        return new ArrayList<Node>(Arrays.asList(openBtn, saveBtn, sp0, undoBtn,sp1, rot90Btn,  
                 sp3 , helpBtn));
    }
}
