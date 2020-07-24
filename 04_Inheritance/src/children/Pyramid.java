
/**
 *	How to use inheritance with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

public class Pyramid extends GeometricObject {
	
	public Pyramid(double width, double height, double depth) {
		super(width, height, depth);
	}
	
	//	given methods below are used for a linear-square based pyramid
	public double calculateVolume() {
		return ((1.0 / 3.0) * width * depth * height);		//	← where width is equal to depth | V = 1/3 * a² * h
	}
	
	public double calculateArea() {
		return (width * depth);								//	A = a * b
	}
}
