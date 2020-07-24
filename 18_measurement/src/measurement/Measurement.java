/**
 *	How to measure the time in Java.
 *
 *	@author 		ITWorks4U
 */
package measurement;

import iteration.CollatzIteration;
import recursion.CollatzRecursion;

public class Measurement {
	public static long counter = 0;										//	"global" counter to use
	private static final long NBR_TO_USE = 12345;						//	the number to use
	
	public static void main(String[] args) {
		CollatzIteration ci = new CollatzIteration();
		CollatzRecursion cr = new CollatzRecursion();
		
		/*	starting measurement	*/
		long startLoop = System.currentTimeMillis();
		ci.calculateCollatz(NBR_TO_USE);
		
		/*	stopping measurement	*/
		long stopLoop = System.currentTimeMillis();
		
		System.out.println(" By using iteration the program tooks " + (stopLoop - startLoop) + "ms.");
		
		System.out.println("*******************************************");
		
		/*	same action for recursion	*/
		long startRecursion = System.currentTimeMillis();
		cr.calculateCollatz(NBR_TO_USE);
		
		long stopRecursion = System.currentTimeMillis();
		
		System.out.println(" By using recursion the program tooks " + (stopRecursion - startRecursion) + "ms.");		
	}
}
