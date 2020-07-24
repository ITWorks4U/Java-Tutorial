/**
 *	How to define an own class for a further purpose.
 *
 *	@author 		ITWorks4U
 */
package math;

public class GeometricObject {
	private int width;
	private int height;
		
	public GeometricObject(int width, int height) {
		System.out.println(" Basic constructor called.");
		
		this.width = width;
		this.height = height;
	}
	
	public GeometricObject(GeometricObject copy) {
		System.out.println(" Copy constructor called.");
		
		width = copy.width;
		height = copy.height;
	}
	
	public int getArea() {
		return this.width * this.height;
	}
	

}
