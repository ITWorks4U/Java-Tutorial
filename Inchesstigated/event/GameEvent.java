/**
 * @package: event
 * @project: Inchesstigated
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package event;

/**
 * @author swunsch
 *
 *         The game event after each turn.
 */
public enum GameEvent {
	/** There is no result yet. */
	NO_RESULT,

	/** The white player has won the game. */
	WHITE_HAS_WON,

	/** The black player has won the game. */
	BLACK_HAS_WON,

	/** A Remis is in use. */
	REMIS;

	/**
	 * Holds the current game event. Required to know, when a game has been
	 * finished.
	 */
	private static GameEvent currentEvent;

	/**
	 * Update the current game event.
	 * 
	 * @param newEvent
	 *            the new game event
	 */
	public static void updateGameEvent(final GameEvent newEvent) {
		GameEvent.currentEvent = newEvent;
	}

	/**
	 * Receiving the current game event.
	 * 
	 * @return the current event
	 */
	public static GameEvent getCurrentGameEvent() {
		return GameEvent.currentEvent;
	}
}
