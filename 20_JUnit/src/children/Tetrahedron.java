/**
 *	How to use JUnit for our own defined classes.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;

public class Tetrahedron<T extends Number> extends Pyramid<T> {

	public Tetrahedron(final T width, final T height, final T depth) throws Exception {
		super(width, height, depth);
	}

	@Override
	public Number calculateVolume() throws Exception {
		return ((Math.pow(width.doubleValue(), 3) / 12) * Math.sqrt(2));
	}

	@Override
	public Number calculateArea() throws Exception {
		return ((width.doubleValue() * width.doubleValue() / 4) * Math.sqrt(3));
	}

	@Override
	public final T getWidth() {
		return width;
	}

	@Override
	public final T getHeight() {
		return height;
	}

	@Override
	public final T getDepth() {
		return depth;
	}

	@Override
	public EnumExample getTypeOfGeometricObject() {
		return EnumExample.TETRAHEDRON;
	}
}
