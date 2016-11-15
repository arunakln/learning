
package puzzle_ad543;

import java.io.File;
import java.io.FileWriter;
import java.util.LinkedList;

/**
 *
 * @author Aruna Duraisingam - ad543    
 * Date- 18/04/2016
 * File manager class to handle the files and prints the solution 
 * to a file and save it.
 */
public class FileManager {
    
    private String location = "./Resources";
    
    // default constructor
    public FileManager(){      
        
    }
    
    // function to Check whether the given directory location exists, if ont create one.
    public boolean checkDirectory(){
        File file = new File(location);        
        if(!file.exists()){
            file.mkdirs();
        }
        return true;
    }
    
    // create a file in the directory location.
    public String createFile(String fileName){        
        if(checkDirectory()){
            try{
                String fullPath = location + "/" + fileName+".txt";
                File f = new File(fullPath);
                f.createNewFile();
                return fullPath;
            }
            catch(Exception e){
                e.printStackTrace();
            }
        } 
        return null;
    }
    
    // print the solution path to a file in matrix format
    // Param - fileFullPath - locaiton of the file.
    // goalPath - list holding the solution path info.
    
    public void addNodeToFile(String fileFullPath, LinkedList goalPath){
        try{
            FileWriter file = new FileWriter(fileFullPath);  
            for (int index =0; index<=3 ; index++){
               for (int list =0; list<goalPath.size(); list++){
                SquareBoard tmp = (SquareBoard) goalPath.get(list);                
                String node =  tmp.arrayToString();
                
                    if( node.length() == 16){  
                        int start = index * 4;
                        int finish = start + 4;
                        
                        String substr = node.substring(start, finish);
                        file.append(substr+" ");
                                        
                    } 
                } 
               file.append("\n");
            }
            
            file.flush();
            file.close();
        }
        catch(Exception e){
              e.printStackTrace();  
        }
        
    }
    
}
