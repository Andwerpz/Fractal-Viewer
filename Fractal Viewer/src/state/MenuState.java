package state;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import button.ButtonManager;
import button.Button;
import button.SliderButton;
import button.ToggleButton;
import main.MainPanel;
import util.GraphicsTools;
import util.TextBox;

public class MenuState extends State{
	
	ButtonManager bm;
	Board board;
	
	java.awt.Point mouse = new java.awt.Point(0, 0);

	public MenuState(StateManager gsm) {
		super(gsm);
		
		bm = new ButtonManager();
		
		//the menu buttons should be centered within their respective halves
		
		int buttonWidth = 250;
		int buttonHeight = 500;
		
		BufferedImage mandelbrotPreview = GraphicsTools.loadImage("/mandelbrot_preview.png");
		BufferedImage newtonRaphsonPreview = GraphicsTools.loadImage("/newtonraphson_preview.png");
		
		bm.addButton(new Button(
				(MainPanel.WIDTH / 2) - MainPanel.WIDTH / 4 - buttonWidth / 2, 
				(MainPanel.HEIGHT / 2) - buttonHeight / 2, 
				buttonWidth, 
				buttonHeight, 
				mandelbrotPreview, 
				true, 
				"Mandelbrot"));
		
		bm.addButton(new Button(
				(MainPanel.WIDTH / 2) + MainPanel.WIDTH / 4 - buttonWidth / 2, 
				(MainPanel.HEIGHT / 2) - buttonHeight / 2, 
				buttonWidth, 
				buttonHeight, 
				newtonRaphsonPreview, 
				true, 
				"NewtonRaphson"));
		
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
		
		Font labelFont = new Font("Dialogue", Font.PLAIN, 36);
		
		String mandelbrotLabel = "Mandelbrot";
		String newtonRaphsonLabel = "Newton - Raphson";
		
		int mandelbrotLabelWidth = GraphicsTools.calculateTextWidth(mandelbrotLabel, labelFont);
		int newtonRaphsonLabelWidth = GraphicsTools.calculateTextWidth(newtonRaphsonLabel, labelFont);
		
		g.setFont(labelFont);
		g.setColor(Color.BLACK);
		
		g.drawString(mandelbrotLabel, MainPanel.WIDTH / 4 - mandelbrotLabelWidth / 2, MainPanel.HEIGHT / 6);
		g.drawString(newtonRaphsonLabel, MainPanel.WIDTH / 2 + MainPanel.WIDTH / 4 - newtonRaphsonLabelWidth / 2, MainPanel.HEIGHT / 6);
		
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
		String which = this.bm.buttonClicked(arg0);
		System.out.println(which);
		if(which != null) {
			switch(which) {
			case "Mandelbrot":
				this.board.mandelbrot = true;
				this.board.newtonRaphson = false;
				this.gsm.states.push(new ViewerState(this.gsm, this.board));
				break;
				
			case "NewtonRaphson":
				this.board.mandelbrot = false;
				this.board.newtonRaphson = true;
				this.gsm.states.push(new ViewerState(this.gsm, this.board));
				break;
			}
		}
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
