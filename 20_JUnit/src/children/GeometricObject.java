/**
 *	How to use JUnit for our own defined classes.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;
import exception.GeometricException;
import interfaces.GeometricFunctions;


public abstract class GeometricObject<E extends Number> implements GeometricFunctions<E> {
	
	/*	properties to use	*/
	protected E width;
	protected E height;
	protected E depth;

	/*	extended constructor to initialize the properties	*/
	public GeometricObject(E width, E height, E depth) throws GeometricException {
		this.width = width;
		this.height = height;
		this.depth = depth;
	}

	@Override
	public abstract E getWidth();

	@Override
	public abstract E getHeight();

	@Override
	public abstract E getDepth();

	@Override
	public abstract Number calculateVolume() throws Exception;

	@Override
	public abstract Number calculateArea() throws Exception;

	@Override
	public abstract Number calculateCircumference() throws Exception;
	
	public EnumExample getTypeOfGeometricObject() {
		return EnumExample.GEOMETRIC_OBJECT;
	}
}
