/**
 *	How to handle with the diamond problem in Java.
 *
 *	@author 		ITWorks4U
 */

package diamond;

/**
 * 	This interface derives from the super interface, too. 
 */
public interface DerivedInterfaceBase_02 extends BaseInterface {
	@Override	
	public default void aSimpleMethod() {
		System.out.println("abc");
	}
}
