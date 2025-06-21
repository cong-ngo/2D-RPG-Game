package main;

import java.awt.Dimension;
import java.awt.Graphics;
import javax.swing.JPanel;
import inputs.KeyboardInputs;
import inputs.MouseInputs;
import static main.Game.GAME_HEIGHT;
import static main.Game.GAME_WIDTH;


public class GamePanel extends JPanel {
	private MouseInputs mouseInputs;
	private Game game;
	
	public GamePanel(Game game) {
		mouseInputs = new MouseInputs(this);
		this.game = game;
		
		setPanelsize(); 
		addKeyListener(new KeyboardInputs(this));
		addMouseListener(mouseInputs);
		addMouseMotionListener(mouseInputs);
	}

	// Panel size
	private void setPanelsize() {
		// window size
		Dimension size = new Dimension(GAME_WIDTH, GAME_HEIGHT);
		setPreferredSize(size);
		System.out.println("size: "+ GAME_WIDTH + " : " + GAME_HEIGHT);
	}
	
	public void updateGame() {

	}
	
	public void paintComponent(Graphics g) {
		super.paintComponent(g);

		game.render(g);
	}
	
	public Game getGame() {
		return game;
	}
	
}
