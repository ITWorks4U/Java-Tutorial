/**
 *	How to use threads in Java.
 *
 *	@author 		ITWorks4U
 */
package threads;

public class ThreadClass2 implements Runnable {
	public static void main(String[] args) {
		for (int i = 0; i < 10; i++) {
//			ThreadClass2 tc2 = new ThreadClass2();
			Thread tc2 = new Thread(new ThreadClass2());
			tc2.start();
		}
	}
	
	public void run() {
		try {
			System.out.println(" Thread ID: " + Thread.currentThread().getId());
			
			Services srv = new Services();
			srv.sayHello();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
