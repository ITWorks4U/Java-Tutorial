/**
 *	How to handle with the diamond problem in Java.
 *
 *	@author 		ITWorks4U
 */

package diamond;

/**
 * 	This interface derives from the super interface. 
 */
public interface DerivedInterfaceBase_01 extends BaseInterface{
	@Override	
	public default void aSimpleMethod() {
		System.out.println("123");
	}
}
