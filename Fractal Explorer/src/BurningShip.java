import java.awt.Color;
import java.awt.image.BufferedImage;


public class BurningShip extends Fractal{
	
	public void plotPoints(){
		int width = getWidth();
		int height = getHeight();
		canvas = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);

		//Paint every pixel on the graphics area of the screen
		//First for width
		for(int x=0; x<width; x++){			
			//Then for height
			for(int y=0; y<height; y++){

				//To calculate a complex number for every single coordinate.
				double cReal = minRe + x*(maxRe-minRe)/(width);
				double cImag = minIm + y*(maxIm-minIm)/(height);

				Complex C = new Complex(cReal, cImag);
				Complex complex = new Complex(cReal,cImag);
			

				//Now calculate the set
				int iter = maxLevel;
				while(complex.modulusSquared()<4 && 0 < iter){
					
					complex.setImag(Math.abs(complex.getImag()));
					complex.setReal(Math.abs(complex.getReal()));
					complex = complex.square().add(C); //Z = Z + C
					
					iter--;
				}
			//The line above presents an extremenly	
				canvas.setRGB( x, y, iter | (iter << 20));
				Color c;
			/*	if(iter <= 0){
					// c = Color.black;
				} else {
					// c = Color.red;
					//Normalized Iteration Count Algorithm  
					 iter += (float)Math.log(Math.log(complex.modulusSquared())/2f)/(float)Math.log(2);
				}
				 c = Color.getHSBColor((float)(maxLevel)/(iter+1f)*2f, 1f, (iter+1)/(float)(maxLevel+1));
				canvas.setRGB( x, y, c.getRGB());
			}*/
		}
		}
	}

}
