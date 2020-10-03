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
import figure.properties.MoveSetKnight;
import figure.properties.ProtectedFigure;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch
 *
 *         The class for each figure Knight. This figure is able to move two
 *         fields in a direction followed by a side step. The Knight is also
 *         able to jump over other figures.
 */
public final class FigureKnight extends BaseFigure {
	/* basic properties */
	private final FigureSet FIGURE_TYPE = FigureSet.KNIGHT;
	private Coordinates loc;
	private final FigureColor colorKnight;
	private List<Coordinates> areasToThreat;
	private final List<ProtectedFigure> listOfProtectedFigures;
	
	/**
	 * Create a new Knight on the field.
	 *
	 * @param location
	 *            the location for this Knight
	 * @param color
	 *            the color of this Knight
	 */
	public FigureKnight(final Coordinates location, final FigureColor color) {
		this.areasToThreat = new ArrayList<>(8);
		this.listOfProtectedFigures = new ArrayList<>(8);
		
		this.loc = location;
		this.colorKnight = color;
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
	 * @see figure.BaseFigure#onHitOpponent(figure.BaseFigure)
	 */
	@Override
	public <T extends BaseFigure> boolean onHitOpponent(final T opponentFigure) {
		final FigureColor color = opponentFigure.getFigureColor();
		final FigureSet type = opponentFigure.getFigureType();
		
		return ((type != FigureSet.KING) && (this.colorKnight != color));
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
		return this.colorKnight;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatAreas(location.Coordinates)
	 */
	@Override
	public void updateThreatAreas(final Coordinates coord) {
		this.areasToThreat.clear();
		this.areasToThreat = Matrix.INSTANCE.createNewThreatList(this, coord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getThreatAreas()
	 */
	@Override
	public List<Coordinates> getThreatAreas() {
		return this.areasToThreat;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatList(java.util.List)
	 */
	@Override
	public void updateThreatList(final List<Coordinates> list) {
		this.areasToThreat = list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureToProtect(figure.BaseFigure,
	 * location.Orientation)
	 */
	@Override
	public <T extends BaseFigure> void updateFigureToProtect(final T figure, final Orientation orientation) {
		final int nbrOfElements = this.listOfProtectedFigures.size();
		
		if (nbrOfElements < 8) {
			final ProtectedFigure pf = new ProtectedFigure(figure, figure.getLocation());
			this.listOfProtectedFigures.add(pf);

			this.removeAreaFromList(figure.getLocation());
		}
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAllThreatAreas()
	 */
	@Override
	public void removeAllThreatAreas() {
		this.areasToThreat.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAllProtectedFigures()
	 */
	@Override
	public void removeAllProtectedFigures() {
		this.listOfProtectedFigures.clear();
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#addAreaToList(location.Coordinates)
	 */
	@Override
	public void addAreaToList(final Coordinates loc) {
		this.areasToThreat.add(loc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAreaFromList(location.Coordinates)
	 */
	@Override
	public void removeAreaFromList(final Coordinates loc) {
		this.areasToThreat.remove(loc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onValidMove(location.Coordinates)
	 */
	@Override
	public MoveEvent onValidMove(final Coordinates loc) {
		if (Matrix.INSTANCE.onValidKnightMove(loc, this, MoveSetKnight.getFullKnightMoveList())) {
			return MoveEvent.SUCCESSFULLY_MOVE;
		}
		
		return MoveEvent.INCORRECT_MOVE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeFigureToProtect(figure.ProtectedFigure)
	 */
	@Override
	public void removeFigureToProtect(final ProtectedFigure f) {
		this.listOfProtectedFigures.remove(f);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getProtectedFigureList()
	 */
	@Override
	public List<ProtectedFigure> getProtectedFigureList() {
		return this.listOfProtectedFigures;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureLocation(location.Coordinates)
	 */
	@Override
	public void updateFigureLocation(final Coordinates loc) {
		this.loc = loc;
	}
}
