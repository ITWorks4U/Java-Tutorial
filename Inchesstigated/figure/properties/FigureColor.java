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
 *         Holds the color for each figure.
 */
public enum FigureColor {
	/* possible colors */
	BLACK, WHITE;
	
	/**
	 * Receive the opposite color.
	 *
	 * @param color
	 *            the current color
	 *
	 * @return the opposite color, <br>
	 * 			or null, of color was also null
	 */
	public static FigureColor getOpponentColor(final FigureColor color) {
		if (color == BLACK) {
			return FigureColor.WHITE;
		} else if (color == WHITE) {
			return BLACK;
		}
		
		return null;
	}

	/**
	 * 	Receive the current color type by given color word.
	 *
	 * 	@param	figureColorAsWord
	 * 				the figure color as a word
	 *
	 *	@return	the current color type, <br>
	 *			or null, if figureColorAsWord
	 *			doesn't match with given colors
	 */
	public static FigureColor getColorType(final String figureColorAsWord) {
		if (figureColorAsWord.equals(WHITE.toString())) {
			return WHITE;
		} else if (figureColorAsWord.equals(BLACK.toString())) {
			return BLACK;
		}

		return null;
	}
}
