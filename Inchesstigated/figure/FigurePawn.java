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
import event.MoveWatcherEvent;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.FigureSet;
import figure.properties.Modifier;
import figure.properties.PawnMoveInformation;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch
 *
 *         The class for each figure Pawn. The figure Pawn is able to move a
 *         single field upwards (white) or downwards (black) while it has been
 *         moved before. If this Pawn has not been moved before, then it's able
 *         to move two fields once only.
 *
 *         If the Pawn has reached it's last field, depending on the color of
 *         the Pawn, then this figure is able to promote to a higher leveled
 *         figure.
 *
 *         The figure Pawn may beat any figure on it's diagonal way only.
 */
public final class FigurePawn extends BaseFigure {
	/** An identification for each Pawn. */
	private final FigureSet FIGURE_TYPE = FigureSet.PAWN;

	/* properties */
	private final FigureColor pawnColor;
	private Coordinates location;
	private List<Coordinates> areasToThreat;
	private final List<ProtectedFigure> listOfProtectedFigures;
	private PawnMoveInformation info;
	private boolean flagForEnPassant;
	private boolean flagAbleToHit;

	/**
	 * The constructor to create a new figure Pawn.
	 *
	 * @param color
	 *            the color of the figure
	 * @param loc
	 *            the location of the figure
	 */
	public FigurePawn(final FigureColor color, final Coordinates loc) {
		this.areasToThreat = new ArrayList<>(2);
		this.listOfProtectedFigures = new ArrayList<>(2);
		this.info = PawnMoveInformation.NOT_MOVED_YET;
		this.flagForEnPassant = false;

		this.pawnColor = color;
		this.location = loc;
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

	/**
	 * Check, if this Pawn is able to hit an opponent Pawn by using "en passant".
	 *
	 * @return true, if this Pawn may hit an opponent Pawn by "en passant",<br>
	 *         false, otherwise
	 */
	public boolean onEnPassant() {
		final FigureColor opponentColor = FigureColor.getOpponentColor(this.pawnColor);
		final BaseFigure lastUsedFigure = MoveWatcherEvent.getLastUsedFigure(opponentColor);

		if (lastUsedFigure.getFigureType() == FigureSet.PAWN) {
			final FigurePawn opponentPawn = (FigurePawn) lastUsedFigure;

			if (opponentPawn.getMoveInformation() == PawnMoveInformation.MOVED_TWO_FIELDS) {
				if (Matrix.INSTANCE.onPawnNeighbors(this, opponentPawn)) {
					this.flagForEnPassant = true;
				} else {
					this.flagForEnPassant = false;
				}
			}
		}

		return this.flagForEnPassant;
	}

	/**
	 * Update the flag "en passant".
	 *
	 * @param flag
	 *            the flag to set
	 */
	public void setEnPassantFlag(final boolean flag) {
		this.flagForEnPassant = flag;
	}

	/**
	 * Check, if this Pawn has reached it's last area to become to promote.
	 *
	 * @return true, if this Pawn has reached it's last area,<br>
	 *         false, otherwise
	 */
	public boolean onLastCoordinate() {
		return Matrix.INSTANCE.onLastMatrixCoordinate(this.location, this);
	}

	/**
	 * Promote this Pawn to a higher leveled figure. The Pawn may transformed to a:
	 * <ul>
	 * <li>Rook (Tower)</li>
	 * <li>Runner</li>
	 * <li>Knight</li>
	 * <li>Queen</li>
	 * </ul>
	 *
	 * @param newFigure
	 *            the selected figure
	 *
	 * @return the new created figure
	 */
	@SuppressWarnings("incomplete-switch")
	public BaseFigure promotePawn(final FigureSet newFigure) {
		BaseFigure tmpFigure = null;

		if (this.onLastCoordinate()) {
			switch (newFigure) {
				case KNIGHT:
					tmpFigure = new FigureKnight(this.location, this.pawnColor);
					FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpFigure);
					FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, this);
					break;
				case QUEEN:
					tmpFigure = new FigureQueen(this.location, this.pawnColor);
					FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpFigure);
					FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, this);

					break;
				case ROOK:
					tmpFigure = new FigureRook(this.location, this.pawnColor, TowerIdentification.IMPOSSIBLE_TO_CASTLE);

					FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpFigure);
					FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, this);
					break;
				case RUNNER:
					tmpFigure = new FigureRunner(this.location, this.pawnColor, Matrix.INSTANCE.getAreaColorOfPawn(this));

					FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpFigure);
					FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, this);
					break;
			}
		}

		return tmpFigure;
	}

	/**
	 * Receiving the last information for this Pawn.
	 *
	 * @return the last move information for this Pawn.
	 */
	private PawnMoveInformation getMoveInformation() {
		return this.info;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureLocation()
	 */
	@Override
	public void updateFigureLocation(final Coordinates loc) {
		this.location = loc;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onHitOpponent(figure.BaseFigure)
	 */
	@Override
	public <T extends BaseFigure> boolean onHitOpponent(final T opponentFigure) {
		this.flagAbleToHit = true;
		final FigureColor color = opponentFigure.getFigureColor();

		if ((opponentFigure.getFigureType() == FigureSet.KING) || (color == this.pawnColor)) {
			this.flagAbleToHit = false;
		}

		// TODO: check, if the King may be threatened after it's move

		return this.flagAbleToHit;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getLocation()
	 */
	@Override
	public Coordinates getLocation() {
		return this.location;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getFigureColor()
	 */
	@Override
	public FigureColor getFigureColor() {
		return this.pawnColor;
	}

	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatAreas(location.Coordinates)
	 */
	@Override
	public void updateThreatAreas(final Coordinates coord) {
		// always remove old entries
		this.areasToThreat.clear();

		// update areas to threat
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

		if (nbrOfElements < 2) {
			final ProtectedFigure tmp = new ProtectedFigure(figure, figure.getLocation(), orientation);
			this.listOfProtectedFigures.add(tmp);

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
		if (Matrix.INSTANCE.onFreeAreas(this, this.location, loc)) {
			final int distance = Math.abs((loc.getPosY().charAt(0) - this.location.getPosY().charAt(0)));

			if (this.onFirstMove() && (distance == 2)) {
				this.updateMoveInformation(PawnMoveInformation.MOVED_TWO_FIELDS);
			} else {
				this.updateMoveInformation(PawnMoveInformation.MOVED_ONE_FIELD);
			}

			return MoveEvent.SUCCESSFULLY_MOVE;
		}

		return MoveEvent.INCORRECT_MOVE;
	}

	/**
	 * Update the move information for this Pawn.
	 *
	 * @param info
	 *            the new information for this Pawn
	 */
	private void updateMoveInformation(final PawnMoveInformation info) {
		this.info = info;
	}

	/**
	 * Check, if this Pawn has not been moved before. If true, then it may move two
	 * fields once only.
	 *
	 * @return true, if and only if the current Pawn has not been moved before,<br>
	 *         false, otherwise
	 */
	public boolean onFirstMove() {
		return (PawnMoveInformation.NOT_MOVED_YET == this.info);
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
}
