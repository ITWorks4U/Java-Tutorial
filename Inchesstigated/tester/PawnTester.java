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
import figure.FigurePawn;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.Modifier;
import figure.properties.ProtectedFigure;
import location.Coordinates;
import location.Matrix;

/**
 * @author swunsch
 *
 *         The tester class for each figure Pawn.
 * 
 *         <hr>
 *         A Pawn may move one field upwards (white) or downwards (black). If
 *         this Pawn has not been moved before, then it's possible to move two
 *         fields once only. The figure Pawn is able to hit any figure, except
 *         the opponents King, on its diagonal way only. Whenever a Pawn has
 *         reached its last coordinate, then it will be promoted to a higher
 *         leveled figure (Rook, Runner, Queen or Knight).
 * 
 *         <hr>
 *         Based on the known issue of JUnit5, that the methods doesn't more run
 *         with NAME_ASCENDING, the assertion tests below may fail, thus it's
 *         impossible to guarantee a flawless test run.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class PawnTester {

	private FigurePawn pawnWhite_defeat = new FigurePawn(FigureColor.WHITE, new Coordinates("A2"));
	private FigurePawn pawnBlack_promote = new FigurePawn(FigureColor.BLACK, new Coordinates("C7"));
	private FigurePawn pawnBlack_enPassant = new FigurePawn(FigureColor.BLACK, new Coordinates("B6"));
	private FigureKing kingWhite = new FigureKing(new Coordinates("B8"), FigureColor.WHITE);
	private FigureKing kingBlack = new FigureKing(new Coordinates("D7"), FigureColor.BLACK);

	@BeforeEach
	public void initializeFigures() {
		FigureHolder.INSTANCE.addKingToHolder(this.kingWhite);
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.pawnWhite_defeat);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.pawnBlack_promote);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.pawnBlack_enPassant);

		Matrix.INSTANCE.setNewFigureLocation(this.kingWhite, this.kingWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.pawnWhite_defeat, this.pawnWhite_defeat.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.pawnBlack_promote, this.pawnBlack_promote.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.pawnBlack_enPassant, this.pawnBlack_enPassant.getLocation(), true);
	}

	/**
	 * Test method for {@link figure.FigurePawn#onValidMove(location.Coordinates)}.
	 * 
	 * Check, if the given move for a figure Pawn is valid.
	 */
	@Test
	public void test_00_OnValidMove() {
		final Coordinates newLoc = new Coordinates("B4");
		final MoveEvent me = this.pawnWhite_defeat.onValidMove(newLoc);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, me);

		final boolean alliedKingIsNotThreatened = this.kingWhite.onThreatened();
		Assertions.assertFalse(alliedKingIsNotThreatened);

		BaseFigure.resetFigureProperties(this.pawnWhite_defeat, newLoc);
		this.pawnWhite_defeat.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.pawnWhite_defeat, newLoc));
	}

	/**
	 * Test method for {@link figure.FigurePawn#onProtectedAlly()}.
	 * 
	 * Check, if a figure Pawn is protected by an allied figure. More required to
	 * know, if the opponent's King wants to hit a figure Pawn.
	 */
	@Test
	public void test_01_OnProtectedAlly() {
		final boolean protectedBy = this.pawnBlack_promote.onProtectedAlly();
		Assertions.assertEquals(false, protectedBy);
	}

	/**
	 * Test method for {@link figure.FigurePawn#onHitOpponent(figure.BaseFigure)}.
	 * 
	 * Check, if an opponent figure is able to hit by a figure Pawn.
	 */
	@Test
	public void test_02_OnHitOpponent() {
		final boolean condition = this.pawnBlack_enPassant.onHitOpponent(this.pawnWhite_defeat);
		Assertions.assertEquals(true, condition);
	}

	/**
	 * Test method for {@link figure.FigurePawn#getThreatAreas()}.
	 * 
	 * Check, if a figure Pawn covers areas depending on it's location, except on
	 * it's last coordinate. In that case the figure Pawn is going to promote to a
	 * higher leveled figure.
	 */
	@Test
	public void test_03_GetThreatAreas() {
		final List<Coordinates> threatAreas = this.pawnBlack_promote.getThreatAreas();

		for (final Coordinates tmp : threatAreas) {
			System.out.println(" covered area: \"" + tmp.getCoordinatesXY() + "\"");
		}
	}

	/**
	 * Test method for
	 * {@link figure.FigurePawn#updateFigureToProtect(figure.BaseFigure, location.Orientation)}.
	 * 
	 * Test, if a figure Pawn is able to protect any other figure.
	 */
	@Test
	public void test_04_UpdateFigureToProtect() {
		this.pawnBlack_promote.updateFigureToProtect(this.pawnBlack_enPassant, null);

		final List<ProtectedFigure> protectedList = this.pawnBlack_promote.getProtectedFigureList();
		for (final ProtectedFigure pf : protectedList) {
			Assertions.assertNotEquals(this.pawnBlack_promote, pf.getProtectedFigure());
		}
	}

	/**
	 * Test method for
	 * {@link figure.FigurePawn#removeFigureToProtect(figure.properties.ProtectedFigure)}.
	 * 
	 * Test, if a figure Pawn is able to remove a given figure from its protected
	 * figure list, if the list is not empty.
	 */
	@Test
	public void test_05_RemoveFigureToProtect() {
		final List<ProtectedFigure> protectedList = this.pawnBlack_promote.getProtectedFigureList();
		Assertions.assertTrue(protectedList.size() == 0);

		for (int i = 0; i < protectedList.size(); i++) {
			final ProtectedFigure pf = protectedList.get(i);
			this.pawnBlack_promote.removeFigureToProtect(pf);
		}

		Assertions.assertTrue(protectedList.size() == 0);
	}

	/**
	 * Test method for {@link figure.FigurePawn#onEnPassant()}.
	 * 
	 * Check, if a figure Pawn is able to hit an another figure Pawn by "en
	 * passant". It's possible, if and only if the opponent Pawn has moved two
	 * fields, where now both Pawns are neighbors.
	 * 
	 * In that case the first Pawn is now able to hit the opponent Pawn by "en
	 * passant", if also the allied King will not being threatened by any other
	 * opponent figure, which area range was blocked by the first Pawn.
	 */
	@Test
	public void test_06_OnEnPassant() {
		final boolean neighbors = Matrix.INSTANCE.onPawnNeighbors(this.pawnWhite_defeat, this.pawnBlack_enPassant);
		Assertions.assertFalse(neighbors);
	}

	/**
	 * Test method for {@link figure.FigurePawn#onFirstMove()}.
	 */
	@Test
	public void test_07_OnFirstMove() {
		final boolean firstMove = this.pawnBlack_promote.onFirstMove();
		Assertions.assertTrue(firstMove == true);
	}

	@AfterEach
	public void cleanUp() {
		this.kingBlack = null;
		this.kingWhite = null;
		this.pawnWhite_defeat = null;
		this.pawnBlack_promote = null;
		this.pawnBlack_enPassant = null;
	}
}
