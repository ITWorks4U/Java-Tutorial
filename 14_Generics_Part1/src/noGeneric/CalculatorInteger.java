/**
 *	How to handle with generics in Java.
 *
 *	@author 		ITWorks4U
 */
package noGeneric;

public class CalculatorInteger {
	private int number01;
	private int number02;

	public CalculatorInteger(int number01, int number02) {
		this.number01 = number01;
		this.number02 = number02;
	}
	
	public int addNumbers() {
		return (this.number01 + this.number02);
	}
	
	public int subtractNumbers() {
		return (this.number01 - this.number02);
	}
	
	public int multiplyNumbers() {
		return (this.number01 * this.number02);
	}

	public int divideNumbers() throws ArithmeticException {
		return (this.number01 / this.number02);
	}
}
