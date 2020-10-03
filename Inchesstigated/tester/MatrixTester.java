/**
 * @package: tester
 * @project: Chess
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
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.runners.MethodSorters;

import event.MoveEvent;
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
import figure.properties.MoveSetKnight;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;
import location.Coordinates;
import location.Matrix;

/**
 * @author swunsch
 *
 *         Testing class of singleton class Matrix. This is the most important
 *         class for the game "Inchesstigated", also for any chess game.
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class MatrixTester {
	// the functions below are not working by a name ascending
	// ==> an issue, which comes with JUnit5

	/* some figures to use */
	private final FigureKing king = new FigureKing(new Coordinates("E1"), FigureColor.WHITE);
	private final FigurePawn tmpPawn_white = new FigurePawn(FigureColor.WHITE, new Coordinates("A5"));
	private final FigurePawn tmpPawn_black = new FigurePawn(FigureColor.BLACK, new Coordinates("B7"));
	private final FigureRook tmpRook = new FigureRook(new Coordinates("A1"), FigureColor.WHITE, TowerIdentification.POSSIBLE_TO_CASTLE);
	private final FigureQueen tmpQueen = new FigureQueen(new Coordinates("D8"), FigureColor.BLACK);
	private final FigureKnight tmpKnight = new FigureKnight(new Coordinates("G8"), FigureColor.BLACK);
	private final FigureKing kingBlack = new FigureKing(new Coordinates("E7"), FigureColor.BLACK);
	private final FigureRunner tmpRunner = new FigureRunner(new Coordinates("C3"), FigureColor.WHITE, AreaColor.WHITE_AREA);

	/**
	 * The function below tests all required methods for class Matrix.<br>
	 * Unlike the expected workflow by JUnit 5 including sorted methods by name
	 * ascending,<br>
	 * these methods will be called in their required order manually.
	 * <hr>
	 * <br>
	 * 
	 * expected results: all sub tests are working without any error
	 */
	@Test
	public void testSequences() {
		/* REQUIRED */ this.initFigures();
		/* WORKS */ this.testGetAreaColorOfPawn();
		/* WORKS */ this.testOnFreeAreas();
		/* WORKS */ this.testOnFreeAreasBlocked();
		/* PENDING */ this.testOnPawnNeighbors();
		/* PENDING */ this.testOnLastMatrixCoordinate();
		/* PENDING */ this.testRemoveFigureFromMatrix();
		/* PENDING */ this.testOnValidKnightMove();
		/* PENDING */ this.testUpdateProtectedListBy();
		/* PENDING */ this.testOnFreeCastlingAreas();
	}

	/**
	 * Before testing the methods below it's required to create each figure.<br>
	 * The figure holder class has to add all used figures and the matrix class<br>
	 * needs to know where the figures are placed.
	 * <hr>
	 * <br>
	 * 
	 * expected result: the test runs successfully
	 */
	// @Before
	public void initFigures() {

		// notify, which figure is going to use
		FigureHolder.INSTANCE.addKingToHolder(this.king);
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.tmpPawn_white);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.tmpPawn_black);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.tmpRook);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.tmpQueen);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.tmpKnight);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.tmpRunner);

		// set the figures to the chess field
		Matrix.INSTANCE.setNewFigureLocation(this.king, this.king.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.tmpPawn_white, this.tmpPawn_white.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.tmpPawn_black, this.tmpPawn_black.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.tmpRook, this.tmpRook.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.tmpQueen, this.tmpQueen.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.tmpKnight, this.tmpKnight.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.tmpRunner, this.tmpRunner.getLocation(), true);

		// finally, create threat lists for each figure, where allied figures may be
		// protect, too
		this.king.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.king, this.king.getLocation()));
		this.tmpPawn_black.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpPawn_black, this.tmpPawn_black.getLocation()));
		this.tmpPawn_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpPawn_white, this.tmpPawn_white.getLocation()));
		this.tmpRook.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpRook, this.tmpRook.getLocation()));
		this.tmpQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpQueen, this.tmpQueen.getLocation()));
		this.tmpKnight.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpKnight, this.tmpKnight.getLocation()));
		this.kingBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingBlack, this.kingBlack.getLocation()));
		this.tmpRunner.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpRunner, this.tmpRunner.getLocation()));
	}

	/**
	 * Test method for
	 * {@link location.Matrix#getAreaColorOfPawn(figure.FigurePawn)}.
	 * <hr>
	 * <br>
	 * 
	 * Receive the area color of a figure Pawn. More required, whenever the Pawn has
	 * reached it's last coordinate and it will be promoted to a figure Runner.<br>
	 * A Runner may move on the same area color where it spawns.
	 * <hr>
	 * <br>
	 * 
	 * expected result: the test runs successfully, if the area color of the figure
	 * Pawn is equal to the given chess field
	 */
	// @Test
	public void testGetAreaColorOfPawn() {
		FigureColor areaColor = Matrix.INSTANCE.getAreaColorOfPawn(this.tmpPawn_white);
		Assertions.assertEquals(areaColor, FigureColor.WHITE);

		areaColor = Matrix.INSTANCE.getAreaColorOfPawn(this.tmpPawn_black);
		Assertions.assertEquals(areaColor, FigureColor.BLACK);
	}

	/**
	 * Test method for
	 * {@link location.Matrix#onFreeAreas(figure.BaseFigure, location.Coordinates, location.Coordinates)}.
	 * <hr>
	 * <br>
	 * 
	 * Required, if a Rook, Runner or Queen wants to move from start to
	 * destination,<br>
	 * where each field between start and destination must not be blocked by any
	 * figure.
	 * <hr>
	 * <br>
	 * 
	 * expected result: the boolean condition check returns true, because the Rook
	 * is located on "A1" and wants to move to "A4".
	 */
	// @Test
	public void testOnFreeAreas() {
		final Coordinates coord_S = this.tmpRook.getLocation();
		final Coordinates coord_D = new Coordinates("A4");
		final boolean validMove = Matrix.INSTANCE.onFreeAreas(this.tmpRook, coord_S, coord_D);
		Assertions.assertTrue(validMove);

		// update figure properties
		BaseFigure.resetFigureProperties(this.tmpRook, coord_D);
		this.tmpRook.markToImpossibleCastling();
		this.tmpRook.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpRook, this.tmpRook.getLocation()));
	}

	/**
	 * Test method for
	 * {@link location.Matrix#onFreeAreas(figure.BaseFigure, location.Coordinates, location.Coordinates)}.
	 * <hr>
	 * <br>
	 * 
	 * Like to method {@link MatrixTester#testOnFreeAreas()} this tests,<br>
	 * if a figure (Rook, Runner or Queen only) wants to move to an area, where a
	 * figure is between start and destination.
	 * <hr>
	 * <br>
	 * 
	 * expected result: the boolean condition check returns false, because the Queen
	 * is located on "D8" and wants to move to "H8", where a black Knight has been
	 * spotted on "F8".
	 */
	// @Test
	public void testOnFreeAreasBlocked() {
		final Coordinates coord_S = this.tmpQueen.getLocation();
		final Coordinates coord_D = new Coordinates("H8");
		final boolean invalidMove = Matrix.INSTANCE.onFreeAreas(this.tmpQueen, coord_S, coord_D);

		Assertions.assertFalse(invalidMove);
	}

	/**
	 * Test method for
	 * {@link location.Matrix#onPawnNeighbors(figure.FigurePawn, figure.FigurePawn)}.
	 * <hr>
	 * <br>
	 * 
	 * Test, if two figure Pawns are neighbors. It's required, if a Pawn wants to
	 * beat the other Pawn by "en passant".
	 * <hr>
	 * <br>
	 * 
	 * expected result: the given Pawns are neighbors
	 */
	// @Test
	public void testOnPawnNeighbors() {
		final Coordinates loc = new Coordinates("B5");
		final MoveEvent event = this.tmpPawn_black.onValidMove(loc);
		Assertions.assertEquals(event, MoveEvent.SUCCESSFULLY_MOVE);

		// update the Pawn's location and it's thread list
		BaseFigure.resetFigureProperties(this.tmpPawn_black, loc); // B7 --> B5
		this.tmpPawn_black.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpPawn_black, loc));

		/*
		 * In this case the given two Pawns are neighbors, but the boolean flag will
		 * return false, because this will usually work only, if a Pawn starts from it's
		 * spawn location (white Pawn: between A2 and H2, black Pawn: between A7 and H7)
		 * and want to move two fields, where the opponent Pawn has moved to the given
		 * special field (black Pawn: from A4 to H4, white Pawn: from A5 to H5), thus in
		 * that case the Pawns might be neighbors. If true, then the opponent Pawn may
		 * be hit by the other Pawn by "en passant".
		 */
		final boolean neighbors = Matrix.INSTANCE.onPawnNeighbors(this.tmpPawn_white, this.tmpPawn_black);
		Assertions.assertFalse(neighbors);
	}

	/**
	 * Test method for
	 * {@link location.Matrix#onLastMatrixCoordinate(location.Coordinates, figure.FigurePawn)}.
	 * <hr>
	 * <br>
	 * 
	 * Check, if a figure Pawn is on it's last coordinate to become to promoted to a
	 * higher leveled figure.
	 * <hr>
	 * <br>
	 * 
	 * expected result: no figure Pawn is on it's last coordinate, thus the
	 * assertFalse() function returns true
	 */
	// @Test
	public void testOnLastMatrixCoordinate() {
		boolean lastCoordinateChecker = Matrix.INSTANCE.onLastMatrixCoordinate(this.tmpPawn_white.getLocation(), this.tmpPawn_white);
		Assertions.assertFalse(lastCoordinateChecker);

		lastCoordinateChecker = Matrix.INSTANCE.onLastMatrixCoordinate(this.tmpPawn_black.getLocation(), this.tmpPawn_black);
		Assertions.assertFalse(lastCoordinateChecker);
	}

	/**
	 * Test method for
	 * {@link location.Matrix#removeFigureFromMatrix(figure.BaseFigure)}.
	 * <hr>
	 * <br>
	 * 
	 * Test, if a beaten figure, also possible, if a figure Pawn has been promoted
	 * to a higher leveled figure, is going to remove from the chess field.
	 * <hr>
	 * <br>
	 * 
	 * expected result: this test function runs successfully, where the white Pawn
	 * has been promoted to a figure Runner.
	 */
	// @Test
	public void testRemoveFigureFromMatrix() {
		Coordinates coordPawn = new Coordinates("A7");

		/*
		 * the white Pawn has not been moved before, thus this figure may move two
		 * fields once only
		 */
		MoveEvent me = this.tmpPawn_white.onValidMove(coordPawn); // A5 → A7
		Assertions.assertEquals(me, MoveEvent.SUCCESSFULLY_MOVE);

		BaseFigure.resetFigureProperties(this.tmpPawn_white, coordPawn);
		this.tmpPawn_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpPawn_white, coordPawn));

		coordPawn = new Coordinates("A8");
		me = this.tmpPawn_white.onValidMove(coordPawn); // A7 → A8
		Assertions.assertEquals(me, MoveEvent.SUCCESSFULLY_MOVE);

		BaseFigure.resetFigureProperties(this.tmpPawn_white, coordPawn);
		this.tmpPawn_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpPawn_white, coordPawn));

		/*
		 * the figure Runner is useless, however, we are going to promote the Pawn into
		 * a Runner; the matrix class should notify, that this Pawn does no longer exist
		 */
		final FigureRunner runner = (FigureRunner) this.tmpPawn_white.promotePawn(FigureSet.RUNNER);
		runner.updateThreatList(Matrix.INSTANCE.createNewThreatList(runner, runner.getLocation()));

		/*
		 * check, if the matrix function is able to detect the correct location
		 */
		final boolean removedFigure = Matrix.INSTANCE.setNewFigureLocation(this.tmpPawn_white, coordPawn, false);
		Assertions.assertTrue(removedFigure);
	}

	/**
	 * Test method for
	 * {@link location.Matrix#onValidKnightMove(location.Coordinates, figure.FigureKnight, figure.FigureKnight.MoveSetKnight[][])}.
	 * <hr>
	 * <br>
	 * 
	 * Test, if a figure Knight may move to a given destination coordinate.
	 * <hr>
	 * <br>
	 * 
	 * expected result: the figure Knight has selected a valid coordinate
	 */
	// @Test
	public void testOnValidKnightMove() {
		final Coordinates coordKnight = new Coordinates("H6");
		final boolean flagKnight = Matrix.INSTANCE.onValidKnightMove(coordKnight, this.tmpKnight, MoveSetKnight.getFullKnightMoveList());
		Assertions.assertTrue(flagKnight == true);

		BaseFigure.resetFigureProperties(this.tmpKnight, coordKnight);
		this.tmpKnight.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpKnight, coordKnight));
	}

	/**
	 * Test method for
	 * {@link location.Matrix#updateProtectedListBy(figure.BaseFigure, figure.BaseFigure, java.util.List)}.
	 * <hr>
	 * <br>
	 * 
	 * Test, if a figure after it's move protects n allied figures, where n = 0, 1,
	 * ..., 8, depending on the given figure.
	 * <hr>
	 * <br>
	 * 
	 * expected result: the function runs successfully
	 */
	// @Test
	public void testUpdateProtectedListBy() {
		final Coordinates coordQueen = new Coordinates("H8");
		final MoveEvent moveEventQueen = this.tmpQueen.onValidMove(coordQueen);
		Assertions.assertEquals(moveEventQueen, MoveEvent.SUCCESSFULLY_MOVE);

		BaseFigure.resetFigureProperties(this.tmpQueen, coordQueen);
		this.tmpQueen.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.tmpQueen, coordQueen));

		// test output
		for (final ProtectedFigure pf : this.tmpQueen.getProtectedFigureList()) {
			System.out.println(String.valueOf(pf.getProtectedFigure().getFigureType()) + " | " + pf.getLocationOfprotectdFigure().getCoordinatesXY() + " | " + pf.getOrientation().toString());
		}
	}

	/**
	 * Test method for
	 * {@link location.Matrix#onFreeCastlingAreas(figure.properties.FigureColor, location.Coordinates, location.Coordinates)}.
	 * <hr>
	 * <br>
	 * 
	 * Check, if all fields between a Rook and a King are free to do a casting. A
	 * castling is unable to do, because the Rook has been moved before.
	 * <hr>
	 * <br>
	 * 
	 * expected result: this function runs successfully
	 */
	// @Test
	public void testOnFreeCastlingAreas() {
		/*
		 * The function call: Matrix.INSTANCE.onFreeCastlingAreas() is going to call
		 * automatically by given function call below.
		 */

		/*
		 * onCastling() → CastlingEvent.INSTANCE.onCastlingPossibility() →
		 * Matrix.INSTANCE.onFreeCastlingAreas()
		 */
		final boolean castlingCheck = this.king.onCastling(this.tmpRook);
		Assertions.assertEquals(castlingCheck, false);
	}
}
