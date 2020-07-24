/**
 *	How to use the keyword 'final' in our own defined class.
 *
 *	@author 		ITWorks4U
 */
package singleton;

/**
 * 	A basic definition of a singleton class.
 * 	A singleton class has at least one private
 * 	constructor, which is able to call from inside
 * 	of this class only. By default, a singleton
 * 	class doesn't need any other kind of
 * 	constructor.
 * 
 * 	Anything from outside is able to get the object
 * 	of this class by a given function.
 */
public class BasicSingleton {
	
	//	definition of an interface between the class and outside
	private static BasicSingleton instance = null;
	
	//	a private set constructor
	private BasicSingleton() {
		//	by default: nothing to do here
	}
	
	//	the public interface for outside
	public static BasicSingleton getInstance() {
		if (BasicSingleton.instance == null) {
			BasicSingleton.instance = new BasicSingleton();
		}
		
		return BasicSingleton.instance;
	}
	
	/*	any other methods / functions...	*/
}
