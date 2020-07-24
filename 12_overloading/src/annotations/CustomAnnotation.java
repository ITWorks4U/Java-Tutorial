/**
 *	How to use method overloading with our own defined class.
 *
 *	@author 		ITWorks4U
 */
package annotations;

import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

@Retention(RUNTIME)
@Target(ElementType.TYPE)

public @interface CustomAnnotation {
	enum Test {
		A, B, C, D, E, F
	}
	
	int value();
	
	String author() default "ITWorks4U";
	
	String[] tags();
}
