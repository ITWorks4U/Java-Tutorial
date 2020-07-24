/**
 *	How to read from the keyboard.
 *
 *	@author 		ITWorks4U
 */
package inputOutput;

import java.io.BufferedReader;
import java.util.Scanner;
import java.io.InputStreamReader;

public class InputOutputConsole {
	
	public static void main(String[] args) throws Exception {
		String content = "Hello World!";
		int data = 35;
		double ID_NBR = 9123.456;	
		
		System.out.println(content + " " + data + " "+ ID_NBR);
		
		//	---	formatted output	---	
		System.out.print(content + " " + data);
		System.out.print(content + " " + data);
		
		System.out.printf(" %s %e %d", content, ID_NBR, ID_NBR);
		
		System.err.println(" A customized error occurred.");
			
		
		//	---	read from keyboard #1	---

		Scanner sc = new Scanner(System.in);
		
		System.out.print("Enter a new value for \"content\": ");
		content = sc.nextLine();
		
		System.out.print("Enter a new value for \"data\": ");
		data = sc.nextInt();
		
		System.out.print("Enter a new value for \"ID_NBR\": ");
		ID_NBR = sc.nextDouble();
		
		System.out.println(content + " " + data + " "+ ID_NBR);
		sc.close();
		
		//	---	read from keyboard #2	---
		
		BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		System.out.print("Enter a new value for \"content\": ");
		content = br.readLine();
		
		System.out.print("Enter a new value for \"data\": ");
		data = Integer.parseInt(br.readLine());
		
		System.out.print("Enter a new value for \"ID_NBR\": ");
		ID_NBR = Double.parseDouble(br.readLine());
		
		System.out.println(content + " " + data + " "+ ID_NBR);
		br.close();
	}
}
