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

import java.util.List;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import event.CastlingEvent;
import event.CastlingEvent.CastlingType;
import event.MoveEvent;
import figure.BaseFigure;
import figure.FigureKing;
import figure.FigureKnight;
import figure.FigureRook;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.Modifier;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;
import location.Coordinates;
import location.Matrix;

/**
 * @author swunsch<br>
 *
 *         The tester class for each figure Rook.
 *
 *         <hr>
 *         A figure Rook is a powerful figure. This figure is able to move to
 *         any field in horizontal or vertical way, unless any figure is between
 *         the starting point and the destination point.<br>
 *         <br>
 *         While the Rook and its King has not been moved before and if all
 *         fields between Rook and King are not blocked by any figure and are
 *         not threatened (covered) by any opponent figure and if the allied
 *         King is also not threatened. If all condition checks are satisfied,
 *         then a castling between Rook and King is possible, depending on the
 *         Rook's location.
 * 
 *         <hr>
 *         Based on the known issue of JUnit5, that the methods doesn't more run
 *         with NAME_ASCENDING, the assertion tests below may fail, thus it's
 *         impossible to guarantee a flawless test run.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
public class RookTester {
	private FigureKing kingWhite = new FigureKing(new Coordinates("E1"), FigureColor.WHITE);
	private FigureKing kingBlack = new FigureKing(new Coordinates("E8"), FigureColor.BLACK);
	private FigureRook rookWhite = new FigureRook(new Coordinates("A1"), FigureColor.WHITE, TowerIdentification.POSSIBLE_TO_CASTLE);
	private FigureRook rookBlack = new FigureRook(new Coordinates("H8"), FigureColor.BLACK, TowerIdentification.POSSIBLE_TO_CASTLE);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		FigureHolder.INSTANCE.addKingToHolder(this.kingWhite);
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.rookWhite);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.rookBlack);

		Matrix.INSTANCE.setNewFigureLocation(this.kingWhite, this.kingWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.rookWhite, this.rookWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.rookBlack, this.rookBlack.getLocation(), true);

		this.kingWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingWhite, this.kingWhite.getLocation()));
		this.kingBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingBlack, this.kingBlack.getLocation()));
		this.rookWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookWhite, this.rookWhite.getLocation()));
		this.rookBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookBlack, this.rookBlack.getLocation()));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.kingWhite = null;
		this.kingBlack = null;
		this.rookWhite = null;
		this.rookBlack = null;
	}

	/**
	 * Test method for
	 * {@link figure.FigureRook#updateFigureToProtect(figure.BaseFigure, location.Orientation)}.
	 * 
	 * Check, if a Rook is able to protect figures.
	 */
	@Test
	public void testUpdateFigureToProtect() {
		final FigureKnight knightWhite = new FigureKnight(new Coordinates("A5"), FigureColor.WHITE);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, knightWhite);
		Matrix.INSTANCE.setNewFigureLocation(knightWhite, knightWhite.getLocation(), true);
		knightWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(knightWhite, knightWhite.getLocation()));

		BaseFigure.resetFigureProperties(this.rookWhite, this.rookWhite.getLocation());
		this.rookWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookWhite, this.rookWhite.getLocation()));

		final List<ProtectedFigure> listOfProtectedFigures = this.rookWhite.getProtectedFigureList();
		Assertions.assertTrue(!(listOfProtectedFigures.isEmpty()));
		Assertions.assertTrue(listOfProtectedFigures.size() == 2);

		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, knightWhite);
		Matrix.INSTANCE.removeFigureFromMatrix(knightWhite);

		BaseFigure.resetFigureProperties(this.rookWhite, this.rookWhite.getLocation());
		this.rookWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookWhite, this.rookWhite.getLocation()));
	}

	/**
	 * Test method for
	 * {@link figure.FigureRook#removeFigureToProtect(figure.properties.ProtectedFigure)}.
	 * 
	 * Check, if a protected figure by this Rook will be removed from it's
	 * protection list, if the protected figure has been moved to an another
	 * location.
	 */
	@Test
	public void testRemoveFigureToProtect() {
		final FigureKnight knightWhite = new FigureKnight(new Coordinates("C1"), FigureColor.WHITE);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, knightWhite);
		Matrix.INSTANCE.setNewFigureLocation(knightWhite, knightWhite.getLocation(), true);
		knightWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(knightWhite, knightWhite.getLocation()));

		BaseFigure.resetFigureProperties(this.rookWhite, this.rookWhite.getLocation());
		this.rookWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookWhite, this.rookWhite.getLocation()));

		final Coordinates coordKnight = new Coordinates("B3");
		BaseFigure.resetFigureProperties(knightWhite, coordKnight);
		Matrix.INSTANCE.setNewFigureLocation(knightWhite, coordKnight, false);

		final List<ProtectedFigure> listOfProtectedFigures = this.rookWhite.getProtectedFigureList();
		Assertions.assertTrue(listOfProtectedFigures.isEmpty());

		// for (final ProtectedFigure pf : listOfProtectedFigures) { // there's a single
		// figure only (expected)
		// Assertions.assertNotEquals(pf.getProtectedFigure(), knightWhite); // where
		// the white Knight is not a part of the Rook's list
		// }

		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, knightWhite);
		Matrix.INSTANCE.removeFigureFromMatrix(knightWhite);

		BaseFigure.resetFigureProperties(this.rookWhite, this.rookWhite.getLocation());
		this.rookWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookWhite, this.rookWhite.getLocation()));
	}

	/**
	 * Test method for {@link figure.FigureRook#onCastling(figure.FigureKing)}.
	 * 
	 * Check, if a Rook is able to do a castling with it's King.
	 */
	@Test
	public void testOnCastling() {
		final boolean castlingCondition = this.rookWhite.onCastling(this.kingWhite);
		Assertions.assertTrue(castlingCondition);

		CastlingEvent.INSTANCE.castlingKingTower(CastlingType.BIG_CASTLING, this.kingWhite, this.rookWhite);
		this.kingWhite.markToImpossibleCastling();
		this.rookWhite.markToImpossibleCastling();

		System.out.println(" new location of King: " + this.kingWhite.getLocation().getCoordinatesXY());
		System.out.println(" new location of Rook: " + this.rookWhite.getLocation().getCoordinatesXY());
	}

	/**
	 * Test method for {@link figure.FigureRook#onAlreadyMoved()}.
	 * 
	 * Test, if a Rook has been moved before.
	 */
	@Test
	public void testOnAlreadyMoved() {
		Coordinates destination = new Coordinates("G7"); // an invalid location for the black Rook
		MoveEvent decision = this.rookBlack.onValidMove(destination);
		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, decision);

		/* do a valid move instead */
		destination = new Coordinates("H2");
		decision = this.rookBlack.onValidMove(destination);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, decision);

		this.rookBlack.markToImpossibleCastling();
		final boolean alreadyMoved = this.rookBlack.onAlreadyMoved();
		Assertions.assertTrue(alreadyMoved);
	}
}
