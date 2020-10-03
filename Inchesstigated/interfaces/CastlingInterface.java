/**
 * @package: interfaces
 * @project: Chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package interfaces;

/**
 * @author swunsch
 *
 *         This interface is in use for a figure Rook and the figure King only.
 *         It holds functions which are required for a castling.
 */
public interface CastlingInterface {
	/**
	 * Send a signal that the King / Rook has been moved, therefore a castling with
	 * this figure is now impossible.
	 */
	public void markToImpossibleCastling();

	/**
	 * Receive the information, if this Rook or King has been moved before.
	 *
	 * @return the figure's move information
	 */
	public boolean onAlreadyMoved();
}
