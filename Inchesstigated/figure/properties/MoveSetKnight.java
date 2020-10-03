/**
 * @package: figure.properties
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
import java.util.List;

/**
 * @author swunsch
 *
 *         Holds all possible moves for each figure Knight.
 */
public enum MoveSetKnight {
	/** Valid moves for a Knight. */
	HORIZONTAL_RIGHT, HORIZONTAL_LEFT, VERTICAL_UP, VERTICAL_DOWN;

	/** A list for further usage. */
	private static List<MoveSetKnight[]> LIST_OF_MOVES;

	/** Create a set of all valid Knight moves. */
	private static final MoveSetKnight[][] SET_OF_KNIGHT_MOVES = {
			/* move to right two times and then one field vertical up */
			{ HORIZONTAL_RIGHT, HORIZONTAL_RIGHT, VERTICAL_UP },
			{ VERTICAL_UP, HORIZONTAL_RIGHT, HORIZONTAL_RIGHT },

			/* move to left two times and then one field vertical up */
			{ HORIZONTAL_LEFT, HORIZONTAL_LEFT, VERTICAL_UP },
			{ VERTICAL_UP, HORIZONTAL_LEFT, HORIZONTAL_LEFT },

			/* move to right two times and then one field vertical down */
			{ HORIZONTAL_RIGHT, HORIZONTAL_RIGHT, VERTICAL_DOWN },
			{ VERTICAL_DOWN, HORIZONTAL_RIGHT, HORIZONTAL_RIGHT },

			/* move to left two times and then one field vertical down */
			{ HORIZONTAL_LEFT, HORIZONTAL_LEFT, VERTICAL_DOWN },
			{ VERTICAL_DOWN, HORIZONTAL_LEFT, HORIZONTAL_LEFT },

			/* move upwards two times and then one field right */
			{ VERTICAL_UP, VERTICAL_UP, HORIZONTAL_RIGHT },
			{ HORIZONTAL_RIGHT, VERTICAL_UP, VERTICAL_UP },

			/* move upwards two times and then one field left */
			{ VERTICAL_UP, VERTICAL_UP, HORIZONTAL_LEFT },
			{ HORIZONTAL_LEFT, VERTICAL_UP, VERTICAL_UP },

			/* move downwards two times and then one field right */
			{ VERTICAL_DOWN, VERTICAL_DOWN, HORIZONTAL_RIGHT },
			{ HORIZONTAL_RIGHT, VERTICAL_DOWN, VERTICAL_DOWN },

			/* move downwards two times and then one field left */
			{ VERTICAL_DOWN, VERTICAL_DOWN, HORIZONTAL_LEFT },
			{ HORIZONTAL_LEFT, VERTICAL_DOWN, VERTICAL_DOWN }
	};

	/* Move all moves for a figure Knight of type of a triple-tuple to a list. */
	static {
		MoveSetKnight.LIST_OF_MOVES = new ArrayList<>();

		for (final MoveSetKnight[] subMove : MoveSetKnight.SET_OF_KNIGHT_MOVES) {
			MoveSetKnight.LIST_OF_MOVES.add(subMove);
		}
	}

	/**
	 * Receiving the full move list for a Knight.
	 * 
	 * @return a valid move list for a Knight
	 */
	public static List<MoveSetKnight[]> getFullKnightMoveList() {
		return MoveSetKnight.LIST_OF_MOVES;
	}
}
