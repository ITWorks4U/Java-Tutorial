/**
 *	How to handle with generics in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import exception.GeometricException;
import singleton.BasicSingletonEnum;

public class Example {
	
	public static void main(String... args) {
		Cube<Double> cube = null;
		Pyramid<Double> pyramid = null;
		
		try {
			System.out.println("*************************************************");
			
			cube = new Cube<Double>(10.4, 20e2, 30.55);
			System.out.println("volume of the cube: " + cube.calculateVolume());
			System.out.println("area of the cube: " + cube.calculateArea());
			System.out.println("circumference of the cube: " + cube.calculateCircumference());
			
			System.out.println("*************************************************");
			
//			pyramid = new Pyramid<Double>(20, 20e2, 40.0);		//	to remember: you may not use integer values for given double on line 9
			pyramid = new Pyramid<Double>(20.0, 20.0, 40.0);
			System.out.println("volume of the pyramid: " + pyramid.calculateVolume());
			System.out.println("area of the pyramid: " + pyramid.calculateArea());
			System.out.println("circumference of the pyramid: " + pyramid.calculateCircumference());
			
			System.out.println("*************************************************");
			
			BasicSingletonEnum.INSTANCE.addObjectToList(cube);
			BasicSingletonEnum.INSTANCE.addObjectToList(pyramid);
			
			for (GeometricObject<?> tmp : BasicSingletonEnum.INSTANCE.getListOfObjects()) {
				System.out.println(tmp.getClass().getName());
			}
			
			System.out.println("*************************************************");
			
		} catch (NullPointerException npe) {
			System.out.println("exception occurred: " + npe);
			
			npe.printStackTrace();
			
		} catch (GeometricException ge) {
			System.err.println(ge.getErrormessage());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
