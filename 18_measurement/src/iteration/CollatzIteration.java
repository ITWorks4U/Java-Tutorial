/**
 *	How to measure the time in Java.
 *
 *	@author 		ITWorks4U
 */
package iteration;

import measurement.Measurement;

public class CollatzIteration {
	public long calculateCollatz(long nbr) {
		while (nbr >= 1) {
			if (nbr == 1) {
				Measurement.counter++;
				System.out.println(" Already done! Last counter is: " + Measurement.counter);
				Measurement.counter = 0;
				break;
				
			} else if (nbr % 2 == 0) {
				Measurement.counter++;
				System.out.println(" test: " + nbr);
				nbr /= 2;										//	equal to: nbr = nbr / 2;
				
			} else {
				Measurement.counter++;
				System.out.println(" test: " + nbr);
				nbr = (3 * nbr + 1);
			}
		}
		
		return nbr;
	}
}
