package fotoshop.MessageQueue;

import java.util.List;

/**
 * Class to handle printing message to users
 * @author ad543 - Aruna Duraisingam
 */
public class PrintQueue {
    
    public PrintQueue(){
        // Constructor
    }
    
     /**
     * Print a message with carriage return
     * @param str the string to print
     */
    public static void printlnMessage(String str){
        System.out.println(str);
    }
    
     /**
     * Print a message without carriage return
     * @param str the string to print
     */
    public static void printMessage(String str){
        System.out.print(str);
    }
    
     /**
     * Print the list of messages line by line
     * @param strList the message list to print
     */
    public static void printlnMessages(List<String> strList){
        strList.stream()
                .forEach(s -> System.out.println(s));
    }
    
    /**
     * Print the list of messages in a single line
     * @param strList the message list to print
     */
    public static void printMessages(List<String> strList){
        strList.stream()
                .forEach(s -> System.out.print(s));
    }
  
}
