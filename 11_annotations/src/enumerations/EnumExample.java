/**
 *	How to use annotation classes with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package enumerations;

import children.GeometricObject;

public enum EnumExample {
	GEOMETRIC_OBJECT, CUBE, PYRAMID, UNKNOWN;
	
	public static boolean isPartOfBaseClass(Class<?> classObject) {
		return GeometricObject.class.isAssignableFrom(classObject);
	}
}
