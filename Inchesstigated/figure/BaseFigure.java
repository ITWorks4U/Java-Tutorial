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
package figure;

import java.util.ArrayList;
import java.util.List;

import event.MoveEvent;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.FigureSet;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch
 *
 *         A base class for each figure. Every figure comes with basic
 *         properties which are defined in this class, like figure color, figure
 *         location and so on.
 */
public abstract class BaseFigure {
	/**
	 * Receiving the figure type.
	 *
	 * @return the figure type
	 */
	public abstract FigureSet getFigureType();

	/**
	 * Check, if the current move is valid.
	 *
	 * @param loc
	 *            the location of the figure
	 *
	 * @return a flag by MoveEvent
	 *
	 * @see MoveEvent
	 */
	public abstract MoveEvent onValidMove(Coordinates loc);

	/**
	 * Check, if this figure is protected by an allied figure.
	 *
	 * @return true, if this figure is protected by an allied figure,<br>
	 *         false, otherwise
	 */
	public abstract boolean onProtectedAlly();

	/** For each move update the figure's location. */
	public abstract void updateFigureLocation(Coordinates loc);

	/**
	 * Check, if the figure is able to hit the opponents figure.
	 *
	 * @param figure
	 *            the figure to hit
	 *
	 * @return true, if the figure is able to hit the opponent's figure,<br>
	 *         false, otherwise
	 */
	public abstract <T extends BaseFigure> boolean onHitOpponent(T opponentFigure);

	/**
	 * Receiving the figure's location.
	 *
	 * @return the location of the given figure
	 */
	public abstract Coordinates getLocation();

	/**
	 * Receiving the figure's color.
	 *
	 * @return the color of the given figure
	 */
	public abstract FigureColor getFigureColor();

	/**
	 * Update the covered areas of the given figure after it's move.
	 *
	 * @param coord
	 *            the new coordinate
	 */
	public abstract void updateThreatAreas(Coordinates coord);

	/**
	 * Returning all areas which are covered by this figure.
	 *
	 * @return the covered areas as list
	 */
	public abstract List<Coordinates> getThreatAreas();

	/**
	 * Update the covered areas.
	 *
	 * @param list
	 *            the list to update
	 */
	public abstract void updateThreatList(List<Coordinates> list);

	/**
	 * Add or replace a figure which will be protected by the given figure.
	 *
	 * @param figure
	 *            the figure to protect
	 * @param orientation
	 *            the orientation of the protected figure
	 */
	public abstract <T extends BaseFigure> void updateFigureToProtect(T figure, Orientation orientation);

	/**
	 * Remove a protected figure, if this figure has been moved or beaten <br>
	 * or if the protector has been moved or beaten.
	 *
	 * @param f
	 *            the figure to remove
	 */
	public abstract void removeFigureToProtect(ProtectedFigure f);

	/**
	 * Receiving the protected figure list of the current figure.
	 *
	 * @return the protected figure list
	 */
	public abstract List<ProtectedFigure> getProtectedFigureList();

	/**
	 * Whenever the current figure has been moved or beaten, the old covered areas
	 * are going to vanish.
	 */
	public abstract void removeAllThreatAreas();

	/**
	 * Whenever the current figure has been moved or beaten, the old protected
	 * figures are going to vanish.
	 */
	public abstract void removeAllProtectedFigures();

	/**
	 * Append an area which is going to covered by the current figure.
	 *
	 * @param loc
	 *            the location to append
	 */
	public abstract void addAreaToList(Coordinates loc);

	/**
	 * Remove an area from list. This area is no longer covered by the current
	 * figure.
	 *
	 * @param loc
	 *            the location to remove
	 */
	public abstract void removeAreaFromList(Coordinates loc);

	/**
	 * Check, if this figure is able to move to a given coordinate.<br>
	 * This function is similar to {@link #onValidMove(Coordinates)}, but it's more
	 * in use, if a figure wants to move from start to destination, where the allied
	 * King may be threatened by any figure of the opponent.<br>
	 * <br>
	 * It's in use for each opponent Rook, Runner and Queen only. Every Pawn, Knight
	 * and the King itself aren't affected by this condition check.<br>
	 * <br>
	 * The original figures are not using, thus these steps below describes the
	 * algorithm:
	 * <ol>
	 * <li>create a clone of the figure to move</li>
	 * <li>set a temporary allied figure of given type to the first detected
	 * opponent figure type</li>
	 * <li>move cloned figure to destination</li>
	 * <li>update the threat list of the temporary allied figure</li>
	 * </ol>
	 *
	 * If <u>this</u> figure may now "protect" the allied King, then the opponent's
	 * figure will also threat the King. In that case the possible move of given
	 * figure T is invalid and it's impossible to move with that figure now.
	 *
	 * @param destination
	 *            the destination coordinate
	 * @param figureToUse
	 *            the figure to use
	 *
	 * @return true, if this figure is able to hit the opponent's figure,<br>
	 *         false, otherwise
	 * @throws CloneNotSupportedException
	 *             if the clone operation fails
	 */
	// FIXME: the instructions below could affect the original figure, thus this
	// method is not implemented in the final game
	public final <T extends BaseFigure> boolean onAbleToMove(final Coordinates destination, final T figureToUse) throws CloneNotSupportedException {
		boolean condition = true;
		boolean alreadyDone = false;

		final FigureColor opponentColor = FigureColor.getOpponentColor(figureToUse.getFigureColor());
		final FigureColor ownColor = figureToUse.getFigureColor();

		final FigureKing alliedKing = FigureHolder.INSTANCE.getKing(ownColor);
		@SuppressWarnings("unchecked")
		final T clonedFigure = (T) figureToUse.clone();

		for (final FigureSet tmpType : FigureSet.values()) {
			if (!alreadyDone) {
				if (tmpType == FigureSet.ROOK) {

					for (final FigureRook rookOpponent : FigureHolder.INSTANCE.getListOfAllRooks(opponentColor)) {
						if (!alreadyDone) {
							/* create an allied figure on the opponent's figure area */
							final FigureRook tmpAlliedRook = new FigureRook(rookOpponent.getLocation(), ownColor, TowerIdentification.IMPOSSIBLE_TO_CASTLE);

							/* move the cloned figure to destination */
							clonedFigure.removeAllProtectedFigures();
							clonedFigure.removeAllThreatAreas();
							Matrix.INSTANCE.removeProtections(clonedFigure, clonedFigure.getFigureColor(), clonedFigure.getFigureType());
							Matrix.INSTANCE.setNewFigureLocation(clonedFigure, destination, false);
							clonedFigure.updateThreatList(Matrix.INSTANCE.createNewThreatList(clonedFigure, destination));

							/* now update the Rook's covered areas */
							tmpAlliedRook.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpAlliedRook, tmpAlliedRook.getLocation()));

							/* and check, which figure might be protected by allied Rook */
							for (final ProtectedFigure pf : tmpAlliedRook.getProtectedFigureList()) {
								if (pf.getProtectedFigure().equals(alliedKing)) { // if the allied King has been spotted
									condition = false; // then the original opponent Rook would now threat the King
									alreadyDone = true; // which means the move of given figure T is invalid.
								}
							}
						}
					}
				} else if (tmpType == FigureSet.RUNNER) {
					for (final FigureRunner runnerOpponent : FigureHolder.INSTANCE.getListOfAllRunners(opponentColor)) {
						if (!alreadyDone) {
							/* create an allied figure on the opponent's figure area */
							final FigureRunner tmpAlliedRunner = new FigureRunner(runnerOpponent.getLocation(), ownColor, runnerOpponent.getAreaColor());

							/* move the cloned figure to destination */
							clonedFigure.removeAllProtectedFigures();
							clonedFigure.removeAllThreatAreas();
							Matrix.INSTANCE.removeProtections(clonedFigure, clonedFigure.getFigureColor(), clonedFigure.getFigureType());
							Matrix.INSTANCE.setNewFigureLocation(clonedFigure, destination, false);
							clonedFigure.updateThreatList(Matrix.INSTANCE.createNewThreatList(clonedFigure, destination));

							/* now update the Runner's covered areas */
							tmpAlliedRunner.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpAlliedRunner, tmpAlliedRunner.getLocation()));

							/* and check, which figure might be protected by allied Runner */
							for (final ProtectedFigure pf : tmpAlliedRunner.getProtectedFigureList()) {
								if (pf.getProtectedFigure().equals(alliedKing)) { // if the allied King has been spotted
									condition = false; // then the original opponent Runner would now threat the King
									alreadyDone = true; // which means the move of given figure T is invalid.
								}
							}
						}
					}
				} else if (tmpType == FigureSet.QUEEN) {

				}
			}
		}

		return condition;
	}

	/**
	 * Check, if the figure is protected at least one of any allied figure.
	 *
	 * @param figure
	 *            the figure to test
	 * @param set
	 *            the figure type
	 *
	 * @return true, if the figure is protected by a given figure type,<br>
	 *         false, otherwise
	 *
	 * @see FigureSet
	 */
	protected final <T extends BaseFigure> boolean onProtectedBySubAlly(final T figure, final FigureSet set) {
		List<ProtectedFigure> listProtected = new ArrayList<>();
		final FigureColor color = figure.getFigureColor();

		if (set == FigureSet.KING) {
			final FigureKing king = FigureHolder.INSTANCE.getKing(figure.getFigureColor());
			listProtected = king.getProtectedFigureList();

			for (final ProtectedFigure pf : listProtected) {
				if (pf.getProtectedFigure().equals(figure)) {
					return true;
				}
			}

			return false;
		} else {
			for (final BaseFigure bf : FigureHolder.INSTANCE.getListOfAllFiguresBy(set, color)) {
				listProtected = bf.getProtectedFigureList();

				for (final ProtectedFigure pf : listProtected) {
					if (pf.getProtectedFigure().equals(figure)) {
						return true;
					}
				}
			}
		}

		return false;
	}

	/**
	 * Whenever a figure has been successfully moved, where the figure's location is
	 * still the same yet, then this required properties are going to reset:<br>
	 * <br>
	 *
	 * <ul>
	 * <li>old threat areas</li>
	 * <li>old protected figures</li>
	 * <li>any other allied figure has also to remove this figure from its protected
	 * figure list, if possible</li>
	 * </ul>
	 * Also to do:
	 * <ul>
	 * <li>set the figure to it's new location (handled by
	 * {@link Matrix#setNewFigureLocation(BaseFigure, Coordinates, boolean)}</li>
	 * <li>every other allied figure has to add this figure to it's protected figure
	 * list, if possible</li>
	 * </ul>
	 *
	 * @param figure
	 *            the figure to reset
	 * @param loc
	 *            the new location of given figure
	 */
	public static final <T extends BaseFigure> void resetFigureProperties(final T figure, final Coordinates loc) {
		/* reset operations */
		figure.removeAllProtectedFigures();
		figure.removeAllThreatAreas();
		Matrix.INSTANCE.removeProtections(figure, figure.getFigureColor(), figure.getFigureType());

		/* new operation */
		Matrix.INSTANCE.setNewFigureLocation(figure, loc, false);

		// also add the figure to the protected list of all allied figures left,
		// if their area move pool contains the location of the given figure T
		final FigureColor color = figure.getFigureColor();

		for (final FigureSet tmpType : FigureSet.values()) {
			if (tmpType == FigureSet.KING) {
				final FigureKing tmpKing = FigureHolder.INSTANCE.getKing(color);
				tmpKing.removeAllProtectedFigures();
				tmpKing.removeAllThreatAreas();
				tmpKing.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpKing, tmpKing.getLocation()));

			} else {
				final List<? extends BaseFigure> figureList = FigureHolder.INSTANCE.getListOfAllFiguresBy(tmpType, color);

				for (final BaseFigure bf : figureList) {
					if (!(bf.equals(figure))) { // the used figure T needs to be ignored
						bf.removeAllThreatAreas();
						bf.removeAllProtectedFigures();
						bf.updateThreatList(Matrix.INSTANCE.createNewThreatList(bf, bf.getLocation()));
					}

					/*
					 * --------------------------------------------------------------------------
					 * FIXME: The instructions below may cause a set of not handled exceptions in
					 * Java system classes, thus every allied figure left will now refresh it's
					 * properties.
					 * --------------------------------------------------------------------------
					 */
					// if (!(bf.getFigureType() == FigureSet.KING)) { // ignore the King
					//
					// for (final Coordinates coveredArea : bf.getThreatAreas()) {
					// if (coveredArea.getCoordinatesXY().equals(loc.getCoordinatesXY())) {
					// switch (tmpType) {
					// case PAWN: // the Pawn, King and the Knight can't be
					// case KNIGHT: // used with the required orientation
					// case KING:
					// bf.updateFigureToProtect(figure, null);
					// break;
					// default: // any other figure left (Rook, Runner, Queen)
					// Matrix.INSTANCE.onInterruptProtectionChain(bf, figure,
					// bf.getProtectedFigureList());
					// } // end switch
					// } // end if (2)
					// } // end for (3)
					// } // end if (1)
				} // end for (2)
			} // end if (1)
		} // end for (1)
	} // end method
}
