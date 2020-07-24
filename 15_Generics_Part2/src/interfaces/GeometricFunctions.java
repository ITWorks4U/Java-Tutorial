/**
 *	How to handle with generics in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package interfaces;

public interface GeometricFunctions<E extends Number> {
	public E getWidth();
		
	public E getHeight();

	public E getDepth();
	
	public Number calculateVolume() throws Exception;
	
	public Number calculateArea() throws Exception;
	
	public Number calculateCircumference() throws Exception;
}
