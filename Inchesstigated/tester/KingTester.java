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

import event.CastlingEvent;
import event.CastlingEvent.CastlingType;
import event.GameEvent;
import event.MoveEvent;
import figure.BaseFigure;
import figure.FigureKing;
import figure.FigurePawn;
import figure.FigureQueen;
import figure.FigureRook;
import figure.properties.FigureColor;
import figure.properties.FigureHolder;
import figure.properties.Modifier;
import figure.properties.ProtectedFigure;
import figure.properties.TowerIdentification;
import location.Coordinates;
import location.Matrix;
import location.Orientation;

/**
 * @author swunsch<br>
 *
 *         The tester class for each figure King.
 *
 *         <hr>
 *         The figure King is the most important figure of the game chess. A
 *         King may protect up to eight figures and is not able to become
 *         protected by any other allied figure. While the King and one of its
 *         Rooks has not been moved before and if the King is not threatened and
 *         all areas between King and Rook are free and also not covered by any
 *         opponent figure, then a castling of King and Rook, depending on the
 *         Rook's position is able to do once only.
 * 
 *         <hr>
 *         Based on the known issue of JUnit5, that the methods doesn't more run
 *         with NAME_ASCENDING, the assertion tests below may fail, thus it's
 *         impossible to guarantee a flawless test run.
 * 
 * @see https://github.com/junit-team/junit5/issues/13
 */
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class KingTester {

	private FigureKing kingWhite = new FigureKing(new Coordinates("G1"), FigureColor.WHITE);
	private FigureKing kingBlack = new FigureKing(new Coordinates("E8"), FigureColor.BLACK);
	private FigurePawn pawnWhite = new FigurePawn(FigureColor.WHITE, new Coordinates("E2"));
	private FigureQueen queenBlack = new FigureQueen(new Coordinates("D8"), FigureColor.BLACK);
	private FigureRook rookWhite = new FigureRook(new Coordinates("H1"), FigureColor.WHITE, TowerIdentification.POSSIBLE_TO_CASTLE);
	private FigureRook rookBlack = new FigureRook(new Coordinates("E2"), FigureColor.BLACK, TowerIdentification.IMPOSSIBLE_TO_CASTLE);
	private FigureRook rookBlack2 = new FigureRook(new Coordinates("H2"), FigureColor.BLACK, TowerIdentification.POSSIBLE_TO_CASTLE);

	/**
	 * @throws java.lang.Exception
	 */
	@BeforeEach
	public void setUp() throws Exception {
		FigureHolder.INSTANCE.addKingToHolder(this.kingWhite);
		FigureHolder.INSTANCE.addKingToHolder(this.kingBlack);

		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.pawnWhite);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.queenBlack);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.rookWhite);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.rookBlack);
		FigureHolder.INSTANCE.updateFigureList(Modifier.ADD, this.rookBlack2);

		Matrix.INSTANCE.setNewFigureLocation(this.kingWhite, this.kingWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.kingBlack, this.kingBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.pawnWhite, this.pawnWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.queenBlack, this.queenBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.rookWhite, this.rookWhite.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.rookBlack, this.rookBlack.getLocation(), true);
		Matrix.INSTANCE.setNewFigureLocation(this.rookBlack2, this.rookBlack2.getLocation(), true);

		this.kingWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingWhite, this.kingWhite.getLocation()));
		this.kingBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingBlack, this.kingBlack.getLocation()));
		this.pawnWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.pawnWhite, this.pawnWhite.getLocation()));
		this.queenBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.queenBlack, this.queenBlack.getLocation()));
		this.rookWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookWhite, this.rookWhite.getLocation()));
		this.rookBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookBlack, this.rookBlack.getLocation()));
		this.rookBlack2.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookBlack2, this.rookBlack2.getLocation()));
	}

	/**
	 * @throws java.lang.Exception
	 */
	@AfterEach
	public void tearDown() throws Exception {
		this.kingWhite = null;
		this.kingBlack = null;
		this.pawnWhite = null;
		this.queenBlack = null;
		this.rookWhite = null;
		this.rookBlack = null;
		this.rookBlack2 = null;

		System.gc();
	}

	/**
	 * Test method for {@link figure.FigureKing#onValidMove(location.Coordinates)}.
	 * 
	 * Check, if a King may be move to another field.
	 */
	@Test
	public void test_00_OnValidMove() {
		final Coordinates invalidCoordinate = new Coordinates("D1");
		final MoveEvent invalid = this.kingWhite.onValidMove(invalidCoordinate);

		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, invalid);
	}

	/**
	 * Test method for
	 * {@link figure.FigureKing#updateFigureToProtect(figure.BaseFigure, location.Orientation)}.
	 * 
	 * Test, if a King is able to protect a figure.
	 */
	@Test
	public void test_01_UpdateFigureToProtect() {
		this.kingWhite.updateFigureToProtect(this.pawnWhite, Orientation.VERTICAL_UP);

		Assertions.assertTrue(!(this.kingWhite.getProtectedFigureList().isEmpty()));
	}

	/**
	 * Test method for
	 * {@link figure.FigureKing#removeFigureToProtect(figure.properties.ProtectedFigure)}.
	 * 
	 * Test, if a King is able to remove a protected figure from its list.
	 */
	@Test
	public void test_02_RemoveFigureToProtect() {
		Assertions.assertTrue(!(this.kingWhite.getProtectedFigureList().isEmpty()));

		for (int i = 0; i < this.kingWhite.getProtectedFigureList().size(); i++) {
			final ProtectedFigure pf = this.kingWhite.getProtectedFigureList().get(i);
			this.kingWhite.removeFigureToProtect(pf);
		}
	}

	/**
	 * Test method for {@link figure.FigureKing#onHitOpponent(figure.BaseFigure)}.
	 * 
	 * Test, if a King is able to hit any opponent figure.
	 */
	@Test
	public void test_03_OnHitOpponent() {
		final boolean hitCondition = this.kingWhite.onHitOpponent(this.rookBlack);
		Assertions.assertTrue(hitCondition);
	}

	/**
	 * Test method for {@link figure.FigureKing#onCastling(figure.FigureRook)}.
	 * 
	 * Check, if a King is able to do a castling with one of its Rooks.
	 */
	@Test
	public void test_04_OnCastling() {
		final boolean castlingCondition = this.kingWhite.onCastling(this.rookWhite);
		Assertions.assertFalse(castlingCondition);

		CastlingEvent.INSTANCE.castlingKingTower(CastlingType.SMALL_CASTLING, this.kingWhite, this.rookWhite);
		this.kingWhite.markToImpossibleCastling();
		this.rookWhite.markToImpossibleCastling();

		System.out.println(" new location of King: " + this.kingWhite.getLocation().getCoordinatesXY());
		System.out.println(" new location of Rook: " + this.rookWhite.getLocation().getCoordinatesXY());
	}

	/**
	 * Test method for {@link figure.FigureKing#removeThreat()}.
	 * 
	 * Check, if the King is able to remove a threat by own.
	 */
	@Test
	public void test_05_RemoveThreat() {
		final Coordinates hitLocation = new Coordinates("E2");
		boolean hitAble = this.rookBlack.onHitOpponent(this.pawnWhite);
		Assertions.assertTrue(hitAble);

		MoveEvent me = this.rookBlack.onValidMove(hitLocation);
		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, me);

		BaseFigure.resetFigureProperties(this.rookBlack, hitLocation);
		FigureHolder.INSTANCE.updateFigureList(Modifier.REMOVE, this.pawnWhite);
		this.rookBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.rookBlack, hitLocation));

		/* The white King shall now being threatened by the unprotected black Rook. */

		final boolean condition = this.kingWhite.onThreatened();
		Assertions.assertTrue(condition);

		this.kingWhite.addKingsThreat(this.rookBlack);
		this.kingWhite.onHitOpponent(this.rookBlack);
		hitAble = this.kingWhite.onHitOpponent(this.rookBlack);
		Assertions.assertTrue(hitAble);

		me = this.kingWhite.onValidMove(hitLocation);
		Assertions.assertEquals(MoveEvent.SUCCESSFULLY_MOVE, me);

		this.kingWhite.removeThreat();
	}

	/**
	 * Test method for {@link figure.FigureKing#onImpossibleMove()}.<br>
	 * <br>
	 * 
	 * Check, if a King is no longer to move to an escape route after threatening
	 * while also no allied figure is able to block the current threat.<br>
	 * <br>
	 * 
	 * In that case a black Rook is on H2, the white King is now on G1, where the
	 * black Queen is on G2, thus the white King is unable to move to any other
	 * possible escape route and there's no allied figure, which may block this
	 * threat. "Game Over" will appear.
	 */
	@Test
	public void test_06_OnImpossibleMove() {
		final Coordinates rookDestination = new Coordinates("H2");

		final Coordinates kingPos = new Coordinates("G1");
		if (!kingPos.getCoordinatesXY().equals(kingPos.getCoordinatesXY())) {
			BaseFigure.resetFigureProperties(this.kingWhite, kingPos);
			this.kingWhite.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.kingWhite, kingPos));
			this.kingWhite.markToImpossibleCastling();
		}

		final Coordinates queenDestination = new Coordinates("G2");
		BaseFigure.resetFigureProperties(this.queenBlack, queenDestination);
		this.queenBlack.updateThreatList(Matrix.INSTANCE.createNewThreatList(this.queenBlack, queenDestination));

		this.kingWhite.addKingsThreat(this.queenBlack);
		MoveEvent possibleHit = this.kingWhite.onValidMove(rookDestination);
		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, possibleHit);

		possibleHit = this.kingWhite.onValidMove(queenDestination);
		Assertions.assertEquals(MoveEvent.INCORRECT_MOVE, possibleHit);

		final List<Coordinates> escapeRoutes = this.kingWhite.createPossibleEscapeFields();
		Assertions.assertTrue(escapeRoutes.isEmpty());

		System.out.println(" GAME STATUS FOR WHITE: " + GameEvent.BLACK_HAS_WON.toString() + " Game Over.");
	}
}
