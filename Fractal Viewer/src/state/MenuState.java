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
	
	TextBox tb;

	public MenuState(StateManager gsm) {
		super(gsm);
		
		bm = new ButtonManager();
		
		bm.addSliderButton(new SliderButton(50, 50, 200, 10, 0, 100, "slider 1", Color.RED));
		bm.addSliderButton(new SliderButton(50, 100, 400, 10, 0, 100, "best slider", Color.YELLOW));
		
		bm.addButton(new Button(50, 200, 100, 100, "weird button", Color.pink, Color.blue));
		
		bm.addToggleButton(new ToggleButton(200, 200, 100, 100, "Toggle"));
		
		tb = new TextBox(100, 300, 300, 300, "This simulation is based off of the Numberphile video, Darts in Higher Dimensions. "
				+ "If you haven't watched it yet, you should go check it out. In the beginning of the video, "
				+ "3Blue1Brown introduces us to a strange game of darts, not one in which you are trying n"
				+ "to get the most points, but one in which you are just trying to throw the most amount "
				+ "of darts. The way the game works is whenever you throw a dart and it lands inside the "
				+ "board, you define the new board diameter by drawing a chord that is perpendicular to "
				+ "the line made from the point to the center. The length of this chord is the new diameter "
				+ "of the circle. Naturally, as you continue to throw more darts, the board continually "
				+ "shrinks, and when a dart lands outside of the board, it's game over. Now, the question posed "
				+ "is as follows: Lets say the board has a diameter of 1, and it is placed with its center at "
				+ "(0.5, 0.5). If a player were to throws darts, with the landing points x and y picked between "
				+ "0 and 1 with a uniform random distribution, what is the average score of the player. Now, he "
				+ "goes on to solve this problem with probability and math, but I just wanted to see if I "
				+ "could get the answer using a simulation.", new Font("Times New Roman", 0, 12));
		
	}

	@Override
	public void init() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void tick(Point mouse) {

		bm.tick(mouse);
		
	}

	@Override
	public void draw(Graphics g) {
		
		bm.draw(g);
		tb.draw(g);
		
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

		if(bm.buttonClicked(arg0).equals("weird button")) {
			System.out.println("wlekrjwelk");
			bm.sliderButtons.get(0).setVal(50);
			bm.sliderButtons.get(1).setVal(75);
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
		
		bm.pressed(arg0);

	}

	@Override
	public void mouseReleased(MouseEvent arg0) {
		
		bm.mouseReleased();
		
	}

	@Override
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		// TODO Auto-generated method stub
		
	}

}
