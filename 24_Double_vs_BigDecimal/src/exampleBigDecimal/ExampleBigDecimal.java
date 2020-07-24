/**
 *	How to differ a double class with big decimal class in Java.
 *
 *	@author 		ITWorks4U
 */
package exampleBigDecimal;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;

public class ExampleBigDecimal {
	
	public BigDecimal subtract(BigDecimal valueA, BigDecimal valueB) {
		return (valueA.subtract(valueB));
	}
	
	public BigDecimal aSimpleCalculation() {
		BigDecimal tmp = BigDecimal.ZERO;
		BigDecimal step = new BigDecimal(0.1);		//	"0.1" (String) differs to 0.1 (Double)
		
		for (int i = 0; i < 10; i++) {
			/*
			 * BigDecimal comes with an add() function,
			 * but it causes an error.
			 */
//			tmp.add(step);			
			tmp = add(tmp, step);
		}

		return tmp;
	}
	
	private BigDecimal add(BigDecimal a, BigDecimal b) {		
		return a.add(b);
	}
	
	public BigDecimal roundNumber(BigDecimal value) {
		return value.round(new MathContext(2, RoundingMode.HALF_EVEN));
	}
}
