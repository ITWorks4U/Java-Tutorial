/**
 * @package: figure.properties
 * @project: Chess
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
 *         An information where a new created figure Rook may be spawn. It
 *         doesn't work, if a figure Pawn has been promoted to a figure Rook.
 * 
 *         It's required for a castling.
 */
public enum TowerSpawnLocation {
	/** This figure Rook is on the left side. */
	LEFT_SIDE,

	/** This figure Rook is on the right side. */
	RIGHT_SIDE
}
