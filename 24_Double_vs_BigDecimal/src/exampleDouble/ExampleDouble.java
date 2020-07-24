/**
 *	How to differ a double class with big decimal class in Java.
 *
 *	@author 		ITWorks4U
 */
package exampleDouble;

public class ExampleDouble {
	
	public double subtract(double valueA, double valueB) {
		return (valueA - valueB);
	}
	
	public double aSimpleCalculation() {
		double tmp = 0.0;
		
		while (tmp < 1.0) {
			tmp += 0.1;
		}
		
		return tmp;
	}
	
	public double roundNumber(double value) {
		long roundedValue = Math.round(1000.0 * value);
		double tmp = 0.1 * roundedValue;
		
		return (Math.rint(tmp) / 100.0);
		
		/*	Math.rint(double a)
		 * 
		 * 	Returns the double value that is closest in value to the argument and is equal to a mathematical integer.
		 */
	}
}
