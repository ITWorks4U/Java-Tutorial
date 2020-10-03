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

import figure.BaseFigure;
import figure.properties.FigureColor;

/**
 * @author swunsch
 *
 *         This class holds the informations which player has used which
 *         figure.
 */
public final class MoveWatcherEvent {

	/* properties */
	private static BaseFigure lastBlackFigure = null;
	private static BaseFigure lastWhiteFigure = null;
	private static FigureColor lastUsedColor = null;
	private static boolean flagBlack = false;
	private static boolean flagWhite = false;

	/**
	 * Notify the last move.
	 *
	 * @param figure
	 *            the figure which has been moved
	 *
	 * @return true, if and only if the current figure color differs to the last
	 *         added figure color,<br>
	 *         false, otherwise
	 */
	public static <T extends BaseFigure> boolean addLastMoveBy(final T figure) {
		boolean collected = true;

		if (figure.getFigureColor() == FigureColor.BLACK) {
			if (!MoveWatcherEvent.flagBlack) {
				MoveWatcherEvent.lastBlackFigure = figure;
				MoveWatcherEvent.flagBlack = true;
				MoveWatcherEvent.flagWhite = false;
				MoveWatcherEvent.lastUsedColor = figure.getFigureColor();
			} else {
				collected = false;
			}
		} else {
			if (!MoveWatcherEvent.flagWhite) {
				MoveWatcherEvent.lastWhiteFigure = figure;
				MoveWatcherEvent.flagWhite = true;
				MoveWatcherEvent.flagBlack = false;
				MoveWatcherEvent.lastUsedColor = figure.getFigureColor();
			} else {
				collected = false;
			}
		}

		return collected;
	}

	/**
	 * Receiving the last opponent figure depending on given color.
	 *
	 * @param color
	 *            the color to use
	 *
	 * @return the required figure
	 */
	public static BaseFigure getLastUsedFigure(final FigureColor color) {
		if (color == FigureColor.WHITE) {
			return MoveWatcherEvent.lastWhiteFigure;
		}

		return MoveWatcherEvent.lastBlackFigure;
	}

	/**
	 * 	Receiving the last used figure color. Necessary for castling mode.
	 *
	 * 	@return	the last used figure color
	 */
	public static FigureColor getLastFigureColor() {
		return MoveWatcherEvent.lastUsedColor;
	}
}
