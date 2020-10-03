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
import figure.properties.FigureSet;
import figure.properties.ProtectedFigure;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch
 *
 *         The class for a figure Queen. The Queen is the most powerful figure
 *         of the chess field. It may move like a Rook or a Runner. The Queen
 *         may protect up to eight allied figures.
 */
public final class FigureQueen extends BaseFigure {
	/** An identification for each Queen. */
	private final FigureSet FIGURE_TYPE = FigureSet.QUEEN;

	/* basic properties */
	private Coordinates loc;
	private final FigureColor colorQueen;
	private List<Coordinates> areaMovepool;
	private final List<ProtectedFigure> protectedFigureList;

	/**
	 * Constructor unit for each new created figure Queen.
	 *
	 * @param loc
	 *            the location of the Queen
	 * @param color
	 *            the Queen's color
	 */
	public FigureQueen(final Coordinates loc, final FigureColor color) {
		this.areaMovepool = new ArrayList<>();
		this.protectedFigureList = new ArrayList<>(8);

		this.loc = loc;
		this.colorQueen = color;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getFigureType()
	 */
	@Override
	public FigureSet getFigureType() {
		return this.FIGURE_TYPE;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onValidMove(location.Coordinates)
	 */
	@Override
	public MoveEvent onValidMove(final Coordinates loc) {
		if (Matrix.INSTANCE.onFreeAreas(this, this.loc, loc)) {
			return MoveEvent.SUCCESSFULLY_MOVE;
		}

		return MoveEvent.INCORRECT_MOVE;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onProtectedAlly()
	 */
	@Override
	public boolean onProtectedAlly() {
		boolean flagProtected = false;

		for (final FigureSet set : FigureSet.values()) {
			if (this.onProtectedBySubAlly(this, set)) {
				flagProtected = true;
				break;
			}
		}

		return flagProtected;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureLocation(location.Coordinates)
	 */
	@Override
	public void updateFigureLocation(final Coordinates loc) {
		this.loc = loc;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onHitOpponent(figure.BaseFigure)
	 */
	@Override
	public <T extends BaseFigure> boolean onHitOpponent(final T opponentFigure) {
		final FigureColor color = opponentFigure.getFigureColor();

		if ((color == this.colorQueen) || (opponentFigure.getFigureType() == FigureSet.KING)) {
			return false;
		}

		return true;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getLocation()
	 */
	@Override
	public Coordinates getLocation() {
		return this.loc;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getFigureColor()
	 */
	@Override
	public FigureColor getFigureColor() {
		return this.colorQueen;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatAreas(location.Coordinates)
	 */
	@Override
	public void updateThreatAreas(final Coordinates coord) {
		this.areaMovepool.clear();
		this.areaMovepool = Matrix.INSTANCE.createNewThreatList(this, coord);
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getThreatAreas()
	 */
	@Override
	public List<Coordinates> getThreatAreas() {
		return this.areaMovepool;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatList(java.util.List)
	 */
	@Override
	public void updateThreatList(final List<Coordinates> list) {
		this.areaMovepool = list;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureToProtect(figure.BaseFigure,
	 * location.Orientation)
	 */
	@Override
	public <T extends BaseFigure> void updateFigureToProtect(final T figure, final Orientation orientation) {
		final int nbrOfElements = this.protectedFigureList.size();

		if (nbrOfElements < 8) {
			final ProtectedFigure pf = new ProtectedFigure(figure, figure.getLocation(), orientation);
			this.protectedFigureList.add(pf);

			this.removeAreaFromList(figure.getLocation());
		} else {
			Matrix.INSTANCE.onInterruptProtectionChain(this, figure, this.protectedFigureList);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * figure.BaseFigure#removeFigureToProtect(figure.properties.ProtectedFigure)
	 */
	@Override
	public void removeFigureToProtect(final ProtectedFigure f) {
		this.protectedFigureList.remove(f);
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getProtectedFigureList()
	 */
	@Override
	public List<ProtectedFigure> getProtectedFigureList() {
		return this.protectedFigureList;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAllThreatAreas()
	 */
	@Override
	public void removeAllThreatAreas() {
		this.areaMovepool.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAllProtectedFigures()
	 */
	@Override
	public void removeAllProtectedFigures() {
		this.protectedFigureList.clear();
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#addAreaToList(location.Coordinates)
	 */
	@Override
	public void addAreaToList(final Coordinates loc) {
		this.areaMovepool.add(loc);
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAreaFromList(location.Coordinates)
	 */
	@Override
	public void removeAreaFromList(final Coordinates loc) {
		this.areaMovepool.remove(loc);
	}
}
