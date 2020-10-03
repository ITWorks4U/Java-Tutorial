/**
 * @package: gui
 * @project: chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package gui;

import java.util.List;

import event.CastlingEvent;
import event.CastlingEvent.CastlingState;
import event.CastlingEvent.CastlingType;
import event.MoveEvent;
import event.MoveWatcherEvent;
import figure.BaseFigure;
import figure.FigureKing;
import figure.FigureKnight;
import figure.FigurePawn;
import figure.FigureQueen;
import figure.FigureRook;
import figure.FigureRunner;
import figure.properties.AreaColor;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.FigureSet;
import figure.properties.Modifier;
import figure.properties.TowerIdentification;
import figure.properties.TowerSpawnLocation;
import gui.MoveHistory.MoveType;
import location.Coordinates;
import location.Matrix;

/**
 * @author swunsch<br>
 *         <br>
 *
 *         This class is representing the "interface" between GUI and core. It's
 *         in use to create the required figures and to do the move sequence
 *         from user interface.<br>
 *         <br>
 *
 *         Each figure comes with a basic set of unique steps for a valid move,
 *         where also a unique set of special moves are in use, depending on the
 *         figure itself.<br>
 *         <br>
 *
 *         For example: A figure Pawn may be promoted to a higher leveled
 *         figure, if the last field has been reached, depending on the color of
 *         the Pawn.
 */
public enum FigureProcedure {
	INSTANCE;

	/* all figures in use */
	private FigurePawn[] whitePawn = null; // up to eight white Pawns
	private FigurePawn[] blackPawn = null; // up to eight black Pawns
	private FigureRook[] whiteRook = null; // start with two white Rooks
	private FigureRook[] blackRook = null; // start with two black Rooks
	private FigureRunner[] whiteRunner = null; // start with two white Runners
	private FigureRunner[] blackRunner = null; // start with two black Runners
	private FigureKnight[] whiteKnight = null; // start with two white Knights
	private FigureKnight[] blackKnight = null; // start with two black Knights
	private FigureQueen whiteQueen = null; // start with a single white Queen
	private FigureQueen blackQueen = null; // start with a single black Queen
	private FigureKing whiteKing = null; // start with a single white King
	private FigureKing blackKing = null; // start with a single black King

	/* other properties to use */
	private static final int NBR_OF_PAWNS = 8;
	private static final int NBR_OF_DOUBLE_FIGURES = 2;
	private MoveState moveState; // holds the current move state (valid or invalid only)

	/**
	 * Hold the location for each required figure.
	 */
	private final String locationHolderForFigures[] = {
			"A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", /* 0 - 7 */
			"A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7", /* 8 - 15 */
			"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", /* 16 - 23 */
			"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1" /* 24 - 31 */
	};

	/**
	 * Initializing all figures by given figure type and it's starting coordinate.
	 *
	 * @see {@link FigureSet} for figure type
	 * @see {@link FigureColor} for the figure color
	 * @see {@link Inchesstigated#commandHolderForButtons} for coordinate in words
	 */
	public void initializeFiguresOnField() {
		for (final FigureSet type : FigureSet.values()) {
			for (final FigureColor color : FigureColor.values()) {
				switch (type) {
					case KING: {
						if (color == FigureColor.BLACK) {
							this.blackKing = new FigureKing(new Coordinates(this.locationHolderForFigures[4]), color); // E8
							FigureHolder.INSTANCE.addKingToHolder(this.blackKing);
							Matrix.INSTANCE.setNewFigureLocation(this.blackKing, this.blackKing.getLocation(), true);
						} else {
							this.whiteKing = new FigureKing(new Coordinates(this.locationHolderForFigures[28]), color); // E1
							FigureHolder.INSTANCE.addKingToHolder(this.whiteKing);
							Matrix.INSTANCE.setNewFigureLocation(this.whiteKing, this.whiteKing.getLocation(), true);
						}
						break;
					}
					case KNIGHT: {
						if (color == FigureColor.BLACK) {
							this.blackKnight = new FigureKnight[FigureProcedure.NBR_OF_DOUBLE_FIGURES];
							for (int i = 0; i < FigureProcedure.NBR_OF_DOUBLE_FIGURES; i++) {
								if (i == 0) {
									this.blackKnight[i] = new FigureKnight(new Coordinates(this.locationHolderForFigures[1]), color); // B8
								} else {
									this.blackKnight[i] = new FigureKnight(new Coordinates(this.locationHolderForFigures[6]), color); // G8
								}

								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.blackKnight[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.blackKnight[i], this.blackKnight[i].getLocation(), true);
								this.blackKnight[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.blackKnight[i], this.blackKnight[i].getLocation()));
							} // end for
						} else {
							this.whiteKnight = new FigureKnight[FigureProcedure.NBR_OF_DOUBLE_FIGURES];
							for (int i = 0; i < FigureProcedure.NBR_OF_DOUBLE_FIGURES; i++) {
								if (i == 0) {
									this.whiteKnight[i] = new FigureKnight(new Coordinates(this.locationHolderForFigures[25]), color); // B1
								} else {
									this.whiteKnight[i] = new FigureKnight(new Coordinates(this.locationHolderForFigures[30]), color); // G1
								}

								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.whiteKnight[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.whiteKnight[i], this.whiteKnight[i].getLocation(), true);
								this.whiteKnight[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.whiteKnight[i], this.whiteKnight[i].getLocation()));
							} // end for
						} // end else
						break;
					}
					case PAWN: {
						if (color == FigureColor.BLACK) {
							this.blackPawn = new FigurePawn[FigureProcedure.NBR_OF_PAWNS];
							for (int i = 0; i < FigureProcedure.NBR_OF_PAWNS; i++) {
								switch (i) {
									case 0:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[8])); // A7
										break;
									case 1:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[9])); // B7
										break;
									case 2:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[10])); // C7
										break;
									case 3:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[11])); // D7
										break;
									case 4:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[12])); // E7
										break;
									case 5:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[13])); // F7
										break;
									case 6:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[14])); // G7
										break;
									case 7:
										this.blackPawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[15])); // H7
										break;
								}

								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.blackPawn[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.blackPawn[i], this.blackPawn[i].getLocation(), true);
								this.blackPawn[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.blackPawn[i], this.blackPawn[i].getLocation()));
							} // end for
						} else {
							this.whitePawn = new FigurePawn[FigureProcedure.NBR_OF_PAWNS];
							for (int i = 0; i < FigureProcedure.NBR_OF_PAWNS; i++) {
								switch (i) {
									case 0:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[16])); // A2
										break;
									case 1:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[17])); // B2
										break;
									case 2:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[18])); // C2
										break;
									case 3:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[19])); // D2
										break;
									case 4:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[20])); // E2
										break;
									case 5:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[21])); // F2
										break;
									case 6:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[22])); // G2
										break;
									case 7:
										this.whitePawn[i] = new FigurePawn(color, new Coordinates(this.locationHolderForFigures[23])); // H2
										break;
								}

								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.whitePawn[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.whitePawn[i], this.whitePawn[i].getLocation(), true);
								this.whitePawn[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.whitePawn[i], this.whitePawn[i].getLocation()));
							} // end for
						} // end else
						break;
					}
					case QUEEN: {
						if (color == FigureColor.BLACK) {
							this.blackQueen = new FigureQueen(new Coordinates(this.locationHolderForFigures[3]), color); // D8
							FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.blackQueen);
							Matrix.INSTANCE.setNewFigureLocation(this.blackQueen, this.blackQueen.getLocation(), true);
							this.blackQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.blackQueen, this.blackQueen.getLocation()));

							/*
							 * Required, because since the black Queen has not been created before, the
							 * black King may "move" to D8. At the beginning of the game, this is
							 * impossible, thus the King's properties needs to be set here.
							 */
							this.blackKing.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.blackKing, this.blackKing.getLocation()));

						} else {
							this.whiteQueen = new FigureQueen(new Coordinates(this.locationHolderForFigures[27]), color); // D1
							FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.whiteQueen);
							Matrix.INSTANCE.setNewFigureLocation(this.whiteQueen, this.whiteQueen.getLocation(), true);
							this.whiteQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.whiteQueen, this.whiteQueen.getLocation()));

							/* Same operation for white King. */
							this.whiteKing.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.whiteKing, this.whiteKing.getLocation()));

						}
						break;
					}
					case ROOK: {
						if (color == FigureColor.BLACK) {
							this.blackRook = new FigureRook[FigureProcedure.NBR_OF_DOUBLE_FIGURES];
							for (int i = 0; i < FigureProcedure.NBR_OF_DOUBLE_FIGURES; i++) {
								if (i == 0) {
									this.blackRook[i] = new FigureRook(new Coordinates(this.locationHolderForFigures[0]), color, TowerIdentification.POSSIBLE_TO_CASTLE); // A8
								} else {
									this.blackRook[i] = new FigureRook(new Coordinates(this.locationHolderForFigures[7]), color, TowerIdentification.POSSIBLE_TO_CASTLE); // H8
								}
								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.blackRook[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.blackRook[i], this.blackRook[i].getLocation(), true);
								this.blackRook[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.blackRook[i], this.blackRook[i].getLocation()));
							} // end for
						} else {
							this.whiteRook = new FigureRook[FigureProcedure.NBR_OF_DOUBLE_FIGURES];
							for (int i = 0; i < FigureProcedure.NBR_OF_DOUBLE_FIGURES; i++) {
								if (i == 0) {
									this.whiteRook[i] = new FigureRook(new Coordinates(this.locationHolderForFigures[24]), color, TowerIdentification.POSSIBLE_TO_CASTLE); // A1
								} else {
									this.whiteRook[i] = new FigureRook(new Coordinates(this.locationHolderForFigures[31]), color, TowerIdentification.POSSIBLE_TO_CASTLE); // H1
								}
								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.whiteRook[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.whiteRook[i], this.whiteRook[i].getLocation(), true);
								this.whiteRook[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.whiteRook[i], this.whiteRook[i].getLocation()));
							} // end for
						} // end else
						break;
					}
					case RUNNER: {
						if (color == FigureColor.BLACK) {
							this.blackRunner = new FigureRunner[FigureProcedure.NBR_OF_DOUBLE_FIGURES];
							for (int i = 0; i < FigureProcedure.NBR_OF_DOUBLE_FIGURES; i++) {
								if (i == 0) {
									final String coordC8 = this.locationHolderForFigures[2];
									this.blackRunner[i] = new FigureRunner(new Coordinates(coordC8), color, AreaColor.getAreaColor(coordC8)); // C8
								} else {
									final String coordF8 = this.locationHolderForFigures[5];
									this.blackRunner[i] = new FigureRunner(new Coordinates(coordF8), color, AreaColor.getAreaColor(coordF8)); // F8
								}
								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.blackRunner[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.blackRunner[i], this.blackRunner[i].getLocation(), true);
								this.blackRunner[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.blackRunner[i], this.blackRunner[i].getLocation()));
							} // end for
						} else {
							this.whiteRunner = new FigureRunner[FigureProcedure.NBR_OF_DOUBLE_FIGURES];
							for (int i = 0; i < FigureProcedure.NBR_OF_DOUBLE_FIGURES; i++) {
								if (i == 0) {
									final String coordC1 = this.locationHolderForFigures[26];
									this.whiteRunner[i] = new FigureRunner(new Coordinates(coordC1), color, AreaColor.getAreaColor(coordC1)); // C1
								} else {
									final String coordF1 = this.locationHolderForFigures[29];
									this.whiteRunner[i] = new FigureRunner(new Coordinates(coordF1), color, AreaColor.getAreaColor(coordF1)); // F1
								}
								FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.whiteRunner[i]);
								Matrix.INSTANCE.setNewFigureLocation(this.whiteRunner[i], this.whiteRunner[i].getLocation(), true);
								this.whiteRunner[i].updateThreatList(Matrix.INSTANCE.createNewThreatList(this.whiteRunner[i], this.whiteRunner[i].getLocation()));
							} // end for
						} // end else

						break;
					}
				}
			}
		}
	}

	/**
	 * Let a figure move from start to destination, depending on the given figure
	 * itself. Doesn't handle a castling move. Use
	 * {@link FigureProcedure#makeCastling(FigureKing, FigureRook, CastlingType)}
	 * instead.
	 *
	 * @param figure
	 *            the figureID to determine which real figure is in use
	 * @param figureColorAsWord
	 *            the figure color in words to identify the correct figure
	 * @param startCoordinateAsWord
	 *            the start coordinate in words
	 * @param destinationCoordinateAsWord
	 *            the destination field in words
	 *
	 * @return true, if, and only if, the the figure move was successful,<br>
	 *         false, otherwise
	 *         <hr>
	 *         TODO: whenever the King may be threatened, then check, if the the
	 *         King may have at least one escape route <b>and</b> if any allied
	 *         figure may be used for to block the current threat
	 */
	@SuppressWarnings("incomplete-switch")
	public void moveFigure(final String figureID, final String figureColorAsWord, final String startCoordinateAsWord, final String destinationCoordinateAsWord) {
		this.moveState = MoveState.VALID; // assuming, that the current move state is valid

		/* properties to use */
		final Coordinates destination = new Coordinates(destinationCoordinateAsWord); // create coordinate to use
		BaseFigure figure = null; // figure to use
		final MoveHistory history = new MoveHistory(); // move history to use
		final FigureColor currentColor = FigureColor.getColorType(figureColorAsWord); // receive the figure color

		if (figureID.equals(Inchesstigated.UNIQUE_FIGURE_ID[0])) { // a Rook is in use
			final List<FigureRook> listOfRooks = FigureHolder.INSTANCE.getListOfAllRooks(currentColor); // reveal which Rook is in use --> by startCoordinateAsWord

			for (final FigureRook tmpRook : listOfRooks) {
				if (tmpRook.getLocation().getCoordinatesXY().equals(startCoordinateAsWord)) { // this is the current Rook to use
					figure = tmpRook; // cast figure with tmpRook
					break; // avoid to call unexpected exceptions for any type
				}
			}
		} else if (figureID.equals(Inchesstigated.UNIQUE_FIGURE_ID[1])) { // a Knight is in use
			final List<FigureKnight> listOfKnights = FigureHolder.INSTANCE.getListOfAllKnights(currentColor);

			for (final FigureKnight tmpKnight : listOfKnights) {
				if (tmpKnight.getLocation().getCoordinatesXY().equals(startCoordinateAsWord)) {
					figure = tmpKnight;
					break;
				}
			}
		} else if (figureID.equals(Inchesstigated.UNIQUE_FIGURE_ID[2])) { // Runner
			final List<FigureRunner> listOfRunners = FigureHolder.INSTANCE.getListOfAllRunners(currentColor);

			for (final FigureRunner tmpRunner : listOfRunners) {
				if (tmpRunner.getLocation().getCoordinatesXY().equals(startCoordinateAsWord)) {
					figure = tmpRunner;
					break;
				}
			}
		} else if (figureID.equals(Inchesstigated.UNIQUE_FIGURE_ID[3])) { // Queen
			final List<FigureQueen> listOfQueens = FigureHolder.INSTANCE.getListOfAllQueens(currentColor);

			for (final FigureQueen tmpQueen : listOfQueens) {
				if (tmpQueen.getLocation().getCoordinatesXY().equals(startCoordinateAsWord)) {
					figure = tmpQueen;
					break;
				}
			}
		} else if (figureID.equals(Inchesstigated.UNIQUE_FIGURE_ID[4])) { // King
			figure = FigureHolder.INSTANCE.getKing(currentColor);
		} else if (figureID.equals(Inchesstigated.UNIQUE_FIGURE_ID[5])) { // Pawn
			final List<FigurePawn> listOfPawns = FigureHolder.INSTANCE.getListOfAllPawns(currentColor);

			for (final FigurePawn tmpPawn : listOfPawns) {
				if (tmpPawn.getLocation().getCoordinatesXY().equals(startCoordinateAsWord)) {
					figure = tmpPawn;
					break;
				}
			}
		}

		final FigureSet type = figure.getFigureType(); // receive the current used figure
		boolean incorrectMove = true; // assume, that every move is incorrect --> update by the condition checks
										// below, if possible
		boolean alreadyDone = false; // a flag to determine, when the process has been finished to avoid to check
										// other conditions

		final FigureColor opponentColor = FigureColor.getOpponentColor(currentColor);
		final FigureKing opponentKing = FigureHolder.INSTANCE.getKing(opponentColor); // may in use, if the foes King may be threatened now
		final FigureKing ownKing = FigureHolder.INSTANCE.getKing(currentColor);

		/*
		 * --------------------------------------------------------------------------
		 * Check, if the current move is successful, where a figure to beat is NOT part
		 * of this condition check below. Also required, if the own King is not
		 * threatened --> Avoids to move any figure to a field, while the King is
		 * threatened. In that case this figure is forced to block this threat.
		 * --------------------------------------------------------------------------
		 */
		if ((figure.onValidMove(destination) == MoveEvent.SUCCESSFULLY_MOVE) && !(ownKing.onThreatened())) {
			alreadyDone = true;
			boolean lastCoordinateForPawn = false;

			/*
			 * --------------------------------------------------------------------------
			 * only in use, if a Pawn has been used; check, if this Pawn has reached it's
			 * last coordinate
			 * --------------------------------------------------------------------------
			 */
			if (figure instanceof FigurePawn) { // check, if this figure is a Pawn
				final FigurePawn tmpPawn = (FigurePawn) figure; // then cast to a Pawn

				if (tmpPawn.onLastCoordinate()) { // and check, if this Pawn is on it's last coordinate
					lastCoordinateForPawn = true;

					BaseFigure.resetFigureProperties(tmpPawn, destination);
					Matrix.INSTANCE.setNewFigureLocation(tmpPawn, destination, false);

					// update button icons and required properties
					Inchesstigated.updateFieldImage(figureID, figureColorAsWord, startCoordinateAsWord, destinationCoordinateAsWord);

					// finally, print the current move to the history
					history.usedFigure = FigureSet.getFigureType(figureID);
					history.startCoord = startCoordinateAsWord;
					history.destCoord = destinationCoordinateAsWord;

					final String promotedFigure = Inchesstigated.showPawnPromotion(); // receive the selected figure

					if (promotedFigure != null) { // TODO: is it possible to avoid to get a null reference?
						final BaseFigure promotion = tmpPawn.promotePawn(FigureSet.getFigureType(promotedFigure));

						Matrix.INSTANCE.removeFigureFromMatrix(tmpPawn);

						Matrix.INSTANCE.setNewFigureLocation(promotion, destination, true);
						promotion.updateThreatList(Matrix.INSTANCE.createNewThreatList(promotion, destination));

						FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, promotion);
						FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, tmpPawn);

						Inchesstigated.updateJLabelOnScreen(0); // add this Pawn to the captured list

						// TODO: also reduce the number of captured figure by one, which is
						// now the promoted Pawn, if possible

						// TODO: update field image, where the new figure is now in use
						// Inchesstigated.updateFieldImage(figureID,
						// figureColorAsWorthis.updateFigureProperties(figure, destination);d,
						// startCoordinateAsWord, destinationCoordinateAsWord);

						// check, if the opponent's King may now being threatened by figure
						if (opponentKing.onThreatened()) {
							final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
							if (kingsEscapeRoutes.isEmpty()) {
								history.moveType = MoveType.GAME_OVER_PAWN_PROMOTION;
							} else {
								history.moveType = MoveType.PAWN_PROMOTION_AND_THREAT;
							}
						} else {
							history.moveType = MoveType.PAWN_PROMOTION;
						}
					}
				}
			}

			/*
			 * --------------------------------------------------------------------------
			 * Only in use, if the King has been moved.
			 * --------------------------------------------------------------------------
			 */
			if (figure instanceof FigureKing) {
				if (figure.getFigureColor().equals(ownKing.getFigureColor())) {
					ownKing.markToImpossibleCastling(); // the King is now unable to do a castling
				}
			}

			/*
			 * --------------------------------------------------------------------------
			 * otherwise the figure was not a Pawn OR a Pawn has been used, but it hasn't
			 * reached it's last coordinate
			 * --------------------------------------------------------------------------
			 */
			if (!(figure instanceof FigurePawn) || ((figure instanceof FigurePawn) && !lastCoordinateForPawn)) {
				this.updateFigureProperties(figure, destination);
				MoveWatcherEvent.addLastMoveBy(figure); // notify this figure move

				// update button icons and required properties
				Inchesstigated.updateFieldImage(figureID, figureColorAsWord, startCoordinateAsWord, destinationCoordinateAsWord);

				// finally, print the current move to the history
				history.usedFigure = FigureSet.getFigureType(figureID);
				history.startCoord = startCoordinateAsWord;
				history.destCoord = destinationCoordinateAsWord;

				// check, if the opponent's King may now being threatened by figure
				if (opponentKing.onThreatened()) {
					final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
					if (kingsEscapeRoutes.isEmpty()) { // there're no escape routes for this king
						history.moveType = MoveType.GAME_OVER; // game over
					} else {
						history.moveType = MoveType.NORMAL_MOVE_AND_THREAT; // otherwise just a threat
					}
				} else {
					history.moveType = MoveType.NORMAL_MOVE; // otherwise a normal move
				}

				Inchesstigated.updateMoveHistory(history);
			}

			incorrectMove = false;

		} else if (ownKing.onThreatened() && !alreadyDone) {
			alreadyDone = true;

			/*
			 * --------------------------------------------------------------------------
			 * This is in use, if the own King is now threatened, thus the destination must
			 * be used to block the current threat, caused by the last used figure.
			 * --------------------------------------------------------------------------
			 */
			final BaseFigure opponentFigure = MoveWatcherEvent.getLastUsedFigure(opponentColor); // null on first move

			/* save the old figure's location */
			final Coordinates currentLocation = figure.getLocation();

			/* Set the figure to this coordinate to test the condition below. */
			this.updateFigureProperties(figure, destination);

			/* "clean" old settings of the opponent figure */
			this.updateFigureProperties(opponentFigure, opponentFigure.getLocation());

			/*
			 * --------------------------------------------------------------------------
			 * Now check, if the destination of the current used figure gives a match with
			 * the threat area of the last used figure, except Knight. If true, then the
			 * current figure should now block the King's threat.
			 * --------------------------------------------------------------------------
			 */

			boolean isMoveSatisfied = false;
			final FigureSet opponentFigureType = opponentFigure.getFigureType();

			if ((opponentFigureType != FigureSet.KNIGHT) && (opponentFigureType != FigureSet.KING) && (opponentFigureType != FigureSet.PAWN)) {
				for (final Coordinates tmpLoc : opponentFigure.getThreatAreas()) {
					if (tmpLoc.getCoordinatesXY().equals(destination.getCoordinatesXY())) {

						/*
						 * --------------------------------------------------------------------------
						 * Remove the threat and update the new figure's properties.
						 * --------------------------------------------------------------------------
						 */
						ownKing.removeThreat();

						MoveWatcherEvent.addLastMoveBy(figure);
						Inchesstigated.updateFieldImage(figureID, figureColorAsWord, startCoordinateAsWord, destinationCoordinateAsWord);

						history.usedFigure = FigureSet.getFigureType(figureID);
						history.startCoord = startCoordinateAsWord;
						history.destCoord = destinationCoordinateAsWord;

						if (opponentKing.onThreatened()) {
							final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
							if (kingsEscapeRoutes.isEmpty()) {
								history.moveType = MoveType.GAME_OVER;
							} else {
								history.moveType = MoveType.NORMAL_MOVE_AND_THREAT;
							}
						} else {
							history.moveType = MoveType.NORMAL_MOVE;
						}

						Inchesstigated.updateMoveHistory(history);
						isMoveSatisfied = true;
						incorrectMove = false;

						break;
					}
				}
			} else {
				/*
				 * --------------------------------------------------------------------------
				 * It's in use only, if a Knight was the last figure, because the Knight's
				 * threat areas are unable to block.
				 * --------------------------------------------------------------------------
				 */
				incorrectMove = true;
			}

			/*
			 * --------------------------------------------------------------------------
			 * This is in use only, if a block was not successful. In that case the current
			 * figure's properties and of the last used figure needs to be restored.
			 * Finally, this selected move is invalid, because the own King is still
			 * threatened.
			 * --------------------------------------------------------------------------
			 */
			if (!isMoveSatisfied && (opponentFigure.getFigureType() != FigureSet.KNIGHT)) {
				incorrectMove = true;

				this.updateFigureProperties(figure, currentLocation);
				this.updateFigureProperties(opponentFigure, opponentFigure.getLocation());
			}

		} else {
			/*
			 * --------------------------------------------------------------------------
			 * Otherwise the current move MAY be invalid. Perhaps the user wants to capture
			 * an opponent figure.
			 * --------------------------------------------------------------------------
			 */
			BaseFigure opponentFigure = MoveWatcherEvent.getLastUsedFigure(opponentColor); // null on first move
			final BaseFigure lastUsedFigure = MoveWatcherEvent.getLastUsedFigure(opponentColor); // null on first move
			opponentFigure = Matrix.INSTANCE.getFigureOnField(destination); // null, if no figure has been detected on this field

			if (opponentFigure != null) {

				/*
				 * --------------------------------------------------------------------------
				 * Check, if the current figure is able to capture the opponent figure.
				 * --------------------------------------------------------------------------
				 */
				final boolean normalCaptureMove = (figure.onHitOpponent(opponentFigure) && (figure.onValidMove(destination) == MoveEvent.SUCCESSFULLY_MOVE)); // except Pawn

				final boolean pawnDiagonalWay = ((Math.abs(figure.getLocation().getPosX().charAt(0) - destination.getPosX().charAt(0)) == 1) && // difference of coordinate X
						(Math.abs(figure.getLocation().getPosY().charAt(0) - destination.getPosY().charAt(0)) == 1)); // and Y shall be exactly 1
				final boolean fullCaptureMoveByPawn = (figure.onHitOpponent(opponentFigure) && (figure instanceof FigurePawn) && pawnDiagonalWay); // Pawn only

				if (normalCaptureMove || fullCaptureMoveByPawn) {

					alreadyDone = true;

					/*
					 * --------------------------------------------------------------------------
					 * Only in use, if the King has been moved.
					 * --------------------------------------------------------------------------
					 */
					if (figure instanceof FigureKing) {
						if (figure.getFigureColor().equals(ownKing.getFigureColor())) {
							ownKing.markToImpossibleCastling(); // the King is now unable to do a castling, if this was still possible before
						}
					}

					/*
					 * --------------------------------------------------------------------------
					 * check, if the own King is threatened and the last used figure is equal to the
					 * spotted threat figure
					 * --------------------------------------------------------------------------
					 */
					if (ownKing.onThreatened() && ownKing.getThreatFigure().equals(lastUsedFigure)) {
						ownKing.removeThreat(); // remove threat
					}

					/*
					 * --------------------------------------------------------------------------
					 * otherwise the own king isn't threatened, then it's just a legal move by
					 * capturing the opponent's figure
					 * --------------------------------------------------------------------------
					 */
					Matrix.INSTANCE.removeFigureFromMatrix(opponentFigure);
					FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, opponentFigure); // remove from list

					/*
					 * --------------------------------------------------------------------------
					 * for GUI: determine which figure was removed to update the label on screen
					 * --------------------------------------------------------------------------
					 */
					final FigureSet typeOfBeatenFigure = opponentFigure.getFigureType();
					final FigureColor colorOfBeatenFigure = opponentFigure.getFigureColor();
					int labelID = -1;

					switch (typeOfBeatenFigure) { /* where a King is unable to beat */
						case PAWN:
							labelID = 0;
							break;
						case KNIGHT:
							labelID = 1;
							break;
						case RUNNER:
							labelID = 2;
							break;
						case ROOK:
							labelID = 3;
							break;
						case QUEEN:
							labelID = 4;
							break;
					}

					if (colorOfBeatenFigure == FigureColor.BLACK) { // only in use, if a black figure has been removed from field
						labelID += 5; // then update the labelID by 5
					}

					Inchesstigated.updateJLabelOnScreen(labelID); // finally, update required JLabel
					/* -------------------------------------------------------------------------- */

					this.updateFigureProperties(figure, destination);
					MoveWatcherEvent.addLastMoveBy(figure); // notify this figure move

					// update button icons and required properties
					Inchesstigated.updateFieldImage(figureID, figureColorAsWord, startCoordinateAsWord, destinationCoordinateAsWord);

					// finally, print the current move to the history
					history.usedFigure = FigureSet.getFigureType(figureID);
					history.startCoord = startCoordinateAsWord;
					history.destCoord = destinationCoordinateAsWord;

					// check, if the opponent's King may now being threatened by figure
					if (opponentKing.onThreatened()) {
						final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
						if (kingsEscapeRoutes.isEmpty()) { // there're no escape routes for this king
							history.moveType = MoveType.GAME_OVER_BEATEN_MOVE;
						} else {
							history.moveType = MoveType.NORMAL_BEATEN_MOVE_AND_THREAT;
						}
					} else {
						history.moveType = MoveType.NORMAL_BEATEN_MOVE;
					}

					Inchesstigated.updateMoveHistory(history);
					incorrectMove = false;

				} else if ((type == FigureSet.PAWN) && !alreadyDone) {

					/*
					 * --------------------------------------------------------------------------
					 * For Pawn only: the current move may be invalid by the normal way, because a
					 * Pawn may move forward and beat any figure by diagonal way only. In that case,
					 * if an "en passant" is available and used, then the current move is valid, if
					 * all sub tests were satisfied.
					 * --------------------------------------------------------------------------
					 */
					if (lastUsedFigure != null) { // avoids to call a NullPointerException in onEnPassant() function below
						final FigurePawn tmpPawn = (FigurePawn) figure;

						if (tmpPawn.onEnPassant()) { // check, if an "en passant" is available
							alreadyDone = true;
							incorrectMove = false;

							/*
							 * avoids to do an "en passant" again; in that case the figure move is valid
							 */
							tmpPawn.setEnPassantFlag(false);

							FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, lastUsedFigure); // it's a Pawn only, which is going to removed from list
							Matrix.INSTANCE.removeFigureFromMatrix(lastUsedFigure);

							final FigureColor colorOfBeatenFigure = lastUsedFigure.getFigureColor();
							int labelID = 0; // start with ID = 0 --> Pawn

							if (colorOfBeatenFigure == FigureColor.BLACK) {
								labelID += 5;
							}

							Inchesstigated.updateJLabelOnScreen(labelID); // finally, update required JLabel
							this.updateFigureProperties(tmpPawn, destination);

							// update button icons and required properties
							Inchesstigated.updateFieldImage(figureID, figureColorAsWord, startCoordinateAsWord, destinationCoordinateAsWord);

							// finally, print the current move to the history
							history.usedFigure = FigureSet.PAWN;
							history.startCoord = startCoordinateAsWord;
							history.destCoord = destinationCoordinateAsWord;

							// check, if the opponent's King may now being threatened by this Pawn
							if (opponentKing.onThreatened()) {
								final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
								if (kingsEscapeRoutes.isEmpty()) { // there're no escape routes for this king
									history.moveType = MoveType.GAME_OVER_EN_PASSANT;
								} else {
									history.moveType = MoveType.EN_PASSANT_BEATEN_MOVE_AND_THREAT;
								}
							} else {
								history.moveType = MoveType.EN_PASSANT_BEATEN_MOVE;
							}

							Inchesstigated.updateMoveHistory(history);
						} else {
							incorrectMove = true;
						}
					}
				}
			} else {
				// it's an invalid move here -> set flag
				incorrectMove = true;
			}
		}

		if (incorrectMove) {
			Inchesstigated.infoDialog(10, "");
			this.moveState = MoveState.INVALID;
		}
	}

	/**
	 * Called, whenever the player has pressed the button for castling and made a
	 * valid decision. This castling mode comes as a word, where the real castling
	 * type needs to be determined.<br>
	 * <br>
	 *
	 * Finally, the function
	 * {@link #makeCastling(FigureKing, FigureRook, CastlingType)} is going to call.
	 *
	 * @param option
	 *            the castling option in words
	 */
	public void collectCastlingDecision(final CastlingType option) {
		CastlingType type = null;
		TowerSpawnLocation locationRook = null;

		if (option.equals(CastlingType.SMALL_CASTLING)) {
			type = CastlingType.SMALL_CASTLING;
			locationRook = TowerSpawnLocation.RIGHT_SIDE;
		} else if (option.equals(CastlingType.BIG_CASTLING)) {
			type = CastlingType.BIG_CASTLING;
			locationRook = TowerSpawnLocation.LEFT_SIDE;
		}

		final FigureColor lastUsedColor = MoveWatcherEvent.getLastFigureColor(); // receive the last opposite used color (null on first move)

		try {
			final FigureColor ownColor = FigureColor.getOpponentColor(lastUsedColor); // receive the current color

			final FigureKing kingToUse = FigureHolder.INSTANCE.getKing(ownColor); // get the current King
			FigureRook rookToUse = null; // and also the current Rook

			for (final FigureRook tmpRook : FigureHolder.INSTANCE.getListOfAllRooks(ownColor)) {
				if (tmpRook.getSpawnLocation().equals(locationRook)) { // check, if the correct Rook has been found
					rookToUse = tmpRook; // then update the current Rook to use (which shall be tested to castle with the
											// King)
					break;
				}
			}

			/*
			 * --------------------------------------------------------------------------
			 * TODO: expected result: the King and the Rook shall now have the same color
			 * --------------------------------------------------------------------------
			 */

			if (rookToUse != null) { // the Rook must not be null
				this.makeCastling(kingToUse, rookToUse, type);
			} else {
				Inchesstigated.infoDialog(11, "Kein Turm f\u00FCr eine Rochade vorhanden!");
			}
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Do a castling between King and Rook.
	 *
	 * @param king
	 *            the King to use
	 * @param rook
	 *            the Rook to use
	 * @param castlingType
	 *            the given castling type
	 *
	 * @see {@link CastlingType} in {@link CastlingEvent}
	 */
	public void makeCastling(final FigureKing king, final FigureRook rook, final CastlingType castlingType) {
		/* only possible, if the castling tests were all successful */

		if (CastlingEvent.INSTANCE.onCastlingPossibility(king, rook) == CastlingState.STILL_POSSIBLE) {
			CastlingEvent.INSTANCE.castlingKingTower(castlingType, king, rook);

			final FigureKing ownKing = king;
			final FigureColor opponentColor = FigureColor.getOpponentColor(ownKing.getFigureColor());

			final FigureKing opponentKing = FigureHolder.INSTANCE.getKing(opponentColor);
			final MoveHistory history = new MoveHistory();

			// update button icons and required properties
			final String figureKingID = ownKing.getFigureType().toString();
			final String figureKingColor = ownKing.getFigureColor().toString();
			String startCoordinateKing;
			String destinationCoordinateKing;

			final String figureRookID = rook.getFigureType().toString();
			final String figureRookColor = rook.getFigureColor().toString();
			String startCoordinateRook;
			String destinationCoordinateRook;

			if (castlingType == CastlingType.SMALL_CASTLING) {
				if (ownKing.getFigureColor() == FigureColor.WHITE) {
					startCoordinateKing = "E1";
					destinationCoordinateKing = "G1";

					startCoordinateRook = "H1";
					destinationCoordinateRook = "F1";
				} else {
					startCoordinateKing = "E8";
					destinationCoordinateKing = "G8";

					startCoordinateRook = "H8";
					destinationCoordinateRook = "F8";
				}

				if (opponentKing.onThreatened()) {
					final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
					if (kingsEscapeRoutes.isEmpty()) {
						history.moveType = MoveType.GAME_OVER_SMALL_CASTLING;
					} else {
						history.moveType = MoveType.SMALL_CASTLING_MOVE_AND_THREAT;
					}
				} else {
					history.moveType = MoveType.SMALL_CASTLING_MOVE;
				}
			} else {
				if (ownKing.getFigureColor() == FigureColor.WHITE) {
					startCoordinateKing = "E1";
					destinationCoordinateKing = "C1";

					startCoordinateRook = "A1";
					destinationCoordinateRook = "D1";
				} else {
					startCoordinateKing = "E8";
					destinationCoordinateKing = "C8";

					startCoordinateRook = "A8";
					destinationCoordinateRook = "D8";
				}

				if (opponentKing.onThreatened()) {
					final List<Coordinates> kingsEscapeRoutes = opponentKing.createPossibleEscapeFields();
					if (kingsEscapeRoutes.isEmpty()) {
						history.moveType = MoveType.GAME_OVER_BIG_CASTLING;
					} else {
						history.moveType = MoveType.BIG_BASTLING_MOVE_AND_THREAT;
					}
				} else {
					history.moveType = MoveType.BIG_CASTLING_MOVE;
				}
			}

			Inchesstigated.updateMoveHistory(history);
			Inchesstigated.updateFieldImage(figureKingID, figureKingColor, startCoordinateKing, destinationCoordinateKing); // update King's position
			Inchesstigated.updateFieldImage(figureRookID, figureRookColor, startCoordinateRook, destinationCoordinateRook); // and also Rook's position
		} else {
			final String errorMessage = CastlingEvent.INSTANCE.getCastlingErrorReason();
			Inchesstigated.infoDialog(11, errorMessage);
		}
	}

	/**
	 * Update the figure properties after each move.
	 *
	 * @param figure
	 *            the figure to use
	 * @param location
	 *            the new location to set
	 */
	public <T extends BaseFigure> void updateFigureProperties(final T figure, final Coordinates location) {
		BaseFigure.resetFigureProperties(figure, location);
		// Matrix.INSTANCE.setNewFigureLocation(figure, location, false); // called in
		// function above
		figure.updateThreatList(Matrix.INSTANCE.createNewThreatList(figure, location));
	}

	/**
	 * Nested enumeration, which holds the current move state.
	 */
	public enum MoveState {
		VALID, INVALID
	}

	/**
	 * Receiving the current move state.
	 * 
	 * @return the current move state
	 */
	public MoveState getCurrentMoveState() {
		return this.moveState;
	}
}
