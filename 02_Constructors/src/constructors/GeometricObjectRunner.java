/**
 *	How to define and use constructors.
 *
 *	@author 		ITWorks4U
 */
package constructors;

public class GeometricObjectRunner {

	public static void main(String[] args) {
		GeometricObject go = new GeometricObject(10,20);		
		System.out.println(go.getArea());
		
		GeometricObject copyOfGO = new GeometricObject(go);
		copyOfGO.setHeight(30);
		copyOfGO.setLength(100);
		
		System.out.println(copyOfGO.getArea());
	}
}
