/**
 * @package: tester
 * @project: chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package tester;

import org.junit.FixMethodOrder;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import event.MoveEvent;
import figure.BaseFigure;
import figure.FigureKing;
import figure.FigureQueen;
import figure.FigureRook;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.Modifier;
import figure.properties.TowerIdentification;
import location.Coordinates;
import location.Matrix;

/**
 * @author swunsch<br>
 *
 *         The tester class for each figure Queen.
 *
 *         <hr>
 *         The Queen is the most powerful figure on the chess field. It unites
 *         the abilities of a Rook and a Runner. The Queen is able to move to
 *         any field in horizontal, vertical or diagonal way to its destination
 *         field unless any figure is between its starting point and destination
 *         point.
 * 
 *         <hr>
 *         Based on the known issue of JUnit5, that the methods doesn't more run
 *         with NAME_ASCENDING, the assertion tests below may fail, thus it's
 *         impossible to guarantee a flawless test run.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class QueenTester {
	private FigureKing kingWhite = new FigureKing(new Coordinates("E1"), FigureColor.WHITE);
	private FigureKing kingBlack = new FigureKing(new Coordinates("E8"), FigureColor.BLACK);
	private FigureQueen queenBlack = new FigureQueen(new Coordinates("D8"), FigureColor.BLACK);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);
		FigureHolder.INSTANCE.addKingToHolder(this.kingWhite);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.queenBlack);

		Matrix.INSTANCE.setNewFigureLocation(this.kingWhite, this.kingWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.queenBlack, this.queenBlack.getLocation(), true);

		this.kingWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingWhite, this.kingWhite.getLocation()));
		this.kingBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingBlack, this.kingBlack.getLocation()));
		this.queenBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.queenBlack, this.queenBlack.getLocation()));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.kingWhite = null;
		this.kingBlack = null;
		this.queenBlack = null;

		System.gc();
	}

	/**
	 * Test method for {@link figure.FigureQueen#onHitOpponent(figure.BaseFigure)}.
	 * 
	 * Check, if the Queen is able to hit any figure of the opponent
	 */
	@Test
	public void testOnHitOpponent() {
		final Coordinates coord = new Coordinates("D5");
		final FigureRook rookToBeat = new FigureRook(coord, FigureColor.WHITE, TowerIdentification.IMPOSSIBLE_TO_CASTLE);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, rookToBeat);
		Matrix.INSTANCE.setNewFigureLocation(rookToBeat, coord, true);

		final MoveEvent queenMoveEvent = this.queenBlack.onValidMove(coord);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, queenMoveEvent);

		// /* the white Rook has now been beaten by the Queen */
		// FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, rookToBeat);
		// BaseFigure.resetFigureProperties(this.queenBlack, coord);
		//
		// Assertions.assertEquals(coord.getCoordinatesXY(),
		// this.queenBlack.getLocation().getCoordinatesXY());
	}

	/**
	 * Test method for {@link figure.FigureQueen#onValidMove(Coordinates)}.
	 * 
	 * Check, if the Queen wants to move to a field, which is invalid, thus the
	 * Queen is unable to this destination field.
	 */
	@Test
	public void testOnInvalidMove() {
		final Coordinates invalidLocation = new Coordinates("H5"); // try to move from D8 to H5
		final MoveEvent decision = this.queenBlack.onValidMove(invalidLocation);

		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, decision);
	}

	/**
	 * Test method for {@link figure.FigureQueen#onValidMove(Coordinates)}.
	 * 
	 * Check, if the Queen wants to move to a field, which is invalid, thus the
	 * Queen is unable to this destination field.
	 */
	@Test
	public void testOnValidMove() {
		final Coordinates validLocation = new Coordinates("B6"); // move from D8 to B6
		final MoveEvent decision = this.queenBlack.onValidMove(validLocation);

		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, decision);

		BaseFigure.resetFigureProperties(this.queenBlack, validLocation);
		Matrix.INSTANCE.setNewFigureLocation(this.queenBlack, validLocation, false);
		this.queenBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.queenBlack, this.queenBlack.getLocation()));
	}
}
