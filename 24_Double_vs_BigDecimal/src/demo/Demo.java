/**
 *	How to differ a double class with big decimal class in Java.
 *
 *	@author 		ITWorks4U
 */
package demo;

import java.math.BigDecimal;

import exampleBigDecimal.ExampleBigDecimal;
import exampleDouble.ExampleDouble;

public class Demo {	
	public static void main(String[] args) {
		double valueA_D = 0.03;
		double valueB_D = 0.02;
		
		BigDecimal valueA_BD = new BigDecimal(valueA_D);
		BigDecimal valueB_BD = new BigDecimal(valueB_D);
		
		ExampleDouble ed = new ExampleDouble();
		ExampleBigDecimal ebd = new ExampleBigDecimal();
		
		System.out.println("using double: " + valueA_D + " - " + valueB_D + " = " + ed.subtract(valueA_D, valueB_D));		
		System.out.println("using BigDecimal: " + valueA_BD + " - " + valueB_BD + " = " + ebd.subtract(valueA_BD, valueB_BD));
		
		/*****************************************************************/
		
		System.out.println("\n***************************************************\n");
		
		System.out.println("0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 = " + ed.aSimpleCalculation());
		System.out.println("0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 + 0.1 = " + ebd.aSimpleCalculation());
		
		/*****************************************************************/
		
		final BigDecimal CRITICAL_VALUE = new BigDecimal("0.6149999999999999911182158029987476766109466552734375");
		final BigDecimal ROUNDED_VALUE = new BigDecimal("0.615");		
		
		System.out.println("\n***************************************************\n");
		
		Double roundedResult_D = ed.roundNumber(CRITICAL_VALUE.doubleValue());
		
		System.out.println("values rounded by two digits:");
		System.out.printf("%s\n", roundedResult_D.toString());
		System.out.println(roundedResult_D + " and " + ROUNDED_VALUE + " are equal: " + (roundedResult_D == ROUNDED_VALUE.doubleValue()));
		
		System.out.println("\n***************************************************\n");
		
		BigDecimal roundedResult_BD = CRITICAL_VALUE;
		System.out.println("values rounded by two digits:");
		System.out.printf("%s\n", roundedResult_BD.toString());
		System.out.println(roundedResult_BD.doubleValue() + " and " + ROUNDED_VALUE + " are equal: " + (roundedResult_BD.doubleValue() == ROUNDED_VALUE.doubleValue()));
	}
}
