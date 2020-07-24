/**
 *	How to use interfaces with our own defined class.
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
		System.out.println("\n ***** GEOMETRIC OBJECT *****\n");
		System.out.println("geometric object's depth = " + go.getDepth());
		System.out.println("geometric object's height = " + go.getHeight());
		System.out.println("geometric object's width = " + go.getWidth());
		
		//	outputs for Cube
		System.out.println("\n ***** CUBE *****\n");
		System.out.println("area = " + cube.calculateArea());
		System.out.println("volume = " + cube.calculateVolume());
		System.out.println("circumference = " + cube.calculateCircumference());
		
		System.out.println("cube width: " + cube.getWidth());
		System.out.println("cube height: " + cube.getHeight());
		System.out.println("cube depth: " + cube.getDepth());
		
		System.out.println("geometric object's depth = " + go.getDepth());
		System.out.println("geometric object's height = " + go.getHeight());
		System.out.println("geometric object's width = " + go.getWidth());
		
		//	outputs for Pyramid
		System.out.println("\n ***** PYRAMID *****\n");
		System.out.println("area = " + pyramid.calculateArea());
		System.out.println("volume = " + pyramid.calculateVolume());
	}
}
