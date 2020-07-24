/**
 *	How to handle with generics in Java.
 *
 *	@author 		ITWorks4U
 */
package calculator;

/*	"<T>" defines an acronym value, which allows you define a class or a function or a variable by given type T.
 * 
 * 	In Java you're also able to add conditions for given type T. This class below is available for
 * 	each type T, if and only if, T derives from the given class Number (java.lang.Number).
 * 
 * 	In contrast to C++, Java doesn't allow to use operators, therefore you can't use operators
 * 	in combination with generics, however, the implementations below are valid for
 * 	arithmetical operations in combination with generics. Have fun!
 */
public class CalculatorGenerics<T extends Number> {
	private T number01;
	private T number02;

	public CalculatorGenerics(T number01, T number02) {
		this.number01 = number01;
		this.number02 = number02;
	}
	
	/*	old code */
//	public T addNumbers() {
//		return (this.number01 + this.number02);
//	}
	
	/*	new code implementation	*/
	public Number addNumbers() throws IllegalArgumentException {
		
		if (number01 instanceof Byte && number02 instanceof Byte) {					//	for any byte type
			return (number01.byteValue() + number02.byteValue());
		}
		
		if (number01 instanceof Short && number02 instanceof Short) {				//	for any short type
			return (number01.shortValue() + number02.shortValue());
		}
		
		if (number01 instanceof Integer && number02 instanceof Integer) {			//	for any int type
			return (number01.intValue() + number02.intValue());
		}
		
		if (number01 instanceof Long && number02 instanceof Long) {					//	for any long type
			return (number01.longValue() + number02.longValue());
		}
				
		if (number01 instanceof Float && number02 instanceof Float) {				//	for any float type
			return (number01.floatValue() + number02.floatValue());
		}
		
		if (number01 instanceof Double && number02 instanceof Double) {				//	for any double type
			return (number01.doubleValue() + number02.doubleValue());
		}
		
		throw new IllegalArgumentException("invalid argument(s) detected...");
	}	
	
//	public T subtractNumbers() {
//		return (this.number01 - this.number02);
//	}
	
	public Number subtractNumbers() throws IllegalArgumentException {
		
		if (number01 instanceof Byte && number02 instanceof Byte) {
			return (number01.byteValue() - number02.byteValue());
		}
		
		if (number01 instanceof Short && number02 instanceof Short) {
			return (number01.shortValue() - number02.shortValue());
		}
		
		if (number01 instanceof Integer && number02 instanceof Integer) {
			return (number01.intValue() - number02.intValue());
		}
		
		if (number01 instanceof Long && number02 instanceof Long) {
			return (number01.longValue() - number02.longValue());
		}
				
		if (number01 instanceof Float && number02 instanceof Float) {
			return (number01.floatValue() - number02.floatValue());
		}
		
		if (number01 instanceof Double && number02 instanceof Double) {
			return (number01.doubleValue() - number02.doubleValue());
		}
		
		throw new IllegalArgumentException("invalid argument(s) detected...");
	}	
	
//	public T multiplyNumbers() {
//		return (this.number01 * this.number02);
//	}
	
	public Number multiplyNumbers() throws IllegalArgumentException {
		
		if (number01 instanceof Byte && number02 instanceof Byte) {
			return (number01.byteValue() * number02.byteValue());
		}
		
		if (number01 instanceof Short && number02 instanceof Short) {
			return (number01.shortValue() * number02.shortValue());
		}
		
		if (number01 instanceof Integer && number02 instanceof Integer) {
			return (number01.intValue() * number02.intValue());
		}
		
		if (number01 instanceof Long && number02 instanceof Long) {
			return (number01.longValue() * number02.longValue());
		}
				
		if (number01 instanceof Float && number02 instanceof Float) {
			return (number01.floatValue() * number02.floatValue());
		}
		
		if (number01 instanceof Double && number02 instanceof Double) {
			return (number01.doubleValue() * number02.doubleValue());
		}
		
		throw new IllegalArgumentException("invalid argument(s) detected...");
	}

//	public T divideNumbers() throws ArithmeticException {
//		return (this.number01 / this.number02);
//	}
	
	public Number divideNumbers() throws IllegalArgumentException, ArithmeticException {
		
		if (number01 instanceof Byte && number02 instanceof Byte) {
			return (number01.byteValue() / number02.byteValue());
		}
		
		if (number01 instanceof Short && number02 instanceof Short) {
			return (number01.shortValue() / number02.shortValue());
		}
		
		if (number01 instanceof Integer && number02 instanceof Integer) {
			return (number01.intValue() / number02.intValue());
		}
		
		if (number01 instanceof Long && number02 instanceof Long) {
			return (number01.longValue() / number02.longValue());
		}
				
		if (number01 instanceof Float && number02 instanceof Float) {
			return (number01.floatValue() / number02.floatValue());
		}
		
		if (number01 instanceof Double && number02 instanceof Double) {
			return (number01.doubleValue() / number02.doubleValue());
		}
		
		throw new IllegalArgumentException("invalid argument(s) detected...");
	}
	
	public void identifyClassType() {
		System.out.println("Value of \"number01\": " + number01);
		System.out.println("Value of \"number02\": " + number02 + "\n");
		
		System.out.println("class is type of: " + number01.getClass().getName() + "\n\n");
	}
}
