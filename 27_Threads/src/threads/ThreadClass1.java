/**
 *	How to use threads in Java.
 *
 *	@author 		ITWorks4U
 */
package threads;

public class ThreadClass1 extends Thread {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
			ThreadClass1 tc1 = new ThreadClass1();
			tc1.start();
		}
	}
	
	@Override
	public void run() {
		try {
			Services srv = new Services();
			srv.sayHello();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
