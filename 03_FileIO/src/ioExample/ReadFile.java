/**
 *	How to read a file in Java.
 *
 *	@author 		ITWorks4U
 */

package ioExample;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;

//	Read the content of a file and print that to the console
public class ReadFile {
	
	public static void main(String[] args) throws Exception {
		File source = new File("src/dummyFileRead.txt");		
		
		//	first example
		String content;
		BufferedReader br = new BufferedReader(new FileReader(source));
		
		while ((content = br.readLine()) != null) {
			System.out.println(content);
		}		
		
		br.close();		
		
		System.out.println("--------------");
		
		//	second example
		FileInputStream fis = new FileInputStream(source);
		int value;
		
		while ((value = fis.read()) != -1) {
			System.out.print((char) value);
		}
		
		fis.close();
	}
}
