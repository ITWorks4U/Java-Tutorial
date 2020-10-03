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

import event.GameEvent;
import figure.properties.FigureSet;

/**
 * @author swunsch<br>
 *         <br>
 *
 *         Holds the history of the current move and also the different types of
 *         moves as properties. It's required to print the current move type as
 *         a kind of the chess notations to the game history.
 */
public class MoveHistory {
	/** The used figure. */
	public FigureSet usedFigure;

	/** Start coordinate. */
	public String startCoord;

	/** Destination coordinate. */
	public String destCoord;

	/** Required move type. */
	public MoveType moveType;

	/** Optional. Holds the new figure of the promoted Pawn. */
	public FigureSet promotedFigure;

	/** Optional, for game end only. Holds the information, which player has won. */
	public GameEvent event;

	/**
	 * Nested enumeration for move type. The move types as chess notation in detail:
	 * 
	 * <ul>
	 * <li>NORMAL_MOVE: <b>-</b></li>
	 * <li>NORMAL_MOVE_AND_THREAT: <b>- +</b></li>
	 * <li>NORMAL_BEATEN_MOVE: <b>:</b></li>
	 * <li>NORMAL_BEATEN_MOVE_AND_THREAT: <b>:+</b></li>
	 * <li>EN_PASSANT_BEATEN_MOVE: <b>e. p.</b></li>
	 * <li>EN_PASSANT_BEATEN_MOVE_AND_THREAT: <b>e. p.+</b></li>
	 * <li>THREATENED_MOVE: <b>+</b></li>
	 * <li>SMALL_CASTLING_MOVE: <b>0-0</b></li>
	 * <li>SMALL_CASTLING_MOVE_AND_THREAT: <b>0-0+</b></li>
	 * <li>BIG_CASTLING_MOVE: <b>0-0-0</b></li>
	 * <li>BIG_BASTLING_MOVE_AND_THREAT: <b>0-0-0+</b></li>
	 * <li>PAWN_PROMOTION: <b><i>promoted figure</i></b></li>
	 * <li>PAWN_PROMOTION_AND_THREAT: <b><i>promoted figure</i>+</b></li>
	 * <li>GAME_OVER: <b>++</b></li>
	 * <li>GAME_OVER_BEATEN_MOVE: <b>start : destination ++</b></li>
	 * <li>GAME_OVER_EN_PASSANT: <b>e. p.++</b></li>
	 * <li>GAME_OVER_PAWN_PROMOTION: <b><i>promoted figure</i>++</b></li>
	 * <li>GAME_OVER_SMALL_CASTLING: <b>0-0++</b></li>
	 * <li>GAME_OVER_BIG_CASTLING: <b>0-0-0++</b></li>
	 * </ul>
	 * 
	 * Not included:
	 * <ul>
	 * <li>offered remis: <b>(=)</b></li>
	 * <li>remis accepted: <b>&half;:&half;</b></li>
	 * </ul>
	 * 
	 */
	public static enum MoveType {
		/** A normal move has been done. */
		NORMAL_MOVE,

		/** Like a normal move including the opponents King's threat. */
		NORMAL_MOVE_AND_THREAT,

		/** A normal beaten move has been done. */
		NORMAL_BEATEN_MOVE,

		/** Like a normal beaten move including the opponents King's threat. */
		NORMAL_BEATEN_MOVE_AND_THREAT,

		/**
		 * Pawn only: The figure Pawn has beaten an another Pawn by using "en passant".
		 */
		EN_PASSANT_BEATEN_MOVE,

		/** Like "en passant" including opponents King's threat. */
		EN_PASSANT_BEATEN_MOVE_AND_THREAT,

		/** The King is threatened. */
		THREATENED_MOVE,

		/** A small castling has been done. */
		SMALL_CASTLING_MOVE,

		/** A big castling has been done. */
		BIG_CASTLING_MOVE,

		/** Like a small castling including opponents King's threat. */
		SMALL_CASTLING_MOVE_AND_THREAT,

		/** Like a big castling including opponents King's threat. */
		BIG_BASTLING_MOVE_AND_THREAT,

		/**
		 * Pawn only: The figure Pawn has reached it's last coordinate and has been
		 * promoted to a higher leveled figure.
		 */
		PAWN_PROMOTION,

		/** Like a pawn promotion including opponents King's threat. */
		PAWN_PROMOTION_AND_THREAT,

		/* -------- a game can be ended by: ----------- */
		/** The Kings threat can no more blocked, thus the game is over. */
		GAME_OVER,

		/** Game end called by a normal beaten move. */
		GAME_OVER_BEATEN_MOVE,

		/** Game end called by "en passant". */
		GAME_OVER_EN_PASSANT,

		/** Game end called by pawn promotion. */
		GAME_OVER_PAWN_PROMOTION,

		/** Game end called by small castling. */
		GAME_OVER_SMALL_CASTLING,

		/** Game end called by big castling. */
		GAME_OVER_BIG_CASTLING
	}
}
