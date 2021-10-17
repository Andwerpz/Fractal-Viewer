package state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;

import main.MainPanel;
import util.ComplexNumber;
import util.Vector;
import util.Point;

public class Board {
	
	public boolean mousePressed = false;
	public java.awt.Point mouse = new java.awt.Point(0, 0);
	public boolean zoomIn = false;
	public boolean zoomOut = false;
	public double zoomFactor = 1.2;
	
	
	public boolean mandelbrot = true;
	public int mandelbrotMaxIterations = 50;
	
	public boolean newtonRaphson = false;
	
	
	public double xLow = -2;
	public double xHigh = 2;
	public double yLow = -2;
	public double yHigh = 2;

	public Board() {
		
	}
	
	public void tick(java.awt.Point mouse) {

	}
	
	public void draw(Graphics g, java.awt.Point mouse) {
		//mouse controls
		
		double xRange = xHigh - xLow;
		double yRange = yHigh - yLow;
		Vector curRealMouse = new Vector(xLow + (((double) this.mouse.x / (double) MainPanel.WIDTH) * xRange), yHigh - (((double) this.mouse.y / (double) MainPanel.HEIGHT) * yRange));
		
		if(this.mousePressed) {
			Vector nextRealMouse = new Vector(xLow + (((double) mouse.x / (double) MainPanel.WIDTH) * xRange), yHigh - (((double) mouse.y / (double) MainPanel.HEIGHT) * yRange));
			
			Vector toNextRealMouse = new Vector((Point) curRealMouse, (Point) nextRealMouse);
			
			xLow -= toNextRealMouse.x;
			xHigh -= toNextRealMouse.x;
			yLow -= toNextRealMouse.y;
			yHigh -= toNextRealMouse.y;
		}
		
		this.mouse.x = mouse.x;
		this.mouse.y = mouse.y;
		
		
		
		//drawing the fractal
		
		BufferedImage fractalImg = new BufferedImage(MainPanel.WIDTH, MainPanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < MainPanel.HEIGHT; i++) {
			for(int j = 0; j < MainPanel.WIDTH; j++) {
				double xCur = xLow + (((double) j / (double) MainPanel.WIDTH) * xRange);
				double yCur = yHigh - (((double) i / (double) MainPanel.HEIGHT) * yRange);
				
				Color curColor = mandelbrotColor(xCur, yCur);
				fractalImg.setRGB(j, i, curColor.getRGB());
			}
		}
		
		//doing zoom in or out
		g.drawImage(fractalImg, 0, 0, null);
		if(this.zoomIn) {
			this.zoomIn = false;
			xLow = curRealMouse.x - (curRealMouse.x - xLow) / this.zoomFactor;
			yLow = curRealMouse.y - (curRealMouse.y - yLow) / this.zoomFactor;
			xHigh = curRealMouse.x + (xHigh - curRealMouse.x) / this.zoomFactor; 
			yHigh = curRealMouse.y + (yHigh - curRealMouse.y) / this.zoomFactor; 
		}
		if(this.zoomOut) {
			this.zoomOut = false;
			xLow = curRealMouse.x - (curRealMouse.x - xLow) * this.zoomFactor;
			yLow = curRealMouse.y - (curRealMouse.y - yLow) * this.zoomFactor;
			xHigh = curRealMouse.x + (xHigh - curRealMouse.x) * this.zoomFactor; 
			yHigh = curRealMouse.y + (yHigh - curRealMouse.y) * this.zoomFactor; 
		}
		
		//drawing ui
		g.setColor(Color.WHITE);
		g.fillRect(0, 0, 250, 30);
		g.setColor(Color.BLACK);
		g.drawString("View Width: " + xRange, 20, 20);
	}
	
	public Color mandelbrotColor(double real, double imaginary) {
		
		ComplexNumber c = new ComplexNumber(real, imaginary);
		ComplexNumber out = new ComplexNumber(0, 0);
		
		for(int i = 0; i < mandelbrotMaxIterations; i++) {
			//System.out.println(c.mod() + " : " + out);
			
			if(Math.abs(out.mod()) > 2) {
				return new Color((int) (((double) i / (double) mandelbrotMaxIterations) * 255), (int) (((double) i / (double) mandelbrotMaxIterations) * 255), 255);
			}
			//System.out.println("BEFORE: " + out);
			out = out.square();
			//System.out.println("AFTER: " + out);
			out.add(c);
			
		}
	
		return Color.BLACK;
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		this.mousePressed = true;
	}
	
	public void mouseReleased() {
		this.mousePressed = false;
	}
	
	public void mouseWheelMoved(MouseWheelEvent arg0) {
		if(arg0.getWheelRotation() < 0) {
			this.zoomIn = true;
		}
		else {
			this.zoomOut = true;
		}
	}
	
}
