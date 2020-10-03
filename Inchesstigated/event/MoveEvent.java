/**
 * @package: event
 * @project: Inchesstigated
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package event;

/**
 * @author swunsch
 *
 *         Holds properties for each turn.
 */
public enum MoveEvent {
	/**	The current move was successful.	*/
	SUCCESSFULLY_MOVE,

	/**	The current move was unsuccessful.	*/
	INCORRECT_MOVE
}
