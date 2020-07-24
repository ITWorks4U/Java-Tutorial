/**
 *	How to handle with exceptions in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import exception.GeometricException;

public class Example {
	
	public static void main(String... args) {
		Cube cube = null;
		Cube cube02;
		
		try {
			cube = new Cube(10, 20, 30);
			System.out.println(cube.calculateVolume());
			
			cube02 = new Cube(0, 20, 30);
			System.out.println(cube02.calculateVolume());
			
			
		} catch (NullPointerException npe) {
			System.out.println("exception occurred: " + npe);
			
			npe.printStackTrace();
			
		} catch (GeometricException ge) {
			System.err.println(ge.getErrormessage());
		
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		System.out.println("Hello!");
	}
}
