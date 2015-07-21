package Tetris;

import java.util.Random;
import java.lang.Math;


public class TetrisFactory {

    private TetrisShapes pieceShape;
    private int coords[][];
    private int[][][] coordsTable;


    public TetrisFactory() {
    	
    	coords = new int[4][2];
        setShape(TetrisShapes.NoShape);

    }

    public void setShape(TetrisShapes shape) {

         coordsTable = new int[][][] {
        	// null shape which is default
            { 	{ 0, 0 },   
            	{ 0, 0 },   
            	{ 0, 0 },   
            	{ 0, 0 } },
            // line ----
            { 	{ 0, -1 },  
            	{ 0, 0 },   
            	{ 0, 1 },   
            	{ 0, 2 } },
            // square shape
            { 	{ -1, 0 },  
            	{ 0, 0 },   
            	{ 1, 0 },   
            	{ 0, 1 } },
            // --|-- shape
            { 	{ 0, 0 },   
            	{ 1, 0 },   
            	{ 0, 1 },   
            	{ 1, 1 } },
            // g shaped shape
            { 	{ -1, -1 }, 
            	{ 0, -1 },  
            	{ 0, 0 },   
            	{ 0, 1 } },
            // g shape mirror image
            { 	{ 1, -1 },  
            	{ 0, -1 },  
            	{ 0, 0 },   
            	{ 0, 1 } }
        };

        for (int i = 0; i < 4 ; i++) {
            for (int j = 0; j < 2; ++j) {
                coords[i][j] = coordsTable[shape.ordinal()][i][j];
            }
        }
        pieceShape = shape;

    }
    // set shapes coordinates and points for rotating
    private void setX(int index, int x) { coords[index][0] = x; }
    private void setY(int index, int y) { coords[index][1] = y; }
    
    public int x(int index) { return coords[index][0]; }
    public int y(int index) { return coords[index][1]; }
    public TetrisShapes getShape()  { return pieceShape; }
    
    // random generator for playing with all shapes in jumbled order
    public void setRandomShape()
    {
        Random random = new Random();
        int x = Math.abs(random.nextInt(4)) + 1;
        TetrisShapes[] values = TetrisShapes.values(); 
        setShape(values[x]);
    }
    
    // rotating Tetris pieces to the left
    public TetrisFactory rotateLeft() 
    {
        if (pieceShape == TetrisShapes.SquareShape)
            return this;

        TetrisFactory result = new TetrisFactory();
        result.pieceShape = pieceShape;

        for (int i = 0; i < 4; ++i) {
            result.setX(i, y(i));
            result.setY(i, -x(i));
        }
        return result;
    }
    // ToDo rotating Tetris pieces to the right

}