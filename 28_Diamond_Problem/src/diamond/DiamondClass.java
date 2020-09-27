/**
 *	How to handle with the diamond problem in Java.
 *
 *	@author 		ITWorks4U
 */

package diamond;

/**
 * 	By default we can implement n interfaces (n = 0, 1, 2, ...), where it could
 * 	be possible to have at least one service with the identical name and also
 * 	the identical number of parameters, however, Java might know what to do here... 
 */
public class DiamondClass implements DerivedInterfaceBase_01, DerivedInterfaceBase_02 {	
	
	public static void main(String[] args) {
		DiamondClass dc = new DiamondClass();
		dc.aSimpleMethod();
	}

	@Override
	public void aSimpleMethod() {
//		System.out.println("A call from this method.");
		
		DerivedInterfaceBase_01.super.aSimpleMethod();
		DerivedInterfaceBase_02.super.aSimpleMethod();
	}	
}
