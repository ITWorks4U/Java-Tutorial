/**
 * @package: gui
 * @project: Chess
 * @author: swunsch, ptoepel
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package gui;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;

import event.GameEvent;
import gui.FigureProcedure.MoveState;

/**
 * @author swunsch, ptoepel
 *
 *         This class is going to hold required action commands to interact from
 *         user with the core.
 */
public class ActionCommand implements ActionListener, MouseListener, MouseMotionListener {
	/* properties to use */
	private final Inchesstigated gui; // interface to the GUI

	/** A flag, if the first field button (from GUI) has been pressed. */
	public boolean firstButtonPressed = false;

	/** A flag, if the second field button (from GUI) has been pressed. */
	public boolean secondButtonPressed = false;

	/** A counter for valid pressed buttons. */
	private int nbrOfPressedButtons = 0;

	/** Holds the action command for two buttons. */
	private final String[] buttonActionCommandHolder = new String[2];

	/**
	 * Notify the first pressed button name to avoid to let to press this button
	 * again. If this mark would not exists, then it's possible to move from START
	 * to DESTINATION, where both areas will be equal.
	 */
	private String markForActionButton = "";

	/** Holds the name of the first pressed button. */
	private String firstButtonName = "";

	/** Holds the name of the second pressed button. */
	private String secondButtonName = "";

	/** start with this result */
	private GameEvent currentEvent = GameEvent.NO_RESULT;

	/** start with a valid move state */
	MoveState currentState = MoveState.VALID;

	/**
	 * Will hold the figure ID by name (position 0), figure color (position 1),
	 * start coordinate (position 2), destination coordinate (position 3).
	 */
	private final String figureMoveAction[] = new String[4];

	/* ---------------- no longer in use */
	// private int mousePosX; // position X of mouse
	// private int mousePosY; // position Y of mouse
	// /* definition of borders, where any action should be valid only */
	// private final int upperBoundX = 310;
	// private final int upperBoundY = 30;
	// private final int lowerBoundX = 920;
	// private final int lowerBoundY = 540;
	// private static String labelIDToUse;
	/* -------------- */

	/**
	 * Base constructor.
	 *
	 * @param gui
	 *            the interface to the GUI
	 */
	public ActionCommand(final Inchesstigated gui) {
		this.gui = gui;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	@Override
	public void actionPerformed(final ActionEvent ae) {

		/* while the game is still active */
		if ((this.currentEvent = GameEvent.getCurrentGameEvent()) == GameEvent.NO_RESULT) {

			final String actionName = ae.getActionCommand();
			/* ----- start with each button field ----- */
			for (int i = 0; i < this.gui.NBR_OF_OBJECTS; i++) {
				if (actionName.equals(this.gui.commandHolderForButtons[i])) { // check, which button is pressed
					if (!this.firstButtonPressed && !(actionName.equals(this.markForActionButton))) { // starting position
						final JButton tmpButton01 = (JButton) ae.getSource(); // get the current button
						this.firstButtonPressed = true; // first button has been pressed
						this.nbrOfPressedButtons++; // raise the number of pressed buttons
						this.markForActionButton = actionName; // mark this button to avoid to fire the second part below
						this.firstButtonName = tmpButton01.getName(); // get the button name for further operations
						this.gui.coloringButton(actionName); // fill this button with green color
						this.buttonActionCommandHolder[0] = actionName; // copy actionName
					}

					if (!this.secondButtonPressed && !(actionName.equals(this.markForActionButton))) { // destination position
						final JButton tmpButton02 = (JButton) ae.getSource();
						this.secondButtonPressed = true;
						this.nbrOfPressedButtons++;
						this.markForActionButton = actionName;
						this.secondButtonName = tmpButton02.getName();
						this.gui.coloringButton(actionName);
						this.buttonActionCommandHolder[1] = actionName;
					}
				}
			}

			// called, whenever the last used move was invalid
			if (this.currentState == MoveState.INVALID) {
				this.resetProperties();
			}

			switch (this.nbrOfPressedButtons) {
				case 1: // the first coordinate has been selected
					System.out.println(this.firstButtonName);
					final String namePartsBtnStart[] = this.firstButtonName.split("_");

					if (!(namePartsBtnStart[0].equals(""))) { // should contain the figure name
						this.figureMoveAction[0] = namePartsBtnStart[0]; // holds figure
						this.figureMoveAction[1] = namePartsBtnStart[1]; // holds the figure color
						this.figureMoveAction[2] = namePartsBtnStart[2]; // holds start coordinate
					} else {
						Inchesstigated.infoDialog(10, "Es wurde keine Figur gew\u00E4hlt!");
						this.resetProperties();
					}

					break;

				case 2: // the second coordinate has been selected
					System.out.println(this.secondButtonName);
					final String namePartsBtnDestination[] = this.secondButtonName.split("_");

					final List<String> destination = new ArrayList<>();
					for (final String tmp : namePartsBtnDestination) {
						destination.add(tmp);
					}

					/*
					 * Holds the coordinate, if there's a figure, which may be hit by the current
					 * figure. Otherwise this field is null. Without the condition check below, an
					 * ArrayIndexOutOfBoundsException or any other exception often called.
					 */
					if (destination.size() == 3) {
						// this.figureMoveAction[3] = namePartsBtnDestination[2]; // holds destination
						// coordinate
						this.figureMoveAction[3] = destination.get(2); // destination coordinate
					} else {
						// this.figureMoveAction[3] = namePartsBtnDestination[1]; // holds destination
						// coordinate
						this.figureMoveAction[3] = destination.get(1);
					}

					if (!(this.figureMoveAction[0].isEmpty() && this.figureMoveAction[1].isEmpty() && this.figureMoveAction[2].isEmpty())) {

						/* let the figure move from start to destination */
						FigureProcedure.INSTANCE.moveFigure(this.figureMoveAction[0], this.figureMoveAction[1], this.figureMoveAction[2], this.figureMoveAction[3]);
					}

					/* reset */
					// this.currentState = FigureProcedure.INSTANCE.getCurrentMoveState();
					this.resetProperties();

					break;
			}

			/* ----- buttons outside the chess board only ----- */
			if (actionName.equals("Castling")) {
				this.DoCastling();
			} else if (actionName.equals("Remis")) {
				this.OfferRemis();
			} else if (actionName.equals("Forfeit")) {
				this.ForfeitGame();
			}
		}
	}

	/** Resets properties. */
	private void resetProperties() {
		this.firstButtonPressed = false;
		this.secondButtonPressed = false;
		this.nbrOfPressedButtons = 0;
		this.gui.resetColoring(this.buttonActionCommandHolder[0]);
		this.gui.resetColoring(this.buttonActionCommandHolder[1]);
		this.currentState = MoveState.VALID;
	}

	/*
	 * (non-Javadoc)
	 * @see
	 * java.awt.event.MouseMotionListener#mouseDragged(java.awt.event.MouseEvent)
	 */
	/**
	 * Fires, when the mouse has no longer been dragged.
	 *
	 * @param me
	 *            the mouse event to handle with
	 */
	@Override
	public void mouseDragged(final MouseEvent me) {
		// this.gui.moveLabel(ActionCommand.labelIDToUse, (me.getXOnScreen() -
		// this.mousePosX), (me.getYOnScreen() - this.mousePosY));
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseMotionListener#mouseMoved(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseMoved(final MouseEvent me) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseClicked(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseClicked(final MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseEntered(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseEntered(final MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseExited(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseExited(final MouseEvent e) {
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mousePressed(java.awt.event.MouseEvent)
	 */
	@Override
	public void mousePressed(final MouseEvent me) {
		/* when the mouse has been pressed, then get the current mouse position */
		// this.mousePosX = me.getX();
		// this.mousePosY = me.getY();
	}

	/*
	 * (non-Javadoc)
	 * @see java.awt.event.MouseListener#mouseReleased(java.awt.event.MouseEvent)
	 */
	@Override
	public void mouseReleased(final MouseEvent e) {
	}

	/**
	 * Let the figure move from start to destination by core.
	 *
	 * @param figureID
	 *            the figure to use
	 * @param figureColorAsWord
	 *            the figure color as word
	 * @param start
	 *            start coordinate
	 * @param destination
	 *            destination coordinate
	 *
	 * @deprecated This function below is no longer in use.
	 */
	@Deprecated
	public void moveFigure(final String figureID, final String figureColorAsWord, final String start, final String destination) {
		// System.out.println("FIGURE: " + figureID + ": COLOR: " + figureColorAsWord +
		// "; " + start + " --> " + destination);
		FigureProcedure.INSTANCE.moveFigure(figureID, figureColorAsWord, start, destination); // now let move a figure from start to destination
	}

	/**
	 * Methods for buttons outside the chess board.
	 *
	 * DoCastling: Starts Castling if conditions are fulfilled.
	 *
	 * OfferRemis: Offers your opponent a remis.
	 *
	 * ForfeitGame: Lets you forfeit the game, making your opponent the winner.
	 */
	public void DoCastling() {
		// System.out.println("Button Rochade wurde gedr\u00FCckt.");
		Inchesstigated.infoDialog(1, "");
	}

	public void OfferRemis() {
		// System.out.println("Button Remis wurde gedr\u00FCckt.");
		Inchesstigated.infoDialog(2, "");
	}

	public void ForfeitGame() {
		// System.out.println("Button Aufgeben wurde gedr\u00FCckt.");
		Inchesstigated.infoDialog(0, "");
	}
}
