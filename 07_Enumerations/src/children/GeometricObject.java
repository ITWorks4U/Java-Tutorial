/**
 *	How to use an enumeration with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;
import interfaces.GeometricFunctions;

public class GeometricObject implements GeometricFunctions {
	
	/*	properties to use	*/
	protected double width;
	protected double height;
	protected double depth;
	
	/*	default constructor	*/
	public GeometricObject() {
		this.width = 0;
		this.height = 0;
		this.depth = 0;
	}
	
	/*	extended constructor to initialize the properties	*/
	public GeometricObject(double width, double height, double depth) {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	@Override
	public double getWidth() {
		// TODO Auto-generated method stub
		return this.width;
	}

	@Override
	public double getHeight() {
		// TODO Auto-generated method stub
		return this.height;
	}

	@Override
	public double getDepth() {
		// TODO Auto-generated method stub
		return this.depth;
	}

	@Override
	public double calculateVolume() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateArea() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public double calculateCircumference() {
		// TODO Auto-generated method stub
		return 0;
	}
	
	public EnumExample getTypeOfGeometricObject() {
		return EnumExample.GEOMETRIC_OBJECT;
	}
}
