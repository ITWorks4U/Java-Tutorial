/**
 *	How to handle with exceptions in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;

public class Cube extends GeometricObject {	

	public Cube(double width, double height, double depth) throws Exception {		
		super(width, height, depth);
	}
	
	public double calculateVolume() {				//	V = a * b * c
		return (width * height * depth);
	}
	
	public double calculateArea() {					//	V = a * b; it's not a square
		return (width * height);
	}
	
	@Override
	public double calculateCircumference() {		//	u = 2 * (a + b)
		return 2 * (width + height);
	}

	@Override
	public EnumExample getTypeOfGeometricObject() {
		// TODO Auto-generated method stub
		return EnumExample.CUBE;
	}

	@Override
	public double getWidth() {
		return this.width;
	}

	@Override
	public double getHeight() {
		return this.height;
	}

	@Override
	public double getDepth() {
		return this.depth;
	}
}
