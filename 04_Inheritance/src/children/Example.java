/**
 *	How to use inheritance with our own defined class.
 *
 *	@author 		ITWorks4U
 */

package children;

public class Example {

	public static void main(String[] args) {
		GeometricObject go = new GeometricObject(5, 5, 5);
		Cube cube = new Cube(3, 6, 9);
		Pyramid pyramid = new Pyramid(10, 10, 22);
		
		//	outputs for GeometricObject
		System.out.println("depth = " + go.getDepth());
		System.out.println("height = " + go.getHeight());
		System.out.println("width = " + go.getWidth());
		
		//	outputs for Cube
		System.out.println("area = " + cube.calculateArea());
		System.out.println("volume = " + cube.calculateVolume());
		System.out.println("circumference = " + cube.calculateCircumference());
		
		//	outputs for Pyramid
		System.out.println("area = " + pyramid.calculateArea());
		System.out.println("volume = " + pyramid.calculateVolume());
	}
}
