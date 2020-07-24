/**
 *	How to use method overloading with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package children;

public class Example {
	
	public static void main(String... args) {
		System.out.println("Hello from from default main");
		Example.main("");
	}
	
	public static void main(String str) {
		System.out.println("Hello from first overwritten main method");
		Example.main("", 0);
	}
	
	public static void main(String str, int f) {
		System.out.println("Hello!");
	}
}
