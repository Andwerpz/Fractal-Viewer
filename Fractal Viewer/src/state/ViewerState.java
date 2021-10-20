package state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;

public class ViewerState extends State{
	
	Board board;
	
	java.awt.Point mouse = new Point(0, 0);
	
	public static boolean drawTutorial = true;

	public ViewerState(StateManager gsm, Board b) {
		super(gsm);
		this.board = b;
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(Point mouse2) {
		this.mouse = mouse2;
		board.tick(mouse2);
	}

	@Override
	public void draw(Graphics g) {
		board.draw(g, this.mouse);
		
		if(ViewerState.drawTutorial) {
			g.setColor(Color.WHITE);
			g.drawString("Click and drag RIGHT MOUSE to move the view around", 20, 600);
			g.drawString("SCROLL WHEEL to zoom in and out", 20, 630);
			g.drawString("M to return to the main menu", 20, 660);
			g.drawString("T to hide tutorial", 20, 690);
		}
	}

	@Override
	public void keyPressed(int k) {
		if(k == KeyEvent.VK_T) {
			ViewerState.drawTutorial = false;
		}
		else if(k == KeyEvent.VK_M) {
			this.gsm.states.pop();
		}
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
		// TODO Auto-generated method stub
		
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
		this.board.mousePressed(arg0);
	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		this.board.mouseReleased();
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		this.board.mouseWheelMoved(arg0);
	}

}
