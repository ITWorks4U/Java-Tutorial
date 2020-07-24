/**
 *	How to use JUnit for our own defined classes.
 *
 *	@author 		ITWorks4U
 */
package children;

import exception.GeometricException;
import singleton.BasicSingletonEnum;

public class Example {
	
	public static void main(final String... args) {
		Cube<Double> cube = null;
		Pyramid<Double> pyramid = null;
		Tetrahedron<Double> tetra = null;
		
		final int value = 5;
		
//		args = new String[2];		//	since args is constant, you're not allowed to modify args
		
		System.out.println("value = " + value);
		
		try {
			System.out.println("*************************************************");
			
			cube = new Cube<Double>(10.4, 20e2, 30.55);
			System.out.println("volume of the cube: " + cube.calculateVolume());
			System.out.println("area of the cube: " + cube.calculateArea());
			System.out.println("circumference of the cube: " + cube.calculateCircumference());
			
			System.out.println("*************************************************");

			pyramid = new Pyramid<Double>(20.0, 20.0, 40.0);
			System.out.println("volume of the pyramid: " + pyramid.calculateVolume());
			System.out.println("area of the pyramid: " + pyramid.calculateArea());
			System.out.println("circumference of the pyramid: " + pyramid.calculateCircumference());
			
			System.out.println("*************************************************");
			
			tetra = new Tetrahedron<Double>(20.0, 20.0, 40.0);
			System.out.println("volume of the tetra: " + tetra.calculateVolume());
			System.out.println("area of the tetra: " + tetra.calculateArea());
			System.out.println("circumference of the tetra: " + tetra.calculateCircumference());
			
			System.out.println("*************************************************");
			
			BasicSingletonEnum.INSTANCE.addObjectToList(cube);
			BasicSingletonEnum.INSTANCE.addObjectToList(pyramid);
			BasicSingletonEnum.INSTANCE.addObjectToList(tetra);
			
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
