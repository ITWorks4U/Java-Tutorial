/**
 *	How to handle with generics in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

import enumerations.EnumExample;

public class Pyramid<P extends Number> extends GeometricObject<P> {
	
	public Pyramid(P width, P height, P depth) throws Exception {
		super(width, height, depth);
	}
	
	//	given methods below are used for a linear-square based pyramid
	
	@Override
	public Number calculateVolume() throws Exception {		//	where width is equal to depth | V = 1/3 * a² * h												
		return ((1.0 / 3.0) * width.doubleValue() * width.doubleValue() * height.doubleValue());		
	}
	
	@Override
	public Number calculateArea() throws Exception {		//	A = a * b
		return (width.doubleValue() * depth.doubleValue());
	}

	@Override
	public EnumExample getTypeOfGeometricObject() {
		return EnumExample.PYRAMID;
	}

	@Override
	public P getWidth() {
		return this.width;
	}

	@Override
	public P getHeight() {
		return this.height;
	}

	@Override
	public P getDepth() {
		return this.depth;
	}

	@Override
	public Number calculateCircumference() {				//	doesn't work for a pyramid
		return 0;
	}
}
