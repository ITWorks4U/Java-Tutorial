/**
 *	How to use method overloading with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;

public class Pyramid extends GeometricObject {
	
	public Pyramid(double width, double height, double depth) {
		super(width, height, depth);
	}
	
	//	given methods below are used for a linear-square based pyramid
	
	@Override
	public double calculateVolume() {
		return ((1.0 / 3.0) * width * depth * height);		//	← where width is equal to depth | V = 1/3 * a² * h
	}
	
	@Override
	public double calculateArea() {
		return (width * depth);								//	A = a * b
	}
	
	public double calculateArea(int value) {
		return 0;
	}
	
	public double calculateArea(int value, int value02) {
		return 0;
	}
	
	public double calculateArea(String a) {		
		return 0;
	}

	@Override
	public EnumExample getTypeOfGeometricObject() {
		// TODO Auto-generated method stub
		return EnumExample.PYRAMID;
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

	@Override
	public double calculateCircumference() {
		return 0;
	}
}
