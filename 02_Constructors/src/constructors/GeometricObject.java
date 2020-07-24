/**
 *	How to define and use constructors.
 *
 *	@author 		ITWorks4U
 */
package constructors;

public class GeometricObject {
	private int length;
	private int height;
	
	/*	extended constructor	*/
	public GeometricObject(int length, int height) {
		System.out.println("Basic constructor called.");
		
		this.length = length;
		this.height = height;
	}
	
	/*	copy constructor		*/
	public GeometricObject(GeometricObject copy) {
		System.out.println("Copy constructor called.");
		
		this.length = copy.length;
		this.height = copy.height;
	}
	
	public int getArea() {
		return (this.length * this.height);
	}
	
	public void setLength(int length) {
		this.length = length; 
	}
	
	public void setHeight(int height) {
		this.height = height;
	}
}
