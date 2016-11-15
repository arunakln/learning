package fotoshop.customEditor;

import fotoshop.base.Editor;

/**
 * This is the main class for the Fotoshop application
 * 
 * @author Richard Jones, ad543 - Aruna Duraisingam
 * @version 2013.09.05
 */
public class Main {
   public static void main(String[] args) {
        String language;
    	String country;
    	/* Added internationalization functionality. It will take the default US
        language if the user did not choose any language else the given language
        editor
        */ 
        if(args.length == 2){
            language = args[0];
            country = args[1];
        }
    	else {
            language = "en";
            country = "US";
        } 

        Editor img = new ImageEditor(language, country);
        img.edit();
    }
}
