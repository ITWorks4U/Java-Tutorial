/**
 * @package: figure.properties
 * @project: Inchesstigated
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package figure.properties;

import location.Matrix;

/**
 * @author swunsch<br>
 *         <br>
 *
 *         Holds the color for an area, which can be filled with a black or
 *         white color only.<br>
 *         <br>
 *
 *         This class has been created to avoid to getting confused whenever a
 *         figure Runner is going to create, which holds these three properties:<br>
 *         <br>
 *
 *         <ul>
 *         <li>location by Coordinates</li>
 *         <li>figure color by <b>FigureColor</b></li>
 *         <li>area color by <b>FigureColor</b></li>
 *         </ul>
 *
 *         The last property is now available to remove the logical
 *         confusion.
 */
public class AreaColor {
	/** Holds the value for a white area only. */
	public static final FigureColor WHITE_AREA = FigureColor.WHITE;
	
	/** Holds the value for a black area only. */
	public static final FigureColor BLACK_AREA = FigureColor.BLACK;
	
	/**
	 * Determine the area color for a given figure Runner, depending on it's color
	 * and it's start location.
	 *
	 * @param startLocation
	 *            the location of the Runner in words
	 *
	 * @return the area color for this Runner
	 */
	public static FigureColor getAreaColor(final String startLocation) {
		return Matrix.INSTANCE.determineAreaColor(startLocation.charAt(0), startLocation.charAt(1));
	}
}
