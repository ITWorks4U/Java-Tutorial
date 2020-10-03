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

/**
 * @author swunsch
 *
 *         Holds an information about this King.
 */
public enum KingMoveInformation {
	/* the King has not moved before */
	NOT_MOVED_BEFORE,

	/* the King just has moved */
	JUST_MOVED,

	/* the King is no longer to move anywhere */
	IMPOSSIBLE_TO_MOVE
}
