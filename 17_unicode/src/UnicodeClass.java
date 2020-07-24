/**
 *	How to unicodes for Java.
 *
 *	@author 		ITWorks4U
 */
public class UnicodeClass {
	public static void main(String... args) {
		
		/*	output in German, where no unicode format (UTF-8) is in use
		 * 	==> depending on the operating system it may be impossible
		 * 	to display some special characters in their correct form...
		 */
		System.out.println(" Umlaute, wie ä, ö oder ü. Sonderzeichen, wie \"ß\" sind auch vorhanden.");
		System.out.println(" Apropos Sonderzeichen: →, ☺, ►, ‼, ♪, ♫, ..." + "\n\n\n");
		
		/*	... however, by using the unicode format it's possible to display
		 * 	the characters on other operating systems, e. g. 'ä' is equal to  \u00E4
		 */
		
		System.out.println(" Umlaute, nach UTF-8-Format: \u00E4, \u00F6 oder \u00FC.\n Sonderzeichen, wie \"\u00DF\" sind auch vorhanden.");
	}
}