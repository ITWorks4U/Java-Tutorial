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
import figure.FigureRook;
import figure.FigureRunner;
import figure.properties.AreaColor;
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
 *         The tester class for each figure Runner.
 *
 *         <hr>
 *         A figure Runner is a well used figure, where it's able to move on a
 *         specified colored field array to any diagonal way only, depending on
 *         its location. A Runner on a white field is able to move to any other
 *         white field only, thus this Runner is unable to hit any figure on a
 *         black field and vice versa.
 * 
 *         <hr>
 *         Based on the known issue of JUnit5, that the methods doesn't more run
 *         with NAME_ASCENDING, the assertion tests below may fail, thus it's
 *         impossible to guarantee a flawless test run.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class RunnerTester {
	private FigureKing kingWhite = new FigureKing(new Coordinates("E1"), FigureColor.WHITE);
	private FigureKing kingBlack = new FigureKing(new Coordinates("E8"), FigureColor.BLACK);
	private FigureRunner runnerWhite_white = new FigureRunner(new Coordinates("C1"), FigureColor.WHITE, AreaColor.WHITE_AREA);
	private FigureRunner runnerBlack_white = new FigureRunner(new Coordinates("F8"), FigureColor.BLACK, AreaColor.WHITE_AREA);
	private FigurePawn protectedPawnByWhiteRunner = new FigurePawn(FigureColor.WHITE, new Coordinates("B2"));

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		FigureHolder.INSTANCE.addKingToHolder(this.kingWhite);
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.runnerWhite_white);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.runnerBlack_white);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.protectedPawnByWhiteRunner);

		Matrix.INSTANCE.setNewFigureLocation(this.kingWhite, this.kingWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.runnerWhite_white, this.runnerWhite_white.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.runnerBlack_white, this.runnerBlack_white.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.protectedPawnByWhiteRunner, this.protectedPawnByWhiteRunner.getLocation(), true);

		this.kingWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingWhite, this.kingWhite.getLocation()));
		this.kingBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingBlack, this.kingBlack.getLocation()));
		this.runnerWhite_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.runnerWhite_white, this.runnerWhite_white.getLocation()));
		this.runnerBlack_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.runnerBlack_white, this.runnerBlack_white.getLocation()));
		this.protectedPawnByWhiteRunner.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.protectedPawnByWhiteRunner, this.protectedPawnByWhiteRunner.getLocation()));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.kingWhite = null;
		this.kingBlack = null;
		this.runnerBlack_white = null;
		this.runnerWhite_white = null;
		this.protectedPawnByWhiteRunner = null;

		System.gc();
	}

	/**
	 * Test method for {@link figure.FigureRunner#onHitOpponent(figure.BaseFigure)}.
	 * 
	 * Check, if a Runner may be able to hit a figure.
	 */
	@Test
	public void testOnHitOpponent() {
		final Coordinates coord = new Coordinates("C5");
		final FigureRook rookToBeat = new FigureRook(coord, FigureColor.WHITE, TowerIdentification.IMPOSSIBLE_TO_CASTLE);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, rookToBeat);
		Matrix.INSTANCE.setNewFigureLocation(rookToBeat, coord, true);

		final MoveEvent moveRunnerBlack = this.runnerBlack_white.onValidMove(coord);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, moveRunnerBlack);

		/* the white Rook has now been beaten by the Runner */
		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, rookToBeat);
		BaseFigure.resetFigureProperties(this.runnerBlack_white, coord);

		Assertions.assertEquals(coord.getCoordinatesXY(), this.runnerBlack_white.getLocation().getCoordinatesXY());
	}

	/**
	 * Test method for
	 * {@link figure.FigureRunner#updateFigureToProtect(figure.BaseFigure, location.Orientation)}.
	 * 
	 * Check, if a Runner is able to protect any allied figure.
	 */
	@Test
	public void testUpdateFigureToProtect() {
		BaseFigure.resetFigureProperties(this.runnerWhite_white, this.runnerWhite_white.getLocation());
		this.runnerWhite_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.runnerWhite_white, this.runnerWhite_white.getLocation()));

		final List<ProtectedFigure> listOfProtectedFigures = this.runnerWhite_white.getProtectedFigureList();
		Assertions.assertTrue(!(listOfProtectedFigures.isEmpty()));

		for (final ProtectedFigure pf : listOfProtectedFigures) { // there's a single figure only
			Assertions.assertEquals(pf.getProtectedFigure(), this.protectedPawnByWhiteRunner);
		}
	}

	/**
	 * Test method for
	 * {@link figure.FigureRunner#removeFigureToProtect(figure.properties.ProtectedFigure)}.
	 * 
	 * Check, if a Runner is able to notify, that a protected figure is no longer
	 * protected by this Runner.
	 */
	@Test
	public void testRemoveFigureToProtect() {
		final FigureKnight knightWhite = new FigureKnight(new Coordinates("D2"), FigureColor.WHITE);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, knightWhite);
		Matrix.INSTANCE.setNewFigureLocation(knightWhite, knightWhite.getLocation(), true);
		knightWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(knightWhite, knightWhite.getLocation()));

		BaseFigure.resetFigureProperties(this.runnerWhite_white, this.runnerWhite_white.getLocation());
		this.runnerWhite_white.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.runnerWhite_white, this.runnerWhite_white.getLocation()));

		final Coordinates coordKnight = new Coordinates("B3");
		BaseFigure.resetFigureProperties(knightWhite, coordKnight);
		Matrix.INSTANCE.setNewFigureLocation(knightWhite, coordKnight, false);

		final List<ProtectedFigure> listOfProtectedFigures = this.runnerWhite_white.getProtectedFigureList();
		Assertions.assertTrue(!(listOfProtectedFigures.isEmpty()));

		for (final ProtectedFigure pf : listOfProtectedFigures) { // there's a single figure only (expected)
			Assertions.assertNotEquals(pf.getProtectedFigure(), knightWhite); // where the white Knight is not a part of the Runner's list
		}
	}

	/**
	 * Test method for {@link figure.FigureRunner#getAreaColor()}.
	 * 
	 * Check, if the area color if a Runner is equal to it's spawning location, such
	 * that, the area color will never switched.
	 */
	@Test
	public void testGetAreaColor() {
		FigureColor areaColor = this.runnerBlack_white.getAreaColor();
		Assertions.assertEquals(AreaColor.WHITE_AREA, areaColor);

		areaColor = this.runnerWhite_white.getAreaColor();
		Assertions.assertEquals(AreaColor.WHITE_AREA, areaColor);
	}

	/**
	 * Test method for {@link figure.FigureRunner#onValidMove(Coordinates)}.
	 * 
	 * Check, if a Runner is able to move from start to destination. In that case
	 * the Runner wants to move to "E3", which is impossible.
	 */
	@Test
	public void testOnValidMove() {
		final FigureRunner tmpRunner = new FigureRunner(new Coordinates("F1"), FigureColor.WHITE, AreaColor.BLACK_AREA);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, tmpRunner);
		Matrix.INSTANCE.setNewFigureLocation(tmpRunner, tmpRunner.getLocation(), true);
		tmpRunner.updateThreatList(Matrix.INSTANCE.createNewThreatList(tmpRunner, tmpRunner.getLocation()));

		final Coordinates destination = new Coordinates("E3"); // try to move a Runner like a Knight --> shall successfully fail

		final MoveEvent decision = tmpRunner.onValidMove(destination);
		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, decision);
	}
}
