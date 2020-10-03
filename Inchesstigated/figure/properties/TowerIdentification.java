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
 *         An enumeration for each Rook (Tower) to determine it's
 *         identification, where "IMPOSSIBLE_TO_CASLE" is in use only if this
 *         Rook has been moved or if a figure Pawn has been promoted to a Rook.
 */
public enum TowerIdentification {
	/**
	 * While this Rook has not been moved before, it may do a castling with its
	 * allied King.
	 */
	POSSIBLE_TO_CASTLE,

	/**
	 * If this Rook has been moved or a figure Pawn has been promoted to a Rook,
	 * then it's no more possible to do a castling.
	 */
	IMPOSSIBLE_TO_CASTLE
}
