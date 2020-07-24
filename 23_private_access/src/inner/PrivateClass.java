/**
 *	How to get access to a private field or private method in Java.
 *
 *	@author 		ITWorks4U
 */
package inner;

public class PrivateClass {
	private String readOnlyContent = "ITWorks4U"; 
		
	public final String getContent() {
		return readOnlyContent;
	}
	
	@SuppressWarnings("unused")
	private void aPrivateMethod() {
		System.out.println("→→→→→→→  This is my private method! ←←←←←←←" + "\n");
	}
}
