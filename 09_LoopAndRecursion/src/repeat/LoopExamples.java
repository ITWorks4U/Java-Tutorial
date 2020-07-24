/**
 *	How to use loops in Java.
 *
 *	@author 		ITWorks4U
 */
package repeat;

public class LoopExamples {
	
	//	Repeat something by using loops
	public void repeatSomething(int limit, int array[]) {
		
		int ctr = 0;
		
		while(ctr < limit) {
			System.out.println(ctr);
			ctr++;
		}
		
		do {
			System.out.println(ctr);
			ctr--;
		} while(ctr != 0);
		
		for (int i = 0; i < limit; i++) {
			System.out.println(i);
		}
		
		for (int element : array) {
			System.out.println(element);
		}
	}

	public static void main(String[] args) {
		int arr[] = {9,21,87,12,0000, 99};
		
		LoopExamples le = new LoopExamples();
		le.repeatSomething(100, arr);	
		
		RecursionExample re = new RecursionExample();
		
		long val01 = 125;
		long val02 = 385;
		
		System.out.println("GCD (" + val01 + "," + val02 + ") = " + re.calculateGCD(val01, val02)); 
		
		System.out.println(re.calculatingFibonacci(10));
	}
}
