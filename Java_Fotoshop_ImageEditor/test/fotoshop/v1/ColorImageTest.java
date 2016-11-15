/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fotoshop.v1;

import java.awt.Color;
import java.awt.image.BufferedImage;
import static java.awt.image.BufferedImage.TYPE_INT_RGB;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 *
 * @author ad543
 */
public class ColorImageTest {
    private BufferedImage  commonImage; 
    
    public ColorImageTest() {
    }
    
    @BeforeClass
    public static void setUpClass() {
    }
    
    @AfterClass
    public static void tearDownClass() {
    }
    
    @Before
    public void setUp() {
        try{
            //commonImage = new ColorImage(ImageIO.read(new File("/resources/Tulips.jpg")));
             File img = new File("Tulips.jpg");
            commonImage = ImageIO.read(img ); 
            //System.out.println(commonImage);
        }
        catch(IOException ex){
            System.out.println(ex.getMessage());
        }
    }
    
    @After
    public void tearDown() {
    }
    
    // Constructor testing
    /**
     * Test  null pointer exception when the image passes 
     * to the constructor is null
     */
    @Test(expected = NullPointerException.class)
    public void testColorImage_Constructor_nullImage() {
        System.out.println("Constructor_nullImage");
        int x = commonImage.getWidth();
        int y = commonImage.getHeight();        
        ColorImage colImage = new ColorImage(null);
    }
    
    /**
     * Test not null case when the image is passed as a parameter to
     * class.
     */
    @Test
    public void testColorImage_Constructor_notNullImage() {
        System.out.println("Constructor_notNullImage");      
        ColorImage colImage = new ColorImage(commonImage);
        assertNotNull(colImage.getWidth());
    } 
    
        /**
     * Test to check whether the actual image width and buffered image width are
     * same
     */
    @Test
    public void testColorImage_Constructor_testingWidth() {
        System.out.println("Constructor_testingWidth");
        int width = commonImage.getWidth();
        int height = commonImage.getHeight(); 
        ColorImage colImage = new ColorImage(width,height);
        assertEquals(width, colImage.getWidth() );
    }
    
            /**
     * Test to check whether the actual image height and buffered image height are
     * same
     */
    @Test
    public void testColorImage_Constructor_testingHeight() {
        System.out.println("Constructor_testingHeight");
        int width = commonImage.getWidth();
        int height = commonImage.getHeight(); 
        ColorImage colImage = new ColorImage(width,height);
        assertEquals(height, colImage.getHeight() );
    }
    
    /**
     * Fail Test to check whether the actual image height and buffered image height are
     * not same
     */
    @Test
    public void testColorImage_Constructor_FailtestingHeightNotsame() {
        System.out.println("Constructor_testingHeight");
        int width = commonImage.getWidth();
        int height = commonImage.getHeight(); 
        ColorImage colImage = new ColorImage(width,height);
        assertEquals(height+1, colImage.getHeight() );
    }
    
    /**
     * Test whether the colour of a pixel at location(x,y) on actual image is 
     * same as colour of pixel at location (x,y) in the buffered image.
     */
    @Test
    public void testColorImage_Constructor_testingSettingPixels() {
        System.out.println("Constructor_testingHeight");
        int x = 100;
        int y= 100;
        ColorImage colImage = new ColorImage(commonImage);
        assertEquals(colImage.getPixel(x,y), new Color(commonImage.getRGB(x,y)));
    }
    
    // Testing SetPixel
    
     /**
     * Test of setPixel method, of class ColorImage to test the colour.
     */
    @Test
    public void testSetPixel_color() {
        System.out.println("setPixel");
        int x = 100;
        int y = 100;
        Color col = new Color(255,   0,   0);
        ColorImage colImage = new ColorImage(commonImage);
        colImage.setPixel(x, y, col);        
        assertEquals(col, colImage.getPixel(x, y));
    }
    
    /**
     * Test of setPixel method, of class ColorImage to test position colour.
     */
    @Test
    public void testSetPixel_CheckPositionColor() {
        System.out.println("setPixel -CheckPositionColor");
        int x = 100;
        int y = 100;
        ColorImage colImage = new ColorImage(commonImage);        
        assertEquals(colImage.getRGB(x, y), commonImage.getRGB(x,y));
    }
    
     /**
     * Testing null pointer exception when the input colour object is null
     */
    @Test(expected = NullPointerException.class)
    public void testSetPixel_nullColor() {
        System.out.println("setPixel - nullColourException");
        int x = commonImage.getWidth();
        int y = commonImage.getHeight();
        Color col = null;
        ColorImage colImage = new ColorImage(commonImage);
        colImage.setPixel(x, y, col);
    }
    
    /**
     * Test of setPixel method, of class ColorImage.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testSetPixel_arrayOutOfBound() {
        System.out.println("setPixel - ArrayOutOfBound");
        int x = commonImage.getWidth() +1 ;
        int y = commonImage.getHeight();
        Color col = new Color(255,   0,   0);
        ColorImage colImage = new ColorImage(commonImage);
        colImage.setPixel(x, y, col);
    }

    // Get pixel testcase
    
    /**
     * Test of getPixel method, of class ColorImage.
     */
    @Test
    public void testGetPixel() {
        System.out.println("getPixel");
        
        int x = 200 ;
        int y = 300;
        Color col = new Color(255,   0,   0);
        
        ColorImage colImage = new ColorImage(commonImage);
        colImage.setPixel(x,y,col);
        assertEquals(col, colImage.getPixel(x, y));
    }
    
        /**
     * Test of getPixel method, of class ColorImage.
     */
    @Test
    public void testGetPixel_negativeTesting() {
        System.out.println("getPixel");
        
        int x = 200 ;
        int y = 300;
        Color col = new Color(255,   0,   0);
        
        ColorImage colImage = new ColorImage(commonImage);
        colImage.setPixel(x,y,col);
        assertEquals(new Color(253,   0,   0), colImage.getPixel(x, y));
    }
    
            /**
     * Test of getPixel method, of class ColorImage.
     */
    @Test (expected = IllegalArgumentException.class)
    public void testGetPixel_IllegalArgumentException() {
        System.out.println("getPixel");
        
        int x = 200 ;
        int y = 300;
        Color col = new Color(256,   0,   0);
        
        ColorImage colImage = new ColorImage(commonImage);
        colImage.setPixel(x,y,col);
        
    }
    
}
