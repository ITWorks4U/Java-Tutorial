/**
 * @package: location
 * @project: Chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package location;

import java.util.ArrayList;
import java.util.List;

import figure.BaseFigure;
import figure.FigureKing;
import figure.FigureKnight;
import figure.FigurePawn;
import figure.FigureRook;
import figure.FigureRunner;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.FigureSet;
import figure.properties.KingDestinationNotifier;
import figure.properties.MoveSetKnight;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;

/**
 * @author swunsch
 *
 *         Definition for each chess field. Every figure can be spotted from
 *         "A1" to "H8". This class holds all required functions for the game.
 *
 *         It's also a singleton class to avoid to get more than one object of
 *         itself.
 */
public enum Matrix {
	/* singleton identification */
	INSTANCE;

	/* constants */
	private static final int MAX_FIELD_NUMBER = 8;

	private final char[] HORIZONTAL_ARRAY = {
			'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H'
	};

	private final char[] VERTICAL_ARRAY = {
			'1', '2', '3', '4', '5', '6', '7', '8'
	};

	/** the matrix to use */
	private CoordinationHolder[][] coordMatrix;

	/** listener for any field */
	private CoordinationHolder holder;

	/** definition of areas which will be filled with a white color */
	private final String[] whiteAreaSet = {
			"A1", "A3", "A5", "A7", "B2", "B4", "B6", "B8", "C1", "C3", "C5", "C7", "D2", "D4", "D6", "D8", "E1", "E3", "E5", "E7", "F2", "F4",
			"F6", "F8", "G1", "G3", "G5", "G7", "H2", "H4", "H6", "H8"
	};

	/** a default value for the horizontal area ('A') */
	private char posHorizontalDefault;

	/** a default value for the vertical area ('1') */
	private char posVerticalDefault;

	/** holds the last vertical value ('8') */
	private char lastVerticalValue;

	/** The constructor for this singleton class. */
	private Matrix() {
		this.createMatrix();
		this.posHorizontalDefault = this.HORIZONTAL_ARRAY[0]; // 'A'
		this.posVerticalDefault = this.VERTICAL_ARRAY[0]; // '1'
		this.lastVerticalValue = this.VERTICAL_ARRAY[7]; // '8'
		this.holder = new CoordinationHolder();
	}

	/**
	 * Create the matrix with basic properties. The matrix will hold the coordinate
	 * {X,Y} for each field, the field color and other required properties.
	 *
	 **/
	private void createMatrix() {
		this.coordMatrix = new CoordinationHolder[Matrix.MAX_FIELD_NUMBER][Matrix.MAX_FIELD_NUMBER];
		for (int i = 0; i < Matrix.MAX_FIELD_NUMBER; i++) {
			for (int j = 0; j < Matrix.MAX_FIELD_NUMBER; j++) {
				this.coordMatrix[i][j] = new CoordinationHolder();
				this.coordMatrix[i][j].pos_horizon = this.HORIZONTAL_ARRAY[i];
				this.coordMatrix[i][j].pos_vertical = this.VERTICAL_ARRAY[j];
				/* determine which field color is going to set */
				this.coordMatrix[i][j].areaColor = this.determineAreaColor(this.HORIZONTAL_ARRAY[i], this.VERTICAL_ARRAY[j]);
				/* default set */
				this.coordMatrix[i][j].figureChess = null;
				this.coordMatrix[i][j].figureColor = null;
				this.coordMatrix[i][j].figureType = null;
				this.coordMatrix[i][j].isReserved = false;
			}
		}
	}

	/**
	 * Determine the area color by given field arguments.
	 *
	 * @param posX
	 *            coordinate X
	 * @param posY
	 *            coordinate Y
	 *
	 * @return color white, if the coordinates are located on a white field,
	 *         otherwise color black
	 */
	public FigureColor determineAreaColor(final char posX, final char posY) {
		final String str = Coordinates.getCoordinatesXY(posX, posY);
		for (final String area : this.whiteAreaSet) {
			if (str.equals(area)) {
				return FigureColor.WHITE;
			}
		}
		return FigureColor.BLACK;
	}

	/**
	 * For a new game each field will be set with a specified figure.
	 *
	 * @param figure
	 *            the figure to use
	 *
	 * @deprecated This method below may be obsolete, because the method
	 *             {@link Matrix#setNewFigureLocation(BaseFigure, Coordinates)} will
	 *             also do the same job including the figure's lists (areas which
	 *             are covered by this figure and a protected figure list)
	 *             modifications
	 */
	@Deprecated
	public <T extends BaseFigure> void fillDefaultFieldValues(final T figure) {
		final FigureColor color = figure.getFigureColor();
		final Coordinates loc = figure.getLocation();
		final int locHorizontal = Math.abs((loc.getPosX().charAt(0)) - this.posHorizontalDefault);
		final int locVertical = Math.abs((loc.getPosY().charAt(0)) - this.posVerticalDefault);
		this.coordMatrix[locHorizontal][locVertical].figureColor = color;
		this.coordMatrix[locHorizontal][locVertical].figureType = figure.getFigureType();
		this.coordMatrix[locHorizontal][locVertical].figureChess = figure;
		this.coordMatrix[locHorizontal][locVertical].isReserved = true;
	}

	/**
	 * Receiving the area color whenever a figure Pawn is able to promote.
	 *
	 * @param figure
	 *            the figure Pawn
	 *
	 * @return the area color of the promoted Pawn
	 */
	public FigureColor getAreaColorOfPawn(final FigurePawn figure) {
		final Coordinates loc = figure.getLocation();
		final char cX = loc.getPosX().charAt(0);
		final char cY = loc.getPosY().charAt(0);
		final int posX = (cX - this.posHorizontalDefault);
		final int posY = (cY - this.posVerticalDefault);
		final FigureColor color = this.coordMatrix[posX][posY].areaColor;
		return color;
	}

	/**
	 * Whenever any figure has been moved, then it's current threat list (for Rook,
	 * Runner and Queen: areaMovepool) needs an update depending on the new
	 * location.
	 *
	 * @param figure
	 *            the figure to use
	 * @param loc
	 *            the new location
	 *
	 * @return a list with all known covered areas by given figure
	 */
	@SuppressWarnings("incomplete-switch")
	public <T extends BaseFigure> List<Coordinates> createNewThreatList(final T figure, final Coordinates loc) {
		List<Coordinates> tmpList = new ArrayList<>();
		final FigureColor figureColor = figure.getFigureColor();
		final char cX = loc.getPosX().charAt(0);
		final char cY = loc.getPosY().charAt(0);
		switch (figure.getFigureType()) {
			case PAWN: {
				Coordinates coordPawn = null;
				BaseFigure bf = null;
				int posHorizon;
				int posVertical;
				/*
				 * check, if the coordinate (white Pawn) {X-1,Y+1} or {X+1, Y+1} OR (black Pawn)
				 * {X-1, Y-1} or {X+1, Y-1} is valid
				 */
				if (figureColor == FigureColor.WHITE) {
					coordPawn = new Coordinates((char) (cX - 1), (char) (cY + 1));
					posHorizon = Math.abs(coordPawn.getPosX().charAt(0) - this.posHorizontalDefault);
					posVertical = Math.abs(coordPawn.getPosY().charAt(0) - this.posVerticalDefault);
					if (this.onValidCoordinate(coordPawn)) {
						if (this.coordMatrix[posHorizon][posVertical].isReserved && (this.coordMatrix[posHorizon][posVertical].figureColor == figureColor)) {
							bf = this.coordMatrix[posHorizon][posVertical].figureChess;
							figure.updateFigureToProtect(bf, null);
						} else {
							tmpList.add(coordPawn);
						}
					}
					coordPawn = new Coordinates((char) (cX + 1), (char) (cY + 1));
					posHorizon = Math.abs(coordPawn.getPosX().charAt(0) - this.posHorizontalDefault);
					posVertical = Math.abs(coordPawn.getPosY().charAt(0) - this.posVerticalDefault);
					if (this.onValidCoordinate(coordPawn)) {
						if (this.coordMatrix[posHorizon][posVertical].isReserved && (this.coordMatrix[posHorizon][posVertical].figureColor == figureColor)) {
							bf = this.coordMatrix[posHorizon][posVertical].figureChess;
							figure.updateFigureToProtect(bf, null);
						} else {
							tmpList.add(coordPawn);
						}
					}
				} else {
					coordPawn = new Coordinates((char) (cX - 1), (char) (cY - 1));
					posHorizon = Math.abs(coordPawn.getPosX().charAt(0) - this.posHorizontalDefault);
					posVertical = Math.abs(coordPawn.getPosY().charAt(0) - this.posVerticalDefault);
					if (this.onValidCoordinate(coordPawn)) {
						if (this.coordMatrix[posHorizon][posVertical].isReserved && (this.coordMatrix[posHorizon][posVertical].figureColor == figureColor)) {
							bf = this.coordMatrix[posHorizon][posVertical].figureChess;
							figure.updateFigureToProtect(bf, null);
						} else {
							tmpList.add(coordPawn);
						}
					}
					coordPawn = new Coordinates((char) (cX + 1), (char) (cY - 1));
					posHorizon = Math.abs(coordPawn.getPosX().charAt(0) - this.posHorizontalDefault);
					posVertical = Math.abs(coordPawn.getPosY().charAt(0) - this.posVerticalDefault);
					if (this.onValidCoordinate(coordPawn)) {
						if (this.coordMatrix[posHorizon][posVertical].isReserved && (this.coordMatrix[posHorizon][posVertical].figureColor == figureColor)) {
							bf = this.coordMatrix[posHorizon][posVertical].figureChess;
							figure.updateFigureToProtect(bf, null);
						} else {
							tmpList.add(coordPawn);
						}
					}
				}
				break;
			}
			case QUEEN: {
				final FigureColor colorQueen = figure.getFigureColor();
				List<Coordinates> firstList = new ArrayList<>();
				FigureRook tmpTower = new FigureRook(loc, colorQueen, TowerIdentification.IMPOSSIBLE_TO_CASTLE);
				firstList = this.createAreasHigherFigures(tmpTower, firstList);

				final int posX = (cX - this.posHorizontalDefault);
				final int posY = (cY - this.posVerticalDefault);
				FigureRunner tmpRunner = new FigureRunner(loc, colorQueen, this.coordMatrix[posX][posY].areaColor);

				tmpList.addAll(this.createAreasHigherFigures(tmpRunner, firstList));

				// finally, collect any known protected figures to Queen
				for (final ProtectedFigure pfRook : tmpTower.getProtectedFigureList()) {
					switch (pfRook.getOrientation()) {
						case HORIZONTAL_LEFT:
						case HORIZONTAL_RIGHT:
						case VERTICAL_DOWN:
						case VERTICAL_UP:
							figure.updateFigureToProtect(pfRook.getProtectedFigure(), pfRook.getOrientation());
					}
				}

				for (final ProtectedFigure pfRunner : tmpRunner.getProtectedFigureList()) {
					switch (pfRunner.getOrientation()) {
						case DIAGONAL_QUARTER_ONE:
						case DIAGONAL_QUARTER_TWO:
						case DIAGONAL_QUARTER_THREE:
						case DIAGONAL_QUARTER_FOUR:
							figure.updateFigureToProtect(pfRunner.getProtectedFigure(), pfRunner.getOrientation());
					}
				}

				tmpRunner = null;
				tmpTower = null;
				firstList = null;
				System.gc();
				break;
			}
			default: {
				/* any other figure left */
				tmpList = this.createAreasHigherFigures(figure, tmpList);
			}
		}
		return tmpList;
	}

	/**
	 * Check, if the created coordinate is valid. A coordinate c = {X,Y} is valid,
	 * if the horizontal area is between 'A' and 'H' and the vertical area is
	 * between '1' and '8'.
	 *
	 * @param tmp
	 *            the coordinate to compare
	 *
	 * @return true, if tmp is a valid coordinate,<br>
	 *         otherwise false
	 */
	private boolean onValidCoordinate(final Coordinates tmp) {
		final char posX = tmp.getPosX().charAt(0);
		final char posY = tmp.getPosY().charAt(0);
		return ((posX >= 'A') && (posX <= 'H') && (posY >= '1') && (posY <= '8'));
	}

	/**
	 * Any higher leveled figure (Rook, Runner, Knight or King) may move more than
	 * one field, except Knight or King.
	 *
	 * @param figure
	 *            the figure to use
	 * @param tmpList
	 *            given list to use
	 *
	 * @return the given list with filled entries
	 */
	@SuppressWarnings("incomplete-switch")
	private <T extends BaseFigure> List<Coordinates> createAreasHigherFigures(final T figure, final List<Coordinates> tmpList) {
		final Coordinates loc = figure.getLocation();
		final FigureColor color = figure.getFigureColor();
		final FigureSet type = figure.getFigureType();

		char posX = loc.getPosX().charAt(0);
		char posY = loc.getPosY().charAt(0);

		Coordinates tmpLoc = new Coordinates(posX, posY);
		int naturalPosX = Math.abs(posX - this.posHorizontalDefault);
		int naturalPosY = Math.abs(posY - this.posVerticalDefault);

		FigureKing spottedKing = null; // in use, if this King is threatened by figure T (except King)

		switch (type) {
			case ROOK:
				/*
				 * → area = {{X-1,Y}, {X-2,Y}, ..., {X+1,Y}, {X+2,Y}, ..., {X,Y-1}, {X,Y-2},
				 * ..., {X, Y+1}, {X,Y+2}, ...
				 */
				/* check left horizontal line */
				tmpLoc = new Coordinates(--posX, posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					/**
					 * check, if on this field:
					 * <ul>
					 * <li>an allied figure has been spotted OR</li>
					 * <li>no figure has been spotted OR</li>
					 * <li>the opponent's King only has been spotted OR</li>
					 * <li>one of the opponent's figure has been spotted (no King)</li>
					 * </ul>
					 */
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						/*
						 * an allied figure has been spotted on this area → add to the protected list
						 * for this Rook
						 */
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.HORIZONTAL_LEFT);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						/* this area is free, where the Rook may move to this field */
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						/*
						 * the opponent's King has been spotted on this location, where all fields
						 * behind this King will also covered by this Rook
						 */
						tmpList.add(tmpLoc);

						/* This King needs also an information of it's current threat. */
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);

					} else {
						/*
						 * otherwise any other opponent figure has been spotted on this area, where this
						 * Rook may move up to this field to be able to beat the opponent's figure
						 */
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(--posX, posY); // go to the next left field
				}

				/* reset tmpLoc */
				tmpLoc = loc;
				posX = loc.getPosX().charAt(0);
				posY = loc.getPosY().charAt(0);

				/* check right horizontal line */
				tmpLoc = new Coordinates(++posX, posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.HORIZONTAL_RIGHT);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(++posX, posY);
				}

				/* reset tmpLoc */
				tmpLoc = loc;
				posX = loc.getPosX().charAt(0);
				posY = loc.getPosY().charAt(0);

				/* check the next bottom vertical line */
				tmpLoc = new Coordinates(posX, --posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.VERTICAL_DOWN);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(posX, --posY);
				}

				/* reset tmpLoc */
				tmpLoc = loc;
				posX = loc.getPosX().charAt(0);
				posY = loc.getPosY().charAt(0);

				/* check the next top vertical line */
				tmpLoc = new Coordinates(posX, ++posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.VERTICAL_UP);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(posX, ++posY);
				}
				break;
			case RUNNER:
				/*
				 * → area = {{X-1,Y-1}, {X-2,Y-2}, ..., {X+1,Y+1}, {X+2,Y+1}, ..., {X+1,Y-1},
				 * {X+1,Y-2}, ..., {X-1, Y+1}, {X-2,Y+2}, ...
				 */
				/* check all areas inside of the top right square */
				tmpLoc = new Coordinates(++posX, ++posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.DIAGONAL_QUARTER_ONE);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(++posX, ++posY);
				}

				/* reset tmpLoc */
				tmpLoc = loc;
				posX = loc.getPosX().charAt(0);
				posY = loc.getPosY().charAt(0);

				/* check all areas inside of the top left square */
				tmpLoc = new Coordinates(--posX, ++posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.DIAGONAL_QUARTER_TWO);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(--posX, ++posY);
				}

				/* reset tmpLoc */
				tmpLoc = loc;
				posX = loc.getPosX().charAt(0);
				posY = loc.getPosY().charAt(0);

				/* check all areas inside of the bottom left square */
				tmpLoc = new Coordinates(--posX, --posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.DIAGONAL_QUARTER_THREE);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(--posX, --posY);
				}

				/* reset tmpLoc */
				tmpLoc = loc;
				posX = loc.getPosX().charAt(0);
				posY = loc.getPosY().charAt(0);

				/* check all areas inside of the bottom right square */
				tmpLoc = new Coordinates(++posX, --posY);
				while (this.onValidCoordinate(tmpLoc)) {
					naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					naturalPosY = Math.abs(posY - this.posVerticalDefault);
					if (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color) {
						final BaseFigure pf = this.coordMatrix[naturalPosX][naturalPosY].figureChess;
						figure.updateFigureToProtect(pf, Orientation.DIAGONAL_QUARTER_FOUR);
						break;
					} else if (!this.coordMatrix[naturalPosX][naturalPosY].isReserved) {
						tmpList.add(tmpLoc);
					} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
						tmpList.add(tmpLoc);
						spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
						spottedKing.addKingsThreat(figure);
					} else {
						tmpList.add(tmpLoc);
						break;
					}
					tmpLoc = new Coordinates(++posX, --posY);
				}
				break;
			case KNIGHT:
				/*
				 * → area = {{X+2,Y+1}, {X+1,Y+2}, {X-1,Y+2}, {X-2,Y+1}, {X-2,Y-1}, {X-1,Y-2},
				 * {X+1,Y-2}, {X+2, Y-1}}
				 */
				final char[][] coordsForKnight = {
						{ this.holder.pos_horizon = (char) (posX + 2), this.holder.pos_vertical = (char) (posY + 1) },
						{ this.holder.pos_horizon = (char) (posX + 1), this.holder.pos_vertical = (char) (posY + 2) },
						{ this.holder.pos_horizon = (char) (posX - 1), this.holder.pos_vertical = (char) (posY + 2) },
						{ this.holder.pos_horizon = (char) (posX - 2), this.holder.pos_vertical = (char) (posY + 1) },
						{ this.holder.pos_horizon = (char) (posX - 2), this.holder.pos_vertical = (char) (posY - 1) },
						{ this.holder.pos_horizon = (char) (posX - 1), this.holder.pos_vertical = (char) (posY - 2) },
						{ this.holder.pos_horizon = (char) (posX + 1), this.holder.pos_vertical = (char) (posY - 2) },
						{ this.holder.pos_horizon = (char) (posX + 2), this.holder.pos_vertical = (char) (posY - 1) }
				};

				for (int i = 0; i < Matrix.MAX_FIELD_NUMBER; i++) {
					final String builtCoord = Coordinates.getCoordinatesXY(coordsForKnight[i][0], coordsForKnight[i][1]);
					tmpLoc = new Coordinates(builtCoord);
					naturalPosX = Math.abs(tmpLoc.getPosX().charAt(0) - this.posHorizontalDefault);
					naturalPosY = Math.abs(tmpLoc.getPosY().charAt(0) - this.posVerticalDefault);
					if (this.onValidCoordinate(tmpLoc)) {
						/*
						 * check, if there is any figure with the same color, then this figure is going
						 * to protect by this Knight
						 */
						if (this.coordMatrix[naturalPosX][naturalPosY].isReserved && (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color)) {
							figure.updateFigureToProtect(this.coordMatrix[naturalPosX][naturalPosY].figureChess, null);
						} else if ((this.coordMatrix[naturalPosX][naturalPosY].figureType == FigureSet.KING) && (this.coordMatrix[naturalPosX][naturalPosY].figureColor != color)) {
							/* check, if the opponent's King has been spotted there */
							spottedKing = FigureHolder.INSTANCE.getKing(FigureColor.getOpponentColor(color));
							spottedKing.addKingsThreat(figure);
						} else {
							/*
							 * otherwise any figure with the opponent's color has been spotted or this field
							 * is empty
							 */
							tmpList.add(tmpLoc);
						}
					}
				}
				break;
			case KING:
				/*
				 * → area = {{X+1,Y}, {X+1,Y+1}, {X,Y+1}, {X-1,Y+1}, {X-1,Y}, {X-1,Y-1}, {X,
				 * Y-1}, {X+1,Y-1}}
				 */
				final char[][] coordsForKing = {
						{ this.holder.pos_horizon = (char) (posX + 1), this.holder.pos_vertical = (posY) },
						{ this.holder.pos_horizon = (char) (posX + 1), this.holder.pos_vertical = (char) (posY + 1) },
						{ this.holder.pos_horizon = (posX), this.holder.pos_vertical = (char) (posY + 1) },
						{ this.holder.pos_horizon = (char) (posX - 1), this.holder.pos_vertical = (char) (posY + 1) },
						{ this.holder.pos_horizon = (char) (posX - 1), this.holder.pos_vertical = (posY) },
						{ this.holder.pos_horizon = (char) (posX - 1), this.holder.pos_vertical = (char) (posY - 1) },
						{ this.holder.pos_horizon = (posX), this.holder.pos_vertical = (char) (posY - 1) },
						{ this.holder.pos_horizon = (char) (posX + 1), this.holder.pos_vertical = (char) (posY - 1) }
				};

				for (int i = 0; i < Matrix.MAX_FIELD_NUMBER; i++) {
					final String builtCoord = Coordinates.getCoordinatesXY(coordsForKing[i][0], coordsForKing[i][1]);
					tmpLoc = new Coordinates(builtCoord);
					naturalPosX = Math.abs(tmpLoc.getPosX().charAt(0) - this.posHorizontalDefault);
					naturalPosY = Math.abs(tmpLoc.getPosY().charAt(0) - this.posVerticalDefault);
					if (this.onValidCoordinate(tmpLoc)) {
						if (this.coordMatrix[naturalPosX][naturalPosY].isReserved && (this.coordMatrix[naturalPosX][naturalPosY].figureColor == color)) {
							figure.updateFigureToProtect(this.coordMatrix[naturalPosX][naturalPosY].figureChess, null);
						} else {
							tmpList.add(tmpLoc);
						}
					}
				}
				break;
		}

		return tmpList;
	}

	/**
	 * Check, if any figure T, except Knight, may move from start to destination,
	 * where:
	 * <ul>
	 * <li>each area between start and destination must be free</li>
	 * <ul>
	 * <li>for each spotted allied figure: this figure must not be located from
	 * start+1 to destination</li>
	 * <li>for each spotted opponent figure: this figure must not be located from
	 * start+1 to destination-1</li>
	 * </ul>
	 * </ul>
	 *
	 * If all conditions returns true, then this function will also return true,
	 * otherwise false.
	 * <hr>
	 * <br>
	 *
	 * <section> The figure King has a special usage for this function. It's only
	 * need to know if the King's destination is not covered by any figure. If true,
	 * then this destination field is free, but there is no guarantee to move to
	 * this destination. In this case a specialized method in King's class checks,
	 * if this free destination is not covered by any figure of the opponent.
	 * Otherwise a figure has been spotted on destination, thus it's required to
	 * know which figure is on this coordinate. </section><br>
	 * <hr>
	 *
	 * <footer> For more details inside of section-tag:
	 * {@link FigureKing#onValidMove(Coordinates)} </footer>
	 *
	 * @param figure
	 *            the figure to use
	 * @param start
	 *            the start location
	 * @param destination
	 *            the destination location
	 *
	 * @return true, if and only if all conditions checks returns true,<br>
	 *         false, otherwise
	 */
	@SuppressWarnings("incomplete-switch")
	public <T extends BaseFigure> boolean onFreeAreas(final T figure, final Coordinates start, final Coordinates destination) {
		// check, if the given coordinates are valid
		if (!this.onValidCoordinate(start) || !this.onValidCoordinate(destination)) {
			return false;
		}

		// also check, if start is not equal to destination
		if (start.getCoordinatesXY().equals(destination.getCoordinatesXY())) {
			return false;
		}

		/*
		 * properties for a better usage; We assume that all fields between start and
		 * destination are free.
		 */
		final String NO_FREE_AREAS_AVAILABLE = "NO_FREE_AREAS_AVAILABLE";
		String currentResult = "STILL_AVAILABLE";
		boolean conditionFreeAreas = true;

		/* variables to use */
		Coordinates tmp = null;
		final FigureSet type = figure.getFigureType();

		char cX = start.getPosX().charAt(0);
		char cY = start.getPosY().charAt(0);
		final char cX_destination = destination.getPosX().charAt(0);
		final char cY_destination = destination.getPosY().charAt(0);

		int posX = 0;
		int posY = 0;
		Orientation orientation = null;

		switch (type) {
			case ROOK:
				orientation = this.getCurrentOrientation(start, destination, type);
				if (orientation == null) { // it's a save action, if the orientation could not being determined
					currentResult = NO_FREE_AREAS_AVAILABLE;
					break;
				} else {
					switch (orientation) {
						case HORIZONTAL_LEFT:
							tmp = new Coordinates(--cX, cY);

							// while the destination coordinate has not been reached yet
							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);

								// just check, if the field f between TMP and DESTINATION is not blocked
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									/*
									 * In this case a figure (no matter, if an allied figure or an opponent's
									 * figure) has been spotted, thus the destination is unable to reach. There is
									 * no check anywhere, thus the boolean flag 'conditionFreeAreas' will be set to
									 * 'false'.
									 */
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}

								// go to the next left area
								tmp = new Coordinates(--cX, cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
						case HORIZONTAL_RIGHT:
							tmp = new Coordinates(++cX, cY);
							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}
								tmp = new Coordinates(++cX, cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
						case VERTICAL_UP:
							tmp = new Coordinates(cX, ++cY);
							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}
								tmp = new Coordinates(cX, ++cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
						case VERTICAL_DOWN:
							tmp = new Coordinates(cX, --cY);
							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}

								tmp = new Coordinates(cX, --cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
					}

					break; // end switch(type)
				}
			case RUNNER:
				orientation = this.getCurrentOrientation(start, destination, type);
				if (orientation == null) { // it's a save action, if the orientation could not being determined
					currentResult = NO_FREE_AREAS_AVAILABLE;
					break;
				} else {
					switch (orientation) {
						case DIAGONAL_QUARTER_ONE:
							tmp = new Coordinates(++cX, ++cY);

							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}
								// go to the next top right area
								tmp = new Coordinates(++cX, ++cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
						case DIAGONAL_QUARTER_TWO:
							tmp = new Coordinates(--cX, ++cY);

							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}
								// go to the next top left area
								tmp = new Coordinates(--cX, ++cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
						case DIAGONAL_QUARTER_THREE:
							tmp = new Coordinates(--cX, --cY);

							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}
								// go to the next bottom left area
								tmp = new Coordinates(--cX, --cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
						case DIAGONAL_QUARTER_FOUR:
							tmp = new Coordinates(++cX, --cY);

							do {
								posX = Math.abs(cX - this.posHorizontalDefault);
								posY = Math.abs(cY - this.posVerticalDefault);
								if (this.coordMatrix[posX][posY].isReserved && (this.coordMatrix[posX][posY].figureColor == figure.getFigureColor())) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
									break;
								}
								// go to the next bottom right area
								tmp = new Coordinates(++cX, --cY);
							} while (!(destination.getCoordinatesXY().equals(tmp.getCoordinatesXY())));

							break; // end switch(orientation)
					}
					break; // end switch(type)
				}
			case QUEEN:
				orientation = this.getCurrentOrientation(start, destination, FigureSet.QUEEN);
				if (orientation == null) { // it's a save action, if the orientation could not being determined
					currentResult = NO_FREE_AREAS_AVAILABLE;
					break;
				} else {
					switch (orientation) {
						case HORIZONTAL_LEFT:
						case HORIZONTAL_RIGHT:
						case VERTICAL_DOWN:
						case VERTICAL_UP:

							/*
							 * The Queen wants to move like a Rook, thus we're creating a temporary Rook on
							 * Queen's position.
							 */
							final FigureRook tmpRook = new FigureRook(start, figure.getFigureColor(), TowerIdentification.IMPOSSIBLE_TO_CASTLE);

							// copy all protected figures from Queen to tmpRook to handle with
							// recursive calls
							for (final ProtectedFigure pf : figure.getProtectedFigureList()) {
								switch (pf.getOrientation()) {
									case HORIZONTAL_LEFT:
									case HORIZONTAL_RIGHT:
									case VERTICAL_DOWN:
									case VERTICAL_UP:
										tmpRook.updateFigureToProtect(pf.getProtectedFigure(), pf.getOrientation());
								}
							}
							// call this method by using recursion
							boolean subChecker = this.onFreeAreas(tmpRook, start, destination);
							if (subChecker == false) {
								/*
								 * In this case the Queen want's to move like a Rook, but there was an obstacle,
								 * thus it's no need to check with a Runner and this method will also return
								 * false.
								 */
								currentResult = NO_FREE_AREAS_AVAILABLE;
							}
							break;
						default:
							/*
							 * Otherwise the Queen wants to move like a Runner. Do the same operations for a
							 * temporary Runner and check, if the Queen is able to move from start to
							 * destination.
							 */
							posX = Math.abs(cX - this.posHorizontalDefault);
							posY = Math.abs(cY - this.posVerticalDefault);
							final FigureRunner tmpRunner = new FigureRunner(start, figure.getFigureColor(), this.coordMatrix[posX][posY].areaColor);
							for (final ProtectedFigure pf : figure.getProtectedFigureList()) {
								switch (pf.getOrientation()) {
									case DIAGONAL_QUARTER_ONE:
									case DIAGONAL_QUARTER_TWO:
									case DIAGONAL_QUARTER_THREE:
									case DIAGONAL_QUARTER_FOUR:
										tmpRunner.updateFigureToProtect(pf.getProtectedFigure(), pf.getOrientation());
								}
							}
							subChecker = this.onFreeAreas(tmpRunner, start, destination);
							if (subChecker == false) {
								currentResult = NO_FREE_AREAS_AVAILABLE;
							}
							break; // end switch(orientation)
					}
					break; // end switch(type)
				}
			case PAWN:
				int fieldDifference = Math.abs(cY - destination.getPosY().charAt(0));
				posX = Math.abs(destination.getPosX().charAt(0) - this.posHorizontalDefault);
				posY = Math.abs(destination.getPosY().charAt(0) - this.posVerticalDefault);
				final FigurePawn tmpPawn = (FigurePawn) figure;
				final FigureColor colorPawn = tmpPawn.getFigureColor();

				switch (colorPawn) {
					case WHITE:
						/*
						 * Check, if the white Pawn is able to do this move having on the field
						 * difference. For each normal move the X axis of start and destination shall be
						 * equal, where the Y axis of start and destination is different.
						 */
						if ((cX == cX_destination) && (cY < cY_destination)) {

							if (fieldDifference == 1) {
								if (this.coordMatrix[posX][posY].isReserved) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
								}
							} else if (fieldDifference == 2) {
								/*
								 * Only in use, if the given figure Pawn has not been moved before, thus it's
								 * possible to move two fields upwards once only.
								 */
								if (tmpPawn.onFirstMove()) {
									while (fieldDifference != 0) {
										if (this.coordMatrix[posX][posY].isReserved) {
											currentResult = NO_FREE_AREAS_AVAILABLE;
											break;
										}

										fieldDifference--;
										posY--;
									}
								} else {
									/*
									 * The Pawn want's move two fields, but it's impossible.
									 */
									currentResult = NO_FREE_AREAS_AVAILABLE;
								}
							} else {
								/*
								 * In this case the Pawn wants to move more than 2 fields, which is still
								 * invalid.
								 */
								currentResult = NO_FREE_AREAS_AVAILABLE;
							}
						} else {
							/*
							 * The Pawn want's to move backwards or sidewards (capturing), which is normally
							 * impossible.
							 */
							currentResult = NO_FREE_AREAS_AVAILABLE;
						}
						break;
					case BLACK:
						if ((cX == cX_destination) && (cY > cY_destination)) {

							if (fieldDifference == 1) {
								if (this.coordMatrix[posX][posY].isReserved) {
									currentResult = NO_FREE_AREAS_AVAILABLE;
								}
							} else if (fieldDifference == 2) {
								/*
								 * Only in use, if the given figure Pawn has not been moved before, thus it's
								 * possible to move two fields upwards once only.
								 */
								if (tmpPawn.onFirstMove()) {
									while (fieldDifference != 0) {
										if (this.coordMatrix[posX][posY].isReserved) {
											currentResult = NO_FREE_AREAS_AVAILABLE;
											break;
										}
										fieldDifference--;
										posY++;
									}
								} else {
									/*
									 * The Pawn want's move two fields, but it's impossible.
									 */
									currentResult = NO_FREE_AREAS_AVAILABLE;
								}
							} else {
								/*
								 * In this case the Pawn wants to move more than 2 fields, which is still
								 * invalid.
								 */
								currentResult = NO_FREE_AREAS_AVAILABLE;
							}
						} else {
							/*
							 * The Pawn want's to move backwards or sidewards, which is normally impossible.
							 */
							currentResult = NO_FREE_AREAS_AVAILABLE;
						}

						break;
				} // end switch(colorPawn)
				break; // end switch(type)
			case KING:
				posX = Math.abs(destination.getPosX().charAt(0) - this.posHorizontalDefault);
				posY = Math.abs(destination.getPosY().charAt(0) - this.posVerticalDefault);
				final FigureKing tmpKing = (FigureKing) figure;
				// just check, if the destination field may be blocked
				if (this.coordMatrix[posX][posY].isReserved) {
					/*
					 * The destination field is blocked. Now it's required to know, which figure
					 * blocks this field.
					 */
					final BaseFigure spottedFigure = this.coordMatrix[posX][posY].figureChess;
					if (this.coordMatrix[posX][posY].figureColor.equals(tmpKing.getFigureColor())) {
						// An allied figure blocks this field.
						tmpKing.updateDestinationNotifier(KingDestinationNotifier.FIELD_IS_BLOCKED_BY_ALLY, spottedFigure);
						currentResult = NO_FREE_AREAS_AVAILABLE;
					} else {
						tmpKing.updateDestinationNotifier(KingDestinationNotifier.FIELD_IS_BLOCKED_BY_OPPONENT, spottedFigure);
					}
				} else {
					/*
					 * Otherwise no figure blocks this field, but there is no information if any
					 * figure by the opponent could cover this area.
					 */
					tmpKing.updateDestinationNotifier(KingDestinationNotifier.FIELD_IS_FREE, null);
				}
				break;
		} // end switch

		if (currentResult.equals(NO_FREE_AREAS_AVAILABLE)) {
			conditionFreeAreas = false;
		}

		return conditionFreeAreas;
	}

	/**
	 * Receive the current orientation depending on current coordinate and it's
	 * destination. Only in use for figure Runner, Rook or Queen.
	 *
	 * @param current
	 *            the current coordinate
	 * @param destination
	 *            the destination coordinate
	 * @param type
	 *            the figure type (Rook, Runner, Queen only)
	 *
	 * @return the current orientation<br>
	 *         or null, if given type was not a required type to handle with or the
	 *         destination is invalid having on given figure
	 *
	 * @see {@link Orientation}
	 */
	@SuppressWarnings("incomplete-switch")
	private Orientation getCurrentOrientation(final Coordinates current, final Coordinates destination, final FigureSet type) {
		if ((type == FigureSet.RUNNER) || (type == FigureSet.ROOK) || (type == FigureSet.QUEEN)) {
			final char currentX = current.getPosX().charAt(0);
			final char currentY = current.getPosY().charAt(0);
			final char destX = destination.getPosX().charAt(0);
			final char destY = destination.getPosY().charAt(0);
			switch (type) {
				case RUNNER:
					/*
					 * Without the checks below a set of different exceptions occurred, if the
					 * Runner wants to move to an invalid destination field.
					 */
					final int correctDistanceX = Math.abs(currentX - destX);
					final int correctDistanceY = Math.abs(currentY - destY);
					final boolean equalValues = (correctDistanceX == correctDistanceY);
					if ((currentX < destX) && (currentY < destY) && equalValues) {
						return Orientation.DIAGONAL_QUARTER_ONE;
					} else if ((currentX > destX) && (currentY < destY) && equalValues) {
						return Orientation.DIAGONAL_QUARTER_TWO;
					} else if ((currentX > destX) && (currentY > destY) && equalValues) {
						return Orientation.DIAGONAL_QUARTER_THREE;
					} else if ((currentX < destX) && (currentY > destY) && equalValues) {
						return Orientation.DIAGONAL_QUARTER_FOUR;
					}
					break;
				case ROOK:
					if ((currentX > destX) && (currentY == destY)) {
						return Orientation.HORIZONTAL_LEFT;
					} else if ((currentX < destX) && (currentY == destY)) {
						return Orientation.HORIZONTAL_RIGHT;
					} else if ((currentX == destX) && (currentY < destY)) {
						return Orientation.VERTICAL_UP;
					} else if ((currentX == destX) && (currentY > destY)) {
						return Orientation.VERTICAL_DOWN;
					}
					break;
				case QUEEN:
					Orientation tmp = this.getCurrentOrientation(current, destination, FigureSet.ROOK); // try to move like a Rook
					if (tmp != null) {
						return tmp; // HORIZOTNAL_LEFT, HORIZOTNAL_RIGHT, VERTICAL_UP or VERTICAL_DOWN
					}
					tmp = this.getCurrentOrientation(current, destination, FigureSet.RUNNER); // try to move like a Runner
					if (tmp != null) {
						return tmp;
					}
			}
		}
		return null;
	}

	/**
	 * Check, if two Pawns, a white and the other one a black Pawn, are neighbors,
	 * which means the difference of the X coordinate of both Pawns is 1 only and
	 * the Y coordinate of both Pawns are identical. It's required to know, if the
	 * first Pawn may hit the second Pawn by "en passant".
	 *
	 * @param firstPawn
	 *            the first Pawn
	 * @param secondPawn
	 *            the second Pawn
	 *
	 * @return true, if and only if the given condition checks returns true, <br>
	 *         otherwise false
	 */
	public boolean onPawnNeighbors(final FigurePawn firstPawn, final FigurePawn secondPawn) {
		boolean neighborsPawn = false;
		final int posX_Pawn01 = Math.abs(firstPawn.getLocation().getPosX().charAt(0) - this.posHorizontalDefault);
		final int posY_Pawn01 = Math.abs(firstPawn.getLocation().getPosY().charAt(0) - this.posVerticalDefault);
		final int posX_Pawn02 = Math.abs(secondPawn.getLocation().getPosX().charAt(0) - this.posHorizontalDefault);
		final int posY_Pawn02 = Math.abs(secondPawn.getLocation().getPosY().charAt(0) - this.posVerticalDefault);
		final char posX = firstPawn.getLocation().getPosX().charAt(0);
		final char posY = firstPawn.getLocation().getPosY().charAt(0);
		// first check, if both Pawns are exactly located on the matrix
		final boolean firstFigureCondition = (this.coordMatrix[posX_Pawn01][posY_Pawn01].isReserved && this.coordMatrix[posX_Pawn01][posY_Pawn01].figureChess.equals(firstPawn));
		final boolean secondFigureCondition = (this.coordMatrix[posX_Pawn02][posY_Pawn02].isReserved && this.coordMatrix[posX_Pawn02][posY_Pawn02].figureChess.equals(secondPawn));
		if (firstFigureCondition && secondFigureCondition) {
			final String locPawn_first = firstPawn.getLocation().getCoordinatesXY();
			final String locPawn_second = secondPawn.getLocation().getCoordinatesXY();
			// check, if the own pawn is located between "A6" and "H6" → in this case
			// "firstPawn" has a white color
			if (((posX >= 'A') && (posY == '6')) && ((posX <= 'H') && (posY == '6'))) {
				// now check, if firstPawn is located on "A6"
				if (locPawn_first.equals("A6")) {
					// in this case the second Pawn have to be on position "B6"
					if (locPawn_second.equals("B6")) {
						neighborsPawn = true;
					}
				} else if (locPawn_first.equals("H6")) {
					/*
					 * otherwise check, if the first Pawn is on position "H6" → in that case the
					 * second Pawn must be on "G6"
					 */
					if (locPawn_second.equals("G6")) {
						neighborsPawn = true;
					}
				} else {
					/*
					 * otherwise the first Pawn is between "B6" and "G6" → check, if the distance of
					 * the second Pawn to the first Pawn is exactly one
					 */
					if ((Math.abs(posX_Pawn01 - posX_Pawn02) == 1) && (posY_Pawn02 == posY_Pawn01)) {
						neighborsPawn = true;
					}
				}
			} else if ((posX >= 'A') && (posY == '4') && (posX <= 'H') && (posY == '4')) {
				if (locPawn_first.equals("A4")) {
					if (locPawn_second.equals("B4")) {
						neighborsPawn = true;
					}
				} else if (locPawn_first.equals("H4")) {
					if (locPawn_second.equals("G4")) {
						neighborsPawn = true;
					}
				} else {
					if ((Math.abs(posX_Pawn01 - posX_Pawn02) == 1) && (posY_Pawn02 == posY_Pawn01)) {
						neighborsPawn = true;
					}
				}
			}
		}
		return neighborsPawn;
	}

	/**
	 * Check, if the given Pawn has reached it's last coordinate, depending on the
	 * Pawn's color. If true, then this Pawn is able to getting promoted to a higher
	 * leveled figure.
	 *
	 * @param location
	 *            the location to test
	 * @param figurePawn
	 *            the Pawn to use
	 *
	 * @return true, if the given figure Pawn is on it's last coordinate,<br>
	 *         otherwise false
	 */
	public boolean onLastMatrixCoordinate(final Coordinates location, final FigurePawn figurePawn) {
		boolean lastCoordFlag = false;
		final FigureColor colorPawn = figurePawn.getFigureColor();
		switch (colorPawn) {
			case WHITE:
				// for white: check, if this Pawn is in the eighth row
				if (location.getPosY().charAt(0) == this.lastVerticalValue) {
					lastCoordFlag = true;
				}
				break;
			case BLACK:
				// for black: check, if this Pawn in the first row
				if (location.getPosY().charAt(0) == this.posVerticalDefault) {
					lastCoordFlag = true;
				}
				break;
		}
		return lastCoordFlag;
	}

	/**
	 * Whenever a figure has been beaten, a Pawn is also beaten, when it became to a
	 * higher leveled figure by promotion, then this figure doesn't no longer exist
	 * in the matrix class. This won't work for a figure King.
	 *
	 * Finally, any allied figure may also protect the figure T, thus every allied
	 * figure has to remove figure T from it's protected figure list, if there was a
	 * match.
	 *
	 * @param figure
	 *            the figure to remove
	 */
	public <T extends BaseFigure> boolean removeFigureFromMatrix(final T figure) {
		boolean success = false;
		// every figure, except the King, is able to being removed from matrix class
		if (figure.getFigureType() != FigureSet.KING) {
			final Coordinates figureLocation = figure.getLocation();
			final int locX = (Math.abs(figureLocation.getPosX().charAt(0)) - this.posHorizontalDefault);
			final int locY = (Math.abs(figureLocation.getPosY().charAt(0)) - this.posVerticalDefault);
			// check, if on this coordinate is the given figure
			if (this.coordMatrix[locX][locY].isReserved && this.coordMatrix[locX][locY].figureChess.equals(figure)) {
				this.coordMatrix[locX][locY].figureChess = null;
				this.coordMatrix[locX][locY].figureColor = null;
				this.coordMatrix[locX][locY].figureType = null;
				this.coordMatrix[locX][locY].isReserved = false;
				success = true;
			}
		}
		return success;
	}

	/**
	 * Whenever a figure has been moved or beaten, then any other allied figure T+1,
	 * which has protected the given figure T has to remove T from it's protected
	 * figure list.
	 *
	 * @param figure
	 *            the figure to remove from any allied protected figure list
	 * @param color
	 *            the figure color
	 * @param type
	 *            the figure type
	 */
	public <T extends BaseFigure> void removeProtections(final T figure, final FigureColor color, final FigureSet type) {
		if (figure != null) {
			// get the allied King
			final FigureKing tmpKing = FigureHolder.INSTANCE.getKing(color);
			for (final ProtectedFigure pf : tmpKing.getProtectedFigureList()) {
				if (pf.getProtectedFigure().equals(figure)) {
					tmpKing.removeFigureToProtect(pf);
					break; // otherwise an InvocationTargetException occurs
				}
			}
			// check any other figure left
			for (final FigureSet tmpType : FigureSet.values()) {
				if (!tmpType.equals(FigureSet.KING)) { // ignore the King
					for (final BaseFigure baseFigure : FigureHolder.INSTANCE.getListOfAllFiguresBy(tmpType, color)) {
						if (!baseFigure.equals(figure)) { // baseFigure must not be equal to the given figure
							for (final ProtectedFigure protectedFigrure : baseFigure.getProtectedFigureList()) {
								if (protectedFigrure.getProtectedFigure().equals(figure)) {
									baseFigure.removeFigureToProtect(protectedFigrure);
									break; // otherwise an InvocationTargetException occurs
								}
							}
						}
					}
				}
			}
		}
	}

	/**
	 * Check, if the Knight is able to move from it's start location to the given
	 * destination.
	 *
	 * @param destination
	 *            the destination coordinate
	 * @param knight
	 *            the Knight to use
	 * @param set
	 *            the given set of valid moves to compare
	 *
	 * @return true, if the move is valid,<br>
	 *         false, otherwise
	 */
	public boolean onValidKnightMove(final Coordinates destination, final FigureKnight knight, final List<MoveSetKnight[]> set) {
		/* get each coordinate */
		char posXStart = knight.getLocation().getPosX().charAt(0);
		char posYStart = knight.getLocation().getPosY().charAt(0);
		final char posXDest = destination.getPosX().charAt(0);
		final char posYDest = destination.getPosY().charAt(0);

		/*
		 * Check, if the distance between start and destination of X and Y coordinate is
		 * not higher than 2. It false, then the Knight would like to move to an invalid
		 * coordinate, thus the current Knight move is invalid.
		 */
		final int distanceHorizon = Math.abs(posXStart - posXDest);
		final int distanceVertical = Math.abs(posYStart - posYDest);

		final boolean validVerticalBegin = ((distanceHorizon == 1) && (distanceVertical == 2));
		final boolean validHorizontalBegin = ((distanceHorizon == 2) && (distanceVertical == 1));

		/* check, if the Knight move is valid */
		if (validVerticalBegin || validHorizontalBegin) {
			/* generating the sub move from start to destination */
			final MoveSetKnight subMove[] = new MoveSetKnight[3];
			int ctrMove = 0;

			/* check horizontal area */
			while (posXStart != posXDest) {
				if (posXStart < posXDest) {
					subMove[ctrMove] = MoveSetKnight.HORIZONTAL_RIGHT;
					posXStart++;
				} else if (posXStart > posXDest) {
					subMove[ctrMove] = MoveSetKnight.HORIZONTAL_LEFT;
					posXStart--;
				}
				ctrMove++;
			}

			/* check vertical area */
			while (posYStart != posYDest) {
				if (posYStart < posYDest) {
					subMove[ctrMove] = MoveSetKnight.VERTICAL_UP;
					posYStart++;
				} else if (posYStart > posYDest) {
					subMove[ctrMove] = MoveSetKnight.VERTICAL_DOWN;
					posYStart--;
				}
				ctrMove++;
			}
			/*
			 * the generated sub move has been created; now check, if this set is a part of
			 * the default move set
			 */
			for (final MoveSetKnight[] tmp : set) {
				if (subMove[0].equals(tmp[0]) && subMove[1].equals(tmp[1]) && subMove[2].equals(tmp[2])) {
					return true;
				}
			}
		}

		return false;
	}

	/**
	 * Update the protection list of figure baseFigure, for Rook, Runner and Queen
	 * only. This figure may protect a figure, like pf1 (protected figure 1).
	 * Whenever any other allied figure (pf2) will be between baseFigure and pf1,
	 * then this figure interrupts the protection chain and pf2 will now be
	 * protected by baseFigure, where pf1 is no longer protected by baseFigure.
	 *
	 * @param baseFigure
	 *            the figure which protects other figures (0, 1, ..., n-1)
	 * @param interruptedFigure
	 *            the figure which interrupts the protection chain
	 * @param listOfProtectedFigures
	 *            the protection list
	 *
	 * @return required, otherwise an InvocationTargetException occurs, thus 'true'
	 *         or 'false' doesn't matter yet
	 */
	@SuppressWarnings("incomplete-switch")
	public <T extends BaseFigure> boolean onInterruptProtectionChain(final T baseFigure, final T interruptedFigure, final List<ProtectedFigure> listOfProtectedFigures) {
		/* basic properties */
		final FigureSet type = baseFigure.getFigureType();
		final FigureColor color = baseFigure.getFigureColor();

		/* get the coordinates between BF and IF */
		final Coordinates locBaseFigure = baseFigure.getLocation();
		final Coordinates locInterrupted = interruptedFigure.getLocation();

		/* holds the location to modify */
		Coordinates tmpLoc = locBaseFigure;
		char posX = tmpLoc.getPosX().charAt(0);
		char posY = tmpLoc.getPosY().charAt(0);

		for (final ProtectedFigure pf : listOfProtectedFigures) {
			switch (type) {
				case ROOK:
					tmpLoc = new Coordinates(--posX, posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.HORIZONTAL_LEFT) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.HORIZONTAL_LEFT);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(--posX, posY);
					}

					/* reset properties */
					tmpLoc = locBaseFigure;
					posX = tmpLoc.getPosX().charAt(0);
					posY = tmpLoc.getPosY().charAt(0);

					tmpLoc = new Coordinates(++posX, posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.HORIZONTAL_RIGHT) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.HORIZONTAL_RIGHT);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(++posX, posY);
					}

					/* reset properties */
					tmpLoc = locBaseFigure;
					posX = tmpLoc.getPosX().charAt(0);
					posY = tmpLoc.getPosY().charAt(0);

					tmpLoc = new Coordinates(posX, --posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.VERTICAL_DOWN) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.VERTICAL_DOWN);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(posX, --posY);
					}

					/* reset properties */
					tmpLoc = locBaseFigure;
					posX = tmpLoc.getPosX().charAt(0);
					posY = tmpLoc.getPosY().charAt(0);

					tmpLoc = new Coordinates(posX, ++posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.VERTICAL_UP) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.VERTICAL_UP);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(posX, ++posY);
					}
					break;
				case RUNNER:
					tmpLoc = new Coordinates(++posX, ++posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.DIAGONAL_QUARTER_ONE) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.DIAGONAL_QUARTER_ONE);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(++posX, ++posY);
					}

					/* reset properties */
					tmpLoc = locBaseFigure;
					posX = tmpLoc.getPosX().charAt(0);
					posY = tmpLoc.getPosY().charAt(0);

					tmpLoc = new Coordinates(--posX, ++posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.DIAGONAL_QUARTER_TWO) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.DIAGONAL_QUARTER_TWO);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(--posX, ++posY);
					}

					/* reset properties */
					tmpLoc = locBaseFigure;
					posX = tmpLoc.getPosX().charAt(0);
					posY = tmpLoc.getPosY().charAt(0);

					tmpLoc = new Coordinates(--posX, --posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.DIAGONAL_QUARTER_THREE) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.DIAGONAL_QUARTER_THREE);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(--posX, --posY);
					}

					/* reset properties */
					tmpLoc = locBaseFigure;
					posX = tmpLoc.getPosX().charAt(0);
					posY = tmpLoc.getPosY().charAt(0);

					tmpLoc = new Coordinates(++posX, --posY);
					while (this.onValidCoordinate(tmpLoc)) {
						if (locInterrupted.getCoordinatesXY().equals(tmpLoc.getCoordinatesXY())) {
							if (pf.getOrientation() == Orientation.DIAGONAL_QUARTER_FOUR) {
								baseFigure.removeFigureToProtect(pf);
								if (interruptedFigure.getFigureColor() == color) {
									baseFigure.updateFigureToProtect(interruptedFigure, Orientation.DIAGONAL_QUARTER_FOUR);
								}
								return true;
							}
						}
						tmpLoc = new Coordinates(++posX, --posY);
					}
					break;
				case QUEEN:
					FigureRook tmpRook = new FigureRook(locBaseFigure, color, TowerIdentification.IMPOSSIBLE_TO_CASTLE);
					tmpRook.updateThreatList(this.createNewThreatList(tmpRook, locBaseFigure));

					/* step 1: tmpRook needs to know all protected figures of the Queen */
					for (final ProtectedFigure pf1 : baseFigure.getProtectedFigureList()) {
						switch (pf1.getOrientation()) {
							case HORIZONTAL_LEFT:
							case HORIZONTAL_RIGHT:
							case VERTICAL_DOWN:
							case VERTICAL_UP:
								tmpRook.updateFigureToProtect(pf1.getProtectedFigure(), pf1.getOrientation());
						}
					}

					/* step 2: tmpRunner needs to know all protected figures of the Queen */
					final int naturalPosX = Math.abs(posX - this.posHorizontalDefault);
					final int naturalPosY = Math.abs(posY - this.posVerticalDefault);
					FigureRunner tmpRunner = new FigureRunner(locBaseFigure, color, this.coordMatrix[naturalPosX][naturalPosY].areaColor);
					tmpRunner.updateThreatList(this.createNewThreatList(tmpRunner, locBaseFigure));

					for (final ProtectedFigure pf2 : baseFigure.getProtectedFigureList()) {
						switch (pf2.getOrientation()) {
							case DIAGONAL_QUARTER_ONE:
							case DIAGONAL_QUARTER_TWO:
							case DIAGONAL_QUARTER_THREE:
							case DIAGONAL_QUARTER_FOUR:
								tmpRunner.updateFigureToProtect(pf2.getProtectedFigure(), pf2.getOrientation());
						}
					}

					/* step 3: do a recursive step with tmpRook and tmpRunner */
					this.onInterruptProtectionChain(tmpRook, interruptedFigure, tmpRook.getProtectedFigureList());
					this.onInterruptProtectionChain(tmpRunner, interruptedFigure, tmpRunner.getProtectedFigureList());

					/*
					 * step 4: remove the Queen's protected figures and replace with tmpRook and
					 * tmpRunner
					 */
					baseFigure.removeAllProtectedFigures();
					for (final ProtectedFigure pf3 : tmpRook.getProtectedFigureList()) {
						switch (pf3.getOrientation()) {
							case HORIZONTAL_LEFT:
							case HORIZONTAL_RIGHT:
							case VERTICAL_DOWN:
							case VERTICAL_UP:
								baseFigure.updateFigureToProtect(pf3.getProtectedFigure(), pf3.getOrientation());
						}
					}

					for (final ProtectedFigure pf4 : tmpRunner.getProtectedFigureList()) {
						switch (pf4.getOrientation()) {
							case DIAGONAL_QUARTER_ONE:
							case DIAGONAL_QUARTER_TWO:
							case DIAGONAL_QUARTER_THREE:
							case DIAGONAL_QUARTER_FOUR:
								baseFigure.updateFigureToProtect(pf4.getProtectedFigure(), pf4.getOrientation());
						}
					}

					/* final step: clean up */
					tmpRunner = null;
					tmpRook = null;
					System.gc();
					break;
			}
		}
		return false;
	}

	/**
	 * Check, if all areas between the King and it's Rook for a castling are free.
	 * It's required to know, if all areas are not blocked by any figure and no
	 * figure from the opponent may not cover one of these areas.
	 *
	 * @param color
	 *            the figure color
	 * @param coordKing
	 *            the King's location
	 * @param coordRook
	 *            the Rook's location
	 * @return true, if all areas are free and not covered by any figure of the
	 *         opponent,<br>
	 *         false, otherwise
	 */
	public boolean onFreeCastlingAreas(final FigureColor color, final Coordinates coordKing, final Coordinates coordRook) {
		/*
		 * both flags below are required, where freeAreas shall be true and
		 * threatenedAreas shall be false to ensure to do a castling
		 */
		boolean freeAreas = true;
		boolean threatenedAreas = false;
		boolean justCompleted = false;
		/* holds all coordinates from King to Rook or in the other way around */
		List<CoordinationHolder> coordHolder = new ArrayList<>();
		boolean rookComesFirst = false;
		for (final char cX : this.HORIZONTAL_ARRAY) {
			if (!justCompleted) {
				for (final char cY : this.VERTICAL_ARRAY) {
					if (!justCompleted) {
						final String tmpCoord = Coordinates.getCoordinatesXY(cX, cY);
						/*
						 * check, which figure comes first; whenever this condition check returns true,
						 * then add all fields left and finally leave both loops
						 */
						if (tmpCoord.equals(coordKing.getCoordinatesXY()) || tmpCoord.equals(coordRook.getCoordinatesXY())) {
							if (tmpCoord.equals(coordRook.getCoordinatesXY())) {
								rookComesFirst = true;
							}
							if (rookComesFirst) {
								/*
								 * the Rook comes first → append all areas between Rook and King
								 */
								coordHolder = this.collectCoordinates(coordRook, coordKing);
							} else {
								/*
								 * otherwise the King comes first → append all areas between King and Rook
								 */
								coordHolder = this.collectCoordinates(coordKing, coordRook);
							}
							justCompleted = true;
						}
					}
				}
			}
		}
		/*
		 * The list now holds all areas between King and Rook or vice versa. Finally,
		 * it's time to check, if all these areas are not covered by any figure of the
		 * opponent.
		 */
		boolean noMoreChecks = false;
		for (final CoordinationHolder ch : coordHolder) {
			if (noMoreChecks) {
				break;
			}
			if (!ch.isReserved) {
				// check, if no figure by the opponent threats this area
				// final FigureColor opponentColor = FigureColor.getOpponentColor(color);
				for (final FigureSet type : FigureSet.values()) {
					if (noMoreChecks) {
						break;
					}
					if (type != FigureSet.KING) {
						final List<? extends BaseFigure> figureList = FigureHolder.INSTANCE.getListOfAllFiguresBy(type, color);

						for (final BaseFigure bf : figureList) {
							if (!this.onRealFreeArea(bf, ch)) {
								threatenedAreas = true;
								noMoreChecks = true;
								break; // break, because the conditions are no longer satisfied
							}
						}
					} else {
						// for the foes King only
						final figure.FigureKing opponentKing = FigureHolder.INSTANCE.getKing(color);
						if (!this.onRealFreeArea(opponentKing, ch)) {
							threatenedAreas = true;
							noMoreChecks = true;
						}
					}
				}
			} else {
				freeAreas = false;
				break;
			}
		}
		return ((freeAreas) && (!threatenedAreas));
	}

	/**
	 * Collect all areas between King and Rook or vice versa with it's required
	 * properties.
	 *
	 * @param start
	 *            start coordinate
	 * @param end
	 *            destination coordinate
	 *
	 * @return a list with all areas
	 */
	private List<CoordinationHolder> collectCoordinates(final Coordinates start, final Coordinates end) {
		final List<CoordinationHolder> tmp = new ArrayList<>();
		char posX = start.getPosX().charAt(0);
		final char posY = start.getPosY().charAt(0);
		Coordinates freeArea = new Coordinates(++posX, posY);
		while (!freeArea.getCoordinatesXY().equals(end.getCoordinatesXY())) {
			final CoordinationHolder ch = new CoordinationHolder();
			ch.pos_horizon = posX;
			ch.pos_vertical = posY;
			final int naturalPosX = Math.abs(posX - this.posHorizontalDefault);
			final int naturalPosY = Math.abs(posY - this.posVerticalDefault);
			ch.isReserved = this.coordMatrix[naturalPosX][naturalPosY].isReserved;
			tmp.add(ch);
			freeArea = new Coordinates(++posX, posY);
		}
		return tmp;
	}

	/**
	 * Check, if every opponent's figure doesn't threat the given free area.
	 * Whenever any figure is able to threat the given coordinate, then the nested
	 * property `decision` returns false.
	 *
	 * @param figure
	 *            the figure to use
	 * @param ch
	 *            the current coordination holder
	 *
	 * @return the content of `decision`
	 */
	private <T extends BaseFigure> boolean onRealFreeArea(final T figure, final CoordinationHolder ch) {
		boolean decision = true;
		final List<Coordinates> threatenedAreaList = figure.getThreatAreas();
		// check each area which is covered by this figure
		for (final Coordinates c : threatenedAreaList) {
			final Coordinates area = new Coordinates(ch.pos_horizon, ch.pos_vertical);
			/*
			 * if field `area`, which is a part of an area between King and Rook, is covered
			 * by figure T, then there is no chance to do a castling
			 */
			if (area.getCoordinatesXY().equals(c.getCoordinatesXY())) {
				decision = false;
			}
		}
		return decision;
	}

	/**
	 * Whenever a figure has been moved, the matrix class and also any other figure
	 * has now to know where the figure T has been moved to. Therefore it's required
	 * to remove old settings and finally update the figure's properties.
	 *
	 * @param figure
	 *            the figure to use
	 * @param coord
	 *            the new coordination
	 * @param newFigureUsage
	 *            a boolean flag to determine, if the given figure comes as a new
	 *            figure for the game or not, where 'true' means, that this figure
	 *            is a new figure and 'false' means, that this figure still exists
	 *            in the game
	 *
	 * @return a flag to check, if everything was satisfied
	 */
	public <T extends BaseFigure> boolean setNewFigureLocation(final T figure, final Coordinates coord, final boolean newFigureUsage) {
		boolean condition = true;
		if ((figure == null) || (coord == null)) {
			condition = false;
		} else {
			int locHorizon, locVertical;
			if (!newFigureUsage) {
				/*
				 * if the figure still exists in the game, then remove this figure from its old
				 * coordinate and finally, set the figure to its new location by given coord
				 */
				final Coordinates oldLoc = figure.getLocation();
				final char cX = oldLoc.getPosX().charAt(0);
				final char cY = oldLoc.getPosY().charAt(0);
				// remove figure from old matrix coordinate
				locHorizon = Math.abs(cX - this.posHorizontalDefault);
				locVertical = Math.abs(cY - this.posVerticalDefault);
				this.coordMatrix[locHorizon][locVertical].figureChess = null;
				this.coordMatrix[locHorizon][locVertical].figureColor = null;
				this.coordMatrix[locHorizon][locVertical].figureType = null;
				this.coordMatrix[locHorizon][locVertical].isReserved = false;
			}
			figure.updateFigureLocation(coord); // the figure has now the new location
			// set figure T to the new matrix coordinate `coord`
			locHorizon = Math.abs(coord.getPosX().charAt(0) - this.posHorizontalDefault);
			locVertical = Math.abs(coord.getPosY().charAt(0) - this.posVerticalDefault);
			this.coordMatrix[locHorizon][locVertical].figureChess = figure;
			this.coordMatrix[locHorizon][locVertical].figureColor = figure.getFigureColor();
			this.coordMatrix[locHorizon][locVertical].figureType = figure.getFigureType();
			this.coordMatrix[locHorizon][locVertical].isReserved = true;
		}
		return condition;
	}

	/**
	 * Check, if this field is reserved by any figure. It's in use, if a figure may
	 * be hit by a figure from the opponent.
	 *
	 * @param loc
	 *            the coordinate to test
	 *
	 * @return true, if this field is reserved by a figure,<br>
	 *         false, otherwise
	 */
	public boolean onFieldReserved(final Coordinates loc) {
		final int posX = Math.abs(loc.getPosX().charAt(0) - this.posHorizontalDefault);
		final int posY = Math.abs(loc.getPosY().charAt(0) - this.posVerticalDefault);
		return this.coordMatrix[posX][posY].isReserved;
	}

	/**
	 * In cooperation with {@link Matrix#onFieldReserved(Coordinates)} return the
	 * located figure on this field, if possible.
	 *
	 * @param loc
	 *            the coordinate to use
	 *
	 * @return the given figure on this field
	 */
	public BaseFigure getFigureOnField(final Coordinates loc) {
		final int posX = Math.abs(loc.getPosX().charAt(0) - this.posHorizontalDefault);
		final int posY = Math.abs(loc.getPosY().charAt(0) - this.posVerticalDefault);
		return this.coordMatrix[posX][posY].figureChess;
	}
}
