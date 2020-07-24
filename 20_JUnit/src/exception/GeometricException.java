/**
 *	How to JUnit for our own defined classes.
 *
 *	@author 		ITWorks4U
 */
package exception;

public class GeometricException extends Exception {

	private String message;
	
	public GeometricException(String string) {
		this.message = string;
	}

	private static final long serialVersionUID = 1L;
	
	public String getErrormessage() {
		return this.message;
	}
}
