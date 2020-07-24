/**
 *	How to use the keyword 'final' in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package enumerations;

import children.GeometricObject;

public enum EnumExample {
	GEOMETRIC_OBJECT, CUBE, PYRAMID, TETRAHEDRON, UNKNOWN;
	
	public static boolean isPartOfBaseClass(Class<?> classObject) {
		return GeometricObject.class.isAssignableFrom(classObject);
	}
}
