import javax.imageio.ImageIO;
import javax.swing.*;

import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;


public abstract class Fractal extends JPanel{

	//Basic configuration
	protected double minRe=-2;
	protected double maxRe=2;
	protected double minIm=-1.6;
	protected double maxIm=1.6; // Could also do(to avoid image size dependency) maxIm = MinIm+(MaxRe-MinRe)*getHeight()/getWidth();
	protected int maxLevel=100; //presents the level of iterations

	//user selected point
	protected double clickedX=0.353;
	protected double clickedY=0.288;

	//Zooming
	protected boolean dragging=false;
	protected double xClicked, yClicked, xReleased, yReleased;
	int x0, y0, x1, y1;
	int width;
	int height;

	protected BufferedImage canvas;


	public Fractal(){	

	}

	public Fractal(double reMin, double reMax, double imMin, double imMax, int maxLevel){
		this.minRe=reMin;
		this.maxRe=reMax;
		this.minIm=imMin;
		this.maxIm=imMax;
		this.maxLevel=maxLevel;
	}

	public Fractal(int maxLevel){	
		this.maxLevel=maxLevel;
	}

	public void init(){
		this.setBackground(Color.white);
	}

	public void plotPoints(){}

	public void paintComponent(Graphics g){
		super.paintComponent(g);
		this.plotPoints();
		g.drawImage(canvas, 0, 0, this);

		if (this.dragging ) {

			drawZoomRectange(g);

		}

	}

	public void drawZoomRectange(Graphics g){
		g.setXORMode(Color.black);
		g.setColor(Color.white);
		g.drawRect(Math.min(x0, x1), Math.min(y0,y1), Math.abs(x1-x0),Math.abs(y1-y0)); //top x, top y, height, width
	}


	//inner class
	class MandelbrotListener implements ActionListener{

		TextField levelTf;
		TextField minReTf;
		TextField maxReTf;
		TextField minImTf;
		TextField maxImTf;
		JButton reset;
		JButton set;
		Fractal leftFractal;
		Fractal juliaSet;


		public MandelbrotListener(TextField level, TextField minRe, TextField maxRe, TextField minIm, TextField maxIm,  JButton reset, JButton set, Fractal rightFractal, Fractal leftFractal){
			this.levelTf=level;
			this.minReTf=minRe;
			this.maxReTf=maxRe;
			this.minImTf=minIm;
			this.maxImTf=maxIm;
			this.reset=reset;
			this.set=set;
			this.juliaSet=rightFractal;
			this.leftFractal=leftFractal;
		}


		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("set")){
				maxLevel=Integer.parseInt(levelTf.getText());
				minRe=Double.parseDouble(minReTf.getText());
				maxRe=Double.parseDouble(maxReTf.getText());
				minIm=Double.parseDouble(minImTf.getText());
				maxIm=Double.parseDouble(maxImTf.getText());
				juliaSet.maxLevel=maxLevel;
				juliaSet.repaint();
				repaint();
			} else if(e.getActionCommand().equals("reset")){
				minRe=-2;
				maxRe=2;
				minIm=-1.6;
				maxIm=1.6; 
				maxLevel=100;
				//put the values in the TextFields 
				maxImTf.setText(Double.toString(maxIm));
				minReTf.setText(Double.toString(minRe));
				maxReTf.setText(Double.toString(maxRe));
				minImTf.setText(Double.toString(minIm));
				levelTf.setText(Integer.toString(maxLevel));
				juliaSet.maxLevel=maxLevel;
				juliaSet.repaint();
				repaint();
			} else if(e.getActionCommand().equals("save as png")){ //save data into file
				//	Database save = new Database(juliaSet);
				//	save.writeInFile();

				String name= JOptionPane.showInputDialog("Please enter name: ");

				File outputfile = new File(name+".png");
				try {
					ImageIO.write(juliaSet.canvas, "png", outputfile);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			} else if (e.getActionCommand().equals("exit")) { System.exit(0);

			}

			else if (e.getActionCommand().equals("add to favourites")) { 
			Database save = new Database(juliaSet);
					save.writeInFile();
					JOptionPane.showMessageDialog(null,"The coordinates of the Julia Set have been sucessfully saved \n" +
							"to find all your favourite sets open saveFractals.txt file.","FAVOURITES",JOptionPane.PLAIN_MESSAGE);	
	//		ArrayList<String> keys = save.getNames();
	//		for(String s : keys){
	///			System.out.println(s);
	//		}
			try {
				HashMap<String, Complex> hm = save.readData();
				Iterator<Complex> it1 = hm.values().iterator();
				//now loop throug hashMap and print out keys and the number of times the word appears
			while(it1.hasNext()) {			
			Complex f = it1.next();
			System.out.println( f.getReal() +" : "+f.getImag() ); 
			}
				
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}		
					
			}else if(e.getActionCommand().equals("about")){
				JOptionPane.showMessageDialog(null,"For more information please contact \n if2g11@soton.ac.uk","ABOUT",JOptionPane.INFORMATION_MESSAGE);
			
			} else if(e.getActionCommand().equals("instructions")){
				JOptionPane.showMessageDialog(null,"Use mouse to zoom in, chose different options from the menu bar \n and use buttons at the bottom to render image.","INSTRUCTIONS",JOptionPane.PLAIN_MESSAGE);
			}




		}
	}

	public int getMaxLevel(){
		return this.maxLevel;
	}	
	public double getMinRe(){
		return this.minRe;
	}

	public double getMaxRe(){
		return this.maxRe;
	}
	public double getMinIm(){
		return this.minIm;
	}
	public double getMaxIm(){
		return this.maxIm;
	}


	class UserSelectedPointListener implements MouseListener, MouseMotionListener{

		TextField clickedNum;
		TextField currentNum;
		Fractal mpDispleyed;

		public UserSelectedPointListener (Fractal mpClicked, Fractal mpDispleyed, TextField clickedNum, TextField currentNum){
			this.clickedNum=clickedNum;
			this.mpDispleyed=mpDispleyed;
			this.currentNum=currentNum;

		}


		Complex userSelectedPoint;

		//get clicked complex number to display it in the box
		public void mouseClicked(MouseEvent e) {
			double cReal = minRe + e.getX()*(maxRe-minRe)/(getWidth());
			double cImag = minIm + e.getY()*(maxIm-minIm)/(getHeight());
			this.clickedNum.setText(Double.toString(cReal)+" + "+Double.toString(cImag)+"i " );

			//update the parameters below to draw corresponding julia set
			mpDispleyed.clickedX=cReal;
			mpDispleyed.clickedY=cImag;
			mpDispleyed.maxLevel=maxLevel;
			mpDispleyed.repaint();

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
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseReleased(MouseEvent arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void mouseDragged(MouseEvent e) {
			Point currentPoint = e.getPoint();


		}


		public void mouseMoved(MouseEvent e) {
			double cReal = minRe + e.getX()*(maxRe-minRe)/(getWidth());
			double cImag = minIm + e.getY()*(maxIm-minIm)/(getHeight());
			this.currentNum.setText(Double.toString(cReal)+" + "+Double.toString(cImag)+"i " );


			//update the parameters below to draw corresponding julia set
			mpDispleyed.clickedX=cReal;
			mpDispleyed.clickedY=cImag;
			mpDispleyed.maxLevel=maxLevel;
			mpDispleyed.repaint();


		}

	}


	class FractalZoom  implements MouseListener, MouseMotionListener{


		Fractal currentSet;
		TextField minReTf;
		TextField maxReTf;
		TextField minImTf;
		TextField maxImTf;

		public FractalZoom(Fractal currentSet){
			this.currentSet=currentSet;
		}
		
		public FractalZoom(Fractal currentSet, TextField minRe, TextField maxRe, TextField minIm, TextField maxIm) {
			this.currentSet=currentSet;
			this.minReTf=minRe;
			this.maxReTf=maxRe;
			this.minImTf=minIm;
			this.maxImTf=maxIm;
		}
		@Override
		public void mouseDragged(MouseEvent e) {
			x1= e.getX();
			y1=e.getY();
			//Now fix the ratio
			dragging = true;
			currentSet.repaint();
		}

		public void mousePressed(MouseEvent e) {
			if(isLeftMouseButtonPressed(e)){ //if left click
				xClicked = xReleased = minRe + e.getX()*(maxRe-minRe)/(getWidth());
				yClicked = yReleased = minIm + e.getY()*(maxIm-minIm)/(getHeight());
				x0=x1= e.getX();
				y0=y1= e.getY();
			} else { //if right click reset to original size
				minRe=-2;
				maxRe=2;
				minIm=-1.6;
				maxIm=1.6;
				currentSet.repaint();
			}

		}


		public void mouseReleased(MouseEvent e) {
			x0=x1=y1=y1=0; //this deleates the rectangle
			dragging = false; //sets the dragging of
			if(isLeftMouseButtonPressed(e)){
				xReleased = minRe + e.getX()*(maxRe-minRe)/(getWidth());
				yReleased = minIm + e.getY()*(maxIm-minIm)/(getHeight());
				if(xClicked != xReleased && yClicked != yReleased){
					currentSet.minRe=Math.min(xClicked, xReleased);
					currentSet.maxRe=Math.max(xClicked, xReleased);
					currentSet.maxIm = Math.max(yClicked, yReleased);
					currentSet.minIm=Math.min(yClicked, yReleased);
					//fixt the ratio
					height=width=Math.max(height, width);
					
					//update values in textfields
					maxImTf.setText(Double.toString(Math.max(yReleased, yClicked)));
					minReTf.setText(Double.toString(Math.min(xReleased, xClicked)));
					maxReTf.setText(Double.toString(Math.max(xReleased, xClicked)));
					minImTf.setText(Double.toString(Math.min(yReleased, yClicked)));
					
					currentSet.repaint();

				}
			}
		}

		boolean isLeftMouseButtonPressed(MouseEvent e){
			return (e.getModifiers() & InputEvent.BUTTON3_MASK) == 0;
		}


		public void mouseMoved(MouseEvent arg0) {}
		public void mouseClicked(MouseEvent arg0) {}
		public void mouseEntered(MouseEvent arg0) {}
		public void mouseExited(MouseEvent arg0) {}

	}



}//End MandelbrotSetPanel Panel






