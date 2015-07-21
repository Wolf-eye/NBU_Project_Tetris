package Tetris;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JFrame;
import javax.swing.JLabel;

@SuppressWarnings("serial")
public class TetrisMain extends JFrame{
	
	JLabel scoreBar;
	// constants for game screen size
	public static final int WIDTH = 400, HEIGHT = 600;
	
	public TetrisMain() {
		
		// adding score counter
		scoreBar = new JLabel("Score 0");
	    add(scoreBar, BorderLayout.SOUTH);
	    GameEngine board = new GameEngine(this);
	    add(board);
	    board.start();
	    board.setBackground(Color.DARK_GRAY);
	    // making game UI menu
	    setSize(WIDTH, HEIGHT);
	    setTitle("Turbo Tetris");
	    setResizable(false);
	    setDefaultCloseOperation(EXIT_ON_CLOSE);
	}
	
	public JLabel getStatusBar() {
			
		return scoreBar;
	}

	public static void main(String[] args) {

		TetrisMain game = new TetrisMain();
		game.setLocationRelativeTo(null);
		game.setVisible(true);

	} 
}
