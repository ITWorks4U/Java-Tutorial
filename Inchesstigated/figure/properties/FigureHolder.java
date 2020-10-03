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
package figure.properties;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import figure.BaseFigure;
import figure.FigureKing;
import figure.FigureKnight;
import figure.FigurePawn;
import figure.FigureQueen;
import figure.FigureRook;
import figure.FigureRunner;

/**
 * @author swunsch
 *
 *         Holds all figures in separated lists.
 */
public enum FigureHolder {
	INSTANCE;

	/* holds figures and color */
	Map<FigureColor, List<FigurePawn>> listOfPawns = new HashMap<>();
	Map<FigureColor, List<FigureRunner>> listofRunners = new HashMap<>();
	Map<FigureColor, List<FigureRook>> listOfRooks = new HashMap<>();
	Map<FigureColor, List<FigureKnight>> listOfKnights = new HashMap<>();
	Map<FigureColor, List<FigureQueen>> listOfQueens = new HashMap<>();

	FigureKing kingWhite = null;
	FigureKing kingBlack = null;

	/**
	 * Update any list depending on modifier (`ADD` or `REMOVE`), where the given
	 * figure is going to add or remove from it's specified list.
	 * 
	 * @param mod
	 *            given modifier
	 * @param figure
	 *            the figure to use
	 */
	@SuppressWarnings("incomplete-switch")
	public <T extends BaseFigure> void updateFigureList(final Modifier mod, final T figure) {
		final FigureColor figureColor = figure.getFigureColor();
		final FigureSet figureType = figure.getFigureType();

		switch (mod) {
			case ADD:
				switch (figureType) {
					case PAWN:
						final List<FigurePawn> tmpListForPawns = this.getListOfAllPawns(figureColor);
						tmpListForPawns.add((FigurePawn) figure);
						this.listOfPawns.put(figureColor, tmpListForPawns);

						break;
					case KNIGHT:
						final List<FigureKnight> tmpListForKnights = this.getListOfAllKnights(figureColor);
						tmpListForKnights.add((FigureKnight) figure);
						this.listOfKnights.put(figureColor, tmpListForKnights);

						break;
					case RUNNER:
						final List<FigureRunner> tmpListForRunners = this.getListOfAllRunners(figureColor);
						tmpListForRunners.add((FigureRunner) figure);
						this.listofRunners.put(figureColor, tmpListForRunners);

						break;
					case ROOK:
						final List<FigureRook> tmpListForRooks = this.getListOfAllRooks(figureColor);
						tmpListForRooks.add((FigureRook) figure);
						this.listOfRooks.put(figureColor, tmpListForRooks);

						break;
					case QUEEN:
						final List<FigureQueen> tmpListForQueens = this.getListOfAllQueens(figureColor);
						tmpListForQueens.add((FigureQueen) figure);
						this.listOfQueens.put(figureColor, tmpListForQueens);

						break;
				}

				break;
			case REMOVE:
				switch (figureType) {
					case PAWN:
						final List<FigurePawn> tmpListForPawns = this.getListOfAllPawns(figureColor);
						tmpListForPawns.remove(figure);
						this.listOfPawns.put(figureColor, tmpListForPawns);

						break;
					case KNIGHT:
						final List<FigureKnight> tmpListForKnights = this.getListOfAllKnights(figureColor);
						tmpListForKnights.remove(figure);
						this.listOfKnights.put(figureColor, tmpListForKnights);

						break;
					case RUNNER:
						final List<FigureRunner> tmpListForRunners = this.getListOfAllRunners(figureColor);
						tmpListForRunners.remove(figure);
						this.listofRunners.put(figureColor, tmpListForRunners);

						break;
					case ROOK:
						final List<FigureRook> tmpListForRooks = this.getListOfAllRooks(figureColor);
						tmpListForRooks.remove(figure);
						this.listOfRooks.put(figureColor, tmpListForRooks);

						break;
					case QUEEN:
						final List<FigureQueen> tmpListForQueens = this.getListOfAllQueens(figureColor);
						tmpListForQueens.remove(figure);
						this.listOfQueens.put(figureColor, tmpListForQueens);

						break;
				}

				break;
		}
	}

	/**
	 * Add the given king to the holder class.
	 * 
	 * @param king
	 *            the King to add
	 */
	public void addKingToHolder(final FigureKing king) {
		final FigureColor color = king.getFigureColor();
		if (color == FigureColor.WHITE) {
			if (this.kingWhite == null) {
				this.kingWhite = king;
			}
		} else {
			if (this.kingBlack == null) {
				this.kingBlack = king;
			}
		}
	}

	/**
	 * Get the list of all Pawns depending on the given color.
	 * 
	 * @param color
	 *            the color to use
	 * 
	 * @return the list of Pawns with the given color only
	 */
	public List<FigurePawn> getListOfAllPawns(final FigureColor color) {
		final List<FigurePawn> pawnList = new ArrayList<>();

		for (final List<FigurePawn> tmp : this.listOfPawns.values()) {
			for (final FigurePawn tmpPawn : tmp) {
				if (color == tmpPawn.getFigureColor()) {
					pawnList.add(tmpPawn);
				}
			}
		}

		return pawnList;
	}

	/**
	 * Get the list of all Runners depending on the given color.
	 * 
	 * @param color
	 *            the color to use
	 * 
	 * @return the list of Runners with the given color only
	 */
	public List<FigureRunner> getListOfAllRunners(final FigureColor color) {
		final List<FigureRunner> runnerList = new ArrayList<>();

		for (final List<FigureRunner> tmp : this.listofRunners.values()) {
			for (final FigureRunner tmpRunner : tmp) {
				if (color == tmpRunner.getFigureColor()) {
					runnerList.add(tmpRunner);
				}
			}
		}

		return runnerList;
	}

	/**
	 * Get the list of all Knights depending on the given color.
	 * 
	 * @param color
	 *            the color to use
	 * 
	 * @return the list of Knights with the given color only
	 */
	public List<FigureKnight> getListOfAllKnights(final FigureColor color) {
		final List<FigureKnight> knightList = new ArrayList<>();

		for (final List<FigureKnight> tmp : this.listOfKnights.values()) {
			for (final FigureKnight tmpKnight : tmp) {
				if (color == tmpKnight.getFigureColor()) {
					knightList.add(tmpKnight);
				}
			}
		}

		return knightList;
	}

	/**
	 * Get the list of all Rooks depending on the given color.
	 * 
	 * @param color
	 *            the color to use
	 * 
	 * @return the list of Rooks with the given color only
	 */
	public List<FigureRook> getListOfAllRooks(final FigureColor color) {
		final List<FigureRook> rookList = new ArrayList<>();

		for (final List<FigureRook> tmp : this.listOfRooks.values()) {
			for (final FigureRook tmpRook : tmp) {
				if (color == tmpRook.getFigureColor()) {
					rookList.add(tmpRook);
				}
			}
		}

		return rookList;
	}

	/**
	 * Get the list of all Queens depending on the given color.
	 * 
	 * @param color
	 *            the color to use
	 * 
	 * @return the list of Queens with the given color only
	 */
	public List<FigureQueen> getListOfAllQueens(final FigureColor color) {
		final List<FigureQueen> queenList = new ArrayList<>();

		for (final List<FigureQueen> tmp : this.listOfQueens.values()) {
			for (final FigureQueen tmpQueen : tmp) {
				if (color == tmpQueen.getFigureColor()) {
					queenList.add(tmpQueen);
				}
			}
		}

		return queenList;
	}

	/**
	 * Get the required King by given color.
	 * 
	 * @param color
	 *            the color to select
	 * 
	 * @return the required King
	 */
	public FigureKing getKing(final FigureColor color) {
		if (color == FigureColor.BLACK) {
			return this.kingBlack;
		}

		return this.kingWhite;
	}

	/**
	 * Receiving the list with all required figures by given figure type and color.
	 * 
	 * @param type
	 *            the type to select
	 * @param color
	 *            the color to use
	 * 
	 * @return the required list
	 */
	@SuppressWarnings("incomplete-switch")
	public List<? extends BaseFigure> getListOfAllFiguresBy(final FigureSet type, final FigureColor color) {
		List<? extends BaseFigure> list = new ArrayList<>();

		switch (type) {
			case PAWN:
				list = this.getListOfAllPawns(color);
				break;
			case KNIGHT:
				list = this.getListOfAllKnights(color);
				break;
			case RUNNER:
				list = this.getListOfAllRunners(color);
				break;
			case ROOK:
				list = this.getListOfAllRooks(color);
				break;
			case QUEEN:
				list = this.getListOfAllQueens(color);
				break;
		}

		return list;
	}
}
