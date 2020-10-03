/**
 * @package: location
 * @project: Chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package location;

/**
 * @author swunsch
 *
 *         Holds the coordinates for any chess figure.
 */
public class Coordinates {
	/** coordinate X */
	private final String posX;

	/** coordinate Y */
	private final String posY;

	/**
	 * Create a new coordinate {X,Y}.
	 *
	 * @param coordinate
	 *            the coordinate of type String to use
	 */
	public Coordinates(final String coordinate) {
		this.posX = new String(coordinate.substring(0, 1));
		this.posY = new String(coordinate.substring(1, 2));
	}

	/**
	 * Create a new coordinate {X,Y} by given characters.
	 * 
	 * @param posX
	 *            coordinate X
	 * @param posY
	 *            coordinate Y
	 */
	public Coordinates(final char posX, final char posY) {
		this.posX = String.valueOf(posX);
		this.posY = String.valueOf(posY);
	}

	/**
	 * Receive coordinate X as String.
	 * 
	 * @return coordinate X
	 */
	public String getPosX() {
		return this.posX;
	}

	/**
	 * Receive coordinate Y as String.
	 * 
	 * @return coordinate Y
	 */
	public String getPosY() {
		return this.posY;
	}

	/**
	 * Receive coordinate c = {X,Y}.
	 * 
	 * @return coordinate pair {X,Y} as word
	 */
	public String getCoordinatesXY() {
		final StringBuffer sb = new StringBuffer();
		sb.append(this.posX);
		sb.append(this.posY);

		return sb.toString();
	}

	/**
	 * Receive a coordinate by given characters. It's in use for Matrix class to
	 * avoid to repeat the code below inside of the most functions.
	 * 
	 * @param x
	 *            x coordinate
	 * @param y
	 *            y coordinate
	 * 
	 * @return coordinate pair {X,Y} as word
	 */
	public static String getCoordinatesXY(final char x, final char y) {
		final StringBuffer sb = new StringBuffer();
		sb.append(String.valueOf(x) + String.valueOf(y));

		return sb.toString();
	}
}
