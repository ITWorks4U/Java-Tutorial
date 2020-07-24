/**
 *	How to handle with generics in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;

/*	Since our super class comes with the acronym "E" the derived class
 * 	"Cube" may use an another word.
 */
public class Cube<T extends Number> extends GeometricObject<T> {

	public Cube(T width, T height, T depth) throws Exception {		
		super(width, height, depth);
	}
	
	public Number calculateVolume() throws Exception {				//	V = a * b * c
		return (width.doubleValue() * height.doubleValue() * depth.doubleValue());
	}
	
	public Number calculateArea() throws Exception {				//	V = a * b; it's not a square
		return (width.doubleValue() * height.doubleValue());
	}
	
	@Override
	public Number calculateCircumference() throws Exception {		//	u = 2 * (a + b)
		return (2 * (width.doubleValue() + height.doubleValue()));
	}

	@Override
	public EnumExample getTypeOfGeometricObject() {
		return EnumExample.CUBE;
	}

	@Override
	public T getWidth() {
		return this.width;
	}

	@Override
	public T getHeight() {
		return this.height;
	}

	@Override
	public T getDepth() {
		return this.depth;
	}
}
