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
import figure.FigureKnight;
import figure.FigurePawn;
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
 *         The tester class for each figure Knight.
 *
 *         <hr>
 *         A Knight is able to jump over to other figures. This figure may
 *         protect up to eight figures. Whenever a Knight has been moved the
 *         area color depending on starting point and destination point is
 *         changing from white to black and vice versa.
 * 
 *         <hr>
 *         Based on the known issue of JUnit5, that the methods doesn't more run
 *         with NAME_ASCENDING, the assertion tests below may fail, thus it's
 *         impossible to guarantee a flawless test run.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KnightTester {

	private FigureKing kingWhite = new FigureKing(new Coordinates("E1"), FigureColor.WHITE);
	private FigureKing kingBlack = new FigureKing(new Coordinates("E8"), FigureColor.BLACK);
	private FigureKnight knightBlack = new FigureKnight(new Coordinates("B8"), FigureColor.BLACK);
	private FigureKnight knightWhite = new FigureKnight(new Coordinates("G1"), FigureColor.WHITE);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		FigureHolder.INSTANCE.addKingToHolder(this.kingWhite);
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.knightBlack);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.knightWhite);

		Matrix.INSTANCE.setNewFigureLocation(this.kingWhite, this.kingWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.knightBlack, this.knightBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.knightWhite, this.knightWhite.getLocation(), true);

		this.kingWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingWhite, this.kingWhite.getLocation()));
		this.kingBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingBlack, this.kingBlack.getLocation()));
		this.knightBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.knightBlack, this.knightBlack.getLocation()));
		this.knightWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.knightWhite, this.knightWhite.getLocation()));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.kingWhite = null;
		this.kingBlack = null;
		this.knightBlack = null;
		this.knightWhite = null;

		System.gc();
	}

	/**
	 * Test method for
	 * {@link figure.FigureKnight#onValidMove(location.Coordinates)}.
	 */
	@Test
	public void test_00_OnValidMove() {
		final Coordinates locWhiteKnight = new Coordinates("E2"); // should be a valid move
		final MoveEvent moveKnightWhite = this.knightWhite.onValidMove(locWhiteKnight);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, moveKnightWhite);

		final Coordinates locBlackKnight = new Coordinates("C5"); // should be an invalid move
		final MoveEvent moveKnightBlack = this.knightBlack.onValidMove(locBlackKnight);
		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, moveKnightBlack);
	}

	/**
	 * Test method for {@link figure.FigureKnight#onHitOpponent(figure.BaseFigure)}.
	 * 
	 * Test, if a Knight is able to hit a figure.
	 */
	@Test
	public void test_01_OnHitOpponent() {
		final Coordinates coord = new Coordinates("D7");
		final FigurePawn pawnToBeat = new FigurePawn(FigureColor.WHITE, coord);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, pawnToBeat);
		Matrix.INSTANCE.setNewFigureLocation(pawnToBeat, coord, true);
		pawnToBeat.updateThreatList(Matrix.INSTANCE.createNewThreatList(pawnToBeat, pawnToBeat.getLocation()));

		final MoveEvent moveKnightBlack = this.knightBlack.onValidMove(coord);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, moveKnightBlack);

		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, pawnToBeat);
		BaseFigure.resetFigureProperties(this.knightBlack, coord);
		Matrix.INSTANCE.setNewFigureLocation(this.knightBlack, coord, false);
		this.knightBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.knightBlack, this.knightBlack.getLocation()));

		Assertions.assertEquals(this.knightBlack.getLocation().getCoordinatesXY(), coord.getCoordinatesXY());
	}

	/**
	 * Test method for
	 * {@link figure.FigureKnight#updateFigureToProtect(figure.BaseFigure, location.Orientation)}.
	 * 
	 * Test, if a Knight is able to protect a figure.
	 */
	@Test
	public void test_02_UpdateFigureToProtect() {
		final FigureKnight knightTmp = new FigureKnight(new Coordinates("B1"), FigureColor.WHITE); // a new created Knight to test
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, knightTmp);
		Matrix.INSTANCE.setNewFigureLocation(knightTmp, knightTmp.getLocation(), true);
		knightTmp.updateThreatList(Matrix.INSTANCE.createNewThreatList(knightTmp, knightTmp.getLocation()));

		final FigureQueen tmpQueen = new FigureQueen(new Coordinates("A3"), FigureColor.WHITE);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpQueen);
		Matrix.INSTANCE.setNewFigureLocation(tmpQueen, tmpQueen.getLocation(), true);
		tmpQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpQueen, tmpQueen.getLocation()));

		final int nbrOfProtectedFigures = knightTmp.getProtectedFigureList().size();
		Assertions.assertEquals(0, nbrOfProtectedFigures);

		// final ProtectedFigure pf = knightTmp.getProtectedFigureList().get(0);
		// Assertions.assertEquals(pf.getProtectedFigure(), tmpQueen);

		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, tmpQueen);
		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, knightTmp);
	}

	/**
	 * Test method for
	 * {@link figure.FigureKnight#removeFigureToProtect(figure.properties.ProtectedFigure)}.
	 * 
	 * Test, if a protected figure of a Knight will be vanished, if this figure has
	 * been moved or beaten.
	 */
	@Test
	public void test_03_RemoveFigureToProtect() {
		final FigureKnight knightTmp = new FigureKnight(new Coordinates("G8"), FigureColor.BLACK); // a new created Knight to test
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, knightTmp);
		Matrix.INSTANCE.setNewFigureLocation(knightTmp, knightTmp.getLocation(), true);
		knightTmp.updateThreatList(Matrix.INSTANCE.createNewThreatList(knightTmp, knightTmp.getLocation()));

		final FigureQueen tmpQueen = new FigureQueen(new Coordinates("H6"), FigureColor.BLACK);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpQueen);
		Matrix.INSTANCE.setNewFigureLocation(tmpQueen, tmpQueen.getLocation(), true);
		tmpQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpQueen, tmpQueen.getLocation()));

		int nbrOfProtectedFigures = knightTmp.getProtectedFigureList().size(); // this Knight shall protect the Queen
		Assertions.assertEquals(0, nbrOfProtectedFigures);

		final Coordinates coordQueen = new Coordinates("H5"); // move the Queen from H6 to H5
		final MoveEvent moveQueen = tmpQueen.onValidMove(coordQueen);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, moveQueen);

		BaseFigure.resetFigureProperties(tmpQueen, coordQueen);
		Matrix.INSTANCE.setNewFigureLocation(tmpQueen, tmpQueen.getLocation(), true);
		tmpQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpQueen, tmpQueen.getLocation()));

		nbrOfProtectedFigures = knightTmp.getProtectedFigureList().size(); // this Knight shall not have any protected figure
		Assertions.assertEquals(0, nbrOfProtectedFigures);

		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, tmpQueen);
		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, knightTmp);
	}

	/**
	 * Test method for {@link figure.FigureKnight#onAbleToHit()}.
	 * 
	 * Test, if a Knight is able to hit a figure. It's more in use, if the allied
	 * King may be threatened after this move. If true, then this move is invalid.
	 * 
	 * The white King is on position E1, where a white Knight is on E2. The white
	 * Knight is still able to beat a figure, like a Queen on F4, but this is
	 * impossible, because a Rook, located on E3, will threat the King, thus the
	 * white Knight is unable to move anywhere.
	 * 
	 * FIXME: The method {@link BaseFigure#onAbleToMove(Coordinates)}, which is
	 * derived to every figure class, is not implemented, thus this test below is
	 * incomplete.
	 */
	@Test
	public void test_04_OnAbleToHit() {
		final FigureKnight knightTmp = new FigureKnight(new Coordinates("E2"), FigureColor.WHITE); // a new created Knight to test
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, knightTmp);
		Matrix.INSTANCE.setNewFigureLocation(knightTmp, knightTmp.getLocation(), true);
		knightTmp.updateThreatList(Matrix.INSTANCE.createNewThreatList(knightTmp, knightTmp.getLocation()));

		final Coordinates coordQueen = new Coordinates("F4");
		final FigureQueen tmpQueen = new FigureQueen(coordQueen, FigureColor.BLACK); // the Queen to beat, which is unable
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpQueen);
		Matrix.INSTANCE.setNewFigureLocation(tmpQueen, tmpQueen.getLocation(), true);
		tmpQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpQueen, tmpQueen.getLocation()));

		final FigureRook tmpRook = new FigureRook(new Coordinates("E3"), FigureColor.BLACK, TowerIdentification.IMPOSSIBLE_TO_CASTLE);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpRook);
		Matrix.INSTANCE.setNewFigureLocation(tmpRook, tmpRook.getLocation(), true);
		tmpRook.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpRook, tmpRook.getLocation()));

		// final boolean ableToMove = knightTmp.onAbleToMove(coordQueen); // try to move
		// the Knight from E2 to F4
		// Assertions.assertFalse(ableToMove); // TODO: complete condition in method

		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, tmpQueen);
		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, knightTmp);
		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, tmpRook);

	}
}
