/**
 *	How to handle with generics in Java.
 *
 *	@author 		ITWorks4U
 */
package noGeneric;

public class CalculatorDouble {
	private double number01;
	private double number02;

	public CalculatorDouble(double number01, double number02) {
		this.number01 = number01;
		this.number02 = number02;
	}
	
	public double addNumbers() {
		return (this.number01 + this.number02);
	}
	
	public double subtractNumbers() {
		return (this.number01 - this.number02);
	}
	
	public double multiplyNumbers() {
		return (this.number01 * this.number02);
	}

	public double divideNumbers() throws ArithmeticException {
		return (this.number01 / this.number02);
	}
}
