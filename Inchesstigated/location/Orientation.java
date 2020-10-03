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
 *         This enumeration holds orientation properties for any figure, where
 *         each horizontal and vertical orientation are necessary for each Rook
 *         (Tower) and each diagonal orientation are necessary for each Runner.
 * 
 *         The Queen is using all orientations.
 */
public enum Orientation {
	/** the current right side of the Rook, Queen */
	HORIZONTAL_RIGHT,

	/** the current left side of the Rook, Queen */
	HORIZONTAL_LEFT,

	/** the current top side of the Rook, Queen */
	VERTICAL_UP,

	/** the current bottom side of the Rook, Queen */
	VERTICAL_DOWN,

	/** right top area, where {X++,Y++} for Runner, Queen */
	DIAGONAL_QUARTER_ONE,

	/** top left area, where {X--, Y++} for Runner, Queen */
	DIAGONAL_QUARTER_TWO,

	/** bottom left area, where {X--, Y--} for Runner, Queen */
	DIAGONAL_QUARTER_THREE,

	/** bottom right area, where {X++, Y--} for Runner, Queen */
	DIAGONAL_QUARTER_FOUR
}
