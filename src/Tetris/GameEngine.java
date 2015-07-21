package Tetris;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.Timer;

import Tetris.TetrisShapes;


@SuppressWarnings("serial")
public class GameEngine extends JPanel implements ActionListener {

	// // constants for size of the playing ground
    final int BoardWidth = 14;
    final int BoardHeight = 24;

    Timer timer;
    boolean isFallFin = false;
    boolean isStarted = false;
    boolean isPaused = false;
    int numLinesRemoved = 0;
    int curX = 0;
    int curY = 0;
    JLabel statusbar;
    TetrisFactory curPiece;
    TetrisShapes[] board;



    public GameEngine(TetrisMain parent) {
    	
    	// set to make shapes and how fast they move, 2fast2furious :)
       setFocusable(true);
       curPiece = new TetrisFactory();
       timer = new Timer(200, this);
       timer.start(); 

       statusbar =  parent.getStatusBar();
       board = new TetrisShapes[BoardWidth * BoardHeight];
       addKeyListener(new Controls());
       clearBoard();  
    }

    public void actionPerformed(ActionEvent e) {
        if (isFallFin) {
        	isFallFin = false;
            newPiece();
        } else {
            oneLineDown();
        }
    }


    int squareWidth() { return (int) getSize().getWidth() / BoardWidth; }
    int squareHeight() { return (int) getSize().getHeight() / BoardHeight; }
    TetrisShapes shapeAt(int x, int y) { return board[(y * BoardWidth) + x]; }


    public void start()
    {
        if (isPaused)
            return;

        isStarted = true;
        isFallFin = false;
        numLinesRemoved = 0;
        clearBoard();

        newPiece();
        timer.start();
    }
    // pause the game and return to play
    private void pause()
    {
        if (!isStarted)
            return;

        isPaused = !isPaused;
        if (isPaused) {
            timer.stop();
            statusbar.setText("Paused, C'mon man!");
        } else {
            timer.start();
            statusbar.setText(String.valueOf(numLinesRemoved));
        }
        repaint();
    }
    // draw different pieces shape, using square like a base shape
    public void paint(Graphics g)
    { 
        super.paint(g);

        Dimension size = getSize();
        int boardTop = (int) size.getHeight() - BoardHeight * squareHeight();


        for (int i = 0; i < BoardHeight; ++i) {
            for (int j = 0; j < BoardWidth; ++j) {
            	TetrisShapes shape = shapeAt(j, BoardHeight - i - 1);
                if (shape != TetrisShapes.NoShape)
                    drawSquare(g, 0 + j * squareWidth(),
                               boardTop + i * squareHeight(), shape);
            }
        }

        if (curPiece.getShape() != TetrisShapes.NoShape) {
            for (int i = 0; i < 4; ++i) {
                int x = curX + curPiece.x(i);
                int y = curY - curPiece.y(i);
                drawSquare(g, 0 + x * squareWidth(),
                           boardTop + (BoardHeight - y - 1) * squareHeight(),
                           curPiece.getShape());
            }
        }
    }
    // drop piece to the bottom
    private void dropDown()
    {
        int newY = curY;
        while (newY > 0) {
            if (!tryMove(curPiece, curX, newY - 1))
                break;
            --newY;
        }
        pieceDropped();
    }
    // faster movement
    private void oneLineDown()
    {
        if (!tryMove(curPiece, curX, curY - 1))
            pieceDropped();
    }


    private void clearBoard()
    {
        for (int i = 0; i < BoardHeight * BoardWidth; ++i)
            board[i] = TetrisShapes.NoShape;
    }

    private void pieceDropped()
    {
        for (int i = 0; i < 4; ++i) {
            int x = curX + curPiece.x(i);
            int y = curY - curPiece.y(i);
            board[(y * BoardWidth) + x] = curPiece.getShape();
        }

        removeFullLines();

        if (!isFallFin)
            newPiece();
    }

    private void newPiece()
    {
    	// add new shape and set from where to where, to start and stop dropping new ones
        curPiece.setRandomShape();
        curX = BoardWidth / 2;
        curY = BoardHeight - 2;

        if (!tryMove(curPiece, curX, curY)) {
            curPiece.setShape(TetrisShapes.NoShape);
            timer.stop();
            isStarted = false;
            statusbar.setText("Game Over. Your final score is: " + String.valueOf(numLinesRemoved));
        }
    }
    // moving and cleaning full lines
    private boolean tryMove(TetrisFactory newPiece, int newX, int newY)
    {
        for (int i = 0; i < 4; ++i) {
            int x = newX + newPiece.x(i);
            int y = newY - newPiece.y(i);
            if (x < 0 || x >= BoardWidth || y < 0 || y >= BoardHeight)
                return false;
            if (shapeAt(x, y) != TetrisShapes.NoShape)
                return false;
        }

        curPiece = newPiece;
        curX = newX;
        curY = newY;
        repaint();
        return true;
    }

    private void removeFullLines()
    {
        int numFullLines = 0;

        for (int i = BoardHeight - 1; i >= 0; --i) {
            boolean lineIsFull = true;

            for (int j = 0; j < BoardWidth; ++j) {
                if (shapeAt(j, i) == TetrisShapes.NoShape) {
                    lineIsFull = false;
                    break;
                }
            }

            if (lineIsFull) {
                ++numFullLines;
                for (int k = i; k < BoardHeight - 1; ++k) {
                    for (int j = 0; j < BoardWidth; ++j)
                         board[(k * BoardWidth) + j] = shapeAt(j, k + 1);
                }
            }
        }

        if (numFullLines > 0) {
            numLinesRemoved += numFullLines;
            statusbar.setText("Score: " + String.valueOf(numLinesRemoved));
            isFallFin = true;
            curPiece.setShape(TetrisShapes.NoShape);
            repaint();
        }
     }
    // add some colors to the shapes
    private void drawSquare(Graphics g, int x, int y, TetrisShapes shape)
    {
        Color colors[] = { 
        	new Color(0, 0, 0),
        	new Color(0, 204, 0), 
            new Color(102, 128, 255),
            new Color(204, 204, 0),
            new Color(255, 50, 50),
        };


        Color color = colors[shape.ordinal()];

        g.setColor(color);
        g.fillRect(x + 1, y + 1, squareWidth() - 2, squareHeight() - 2);
        
    }
    // keyboard functionality
    class Controls extends KeyAdapter {
         public void keyPressed(KeyEvent e) {

             if (!isStarted || curPiece.getShape() == TetrisShapes.NoShape) {  
                 return;
             }

             int keycode = e.getKeyCode();

             if (keycode == 'p' || keycode == 'P') {
                 pause();
                 return;
             }

             if (isPaused)
                 return;

             switch (keycode) {
             case KeyEvent.VK_LEFT:
                 tryMove(curPiece, curX - 1, curY);
                 break;
             case KeyEvent.VK_RIGHT:
                 tryMove(curPiece, curX + 1, curY);
                 break;
             case KeyEvent.VK_UP:
                 tryMove(curPiece.rotateLeft(), curX, curY);
                 break;
             case KeyEvent.VK_DOWN:
            	 oneLineDown();
                 //tryMove(curPiece.rotateRight(), curX, curY);
                 break;
             case KeyEvent.VK_SPACE:
                 dropDown();
                 break;
             }

         }
     }
}
