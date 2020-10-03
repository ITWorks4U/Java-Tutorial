/**
 * @package: runner
 * @project: Chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package runner;

import java.awt.EventQueue;

import event.GameEvent;
import gui.ActionCommand;
import gui.FigureProcedure;
import gui.Inchesstigated;

/**
 * @author swunsch
 *
 *         The runner program for the game Chess. It will hold the main method
 *         only, where all graphical content is located on package "gui".
 */
public class ChessRunner {
	/**
	 * @param args
	 *            given arguments, where no arguments are in use
	 */
	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			@Override
			public void run() {
				try {
					final Inchesstigated game = new Inchesstigated(); // load game board
					final ActionCommand control = new ActionCommand(game); // load action handler
					game.notifyActions(control); // activating action handler

					FigureProcedure.INSTANCE.initializeFiguresOnField(); // initialize interface
					GameEvent.updateGameEvent(GameEvent.NO_RESULT);

					game.setVisible(true);
				} catch (final Exception e) {
					e.printStackTrace();
				}
			}
		});
	}
}
