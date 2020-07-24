/**
 *	How to use inheritance with our own defined class.
 *
 *	@author 		ITWorks4U
 */

package children;

public class Cube extends GeometricObject {	

	public Cube(double width, double height, double depth) {
//		this.width = width;
//		this.height = height;
//		this.depth = depth;
		
		super(width, height, depth);
	}
	
	public double calculateVolume() {				//	V = a * b * c
		return (width * height * depth);
	}
	
	public double calculateArea() {					//	V = a * b; it's not a square
		return (width * height);
	}
	
	public double calculateCircumference() {		//	u = 2 * (a + b)
		return 2 * (width + height);
	}
}
