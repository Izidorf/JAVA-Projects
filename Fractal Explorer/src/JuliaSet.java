import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;


public class JuliaSet extends Fractal {

	
	public JuliaSet(){	

	}
	
	public JuliaSet(double xClicked, double yClicked){	
		this.clickedX=xClicked;
		this.yClicked=yClicked;
	}

	public void plotPoints(){
		int width = getWidth();
		int height = getHeight();
		canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		//Paint every pixel on the graphics area of the screen
		//First for width
		for(int x=0; x<width; x++){		
			double cReal = minRe + x*(maxRe-minRe)/(width); //calculate cReal for every coordinate here to gain efficiency
			//Then for height
			for(int y=0; y<height; y++){

				//To calculate a complex number for every single coordinate.
				
				double cImag = minIm + y*(maxIm-minIm)/(height);

				//Complex C = new Complex(cReal, cImag);
				Complex complex = new Complex(cReal,cImag);
				Complex Z = new Complex(clickedX, clickedY); //To calculate julia set for user selected point pass in the coordinates


				int iter = maxLevel;
				while(complex.modulusSquared()<4 && 0 < iter){
					complex = complex.square().add(Z); //Z = Z + contant
					iter--;
				}
				Color c;
				if(iter <= 0){
					// c = Color.black;
				} else {
					// c = Color.red;
					//Normalized Iteration Count Algorithm  
					 iter += (float)Math.log(Math.log(complex.modulusSquared())/2f)/(float)Math.log(2);
				}
				 c = Color.getHSBColor((float)(maxLevel)/(iter+1f)*2f, 1f, (iter+1)/(float)(maxLevel+1));
				canvas.setRGB( x, y, c.getRGB());
			}
		}

	}


}