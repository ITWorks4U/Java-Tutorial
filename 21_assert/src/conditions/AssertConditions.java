/**
 *	How to use the assert command in Java. 
 *
 *	@author 		ITWorks4U
 */
package conditions;

import org.junit.jupiter.api.Assertions;

/**
 * @author itworks4u
 *
 */
public class AssertConditions {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		boolean condition = (10 > 4 && false);
		
		
//		assert (condition == true);
		
		System.out.println(condition);				//	prints "false"
		
		Assertions.assertTrue(condition);
	}
}
