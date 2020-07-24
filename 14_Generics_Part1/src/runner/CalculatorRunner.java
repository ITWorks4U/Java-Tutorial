/**
 *	How to handle with generics in Java.
 *
 *	@author 		ITWorks4U
 */
package runner;

import calculator.CalculatorGenerics;

//import noGeneric.CalculatorDouble;
//import noGeneric.CalculatorInteger;

public class CalculatorRunner {
	public static void main(String[] args) {
//		CalculatorInteger ci = new CalculatorInteger(10, 15);								//	(10, 0) = ? 
//		CalculatorDouble cd = new CalculatorDouble(61.6465676334737, 877.9465343432432);	//	(61.6465676334737, 0) = ?
		
		CalculatorGenerics<Integer> cgi = new CalculatorGenerics<Integer>(10, 15);
		cgi.identifyClassType();
		
		CalculatorGenerics<Double> cgd = new CalculatorGenerics<Double>(61.6465676334737, 877.9465343432432);
		cgd.identifyClassType();
		
		/*	bound mismatch, because a String is not a part of a Number	*/
//		CalculatorGenerics<String> cgs = new CalculatorGenerics<String>("19", "31");
//		cgs.identifyClassType();
		
		
		try {
		System.out.println("*************************************************");
		
//		System.out.println(ci.addNumbers());
//		System.out.println(ci.subtractNumbers());
//		System.out.println(ci.multiplyNumbers());
//		System.out.println(ci.divideNumbers());
		
		System.out.println(cgi.addNumbers());
		System.out.println(cgi.subtractNumbers());
		System.out.println(cgi.multiplyNumbers());
		System.out.println(cgi.divideNumbers());
		
		System.out.println("*************************************************");
		
//		System.out.println(cd.addNumbers());
//		System.out.println(cd.subtractNumbers());
//		System.out.println(cd.multiplyNumbers());
//		System.out.println(cd.divideNumbers());
		
		System.out.println(cgd.addNumbers());
		System.out.println(cgd.subtractNumbers());
		System.out.println(cgd.multiplyNumbers());
		System.out.println(cgd.divideNumbers());
		
		System.out.println("*************************************************");
//		System.out.println(cgs.addNumbers(null, 41));	//	no longer in use		
		System.out.println("*************************************************");
		
		} catch (ArithmeticException ae) {
			ae.printStackTrace();
		} catch (IllegalArgumentException iae) {
			iae.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
