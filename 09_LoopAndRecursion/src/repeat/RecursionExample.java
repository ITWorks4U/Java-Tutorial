/**
 *	How to use a recursion in Java.
 *
 *	@author 		ITWorks4U
 */
package repeat;

public class RecursionExample {
	
	//	calculating the greatest common divisor by recursion
	public long calculateGCD(long a, long b) {
		if (b == 0) {
			return a;
		}		
		
		return calculateGCD(b, a % b);
	}
	
/*
 * 	This function below is equal to the given recursion function above.
 * 
	public long calculateGCD(long a, long b) {
		if (a < b) {
			long tmp = a;
			a = b;
			b = tmp;
		}
		
		long r = a % b;
		
		while (r > 0) {
			a = b;
			b = r;
			r = a % b;
		}
		
		return b;
	}
*/
	
	//	Calculating the sequence of Fibonacci
	public long calculatingFibonacci(long nbr) {
		if ((nbr == 0) || (nbr == 1)) {
			return 1;
		}
		
		return (calculatingFibonacci(nbr - 1) + calculatingFibonacci(nbr - 2));
	}
}
