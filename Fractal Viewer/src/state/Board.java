package state;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;

import main.MainPanel;
import util.ComplexNumber;
import util.MathTools;
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
	
	public boolean newtonRaphson = true;
	public int newtonRaphsonIterations = 5;
	public ArrayList<ComplexNumber> roots = new ArrayList<ComplexNumber>(Arrays.asList(new ComplexNumber(-1, -1), new ComplexNumber(-1, 1), new ComplexNumber(-2, 0), new ComplexNumber(1, 1), new ComplexNumber(1, -1)));
	public ArrayList<Color> rootColors = new ArrayList<Color>(Arrays.asList(Color.RED, Color.BLUE, Color.GREEN, Color.PINK, Color.CYAN));
	public ArrayList<ComplexNumber> function = new ArrayList<ComplexNumber>();
	public ArrayList<ComplexNumber> derivative = new ArrayList<ComplexNumber>();
	
	public boolean rootHeld = false;	//true if the user is dragging a root
	public int whichRootHeld = 0;
	public int rootHitboxRadius = 5;	//screen size of root
	
	public double xLow = -2;
	public double xHigh = 2;
	public double yLow = -2;
	public double yHigh = 2;

	public Board() {
		this.function = new ArrayList<ComplexNumber>();
		this.derivative = this.calculateDerivative(this.roots, this.function);
	}
	
	public void tick(java.awt.Point mouse) {

	}
	
	public void draw(Graphics g, java.awt.Point mouse) {
		//mouse controls
		
		double xRange = xHigh - xLow;
		double yRange = yHigh - yLow;
		Vector curRealMouse = new Vector(xLow + (((double) this.mouse.x / (double) MainPanel.WIDTH) * xRange), yHigh - (((double) this.mouse.y / (double) MainPanel.HEIGHT) * yRange));
		
		if(this.mousePressed) {
			
			if(this.rootHeld) {
				this.roots.set(this.whichRootHeld, new ComplexNumber(curRealMouse.x, curRealMouse.y));
				this.calculateDerivative(this.roots, this.function);
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
		for(int i = 0; i < this.roots.size(); i++) {
			double screenX = ((this.roots.get(i).getRe() - xLow) / xRange) * (double) MainPanel.WIDTH;
			double screenY = ((-this.roots.get(i).getIm() + yHigh) / yRange) * (double) MainPanel.HEIGHT;
			
			g.drawOval((int) screenX - this.rootHitboxRadius, (int) screenY - this.rootHitboxRadius, this.rootHitboxRadius * 2, this.rootHitboxRadius * 2);
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
		g.fillRect(0, 0, 250, 80);
		g.setColor(Color.BLACK);
		g.drawString("View Width: " + xRange, 20, 20);
		g.drawString("Mouse Position:", 20, 35);
		g.drawString("X: " + curRealMouse.x, 20, 50);
		g.drawString("Y: " + curRealMouse.y, 20, 65);
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
		
		//System.out.println("D: " + derivative);
		
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
		
		//System.out.println("BEFORE: " + real + " " + imaginary);
		
		//do iterations first
		for(int i = 0; i < this.newtonRaphsonIterations; i++) {
			//System.out.println("TERATE");
			ComplexNumber funcOut = this.calculateOutput(this.function, point);
			ComplexNumber derivativeOut = this.calculateOutput(this.derivative, point);
			funcOut.divide(derivativeOut);
			//System.out.println("FUNC OUT: " + funcOut);
			point.subtract(funcOut);
		}
		
		//System.out.println("AFTER: " + point.getRe() + " " + point.getIm());
		
		//check which root this point is closest to, and return the corresponding color.
		double dist = MathTools.dist(point.getRe(), point.getIm(), this.roots.get(0).getRe(), this.roots.get(0).getIm());
		Color ans = this.rootColors.get(0);
		for(int i = 1; i < this.roots.size(); i++) {
			double nextDist = MathTools.dist(point.getRe(), point.getIm(), this.roots.get(i).getRe(), this.roots.get(i).getIm());
			if(nextDist < dist) {
				ans = this.rootColors.get(i);
				dist = nextDist;
			}
		}
		
		return ans;
		
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
