/**
 *	How to use an abstract class with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package singleton;

import java.util.ArrayList;
import java.util.List;

import children.GeometricObject;

public enum BasicSingletonEnum {
	INSTANCE;
	
	private List<GeometricObject> listOfGeometricObjects = new ArrayList<GeometricObject>();
	
	public void addObjectToList(GeometricObject tmp) {
		listOfGeometricObjects.add(tmp);
	}
	
	public List<GeometricObject> getListOfObjects() {
		return listOfGeometricObjects;
	}
}
