/**
 * @package: figure
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
 *         A set of figures.
 */
public enum FigureSet {
	PAWN, KNIGHT, ROOK, RUNNER, KING, QUEEN;

	/**
	 * Receiving the figure type by given figure ID as word
	 * 
	 * @param figureID
	 *            the figure ID
	 * 
	 * @return the figure type
	 */
	public static FigureSet getFigureType(final String figureID) {
		if (figureID.equals(PAWN.toString())) {
			return PAWN;
		} else if (figureID.equals(KNIGHT.toString())) {
			return FigureSet.KNIGHT;
		} else if (figureID.equals(ROOK.toString())) {
			return ROOK;
		} else if (figureID.equals(RUNNER.toString())) {
			return FigureSet.RUNNER;
		} else if (figureID.equals(KING.toString())) {
			return KING;
		}

		return FigureSet.QUEEN;
	}
}
