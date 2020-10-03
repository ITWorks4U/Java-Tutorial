/**
 * @package: annotation
 * @project: Inchesstigated
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package annotation;

import static java.lang.annotation.ElementType.METHOD;

import java.lang.annotation.Target;

/**
 * @author swunsch<br>
 *         <br>
 *
 *         Experimental customized annotation for JUnit5. JUnit5 comes with a
 *         method sorters class which allows you to sort your given methods in a
 *         certain sequence, however, since JUnit5 it doesn't support the method
 *         sorters, thus all given methods inside of a test class are running
 *         randomly.
 * 
 *         This annotation could fix that issue.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
@Target(METHOD)
public @interface CustomMethodSorters {
	static enum MethodSequence {
		CUSTOMIZED_ORDER_SEQUENCE
	}

	int orderValue() default 0;

	String author() default "swunsch";

	String createdOn() default "May 9th, 2019";
}
