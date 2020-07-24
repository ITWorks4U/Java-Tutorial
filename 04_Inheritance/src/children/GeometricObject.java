/**
 *	How to use inheritance with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

public class GeometricObject {
	
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
	
	/*	setter methods	*/
	public double getWidth() {
		return this.width;
	}
	
	public double getHeight() {
		return this.height;
	}
	
	public double getDepth() {
		return this.depth;
	}
}
