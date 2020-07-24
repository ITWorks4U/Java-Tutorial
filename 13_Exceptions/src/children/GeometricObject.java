/**
 *	How to handle with exceptions in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;
import exception.GeometricException;
import interfaces.GeometricFunctions;

public abstract class GeometricObject implements GeometricFunctions {
	
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
	public GeometricObject(double width, double height, double depth) throws GeometricException {
		if (width < 0.1F || height < 0.1F || depth < 0.1F) {
			throw new GeometricException("invalid size detected");
		} else {
			this.width = width;
			this.height = height;
			this.depth = depth;
		}
	}

	@Override
	public abstract double getWidth();

	@Override
	public abstract double getHeight();

	@Override
	public abstract double getDepth();

	@Override
	public abstract double calculateVolume();

	@Override
	public abstract double calculateArea();

	@Override
	public abstract double calculateCircumference();
	
	public EnumExample getTypeOfGeometricObject() {
		return EnumExample.GEOMETRIC_OBJECT;
	}
}
