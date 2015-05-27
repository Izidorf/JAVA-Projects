public class Complex {
	
	private double real;
	private double imag;
	
	public Complex() {
		this.real=0;
		this.imag=0;
	}
	
	public Complex(double real, double imag) {
		this.real=real;
		this.imag=imag;
	}

	public Complex square() {
	//	double re = this.getReal()*this.getReal()-2*this.getImag()*this.getImag();
		//this.setReal(this.getReal()*this.getReal()-this.getImag()*this.getImag()); 
	  //  this.setImag(2*this.getImag()*this.getReal());
		return new Complex(this.getReal()*this.getReal()-this.getImag()*this.getImag(), 2*this.getImag()*this.getReal());
		
	}
	
	//return modulus.. the positive square root of the sum of the squares of the real and imaginary parts of a complex number.
	public double modulusSquared() { 
		double result = this.getReal()*this.getReal()+this.getImag()*this.getImag();	
		return result;
	}
	
	//adds two complex numbers and stores result in this complex number eg. a.add(d) stores result in a
	public Complex add(Complex d) {
		double imagSum =this.getImag()+ d.getImag();
		double realSum = this.getReal()+d.getReal();
		Complex result = new Complex(realSum, imagSum);
		return result;
	}
	
	public double getReal() {
		return real;
	}
	
	public double getImag() {
		return imag;
	}
	
	public void setReal(double real) {
		this.real = real;
	}
	
	public void setImag(double imag) {
		this.imag = imag;
	}
}
