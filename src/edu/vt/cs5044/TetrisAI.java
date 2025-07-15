package edu.vt.cs5044;

import java.util.Set;

import edu.vt.cs5044.tetris.AI;
import edu.vt.cs5044.tetris.Board;
import edu.vt.cs5044.tetris.Placement;
import edu.vt.cs5044.tetris.Rotation;
import edu.vt.cs5044.tetris.Shape;

/**
 * This is the starting class for the TetrisAI project. 
 * All of the variables are initialized in the original class, and assigned
 * values in a seperate method. 
 * 
 * All descriptions of the classes are in the Javadoc.
 *
 * @author willm
 * @version Oct 4, 2023
 *
 */
public class TetrisAI implements AI {
    
    private int averageHeightWeight = 8; //alone returned 40-50
    private int varianceHeightWeight = 2; //alone returned 30-40
    private int rangeHeightWeight = 6; //alone returned 35-45
    private int gapCountWeight = 8; //alone returned 100-120
    
    /**
     * Calculates the cost of each placement and rotation of the shape on board. 
     * Returns the cost as an integer. 
     * 
     * @param currentBoard - board that is being evaluated
     * @return cost of the value times the weight specified above. 
     */
    private int calculateCost(Board currentBoard) {
        return averageHeightWeight * getAverageColumnHeight(currentBoard) + 
            varianceHeightWeight * getColumnHeightVariance(currentBoard) + 
            rangeHeightWeight * getColumnHeightRange(currentBoard) + 
            gapCountWeight * getTotalGapCount(currentBoard);
    }
    
     
    /**
     * Cycles through each possible rotation and starting location for 
     * each shape, returns the one with the lowest cost based on the weight
     * of each possible calculation.
     * 
     * @param currentBoard - current board in tetris game (without new block)
     * @param shape - current block in its original rotation
     * @return - bestPlacement - placement enum that returns the shape and rotation
     * with the lowest cost.
     */
    @Override
    public Placement findBestPlacement(Board currentBoard, Shape shape) {
        Placement bestPlacement = null;
        int minCost = Integer.MAX_VALUE;
        Set<Rotation> rotations = shape.getValidRotationSet();
        
        //loop through each column
        for (int col = 0; col < currentBoard.WIDTH; col++) {
            
            //loop through each rotation
            for (Rotation rotation : rotations) {
                
                //gets the rightmost block for that shape
                int rightMost = shape.getWidth(rotation) + col;
                
                //checks to make sure that the rightmost block is in the board still
                if (rightMost < currentBoard.WIDTH + 1) {
                    //sets the new placement and board
                    Placement temp = new Placement(rotation, col);
                    Board newBoard = currentBoard.getResultBoard(shape, temp);
                    
                    //calculate the cost
                    int cost = calculateCost(newBoard);

                    //if the cost is less than the min cost, update the best placement. 
                    if (cost < minCost) {
                        minCost = cost;
                        bestPlacement = temp;
                    }
                }

            }
        }
        
        //the last block with the best placement will be placed. 
        return bestPlacement;
    }
    
    
    /**
     * Sums all of the columns and divides by the width of the column
     * to return the average column height with the new shape placed
     * in its new rotation and location
     * 
     * @param board - whole board object
     * @return - (sumheights / numCols) - integer that counts the criteria above
     * 
     */
    @Override
    public int getAverageColumnHeight(Board board) {
        
        boolean [][] blocks = board.getFixedBlocks();
        int sumHeights = 0;
        int numCols = Board.WIDTH;
        
        //loop through each column
        for (int col = 0; col < numCols; col++) {
            //add column height to the sum of hights
            sumHeights += getHeightAtColumn(blocks[col]); 
        }
        //return the average
        return sumHeights / numCols;
    }
    
    /**
     * Subtracts the highest column from the lowest column to return the 
     * range of the new shape in its new rotation and location. 
     * 
     * @param board - whole board object
     * @return - (max-min) - integer that counts the criteria above
     * 
     */
    @Override
    public int getColumnHeightRange(Board board) {
        
        int min = Integer.MAX_VALUE; 
        int max = Integer.MIN_VALUE;
        
        boolean [][] blocks = board.getFixedBlocks();

        //loop through each column
        for (int col = 0; col < Board.WIDTH; col++) {
            //set the height of the column to tempHeight
            int tempHeight = getHeightAtColumn(blocks[col]);
            
            //if the hight is higher than the max, set the new value
            if (tempHeight > max) {
                max = tempHeight;
            }
            
            //if the hight is lower than the min, set the new value
            if (tempHeight <= min) {
                min = tempHeight;
            }
        }
        
        //return the difference
        return max - min;
    }

    /**
     * Loops through the board. With each passing column, it calculates the 
     * absolute value of the difference between that column and the one before
     * the sum is returned as an integer. 
     * 
     * @param board - whole board object
     * @return - sumVariance - integer that counts the criteria above
     * 
     */
    @Override
    public int getColumnHeightVariance(Board board) {
        boolean [][] blocks = board.getFixedBlocks();
        
        //find the initial height of the first column
        int lastColHeight = getHeightAtColumn(blocks[0]);
        int sumDiff = 0;
                
        //loop through each column
        for (int col = 0; col < Board.WIDTH; col++) {
            //get the difference between this column and the last, absolute value
            int tempDiff = (lastColHeight - getHeightAtColumn(blocks[col]));
            sumDiff += Math.abs(tempDiff);
            //set the last column height to this column for the next loop
            lastColHeight = getHeightAtColumn(blocks[col]);
        }
        
        return sumDiff;
    }

    /**
     * Loops through every block to see if there is a blank that can't be filled
     * unless every row above is cleared and a shape is placed inside the block. 
     * This one is weighed the highest due to the importance of minimizing gaps. 
     * 
     * @param board - the whole board object
     * @return totalCount - the count of gaps fitting the criteria above.
     */
    @Override
    public int getTotalGapCount(Board board) {
        
        boolean [][] blocks = board.getFixedBlocks();
        int totalCount = 0;
        
        //loop through each column
        for (int col = 0; col < Board.WIDTH; col++) {
            //subtract blocks from total height to get gaps.
            totalCount += getHeightAtColumn(blocks[col]) - getBlocksAtCol(blocks[col]); 
        }
        
        return totalCount;
    }

    /**
     * 
     * Counts the gaps in a column.
     *
     * @param column boolean array for that column of rows.
     * @return count of gaps
     */
    private int getBlocksAtCol(boolean[] column) {
        int countBlocks = 0;
        
        //loop through each row
        for (int row = 0; row < Board.HEIGHT; row++) {
            //if theres a brick at that intersection, add one to countBlocks
            if (column[row]) { 
                countBlocks++;
            }
        }
        return countBlocks;
    }

    /**
     * 
     * Counts the height at a given column starting from the top.
     *
     * @param column boolean array for that column of rows
     * @return height integer
     */ 
    private int getHeightAtColumn(boolean[] column) {
        
        //loop through each row
        for (int row = Board.HEIGHT - 1; row >= 0; row--) {
            //if theres a brick at that intersection, return that row+1
            if (column[row]) { 
                return row + 1;
            }
        }
        return 0; 
    }

}
