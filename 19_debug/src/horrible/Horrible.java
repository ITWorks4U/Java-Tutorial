/**
 *	How to debug Java code.
 *
 *	@author 		ITWorks4U
 */
package horrible;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class Horrible {
	public static void main(String...strings) throws Exception {
		int nbr = 100;
		File destination = new File("src/output.txt");
		BufferedWriter bw = new BufferedWriter(new FileWriter(destination));
		
		/*	causing a logical error unless
		 * 	the square root of negative number
		 * 	is allowed to use
		 */
		while (nbr > -100) {
			bw.write("\u221A" + nbr + " = " + Math.sqrt(nbr) + "\n");	//	\u221A prints the square root character
			nbr--;
		}
		
		bw.close();
		System.out.println(" Aaaaaand done!");
	}
}
