package edu.vt.cs5044;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import edu.vt.cs5044.tetris.*;

/**
 * The tetrisAITest class works with the TetrisAI.java script to analyze the functions
 * and test their prediceted and actual values in a controlled environment. 
 * I am testing an empty board, one block board, and two random boards. 
 * 
 * The results should output in the JUnit column to the lefthand side. 
 *
 * @author willm
 * @version Oct 4, 2023
 *
 */
public class TetrisAITest {
    
    AI smartPlayer;
    Board emptyBoard;
    Board oneBlockBoard;
    Board opaqueBoard;
    Board notQuiteapiBoard;
    Board justAnotherBoard;
    
    
    /**
     * After establishigng the boards and the smartPlayer above, 
     * I assign four boards with varying values to test their validity.
     */
    @Before
    public void setUP() {
        smartPlayer = new TetrisAI();
        emptyBoard = new Board();
        
        oneBlockBoard = new Board("   #      ");
        
        opaqueBoard =   new Board("##########",
                                  "##########");
        
        notQuiteapiBoard = new Board("          ",
                                     "          ",
                                     "          ",
                                     "## ##    #",
                                     "# ##### ##",
                                     "#### #####",
                                     "# ##### ##",
                                     "## #######",
                                     "######### ",
                                     " #########",
                                     " #########",
                                     "###  #####",
                                     "####### ##",
                                     "######## #",
                                     " #### ####");
        
        justAnotherBoard = new Board("## ###   #",
                                     "# ########",
                                     "###  #####",
                                     "### ### ##",
                                     "######## #",
                                     "##### ####");
    }
    
    /**
     * This method tests the average column height using JUnit. 
     * 
     * The first number inside the assertEquals method is the expected value.
     * The methods following the expected value call the getAverageColumnHeight
     * method from the SmartPlayer object to run the method housed in TetrisAI.java.
     * 
     * Look at the Javadoc in the TetrisAI document to see formula.
     *
     */
    @Test
    public void testAverageColumnHeight() {
        assertEquals(0,smartPlayer.getAverageColumnHeight(emptyBoard));
        assertEquals(0,smartPlayer.getAverageColumnHeight(oneBlockBoard));
        assertEquals(2,smartPlayer.getAverageColumnHeight(opaqueBoard));
        assertEquals(11,smartPlayer.getAverageColumnHeight(notQuiteapiBoard));
        assertEquals(5,smartPlayer.getAverageColumnHeight(justAnotherBoard));
    }
    
    /**
     * This method tests the column height range using JUnit. 
     * 
     * The first number inside the assertEquals method is the expected value.
     * The methods following the expected value call the getColumnHeightRange
     * method from the SmartPlayer object to run the method housed in TetrisAI.java.
     *
     */
    @Test
    public void testColumnHeightRange() {
        assertEquals(0,smartPlayer.getColumnHeightRange(emptyBoard));
        assertEquals(1,smartPlayer.getColumnHeightRange(oneBlockBoard));
        assertEquals(0,smartPlayer.getColumnHeightRange(opaqueBoard));
        assertEquals(2,smartPlayer.getColumnHeightRange(notQuiteapiBoard));
        assertEquals(1,smartPlayer.getColumnHeightRange(justAnotherBoard));
    }
    
    /**
     * This method tests the total gap count using JUnit. 
     * 
     * The first number inside the assertEquals method is the expected value.
     * The methods following the expected value call the getTotalGapCount
     * method from the SmartPlayer object to run the method housed in TetrisAI.java.
     *
     */
    @Test
    public void testTotalGapCount() {
        assertEquals(0,smartPlayer.getTotalGapCount(emptyBoard));
        assertEquals(0,smartPlayer.getTotalGapCount(oneBlockBoard));
        assertEquals(0,smartPlayer.getTotalGapCount(opaqueBoard));
        assertEquals(14,smartPlayer.getTotalGapCount(notQuiteapiBoard));
        assertEquals(7,smartPlayer.getTotalGapCount(justAnotherBoard));

    }
    
    /**
     * 
     * This method tests the column height variance using JUnit. 
     * 
     * The first number inside the assertEquals method is the expected value.
     * The methods following the expected value call the getColumnHeightVariance
     * method from the SmartPlayer object to run the method housed in TetrisAI.java.
     *
     */
    
    @Test
    public void testColumnHeightVariance() {
        assertEquals(0,smartPlayer.getColumnHeightVariance(emptyBoard));
        assertEquals(2,smartPlayer.getColumnHeightVariance(oneBlockBoard));
        assertEquals(0,smartPlayer.getColumnHeightVariance(opaqueBoard));
        assertEquals(6,smartPlayer.getColumnHeightVariance(notQuiteapiBoard));
        assertEquals(4,smartPlayer.getColumnHeightVariance(justAnotherBoard));
    }
    
    @Test
    public void testBestPlacement() {
        //testing empty board with I shape
        Placement temp = new Placement(Rotation.CCW_90, 0);
        assertEquals(temp, smartPlayer.findBestPlacement(emptyBoard, Shape.I));
        
        //testing one block board with J shape
        temp = new Placement(Rotation.CCW_270, 0);
        assertEquals(temp, smartPlayer.findBestPlacement(oneBlockBoard, Shape.J));
        
        //testing opaque board with L shape
        temp = new Placement(Rotation.NONE, 0);
        assertEquals(temp, smartPlayer.findBestPlacement(opaqueBoard, Shape.T));
        
        //testing notQuiteapiBoard board with L shape
        temp = new Placement(Rotation.CCW_180, 6);
        assertEquals(temp, smartPlayer.findBestPlacement(notQuiteapiBoard, Shape.T));
        
        //testing notQuiteapiBoard board with L shape
        temp = new Placement(Rotation.NONE, 5);
        assertEquals(temp, smartPlayer.findBestPlacement(justAnotherBoard, Shape.Z));
    }
    
}
