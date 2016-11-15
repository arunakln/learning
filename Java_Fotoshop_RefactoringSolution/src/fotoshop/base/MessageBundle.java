/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoshop.base;

import java.util.*;

/**
 * Singleton class to implement Internationalization.  
 * @author ad543 - Aruna Duraisingam
 */
public class MessageBundle {
     
    private  static Locale currentLocale; // Holds the user Locale
    private static ResourceBundle messages;    // Holds the message bundle based on the user locale
    private static MessageBundle instance;
    
    private MessageBundle(String language, String country, String editorPackage){
        currentLocale = new Locale(language, country);
        messages = ResourceBundle.getBundle(editorPackage + ".MessageBundle", currentLocale);       
    }
    
    
    public static MessageBundle getInstance(String language, String country, String editorPackage){
        
        if(instance == null){
            instance = new MessageBundle(language,country, editorPackage);
        }
       return instance;
    }
    
    /*
        * Accessors
        * getMessage returns the Locale message (from the properties file) for the given key
    */
    
    public  static String getMessage(String key){        
        try{
            return messages.getString(key);
        }
        catch(MissingResourceException ex){
            return null;
        }        
    }
    
  
}
