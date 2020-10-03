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

import event.CastlingEvent;
import event.CastlingEvent.CastlingState;
import event.MoveEvent;
import figure.properties.FigureColor;
import figure.properties.FigureSet;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;
import figure.properties.TowerSpawnLocation;
import interfaces.CastlingInterface;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch
 *
 *         The class for each figure Rook (Tower). This figure is able to move
 *         in horizontal or vertical way only, but it may move up to seven
 *         fields for a single direction.
 *
 *         This figure is also able to do a castling with it's allied King,
 *         unless this Rook or the its King hasn't been moved before.
 */
public final class FigureRook extends BaseFigure implements CastlingInterface {
	/* basic properties */
	private final FigureSet FIGURE_TYPE = FigureSet.ROOK;
	private Coordinates loc;
	private final FigureColor colorRook;
	private List<Coordinates> areaMovepool;
	private final List<ProtectedFigure> listOfProtectedFigures;
	private final TowerIdentification ID;
	private CastlingEvent.CastlingState state;
	private TowerSpawnLocation spawnLoc;

	/**
	 * The constructor for each figure Rook.
	 *
	 * @param location
	 *            the location for this Rook
	 * @param color
	 *            the color for this Rook
	 * @param ID
	 *            an identification, if this Rook is able to do a castling
	 */
	public FigureRook(final Coordinates location, final FigureColor color, final TowerIdentification ID) {
		this.areaMovepool = new ArrayList<>();
		this.listOfProtectedFigures = new ArrayList<>(4);
		this.loc = location;
		this.colorRook = color;
		this.ID = ID;

		if (ID == TowerIdentification.POSSIBLE_TO_CASTLE) {
			this.state = CastlingState.STILL_POSSIBLE;
		} else {
			this.state = CastlingState.IMPOSSIBLE;
		}

		final String currentLocation = this.loc.getCoordinatesXY();

		if (this.colorRook == FigureColor.WHITE) {
			if (currentLocation.equals("A1")) {
				this.spawnLoc = TowerSpawnLocation.LEFT_SIDE;
			} else if (currentLocation.equals("H1")) {
				this.spawnLoc = TowerSpawnLocation.RIGHT_SIDE;
			}
		} else {
			if (currentLocation.equals("A8")) {
				this.spawnLoc = TowerSpawnLocation.LEFT_SIDE;
			} else if (currentLocation.equals("H8")) {
				this.spawnLoc = TowerSpawnLocation.RIGHT_SIDE;
			}
		}
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
	 * @see interfaces.CastlingInterface.markToImpossibleCastling()
	 */
	@Override
	public void markToImpossibleCastling() {
		this.state = CastlingState.IMPOSSIBLE;
	}

	/**
	 * Check, if this Rook is able to do a castling with it's King.
	 *
	 * @param king
	 *            the King to compare
	 *
	 * @return true, if the receiving state is equal to
	 *         "CastlingState.STILL_POSSIBLE", <br>
	 *         otherwise false
	 */
	public boolean onCastling(final figure.FigureKing king) {
		this.state = CastlingEvent.INSTANCE.onCastlingPossibility(king, this);

		return (this.state == CastlingState.STILL_POSSIBLE);
	}

	/**
	 * Receiving the ID for this Rook.
	 *
	 * @param this
	 *            rook ID
	 */
	public TowerIdentification getTowerIdentification() {
		return this.ID;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onProtectedAlly()
	 */
	@Override
	public boolean onProtectedAlly() {
		boolean flagProtected = false;

		for (final FigureSet s : FigureSet.values()) {
			if (this.onProtectedBySubAlly(this, s)) {
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

		if (((opponentFigure.getFigureType() == FigureSet.KING) || (color == this.colorRook))) {
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
		return this.colorRook;
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
		final int nbrOfElements = this.listOfProtectedFigures.size();

		if (nbrOfElements < 4) {
			final ProtectedFigure pf = new ProtectedFigure(figure, figure.getLocation(), orientation);
			this.listOfProtectedFigures.add(pf);

			this.removeAreaFromList(figure.getLocation());
		} else if (nbrOfElements == 4) {
			/*
			 * Whenever an another allied figure has been moved inside of the range of this
			 * Rook (also for Runner and Queen), then this figure is now protected by this
			 * Rook. Thus, the protected list is going to update.
			 */
			Matrix.INSTANCE.onInterruptProtectionChain(this, figure, this.listOfProtectedFigures);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAllThreatAreas()
	 */
	@Override
	public void removeAllThreatAreas() {
		this.areaMovepool.clear();
	}

	/**
	 * Receiving a boolean state, if this Rook is on it's correct required position.
	 *
	 * @param cmp
	 *            the state to compare
	 *
	 * @return true, if cmp is equal to the Rook's ID,<br>
	 *         false, otherwise
	 */
	public boolean onCorrectLocationIdentification(final TowerSpawnLocation cmp) {
		return (this.spawnLoc == cmp);
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

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onValidMove(location.Coordinates)
	 */
	@Override
	public MoveEvent onValidMove(final Coordinates loc) {
		if (Matrix.INSTANCE.onFreeAreas(this, this.loc, loc)) {
			this.markToImpossibleCastling();
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

	/**
	 * Receiving the spawn location for this figure Rook.
	 *
	 * @return the spawn location for this Rook
	 */
	public TowerSpawnLocation getSpawnLocation() {
		return this.spawnLoc;
	}

	/*
	 * (non-Javadoc)
	 * @see interfaces.CastlingInterface#onAlreadyMoved()
	 */
	@Override
	public boolean onAlreadyMoved() {
		return (this.state != CastlingState.STILL_POSSIBLE);
	}
}
