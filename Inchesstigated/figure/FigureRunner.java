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
 *         The definition for each figure Runner. This figure type is able to
 *         move in a diagonal way only. This figure is not able to hit any
 *         figure which is on an opposite area color, which means a Runner on a
 *         black area may hit figures only, which are also on a black area.
 */
public final class FigureRunner extends BaseFigure {
	/** An identification for each Runner. */
	private final FigureSet FIGURE_TYPE = FigureSet.RUNNER;

	/* basic properties */
	private Coordinates loc;
	private final FigureColor colorRunner;
	private final FigureColor areaColor;
	private List<Coordinates> areaMovepool;
	private final List<ProtectedFigure> protectedFigureList;

	/**
	 * Create a new figure Runner, where also a figure Pawn can be promoted to a
	 * figure Runner.
	 *
	 * @param loc
	 *            the Runner's location
	 * @param color
	 *            the Runner's color
	 * @param areaColor
	 *            the color of the area where this Runner may move only on these
	 *            fields
	 */
	public FigureRunner(final Coordinates loc, final FigureColor color, final FigureColor areaColor) {
		this.areaMovepool = new ArrayList<>();
		this.protectedFigureList = new ArrayList<>(4);

		this.loc = loc;
		this.colorRunner = color;
		this.areaColor = areaColor;
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

		if ((opponentFigure.getFigureType() == FigureSet.KING) || (color == this.colorRunner)) {
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
		return this.colorRunner;
	}

	/**
	 * Receiving the area color for this Runner.
	 *
	 * @return the area color
	 */
	public FigureColor getAreaColor() {
		return this.areaColor;
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

		if (nbrOfElements < 4) {
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
