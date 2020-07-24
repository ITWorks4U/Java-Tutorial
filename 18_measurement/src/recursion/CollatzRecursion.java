/**
 *	How to measure the time in Java.
 *
 *	@author 		ITWorks4U
 */
package recursion;

import measurement.Measurement;

public class CollatzRecursion {
	public long calculateCollatz(long nbr) {
		if (nbr == 1) {
			Measurement.counter++;
			System.out.println(" Already done! Last counter is: " + Measurement.counter);
			Measurement.counter = 0;
			
			return nbr;
		}
		
		if (nbr % 2 == 0) {
			Measurement.counter++;
			System.out.println(" test: " + nbr);
			
			return calculateCollatz(nbr / 2);
		}
		
		Measurement.counter++;
		System.out.println(" test: " + nbr);
		return calculateCollatz(3 * nbr + 1);
		
	}
}
