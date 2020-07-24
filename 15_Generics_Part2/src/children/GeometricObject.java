/**
 *	How to handle with generics in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;
import exception.GeometricException;
import interfaces.GeometricFunctions;

/*
 * 	--------------------------------------
 * 	If you've missed watching the first part of Java generics, then watch the previous video.
 *	--------------------------------------
 *
 *	We're going to convert our class to a generic class, where the implemented functions
 *	from GeometricFunctions are also required to use.
 */
public abstract class GeometricObject<E extends Number> implements GeometricFunctions<E> {
	
	/*	properties to use	*/
	protected E width;
	protected E height;
	protected E depth;
	
/*	default constructor ||| This constructor below is no longer in use.
	public GeometricObject() {
		this.width = 0;
		this.height = 0;
		this.depth = 0;
	}
*/
	
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
