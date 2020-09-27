/**
 *	How to handle with the diamond problem in Java.
 *
 *	@author 		ITWorks4U
 */

package diamond;

/**
 * 	This interface derives from both interfaces. In Java this setup works fine,
 * 	whereas some questions are still not answered yet... 
 */
public interface DiamondInterface extends DerivedInterfaceBase_01, DerivedInterfaceBase_02 {

	/**
	 *	This method is defined in {@link BaseInterface}, {@link DerivedInterfaceBase_01}
	 *	and also in {@link DerivedInterfaceBase_02}. At this moment we don't know,
	 *	from which interface this service below is inherited. 
	 */
	@Override
	public void aSimpleMethod();
}
