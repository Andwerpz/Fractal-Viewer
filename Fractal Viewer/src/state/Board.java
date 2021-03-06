package state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import button.ButtonManager;
import button.SliderButton;
import main.MainPanel;
import util.ComplexNumber;
import util.MathTools;
import util.Vector;
import util.Point;

public class Board {
	
	ButtonManager bm;
	
	public int iterations = 50;	//how many iterations of the function per pixel
	
	public boolean mousePressed = false;
	public java.awt.Point mouse = new java.awt.Point(0, 0);
	public boolean zoomIn = false;
	public boolean zoomOut = false;
	public double zoomFactor = 1.2;
	
	
	public boolean mandelbrot = true;
	
	public boolean newtonRaphson = false;
	public double newtonRaphsonCushion = 0.001;	//how close do you have to get to a root to reach it
	public ArrayList<ComplexNumber> roots = new ArrayList<ComplexNumber>(Arrays.asList(new ComplexNumber(0, 0), new ComplexNumber(-0.5, Math.sqrt(3) / 2d), new ComplexNumber(-0.5, -Math.sqrt(3) / 2d), new ComplexNumber(-1.5, Math.sqrt(3) / 2d), new ComplexNumber(-1.5, -Math.sqrt(3) / 2d)));
	public ArrayList<Color> rootColors = new ArrayList<Color>(Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.CYAN));
	public ArrayList<ComplexNumber> function = new ArrayList<ComplexNumber>();
	public ArrayList<ComplexNumber> derivative = new ArrayList<ComplexNumber>();
	
	public boolean rootHeld = false;	//true if the user is dragging a root
	public int whichRootHeld = 0;
	public int rootHitboxRadius = 7;	//screen size of root
	
	public double xLow = -2;
	public double xHigh = 2;
	public double yLow = -2;
	public double yHigh = 2;

	public Board() {
		this.function = new ArrayList<ComplexNumber>();
		this.derivative = this.calculateDerivative(this.roots, this.function);
		
		this.bm = new ButtonManager();
		this.bm.addSliderButton(new SliderButton(200, 30, 200, 10, 1, 200, "Iterations"));	this.bm.sliderButtons.get(0).setVal(this.iterations);
	}
	
	public void tick(java.awt.Point mouse) {
		bm.tick(mouse);
	}
	
	public void draw(Graphics g, java.awt.Point mouse) {
		//mouse controls
		
		double xRange = xHigh - xLow;
		double yRange = yHigh - yLow;
		Vector curRealMouse = new Vector(xLow + (((double) this.mouse.x / (double) MainPanel.WIDTH) * xRange), yHigh - (((double) this.mouse.y / (double) MainPanel.HEIGHT) * yRange));
		
		if(this.mousePressed) {
			
			if(this.rootHeld) {
				this.roots.set(this.whichRootHeld, new ComplexNumber(curRealMouse.x, curRealMouse.y));
				this.function = new ArrayList<ComplexNumber>();
				this.derivative = this.calculateDerivative(this.roots, this.function);
			}
			else {
				Vector nextRealMouse = new Vector(xLow + (((double) mouse.x / (double) MainPanel.WIDTH) * xRange), yHigh - (((double) mouse.y / (double) MainPanel.HEIGHT) * yRange));
				
				Vector toNextRealMouse = new Vector((Point) curRealMouse, (Point) nextRealMouse);
				
				xLow -= toNextRealMouse.x;
				xHigh -= toNextRealMouse.x;
				yLow -= toNextRealMouse.y;
				yHigh -= toNextRealMouse.y;
			}
			
			
		}
		
		this.mouse.x = mouse.x;
		this.mouse.y = mouse.y;
		
		
		
		//drawing the fractal
		
		this.iterations = this.bm.sliderButtons.get(0).getVal();
		
		BufferedImage fractalImg = new BufferedImage(MainPanel.WIDTH, MainPanel.HEIGHT, BufferedImage.TYPE_INT_RGB);
		
		for(int i = 0; i < MainPanel.HEIGHT; i++) {
			for(int j = 0; j < MainPanel.WIDTH; j++) {
				double xCur = xLow + (((double) j / (double) MainPanel.WIDTH) * xRange);
				double yCur = yHigh - (((double) i / (double) MainPanel.HEIGHT) * yRange);
				
				Color curColor = Color.black;
				
				if(this.mandelbrot) {
					curColor = mandelbrotColor(xCur, yCur);
				}
				else if(this.newtonRaphson) {
					curColor = newtonRaphsonColor(xCur, yCur);
				}
				fractalImg.setRGB(j, i, curColor.getRGB());
			}
		}
		g.drawImage(fractalImg, 0, 0, null);
		
		//if newton-raphson, draw the roots
		if(this.newtonRaphson) {
			g.setColor(Color.WHITE);
			for(int i = 0; i < this.roots.size(); i++) {
				double screenX = ((this.roots.get(i).getRe() - xLow) / xRange) * (double) MainPanel.WIDTH;
				double screenY = ((-this.roots.get(i).getIm() + yHigh) / yRange) * (double) MainPanel.HEIGHT;
				
				g.fillOval((int) screenX - this.rootHitboxRadius, (int) screenY - this.rootHitboxRadius, this.rootHitboxRadius * 2, this.rootHitboxRadius * 2);
			}
		}
		
		
		
		//doing zoom in or out
		
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
		g.fillRect(0, 0, MainPanel.WIDTH, 80);
		g.setColor(Color.BLACK);
		g.drawString("View Width: " + xRange, 20, 20);
		g.drawString("Mouse Position:", 20, 35);
		g.drawString("X: " + curRealMouse.x, 20, 50);
		g.drawString("Y: " + curRealMouse.y, 20, 65);
		
		bm.draw(g);
	}
	
	public ArrayList<ComplexNumber> calculateDerivative(ArrayList<ComplexNumber> roots, ArrayList<ComplexNumber> outFunction) {
		
		
		
		ArrayList<ComplexNumber> derivative = new ArrayList<ComplexNumber>();
		
		//calculate derivative of the original equation
		//first we must expand the equation from (x - r1)(x - r2) ... (x - rn) to ax^0 + bx^1 + cx^2 ... nx^(n - 1). 
		
		derivative.add(new ComplexNumber(1, 0));
		for(int i = 0; i < this.roots.size(); i++) {
			ComplexNumber nextRoot = new ComplexNumber(this.roots.get(i));
			nextRoot.multiply(new ComplexNumber(-1, 0));
			ArrayList<ComplexNumber> powerUp = new ArrayList<ComplexNumber>();
			ArrayList<ComplexNumber> multiple = new ArrayList<ComplexNumber>();
			for(int j = 0; j < derivative.size(); j++) {
				powerUp.add(new ComplexNumber(derivative.get(j)));
				multiple.add(ComplexNumber.multiply(derivative.get(j), nextRoot));
			}
			derivative.add(new ComplexNumber());
			derivative.set(0, new ComplexNumber());
			for(int j = 0; j < powerUp.size(); j++) {
				derivative.set(j + 1, powerUp.get(j));
			}
			for(int j = 0; j < multiple.size(); j++) {
				derivative.get(j).add(multiple.get(j));
			}
		}
		
		//copy the function to output
		this.function = new ArrayList<ComplexNumber>();
		for(int i = 0; i < derivative.size(); i++) {
			this.function.add(new ComplexNumber(derivative.get(i)));
		}
		
		//System.out.println("FUNC: " + outFunction);
		
		//now with the expanded form, we can easily calculate the derivative. Just use power rule
		
		for(int i = 0; i < derivative.size(); i++) {
			if(i == 0) {
				continue;
			}
			else {
				derivative.set(i - 1, ComplexNumber.multiply(derivative.get(i), new ComplexNumber(i, 0)));
			}
		}
		
		derivative.remove(derivative.size() - 1);
		
//		System.out.println("F: " + this.function);
//		System.out.println("D: " + derivative);
		
		return derivative;
	}
	
	public ComplexNumber calculateOutput(ArrayList<ComplexNumber> function, ComplexNumber in) {
		ComplexNumber ans = new ComplexNumber();
		for(int i = function.size() - 1; i >= 0; i--) {
			ans.multiply(in);
			ans.add(function.get(i));
			//System.out.println("ANS : " + ans);
		}
		return ans;
	}
	
	public Color newtonRaphsonColor(double real, double imaginary) {
		ComplexNumber point = new ComplexNumber(real, imaginary);
		
		//check if the point is already close enough to a root
		for(int i = 0; i < this.roots.size(); i++) {
			double dist = MathTools.dist(real, imaginary, this.roots.get(i).getRe(), this.roots.get(i).getIm());
			if(dist < this.newtonRaphsonCushion) {
				return this.rootColors.get(i);
			}
		}
		
		//do iterations first
		for(int i = 0; i < this.iterations; i++) {
			ComplexNumber funcOut = this.calculateOutput(this.function, point);
			ComplexNumber derivativeOut = this.calculateOutput(this.derivative, point);
			funcOut.divide(derivativeOut);
			point.subtract(funcOut);
			
			//check if point is close enough to root
			for(int j = 0; j < this.roots.size(); j++) {
				double dist = MathTools.dist(point.getRe(), point.getIm(), this.roots.get(j).getRe(), this.roots.get(j).getIm());
				if(dist < this.newtonRaphsonCushion) {
					double darknessRatio = 1d - ((double) i / (double) this.iterations);
					return new Color((int) (this.rootColors.get(j).getRed() * darknessRatio), (int) (this.rootColors.get(j).getGreen() * darknessRatio), (int) (this.rootColors.get(j).getBlue() * darknessRatio));
				}
			}
		}
		
		//no root has been reached, return black
		return Color.black;
		
	}
	
	public Color mandelbrotColor(double real, double imaginary) {
		
		ComplexNumber c = new ComplexNumber(real, imaginary);
		ComplexNumber out = new ComplexNumber(0, 0);
		
		for(int i = 0; i < this.iterations; i++) {
			//System.out.println(c.mod() + " : " + out);
			
			if(Math.abs(out.mod()) > 2) {
				return new Color((int) (((double) i / (double) this.iterations) * 255), (int) (((double) i / (double) this.iterations) * 255), 255);
			}
			//System.out.println("BEFORE: " + out);
			out = out.square();
			//System.out.println("AFTER: " + out);
			out.add(c);
			
		}
	
		return Color.BLACK;
		
	}
	
	public void mousePressed(MouseEvent arg0) {
		bm.pressed(arg0);
		if(bm.getPressed(arg0)) {
			return;
		}
		this.mousePressed = true;
		
		//if newton-raphson, then check if the mouse was pressed over a root
		double xRange = xHigh - xLow;
		double yRange = yHigh - yLow;
		for(int i = 0; i < this.roots.size(); i++) {
			double screenX = ((this.roots.get(i).getRe() - xLow) / xRange) * (double) MainPanel.WIDTH;
			double screenY = ((-this.roots.get(i).getIm() + yHigh) / yRange) * (double) MainPanel.HEIGHT;
			
			if(MathTools.dist(screenX, screenY, (double) arg0.getX(), (double) arg0.getY()) < (double) this.rootHitboxRadius) {
				this.rootHeld = true;
				this.whichRootHeld = i;
			}
		}
	}
	
	public void mouseReleased() {
		bm.mouseReleased();
		this.mousePressed = false;
		this.rootHeld = false;
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
