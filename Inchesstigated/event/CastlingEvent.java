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

import figure.FigureKing;
import figure.FigureRook;
import figure.properties.FigureColor;
import figure.properties.TowerSpawnLocation;
import gui.FigureProcedure;
import location.Coordinates;
import location.Matrix;

/**
 * @author swunsch, ptoepel
 *
 *         This enumeration holds a state for the figures King and Rook (Tower)
 *         for a castle event. It checks the possibility for the given figures
 *         and holds the type of the castling.
 */
public enum CastlingEvent {
	INSTANCE;

	/**
	 * Nested enumeration, which holds the state for a castling.
	 */
	public enum CastlingState {
		STILL_POSSIBLE, IMPOSSIBLE
	}

	/**
	 * Nested enumeration, which holds the castling type.
	 */
	public enum CastlingType {
		SMALL_CASTLING, BIG_CASTLING
	}

	/** Holds the reason, why a castling was not successfully. */
	private String castlingErrorReason;

	/**
	 * Receiving the current reason, why a castling was not successfully.
	 *
	 * @return the error reason
	 */
	public final String getCastlingErrorReason() {
		return this.castlingErrorReason;
	}

	/**
	 * Check the possibility of the castling state by King and given Rook. It
	 * returns "STILL_POSSIBLE", if and only if, all sub conditions are satisfied,
	 * otherwise "IMPOSSIBLE".
	 *
	 * @param king
	 *            given King
	 * @param rook
	 *            given Rook
	 *
	 * @return {@value CastlingState#STILL_POSSIBLE}, if all conditions are
	 *         satisfied,<br>
	 *         {@value CastlingState#IMPOSSIBLE}, otherwise
	 */
	public CastlingState onCastlingPossibility(final FigureKing king, final FigureRook rook) {
		if ((king != null) && (rook != null)) {

			// the King or Rook must not moved before
			if (king.onAlreadyMoved()) {
				this.castlingErrorReason = "Der K\u00F6nig wurde bereits gezogen.";
				return CastlingState.IMPOSSIBLE;
			}

			if (rook.onAlreadyMoved()) {
				this.castlingErrorReason = "Der Turm wurde bereits gezogen.";
				return CastlingState.IMPOSSIBLE;
			}

			// the color of both figures must be equal
			if (king.getFigureColor() != rook.getFigureColor()) {
				this.castlingErrorReason = "Die Farbe von K\u00F6nig und Turm sind verschieden!";
				return CastlingState.IMPOSSIBLE;
			}

			// the king must not being threatened
			if (king.onThreatened()) {
				this.castlingErrorReason = "Der K\u00F6nig steht im \"Schach\"!";
				return CastlingState.IMPOSSIBLE;
			}

			// check, if all fields between King and Rook are free and are also not
			// threatened by the opponent
			final FigureColor opponentColor = FigureColor.getOpponentColor(king.getFigureColor());
			if (!(Matrix.INSTANCE.onFreeCastlingAreas(opponentColor, king.getLocation(), rook.getLocation()))) {
				this.castlingErrorReason = "Die Felder zwischen K\u00F6nig und Turm sind nicht frei!";
				return CastlingState.IMPOSSIBLE;
			}

			return CastlingState.STILL_POSSIBLE;
		}

		return CastlingState.IMPOSSIBLE;
	}

	/**
	 * Do a castling between the given King and it's allied Rook with the requested
	 * castling mode. This will work only, if all castling conditions between King
	 * and Rook are satisfied, otherwise it won't work.
	 *
	 * @param castlingMode
	 *            the required mode
	 * @param king
	 *            the given King
	 * @param rook
	 *            the given Rook
	 */
	public void castlingKingTower(final CastlingType castlingMode, final FigureKing king, final FigureRook rook) {
		final FigureColor colorToListen = king.getFigureColor();
		Coordinates coordKing;
		Coordinates coordTower;

		// small castling is in use
		if (CastlingEvent.CastlingType.SMALL_CASTLING.equals(castlingMode)) {
			if (colorToListen == FigureColor.WHITE) {

				/* white figures are in use | ↓ check, if the right Rook is in use */
				if (rook.onCorrectLocationIdentification(TowerSpawnLocation.RIGHT_SIDE)) {
					coordKing = new Coordinates("G1");

					FigureProcedure.INSTANCE.updateFigureProperties(king, coordKing);
					king.markToImpossibleCastling();

					coordTower = new Coordinates("F1");
					FigureProcedure.INSTANCE.updateFigureProperties(rook, coordTower);
					rook.markToImpossibleCastling();
				}
			} else {
				/* black figures are in use | ↓ check, if the right Rook is in use */
				if (rook.onCorrectLocationIdentification(TowerSpawnLocation.RIGHT_SIDE)) {
					coordKing = new Coordinates("G8");
					FigureProcedure.INSTANCE.updateFigureProperties(king, coordKing);
					king.markToImpossibleCastling();

					coordTower = new Coordinates("F8");
					FigureProcedure.INSTANCE.updateFigureProperties(rook, coordTower);
					rook.markToImpossibleCastling();
				}
			}
		} else { /* otherwise a big castling has been selected */
			/* check, if the white figures are in use */
			if (colorToListen == FigureColor.WHITE) {
				if (rook.onCorrectLocationIdentification(TowerSpawnLocation.LEFT_SIDE)) {
					coordKing = new Coordinates("C1");
					FigureProcedure.INSTANCE.updateFigureProperties(king, coordKing);
					king.markToImpossibleCastling();

					coordTower = new Coordinates("D1");
					FigureProcedure.INSTANCE.updateFigureProperties(rook, coordTower);
					rook.markToImpossibleCastling();
				}
			} else {
				if (rook.onCorrectLocationIdentification(TowerSpawnLocation.LEFT_SIDE)) {
					coordKing = new Coordinates("C8");
					FigureProcedure.INSTANCE.updateFigureProperties(king, coordKing);
					king.markToImpossibleCastling();

					coordTower = new Coordinates("D8");
					FigureProcedure.INSTANCE.updateFigureProperties(rook, coordTower);
					rook.markToImpossibleCastling();
				}
			}
		}
	}
}