/**
 *	How to write content to a file.
 *
 *	@author 		ITWorks4U
 */

package ioExample;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;

//	Write some content into a file
public class WriteFile {
	
	public static void main(String[] args) throws Exception {
		File destination = new File("src/dummyFileWrite.txt");
				
		//	first example
		String example01 = " You've learned about \"How to write content to a file\".";
		
		BufferedWriter bw = new BufferedWriter(new FileWriter(destination, true));
		bw.write(example01);
		bw.close();
		
		//	second example
		String example02 = "\n By the way, it's possible to write to a file by this way.";
		boolean append = true;
		
		FileOutputStream fos = new FileOutputStream(destination, append);
		byte ex02ByteArray[] = example02.getBytes();
		
		for (byte b : ex02ByteArray) {
			fos.write(b);
		}
		
		fos.close();
	}
}
