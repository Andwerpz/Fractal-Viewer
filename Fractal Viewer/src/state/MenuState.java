package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

import button.ButtonManager;
import button.Button;
import button.SliderButton;
import button.ToggleButton;
import util.TextBox;

public class MenuState extends State{
	
	ButtonManager bm;
	Board board;
	
	java.awt.Point mouse = new java.awt.Point(0, 0);

	public MenuState(StateManager gsm) {
		super(gsm);
		
		bm = new ButtonManager();
		board = new Board();
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(Point mouse) {
		this.mouse = mouse;
		bm.tick(mouse);
		
	}

	@Override
	public void draw(Graphics g) {
		board.draw(g, this.mouse);
		bm.draw(g);
	}

	@Override
	public void keyPressed(int k) {
		//TODO Auto-generated method stub
		
	}

	@Override
	public void keyReleased(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void keyTyped(int k) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseClicked(MouseEvent arg0) {

	}

	@Override
	public void mouseEntered(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mouseExited(MouseEvent arg0) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void mousePressed(MouseEvent arg0) {
		board.mousePressed(arg0);
		bm.pressed(arg0);

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		board.mouseReleased();
		bm.mouseReleased();
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		board.mouseWheelMoved(arg0);
	}

}
