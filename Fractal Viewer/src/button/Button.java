package button;

import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import util.GraphicsTools;

public class Button {
	
	private int x, y, width, height;
	private String text;
	private boolean pressed = false;
	private Font font;
	
	private Color baseColor;
	private Color pressedColor;
	
	private int textWidth;
	private int textHeight;
	
	private boolean drawImage;	//if true, then no text will be drawn on the button, only an image.
	private boolean maintainImageAspectRatio; //if true, then the image will be just be drawn to fill the button, without regard to if it goes out of the button
	private BufferedImage img;
	
	public Button(int x, int y, int width, int height, String text) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.font = new Font("Dialogue", Font.PLAIN, 12);	//default font for java swing
		
		this.baseColor = Color.white;
		this.pressedColor = Color.black;
		
		this.textWidth = calculateTextWidth();
		this.drawImage = false;
	}
	
	public Button(int x, int y, int width, int height, String text, Color baseColor, Color pressedColor) {
		
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		this.font = new Font("Dialogue", Font.PLAIN, 12);	//default font for java swing
		
		
		
		this.baseColor = baseColor;
		this.pressedColor = pressedColor;
		
		this.textWidth = calculateTextWidth();
		this.drawImage = false;
		
	}
	
	public Button(int x, int y, int width, int height, BufferedImage img, boolean maintainImageAspectRatio, String text) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
		this.text = text;
		
		this.baseColor = Color.white;
		this.pressedColor = Color.black;
		
		this.drawImage = true;
		this.img = img;
		this.maintainImageAspectRatio = maintainImageAspectRatio;
		
		if(this.maintainImageAspectRatio) {
			BufferedImage buttonImg = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
			Graphics gImg = buttonImg.getGraphics();
			
			//calculating ratios between button and input img
			double widthRatio = (double) width / (double) img.getWidth();	//button width / img width
			double heightRatio = (double) height / (double) img.getHeight();//button height / img height
			
			//enlarge the image to whichever ratio is bigger
			gImg.drawImage(img, 0, 0, (int) (Math.max(widthRatio, heightRatio) * (double) img.getWidth()), (int) (Math.max(widthRatio, heightRatio) * (double) img.getHeight()), null);
			
			System.out.println((int) (Math.max(widthRatio, heightRatio) * (double) img.getWidth()));
			
			//set the button img to the new img
			this.img = buttonImg;
		}
	}
	
	public void draw(Graphics g) {
		if(!this.drawImage) {
			g.setColor(baseColor);
			
			if(pressed) {
				g.setColor(pressedColor);
				//g.fillRect(x, y, this.width, height);
			}
			
			g.setFont(font);
			
			g.fillRect(x, y, width, height);
			
			g.setColor(Color.black);
			g.drawRect(x, y, this.width, height);
			g.drawString(text, x + (width / 2) - (textWidth / 2), y + (height / 2) + font.getSize() / 2);
		}
		else {
			BufferedImage drawnImg = GraphicsTools.copyImage(this.img);
			if(pressed) {
				drawnImg = GraphicsTools.darkenImage(0.5, drawnImg);
			}
			
			g.drawImage(drawnImg, this.x, this.y, this.width, this.height, null);
			
			g.setColor(Color.black);
			g.drawRect(this.x, this.y, this.width, this.height);
		}
		
		
	}
	
	public int calculateTextWidth() {
		BufferedImage img = new BufferedImage(1, 1, BufferedImage.TYPE_INT_ARGB);
		FontMetrics fm = img.getGraphics().getFontMetrics(font);
		return fm.stringWidth(text);
	}
	
	public void setFont(Font font) {
		this.font = font;
		this.textWidth = calculateTextWidth();
	}
	
	public void setText(String text) {
		this.text = text;
		this.textWidth = calculateTextWidth();
	}
	
	public void setX(int x) {
		this.x = x;
	}
	
	public void setY(int y) {
		this.y = y;
	}
	
	public int getY() {
		return this.y;
	}
	
	public int getX() {
		return this.x;
	}
	
	public String getText() {
		return text;
	}
	
	public boolean getPressed() {
		return pressed;
	}
	
	public int getWidth() {
		return width;
	}
	
	public int getHeight() {
		return height;
	}
	
	public Color getBaseColor() {
		return baseColor;
	}
	
	public Font getFont() {
		return font;
	}
	
	public Color getPressedColor() {
		return pressedColor;
	}
	
	//is the mouse inside the boundaries of this button
	public boolean contains(Point m) {
		Rectangle rect = new Rectangle(this.x, this.y, this.width, this.height);
		return rect.contains(m);
	}
	
	public void pressed(MouseEvent m) {
		Rectangle r = new Rectangle(x, y, width, height);
		if(r.contains(new Point(m.getX(), m.getY()))) {
			pressed = true;
		}
	}
	
	public boolean isPressed(Point m) {
		Rectangle r = new Rectangle(x, y, width, height);
		if(r.contains(m)) {
			return true;
		}
		return false;
	}
	
	public void released() {
		pressed = false;
	}
	
//	public boolean isClicked(int x, int y) {
//		
//		if(this.contains(new Point(x, y))) {
//			return true;
//		}
//		
//		return false;
//		
//	}
	
	public boolean isClicked(MouseEvent arg0) {
		Rectangle r = new Rectangle(x, y, width, height);
		if(r.contains(new Point(arg0.getX(), arg0.getY()))) {
			return true;
		}
		
		return false;
		
	}


}
