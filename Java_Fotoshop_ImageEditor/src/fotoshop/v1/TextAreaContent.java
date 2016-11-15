
package fotoshop.v1;

import java.util.List;
import javafx.scene.control.TextArea;

/**
 *
 * @author ad543 - Aruna Duraisingam
 * This a a static global textarea class to append contents in
 * console tab and filter history tab. 
 */
public class TextAreaContent extends TextArea{
    
    private static TextArea outputStream = new TextArea();        
    private static TextArea filterHistory = new TextArea();        
        
    //Constructor
    public TextAreaContent(){
        super();
        outputStream.setWrapText(true);    
        filterHistory.setWrapText(true); 
    } 
    
    /**
     *Append the given string to the console text area box.    
     * @param str string to append
     */
    public static void appendConsoleText(String str){
        outputStream.appendText(str);
        outputStream.appendText("\n");  
    }
    
     /**
     *Append the given string to the filter history text area box.    
     * @param str string to append
     */
    public static void appendFilterHistory(String str){
        filterHistory.appendText(str);
        filterHistory.appendText("\n");  
    }
    
    /**
     * returns the console text area object to the main stage.
     * @return console textarea to display
     */
    public static TextArea getOutputTextArea(){
        return outputStream;
    }
    
     /**
     * returns the filter history text area object to the main stage.
     * @return filter history textarea to display
     */
    
    public static TextArea getFilterHistoryTextArea(){
        return filterHistory;
    }
    
     /**
     * It clears off the filter history textarea and update it with the given list of strings
     * @param filteList list of filters to display.
     */
    public static  void refreshFilterHistory(List<String> filterList){         
        filterList.stream().forEach(s -> appendFilterHistory(s));
    }
    
     /**
     * It clears off the filter history  textarea 
     */
    public static void clearFilterHistory(){
        filterHistory.setText("");        
    }
    
}
