/**
 *	How to use interfaces with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

public class Cube extends GeometricObject {	

	public Cube(double width, double height, double depth) {		
		super(width, height, depth);
	}
	
	@Override
	public double calculateVolume() {				//	V = a * b * c
		return (width * height * depth);
	}
	
	@Override
	public double calculateArea() {					//	V = a * b; it's not a square
		return (width * height);
	}
	
	@Override
	public double calculateCircumference() {		//	u = 2 * (a + b)
		return 2 * (width + height);
	}	
}
