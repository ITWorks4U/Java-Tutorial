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

import figure.FigurePawn;

/**
 * @author swunsch
 *
 *         Offers a set of possible move informations for the figure Pawn only.
 * 
 * @see {@link FigurePawn}
 */
public enum PawnMoveInformation {
	/** This Pawn has not been moved before. */
	NOT_MOVED_YET,

	/**
	 * Since this Pawn has been moved, then it is able to move a single field only.
	 */
	MOVED_ONE_FIELD,

	/**
	 * When the Pawn has not been moved before, then it's possible to move with two
	 * fields once only.
	 */
	MOVED_TWO_FIELDS
}
