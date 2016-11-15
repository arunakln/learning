/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoshop.cache;

import fotoshop.base.ColorImage;

/**
 * This class is simple interface for image cache.
 * @author ad543 - Aruna Duaisingam
 * @version 2015.11.07
 */
public interface ICache {
    
    //Gets filename as parameter and returns the associated image from cache     
    ColorImage get(String key);
    
    //loads the file from the respective location and add image to cache.
    boolean put(String key);
}
