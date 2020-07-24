/**
 *	How to use arguments for the main method.
 *
 *	@author 		ITWorks4U
 */
package simple;

public class ASimpleClass {

	/**
	 * @param args	commands to use
	 */
	public static void main(String[] args) {
		System.out.println(" Let's check out all arguments:");
		
		for (String tmp : args) {
			System.out.println(tmp);
			
			try {
				Thread.sleep(500);						//	500ms to wait
			} catch (InterruptedException ie) {
				ie.printStackTrace();
			}
		}
	}
}
