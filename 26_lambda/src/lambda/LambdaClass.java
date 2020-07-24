/**
 *	How to use lambda expressions in Java.
 *
 *	@author 		ITWorks4U
 */
package lambda;

public class LambdaClass<L> {

	public static void main(String[] args) {
		LambdaClass<Integer> lambdaClass = new LambdaClass<Integer>();
		
		LambdaMath<Integer> addition = (Integer a, Integer b) -> (a + b);
		LambdaMath<Integer> subtraction = (Integer a, Integer b) -> (a - b);
		
		int a = 10;
		int b = 5;
		
		System.out.println(" a + b = " + lambdaClass.operateWithLambda(a, b, addition));
		System.out.println(" a - b = " + lambdaClass.operateWithLambda(a, b, subtraction));
		
		LambdaClass<Double> lambdaDouble = new LambdaClass<Double>();
		LambdaMath<Double> multiplyDouble = (Double c, Double d) -> (c * d);
		LambdaMath<Double> divideWithTrigonometry = (Double c, Double d) -> (Math.sin(c) / Math.cos(d));
		
		final double c = 3.0;
		final double d = Math.PI;
		
		System.out.println(c + " * " + d + " = " + lambdaDouble.operateWithLambda(c, d, multiplyDouble));
		System.out.println("sin(" + c + ")" + "/" + "cos(" + d + ") = " + lambdaDouble.operateWithLambda(c, d, divideWithTrigonometry));
	}
	
	public L operateWithLambda(L a, L b, LambdaMath<L> operation) {
		return operation.doOperation(a, b);
	}
}
