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
import figure.properties.FigureHolder;
import figure.properties.FigureSet;
import figure.properties.KingDestinationNotifier;
import figure.properties.KingMoveInformation;
import figure.properties.ProtectedFigure;
import interfaces.CastlingInterface;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch
 *
 *         The definition for each figure King. The King is the most important
 *         figure of the game. It way move one field only an can be threatened
 *         by any opponent figure.
 *
 *         While the King and one of it's Rook hasn't moved before and if all
 *         areas between King and Rook are not covered by any opponent's figure
 *         and are not reserved by any allied figure and if the King is also not
 *         threatened, then a castling is able to do.
 *
 *         If the King is not possible to move to another field while it's
 *         threatened or any other allied figure is unable to block the current
 *         threat, then the game is over.
 */
public final class FigureKing extends BaseFigure implements CastlingInterface {
	/** An identification for each King. */
	private final FigureSet FIGURE_TYPE = FigureSet.KING;
	
	/* properties */
	private final KingMoveInformation kingInfo;
	private CastlingState castlingState;
	private KingDestinationNotifier destinationKing;
	
	private Coordinates loc;
	private final FigureColor colorKing;
	private boolean threatened;
	private boolean flagAbleToHit;
	private BaseFigure threatFigure;
	private static BaseFigure spottedFigure;
	private List<Coordinates> kingsCircle;
	private final List<ProtectedFigure> listOfProtectedFigures;
	
	/**
	 * The constructor for each King.
	 *
	 * @param loc
	 *            the location
	 * @param color
	 *            the color of the King
	 */
	public FigureKing(final Coordinates loc, final FigureColor color) {
		this.kingInfo = KingMoveInformation.NOT_MOVED_BEFORE;
		this.destinationKing = KingDestinationNotifier.FIELD_IS_FREE;
		FigureKing.spottedFigure = null;
		this.castlingState = CastlingState.STILL_POSSIBLE;
		this.listOfProtectedFigures = new ArrayList<>(8);
		this.kingsCircle = new ArrayList<>(8);
		
		this.loc = loc;
		this.colorKing = color;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getFigureType()
	 */
	@Override
	public FigureSet getFigureType() {
		return this.FIGURE_TYPE;
	}
	
	/**
	 * Update the circle fields of this King.
	 *
	 * @param loc
	 *            the new location of this King
	 */
	public void updateCircleFields(final Coordinates loc) {
		this.kingsCircle.clear();
		this.kingsCircle = Matrix.INSTANCE.createNewThreatList(this, loc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#onProtectedAlly()
	 */
	@Override
	public boolean onProtectedAlly() {
		/* Won't work for the King. */
		return false;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureLocation()
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
		FigureColor color = opponentFigure.getFigureColor();
		if (color == this.colorKing) {
			return false;
		}
		
		/* predefined modification: the King is able to hit any figure */
		this.flagAbleToHit = true;
		
		/* check, if the opponent figure is not protected by any other figure */
		color = FigureColor.getOpponentColor(this.colorKing);
		boolean alreadyDone = false;
		
		if (this.onOpponentProtection(opponentFigure, FigureHolder.INSTANCE.getKing(color))) {
			this.flagAbleToHit = false;
			alreadyDone = true;
		}
		
		if (!alreadyDone) {
			for (final FigureSet tmpType : FigureSet.values()) {
				if (!(tmpType == FigureSet.KING)) { // ignore the King
					
					final List<? extends BaseFigure> figureList = FigureHolder.INSTANCE.getListOfAllFiguresBy(tmpType, color);
					
					for (final BaseFigure bf : figureList) {
						if (this.onOpponentProtection(opponentFigure, bf)) {
							this.flagAbleToHit = false;
							alreadyDone = true;
							break;
						}
					}
				}
			}
		}
		
		return this.flagAbleToHit;
	}
	
	/**
	 * Check, if this King is able to hit any figure, where the given figure may be
	 * protected by any other figure.
	 *
	 * @param opponentFigure
	 *            the figure to hit
	 * @param bf
	 *            this figure may protect opponentFigure
	 * @return true, if opponentFigure is not protected by given bf,<br>
	 *         false, otherwise
	 */
	private <T extends BaseFigure> boolean onOpponentProtection(final T opponentFigure, final BaseFigure bf) {
		final List<ProtectedFigure> protectedFigureListBy = bf.getProtectedFigureList();
		
		for (final ProtectedFigure pf : protectedFigureListBy) {
			if (pf.getProtectedFigure().equals(opponentFigure)) {
				return true;
			}
		}
		
		return false;
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
		return this.colorKing;
	}
	
	/**
	 * Check, if this King is able to do a castling with the given Rook.
	 *
	 * @param rook
	 *            the Rook to compare
	 *
	 * @return true, if all required conditions were satisfied,<br>
	 *         false, otherwise
	 */
	public boolean onCastling(final FigureRook rook) {
		this.castlingState = CastlingEvent.INSTANCE.onCastlingPossibility(this, rook);
		return (this.castlingState == CastlingState.STILL_POSSIBLE);
	}
	
	/*
	 * (non-Javadoc)
	 * @see interfaces.CastlingInterface.markToImpossibleCastling()
	 */
	@Override
	public void markToImpossibleCastling() {
		this.castlingState = CastlingState.IMPOSSIBLE;
	}
	
	/*
	 * (non-Javadoc)
	 * @see interfaces.CastlingInterface.onAlreadyMoved()
	 */
	@Override
	public boolean onAlreadyMoved() {
		return (this.kingInfo != KingMoveInformation.NOT_MOVED_BEFORE);
	}
	
	/**
	 * Receiving the castling state for this King.
	 *
	 * @return the King's castling state
	 */
	public CastlingState getCastlingState() {
		return this.castlingState;
	}
	
	/**
	 * Add the given figure as threat for this King.
	 *
	 * @param figure
	 *            the figure to add
	 */
	public <T extends BaseFigure> void addKingsThreat(final T figure) {
		this.threatened = true;
		this.threatFigure = figure;
	}
	
	/**
	 * Remove the King's threat.
	 */
	public void removeThreat() {
		this.threatFigure = null;
		this.threatened = false;
	}
	
	/**
	 * Check, if this King is unable to move.
	 *
	 * @return true, if this King is unable to move,<br>
	 *         false, otherwise
	 */
	public boolean onImpossibleMove() {
		return (this.kingInfo == KingMoveInformation.IMPOSSIBLE_TO_MOVE);
	}
	
	/**
	 * Check, if this King is threatened by any opponent's figure.
	 *
	 * @return true, if this King is threatened,<br>
	 *         false, otherwise
	 */
	public boolean onThreatened() {
		if (this.threatened) { // just check, if the King is still threatened
			return true;
		}
		
		final FigureColor color = FigureColor.getOpponentColor(this.colorKing);
		boolean justCompleted = false;
		
		for (final FigureSet set : FigureSet.values()) {
			if (set != this.FIGURE_TYPE) {
				final List<? extends BaseFigure> figureList = FigureHolder.INSTANCE.getListOfAllFiguresBy(set, color);
				
				for (final BaseFigure bf : figureList) {
					if (!justCompleted) {
						for (final Coordinates c : bf.getThreatAreas()) {
							if (c.getCoordinatesXY().equals(this.loc.getCoordinatesXY())) {
								justCompleted = true;
								this.threatened = true;
								this.addKingsThreat(bf);
								break;
							}
						}
					}
				}
			}
			
			if (justCompleted) {
				break;
			}
		}
		
		return this.threatened;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatAreas(location.Coordinates)
	 */
	@Override
	public void updateThreatAreas(final Coordinates coord) {
		this.kingsCircle.clear();
		this.kingsCircle = Matrix.INSTANCE.createNewThreatList(this, coord);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#getThreatAreas()
	 */
	@Override
	public List<Coordinates> getThreatAreas() {
		return this.kingsCircle;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateThreatList(java.util.List)
	 */
	@Override
	public void updateThreatList(final List<Coordinates> list) {
		this.kingsCircle = list;
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#updateFigureToProtect(figure.BaseFigure,
	 * location.Orientation)
	 */
	@Override
	public <T extends BaseFigure> void updateFigureToProtect(final T figure, final Orientation orientation) {
		final int nbrOfProtectedFigures = this.listOfProtectedFigures.size();
		
		if (nbrOfProtectedFigures < 8) {
			final ProtectedFigure pf = new ProtectedFigure(figure, figure.getLocation(), orientation);
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
		this.kingsCircle.clear();
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
		this.kingsCircle.add(loc);
	}
	
	/*
	 * (non-Javadoc)
	 * @see figure.BaseFigure#removeAreaFromList(location.Coordinates)
	 */
	@Override
	public void removeAreaFromList(final Coordinates loc) {
		this.kingsCircle.remove(loc);
	}
	
	/**
	 *
	 * Check, if the last move is valid. There are three possible cases:
	 * <ul>
	 * <li><b>FIELD_IS_FREE</b>: this field is free, but may be covered by any
	 * opponent's figure</li>
	 * <li><b>FIELD_IS_BLOCKED_BY_ALLY</b>: this field is blocked by any allied
	 * figure</li>
	 * <li><b>FIELD_IS_BLOCKED_BY_FOE</b>: this field is blocked by any opponent's
	 * figure</li>
	 * </ul>
	 *
	 * @param loc
	 *            the new location
	 *
	 * @return SUCESSFULLY_MOVE, if successful,<br>
	 *         INCORRECT_MOVE, otherwise
	 *
	 * @see figure.BaseFigure#onValidMove(location.Coordinates)
	 * @see figure.properties.KingDestinationNotifier
	 */
	@Override
	public MoveEvent onValidMove(final Coordinates loc) {
		MoveEvent moveResult = MoveEvent.SUCCESSFULLY_MOVE;
		
		/*
		 * identify the destination area and update the destination notifier for this
		 * King
		 */
		Matrix.INSTANCE.onFreeAreas(this, this.loc, loc);
		
		switch (this.destinationKing) {
			case FIELD_IS_FREE: {
				boolean alreadyDone = false;
				
				/* check, if this field is not covered by any opponent figure */
				final FigureColor opponentColor = FigureColor.getOpponentColor(this.colorKing);
				
				/* check, if this King is not inside of the opponent's Kings circle */
				if (!this.onPossibleKingMove(loc, FigureHolder.INSTANCE.getKing(opponentColor))) {
					moveResult = MoveEvent.INCORRECT_MOVE;
					alreadyDone = true;
				}
				
				/*
				 * also check, if the new field is not covered by another opponent figure, if
				 * necessary
				 */
				if (!alreadyDone && (moveResult == MoveEvent.SUCCESSFULLY_MOVE)) {
					for (final FigurePawn tmpPawn : FigureHolder.INSTANCE.getListOfAllPawns(opponentColor)) {
						if (!this.onPossibleKingMove(loc, tmpPawn)) {
							moveResult = MoveEvent.INCORRECT_MOVE;
							alreadyDone = true;
						}
					}
				}
				
				if (!alreadyDone && (moveResult == MoveEvent.SUCCESSFULLY_MOVE)) {
					for (final FigureKnight tmpKnight : FigureHolder.INSTANCE.getListOfAllKnights(opponentColor)) {
						if (!this.onPossibleKingMove(loc, tmpKnight)) {
							moveResult = MoveEvent.INCORRECT_MOVE;
							alreadyDone = true;
						}
					}
				}
				
				if (!alreadyDone && (moveResult == MoveEvent.SUCCESSFULLY_MOVE)) {
					for (final FigureRook tmpRook : FigureHolder.INSTANCE.getListOfAllRooks(opponentColor)) {
						if (!this.onPossibleKingMove(loc, tmpRook)) {
							moveResult = MoveEvent.INCORRECT_MOVE;
							alreadyDone = true;
						}
					}
				}
				
				if (!alreadyDone && (moveResult == MoveEvent.SUCCESSFULLY_MOVE)) {
					for (final FigureRunner tmpRunner : FigureHolder.INSTANCE.getListOfAllRunners(opponentColor)) {
						if (!this.onPossibleKingMove(loc, tmpRunner)) {
							moveResult = MoveEvent.INCORRECT_MOVE;
							alreadyDone = true;
						}
					}
				}
				
				if (!alreadyDone && (moveResult == MoveEvent.SUCCESSFULLY_MOVE)) {
					for (final FigureQueen tmpQueen : FigureHolder.INSTANCE.getListOfAllQueens(opponentColor)) {
						if (!this.onPossibleKingMove(loc, tmpQueen)) {
							moveResult = MoveEvent.INCORRECT_MOVE;
							alreadyDone = true;
						}
					}
				}
				
				break;
			}
			case FIELD_IS_BLOCKED_BY_ALLY: {
				moveResult = MoveEvent.INCORRECT_MOVE;
				break;
			}
			case FIELD_IS_BLOCKED_BY_OPPONENT: {
				/* check, if the opponent's figure is not protected by any other figure */
				if (!this.onHitOpponent(FigureKing.spottedFigure)) {
					moveResult = MoveEvent.INCORRECT_MOVE;
				}
				
				break;
			}
		}
		
		return moveResult;
	}
	
	/**
	 * Check, if this King is able to move to the given location. It's a save area,
	 * if this is not covered by any opponent's figure.
	 *
	 * @param loc
	 *            the new possible location
	 * @param figure
	 *            the figure to compare
	 * @return true, if this field is save,<br>
	 *         false, otherwise
	 */
	private <T extends BaseFigure> boolean onPossibleKingMove(final Coordinates loc, final T figure) {
		final List<Coordinates> threatAreasBy = figure.getThreatAreas();
		
		/* check, if the opponent figure covers the new location of this King */
		for (final Coordinates c : threatAreasBy) {
			if (loc.getCoordinatesXY().equals(c.getCoordinatesXY())) {
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Whenever this King is threatened, a list of possible escape routes are going
	 * to create, if the King is required to move.
	 *
	 * @return a list of possible escape routes for this King
	 */
	public List<Coordinates> createPossibleEscapeFields() {
		final List<Coordinates> routes = new ArrayList<>();
		
		for (final Coordinates c : this.kingsCircle) {
			final MoveEvent subEvent = this.onValidMove(c);
			
			if (subEvent == MoveEvent.SUCCESSFULLY_MOVE) {
				routes.add(c);
			}
		}
		
		return routes;
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
	
	/**
	 * For debug: Receiving the figure which threats this King.
	 */
	public BaseFigure getThreatFigure() {
		return this.threatFigure;
	}
	
	/**
	 * Update the destination notification property by given informations.
	 *
	 * @param info
	 *            the new information
	 * @param spotted
	 *            the spotted figure
	 */
	public void updateDestinationNotifier(final KingDestinationNotifier info, final BaseFigure spotted) {
		this.destinationKing = info;
		FigureKing.spottedFigure = spotted;
	}
}
