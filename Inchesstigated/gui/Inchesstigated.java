/**
 * @package: gui
 * @project: Chess
 * @author: swunsch, ptoepel, dlang2
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package gui;

import java.awt.Color;
import java.awt.Container;
import java.awt.Font;

import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.WindowConstants;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

import event.CastlingEvent.CastlingType;
import event.GameEvent;
import event.MoveWatcherEvent;
import figure.properties.FigureColor;
import figure.properties.FigureSet;
import gui.MoveHistory.MoveType;

/**
 * @author swunsch, ptoepel, dlang2
 *
 *         This class contains the graphical user interface for the WW1 project
 *         "Inchesstigated". It will hold buttons, labels and many more to offer
 *         to interact from outside to the code core.
 */
public class Inchesstigated extends JFrame {

	/* default created serial version user ID */
	private static final long serialVersionUID = 1L;

	/* the JPanel and other container to use */
	private final JPanel contentPane;

	private static JButton Castling;
	private static JButton Remis;
	private static JButton Forfeit;
	private JScrollPane scrollPane;
	private static JTextArea GameHistory;
	private static JLabel LblPawnWhite;
	private static JLabel LblPawnBlack;
	private static JLabel LblRookWhite;
	private static JLabel LblRookBlack;
	private static JLabel LblQueenWhite;
	private static JLabel LblQueenBlack;
	private static JLabel LblKnightWhite;
	private static JLabel LblKnightBlack;
	private static JLabel LblRunnerWhite;
	private static JLabel LblRunnerBlack;
	private static JLabel lblForCurrentRound;

	private static JButton btnsForGame[] = null; // create up to 64 buttons for an "interface"

	/* other properties to use */
	private final static int WINDOW_WIDTH = 1265; // instead 1280: the buttons are almost centered on each field
	private final static int WINDOW_HEIGHT = 730; // instead 720: like WINDOW_HEIGHT
	public final int NBR_OF_OBJECTS = 64; // we have 64 buttons to use
	private static int numberOfRound = 1; // the current number of rounds

	/** Source path for the background image. */
	private final String backgroundImage = "../pictures/inchesstigated_surface.png";

	/**
	 * Holds all required images for the chess buttons.
	 */
	private final ImageIcon iconToUse[] = {
			new ImageIcon(Inchesstigated.class.getResource("/pictures/rook_black_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/knight_black_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/runner_black_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/queen_black_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/king_black_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/pawn_black_small.png")),

			new ImageIcon(Inchesstigated.class.getResource("/pictures/rook_white_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/knight_white_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/runner_white_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/queen_white_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/king_white_small.png")),
			new ImageIcon(Inchesstigated.class.getResource("/pictures/pawn_white_small.png"))
	};

	/**
	 * Holds a set of commands for each button to identify which button has been
	 * pressed.
	 */
	public final String commandHolderForButtons[] = {
			"A8", "B8", "C8", "D8", "E8", "F8", "G8", "H8", /* 0 - 7 */
			"A7", "B7", "C7", "D7", "E7", "F7", "G7", "H7", /* 8 - 15 */
			"A6", "B6", "C6", "D6", "E6", "F6", "G6", "H6", /* 16 - 23 */
			"A5", "B5", "C5", "D5", "E5", "F5", "G5", "H5", /* 24 - 31 */
			"A4", "B4", "C4", "D4", "E4", "F4", "G4", "H4", /* 32 - 39 */
			"A3", "B3", "C3", "D3", "E3", "F3", "G3", "H3", /* 40 - 47 */
			"A2", "B2", "C2", "D2", "E2", "F2", "G2", "H2", /* 48 - 55 */
			"A1", "B1", "C1", "D1", "E1", "F1", "G1", "H1" /* 56 - 63 */
	};

	/**
	 * Holds a set of unique IDs for each used button to identify which button is
	 * now in use, where each name also holds the figure's default spawning location
	 * from {@link Inchesstigated#commandHolderForButtons} above.<br>
	 * <br>
	 *
	 * The last index <b>""</b> is in use, to identify each field left (button),
	 * where no figure is located on that field.
	 */
	public static final String UNIQUE_FIGURE_ID[] = {
			"ROOK", "KNIGHT", "RUNNER", "QUEEN", "KING", "PAWN", ""
	};

	/**
	 * Create the frame.
	 */
	public Inchesstigated() {
		this.setTitle("Inchesstigated - 1.0.0");
		this.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		this.setContentPane(this.getBackgroundImage(this.backgroundImage));
		this.setResizable(false);
		this.setBounds(50, 50, Inchesstigated.WINDOW_WIDTH, Inchesstigated.WINDOW_HEIGHT);
		this.contentPane = new JPanel();
		this.contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		this.contentPane.setLayout(null);

		Inchesstigated.lblForCurrentRound.setText("weiss - " + Inchesstigated.numberOfRound); // start with the first round for white
	}

	/**
	 * Create a JButton for user interaction.
	 *
	 * @param buttonID
	 *            given ID to identify which button to create
	 *
	 * @return created button, or<br>
	 *         null, if buttonID doesn't match with criteria
	 */
	private JButton createJButton(final int buttonID) {
		JButton tmp = null;
		switch (buttonID) {
			case 0: /* castling */
				tmp = new JButton();
				tmp.setBounds(990, 90, 260, 40);
				tmp.setActionCommand("Castling");
				tmp.setIcon(new ImageIcon(this.getClass().getResource("/pictures/Castling.png")));
				tmp.setToolTipText("Eine gro\u00DFe oder kleine Rochade ausf\u00FChren.");
				break;
			case 1: /* offer remis */
				tmp = new JButton();
				tmp.setBounds(990, 135, 260, 40);
				tmp.setActionCommand("Remis");
				tmp.setIcon(new ImageIcon(this.getClass().getResource("/pictures/Remis.png")));
				tmp.setToolTipText("Deinem Gegenspieler ein Remis anbieten.");
				break;
			case 2: /* forteit */
				tmp = new JButton();
				tmp.setBounds(990, 180, 260, 40);
				tmp.setActionCommand("Forfeit");
				tmp.setIcon(new ImageIcon(this.getClass().getResource("/pictures/Forfeit.png")));
				tmp.setToolTipText("Das Spiel aufgeben.");
				break;
		}

		/*
		 * Each button comes with a basic border and offers a visual effect, when
		 * pressed. The other settings below becomes disabled.
		 */
		tmp.setContentAreaFilled(false);
		tmp.setFocusPainted(false);
		tmp.setOpaque(false);

		return tmp;
	}

	/**
	 * Create a JLabel for user interaction.
	 *
	 * @param labelID
	 *            given ID to identify which label to create
	 *
	 * @return created label, or<br>
	 *         null, if labelID doesn't match with criteria
	 */
	private JLabel createJLabel(final int labelID) {
		JLabel tmp = null;
		switch (labelID) {
			case 0: // white pawn image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[11]);
				tmp.setBounds(20, 90, 70, 70);
				break;
			case 1: // white rook image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[6]);
				tmp.setBounds(150, 90, 70, 70);
				break;
			case 2: // white queen image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[9]);
				tmp.setBounds(20, 280, 70, 70);
				break;
			case 3: // white knight image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[7]);
				tmp.setBounds(20, 190, 70, 70);
				break;
			case 4: // white runner image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[8]);
				tmp.setBounds(150, 190, 70, 70);
				break;
			case 5: // black pawn image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[5]);
				tmp.setBounds(20, 430, 70, 70);
				break;
			case 6: // black rook image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[0]);
				tmp.setBounds(150, 430, 70, 70);
				break;
			case 7: // black queen image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[3]);
				tmp.setBounds(20, 620, 70, 70);
				break;
			case 8: // black knight image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[1]);
				tmp.setBounds(20, 535, 70, 70);
				break;
			case 9: // black runner image
				tmp = new JLabel();
				tmp.setIcon(this.iconToUse[2]);
				tmp.setBounds(150, 535, 70, 70);
				break;
		}

		tmp.setForeground(Color.WHITE); // setting up the text color to white
		return tmp;
	}

	/**
	 * Loading and returning the background image by given source path.
	 *
	 * @param source
	 *            the located source path to use to.
	 *
	 * @return the loaded image to the window
	 */
	private Container getBackgroundImage(final String source) {
		final Background bg = new Background(this.getClass().getResource(source));
		bg.setLayout(null);

		Inchesstigated.Castling = this.createJButton(0); // create button for castling
		bg.add(Inchesstigated.Castling);

		Inchesstigated.Remis = this.createJButton(1); // create button for remis
		bg.add(Inchesstigated.Remis);

		Inchesstigated.Forfeit = this.createJButton(2); // create button for forfeit
		bg.add(Inchesstigated.Forfeit);

		Inchesstigated.GameHistory = new JTextArea(); // JTextArea in combination with JScrollPane
		Inchesstigated.GameHistory.setWrapStyleWord(true);
		Inchesstigated.GameHistory.setEditable(false);
		Inchesstigated.GameHistory.setFont(new Font("Arial", Font.PLAIN, 18));
		Inchesstigated.GameHistory.setForeground(Color.WHITE);
		Inchesstigated.GameHistory.setOpaque(false);
		Inchesstigated.GameHistory.setBorder(new LineBorder(Color.BLACK, 2));

		this.scrollPane = new JScrollPane(); // JScrollPane for game history
		this.scrollPane.setBounds(1000, 263, 240, 400);
		this.scrollPane.setOpaque(false);

		this.scrollPane.setViewportView(Inchesstigated.GameHistory);
		this.scrollPane.getViewport().setOpaque(false);

		bg.add(this.scrollPane);

		Inchesstigated.LblPawnWhite = this.createJLabel(0); // label for white beaten pawn
		bg.add(Inchesstigated.LblPawnWhite);

		Inchesstigated.LblRookWhite = this.createJLabel(1); // label for white rook
		bg.add(Inchesstigated.LblRookWhite);

		Inchesstigated.LblQueenWhite = this.createJLabel(2); // label for white queen
		bg.add(Inchesstigated.LblQueenWhite);

		Inchesstigated.LblKnightWhite = this.createJLabel(3); // label for white knight
		bg.add(Inchesstigated.LblKnightWhite);

		Inchesstigated.LblRunnerWhite = this.createJLabel(4); // label for white runner
		bg.add(Inchesstigated.LblRunnerWhite);

		Inchesstigated.LblPawnBlack = this.createJLabel(5); // label for black pawn
		bg.add(Inchesstigated.LblPawnBlack);

		Inchesstigated.LblRookBlack = this.createJLabel(6); // label for black rook
		bg.add(Inchesstigated.LblRookBlack);

		Inchesstigated.LblQueenBlack = this.createJLabel(7); // label for black queen
		bg.add(Inchesstigated.LblQueenBlack);

		Inchesstigated.LblKnightBlack = this.createJLabel(8); // label for black knight
		bg.add(Inchesstigated.LblKnightBlack);

		Inchesstigated.LblRunnerBlack = this.createJLabel(9); // label for black runner
		bg.add(Inchesstigated.LblRunnerBlack);
		bg.add(Inchesstigated.getLabelForCurrentRound());

		this.createButtonsForGame();
		for (int i = 0; i < this.NBR_OF_OBJECTS; i++) {
			bg.add(Inchesstigated.btnsForGame[i]);
		}

		return bg;
	}

	/**
	 * Create all required 64 buttons for the game, where each button has an aspect
	 * ratio of 70x70 and it's neighbor button is located on the current button's
	 * position x + 80 and also on position y + 80.<br>
	 * <br>
	 *
	 * The first button is on position {318,33} and the last button is on position
	 * {878, 593}.
	 */
	private void createButtonsForGame() {
		final int btnDimensions = 70;
		final int nextButtonPosition = 80;
		int startPosX = 238;
		int startPosY = 33;
		Inchesstigated.btnsForGame = new JButton[this.NBR_OF_OBJECTS];

		for (int i = 0; i < this.NBR_OF_OBJECTS; i++) {
			Inchesstigated.btnsForGame[i] = new JButton();
			final StringBuffer sb = new StringBuffer();
			if ((i > 0) && ((i % 8) == 0)) { // after each eighth button: go to the next line and continue the process
				startPosY += nextButtonPosition;
				startPosX = 318;
			} else {
				startPosX += nextButtonPosition;
			}

			Inchesstigated.btnsForGame[i].setBounds(startPosX, startPosY, btnDimensions, btnDimensions);

			/* make each button invisible, but still available to the window */
			Inchesstigated.btnsForGame[i].setBorder(null);
			Inchesstigated.btnsForGame[i].setBorderPainted(false);
			Inchesstigated.btnsForGame[i].setContentAreaFilled(false);
			Inchesstigated.btnsForGame[i].setFocusPainted(false);
			Inchesstigated.btnsForGame[i].setOpaque(false);
			Inchesstigated.btnsForGame[i].setActionCommand(this.commandHolderForButtons[i]);

			/* determine which button will hold which name, which figure */
			switch (i) {
				case 0: /* black Rooks */
				case 7:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[0] + "_" + FigureColor.BLACK.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[0]);
					break;
				case 56: /* white Rooks */
				case 63:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[0] + "_" + FigureColor.WHITE.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[6]);
					break;
				case 1: /* black Knights */
				case 6:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[1] + "_" + FigureColor.BLACK.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[1]);
					break;
				case 57: /* white Knights */
				case 62:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[1] + "_" + FigureColor.WHITE.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[7]);
					break;
				case 2: /* black Runners */
				case 5:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[2] + "_" + FigureColor.BLACK.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[2]);
					break;
				case 58: /* white Runners */
				case 61:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[2] + "_" + FigureColor.WHITE.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[8]);
					break;
				case 3: /* black Queen */
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[3] + "_" + FigureColor.BLACK.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[3]);
					break;
				case 59: /* white Queen */
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[3] + "_" + FigureColor.WHITE.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[9]);
					break;
				case 4: /* black King */
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[4] + "_" + FigureColor.BLACK.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[4]);
					break;
				case 60: /* white King */
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[4] + "_" + FigureColor.WHITE.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[10]);
					break;
				case 8: /* black Pawns */
				case 9:
				case 10:
				case 11:
				case 12:
				case 13:
				case 14:
				case 15:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[5] + "_" + FigureColor.BLACK.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[5]);
					break;
				case 48: /* white Pawns */
				case 49:
				case 50:
				case 51:
				case 52:
				case 53:
				case 54:
				case 55:
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[5] + "_" + FigureColor.WHITE.toString() + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
					Inchesstigated.btnsForGame[i].setIcon(this.iconToUse[11]);
					break;
				default: /* no figure on this field */
					sb.append(Inchesstigated.UNIQUE_FIGURE_ID[6] + "_" + this.commandHolderForButtons[i]);
					Inchesstigated.btnsForGame[i].setName(sb.toString());
			}

			// System.out.println(this.btnsForGame[i].getName());
		}
	}

	/**
	 * Notification to add all required container action events to a listener.
	 *
	 * @param command
	 *            a pointer to {@link ActionCommand}
	 */
	public void notifyActions(final ActionCommand command) {
		for (int i = 0; i < this.NBR_OF_OBJECTS; i++) {
			Inchesstigated.btnsForGame[i].addActionListener(command);
		}

		Inchesstigated.Castling.addActionListener(command);
		Inchesstigated.Remis.addActionListener(command);
		Inchesstigated.Forfeit.addActionListener(command);
	}

	/**
	 * Whenever a button has been pressed, then this button will be filled with a
	 * green color to see that this button was in use.
	 *
	 * @param buttonID
	 *            the button ID to determine which button is in use
	 */
	public void coloringButton(final String buttonID) {
		for (int i = 0; i < this.NBR_OF_OBJECTS; i++) {
			if (Inchesstigated.btnsForGame[i].getActionCommand().equals(buttonID)) {
				Inchesstigated.btnsForGame[i].setBackground(Color.GREEN);
				Inchesstigated.btnsForGame[i].setContentAreaFilled(true);
			}
		}
	}

	/**
	 * After each move the colored buttons needs to reset.
	 *
	 * @param buttonID
	 *            the button ID to determine which button is in use
	 */
	public void resetColoring(final String buttonID) {
		for (final JButton tmp : Inchesstigated.btnsForGame) {
			if (tmp.getActionCommand().equals(buttonID)) {
				tmp.setBackground(null);
				tmp.setContentAreaFilled(false);
			}
		}
	}

	/**
	 * Whenever a figure has been removed from the field, then the required JLabel
	 * needs an update.
	 *
	 * @param orderID
	 *            the ID which JLabel is in use
	 */
	public static void updateJLabelOnScreen(final int orderID) {
		final StringBuilder sb = new StringBuilder();
		String numberHolderAsWord = "";
		int numberHolder = -1;
		try {
			switch (orderID) {
				case 0: // white Pawn
					if (Inchesstigated.LblPawnWhite.getText().equals("")) { // if no text current exists
						Inchesstigated.LblPawnWhite.setText("x 1"); // then update the text
					} else {
						sb.insert(0, Inchesstigated.LblPawnWhite.getText()); // receive the text content
						numberHolderAsWord = sb.toString().substring(2); // just get the current number of beaten Pawns as a word
						numberHolder = Integer.parseInt(numberHolderAsWord); // receive the number of beaten Pawns
						numberHolder++; // raise the number by 1
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0)); // update text --> the number has now been changed
						Inchesstigated.LblPawnWhite.setText(sb.toString()); // finally, update the text for this label
					}
					break;
				case 1: // white Knight
					if (Inchesstigated.LblKnightWhite.getText().equals("")) {
						Inchesstigated.LblKnightWhite.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblKnightWhite.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblKnightWhite.setText(sb.toString());
					}
					break;
				case 2: // white Runner
					if (Inchesstigated.LblRunnerWhite.getText().equals("")) {
						Inchesstigated.LblRunnerWhite.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblRunnerWhite.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblRunnerWhite.setText(sb.toString());
					}
					break;
				case 3: // white Rook
					if (Inchesstigated.LblRookWhite.getText().equals("")) {
						Inchesstigated.LblRookWhite.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblRookWhite.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblRookWhite.setText(sb.toString());
					}
					break;
				case 4: // white Queen
					if (Inchesstigated.LblQueenWhite.getText().equals("")) {
						Inchesstigated.LblQueenWhite.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblQueenWhite.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblQueenWhite.setText(sb.toString());
					}
					break;
				case 5: // black Pawn
					if (Inchesstigated.LblPawnBlack.getText().equals("")) {
						Inchesstigated.LblPawnBlack.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblPawnBlack.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblPawnBlack.setText(sb.toString());
					}
					break;
				case 6: // black Knight
					if (Inchesstigated.LblKnightBlack.getText().equals("")) {
						Inchesstigated.LblKnightBlack.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblKnightBlack.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblKnightBlack.setText(sb.toString());
					}
					break;
				case 7: // black Runner
					if (Inchesstigated.LblRunnerBlack.getText().equals("")) {
						Inchesstigated.LblRunnerBlack.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblRunnerBlack.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblRunnerBlack.setText(sb.toString());
					}
					break;
				case 8: // black Rook
					if (Inchesstigated.LblRookBlack.getText().equals("")) {
						Inchesstigated.LblRookBlack.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblRookBlack.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblRookBlack.setText(sb.toString());
					}
					break;
				case 9: // black Queen
					if (Inchesstigated.LblQueenBlack.getText().equals("")) {
						Inchesstigated.LblQueenBlack.setText("x 1");
					} else {
						sb.insert(0, Inchesstigated.LblQueenBlack.getText());
						numberHolderAsWord = sb.toString().substring(2);
						numberHolder = Integer.parseInt(numberHolderAsWord);
						numberHolder++;
						sb.setCharAt(2, String.valueOf(numberHolder).charAt(0));
						Inchesstigated.LblQueenBlack.setText(sb.toString());
					}
					break;
			}
		} catch (final NumberFormatException nfe) {
			nfe.printStackTrace();
		} catch (final Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * For each valid move from start to destination update the image of the
	 * selected figure to show the "move" from start to destination.
	 *
	 * The buttons needs also an update of their name by given parameters to avoid
	 * to call exceptions.
	 *
	 * @param figureID
	 *            the figureID to use
	 * @param figureColorAsWord
	 *            the figure color in words
	 * @param startCoordAsWord
	 *            start coordinate
	 * @param destinationCoordAsWord
	 *            destination coordinate
	 */
	public static void updateFieldImage(final String figureID, final String figureColorAsWord, final String startCoordAsWord, final String destinationCoordAsWord) {
		Icon backup = null;

		for (final JButton tmp : Inchesstigated.btnsForGame) {
			final String tmpName = tmp.getName();

			if (tmpName.endsWith(startCoordAsWord)) { // found start button
				backup = tmp.getIcon(); // backing up the image
				tmp.setIcon(null); // remove image from button
				tmp.setName("_" + startCoordAsWord); // update button name -> holds now "_START_COORD"
				break;
			}
		}

		for (final JButton tmp : Inchesstigated.btnsForGame) { // found destination button
			final String tmpName = tmp.getName();

			if (tmpName.endsWith(destinationCoordAsWord)) {
				// set icon to destination field
				tmp.setIcon(backup);

				// update button name -> holds now "FIGURE_COLOR_DEST_COORD"
				tmp.setName(figureID + "_" + figureColorAsWord + "_" + destinationCoordAsWord);
				break;
			}
		}
	}

	/**
	 * Update the move history by given informations.
	 *
	 * @param history
	 *            holds all required properties
	 *
	 * @see MoveHistory
	 */
	public static void updateMoveHistory(final MoveHistory history) {
		final FigureColor lastUsedColor = MoveWatcherEvent.getLastFigureColor();
		if (lastUsedColor == FigureColor.WHITE) {

			/* black may now move a figure */
			Inchesstigated.lblForCurrentRound.setText("schwarz - " + Inchesstigated.numberOfRound);

		} else {

			/* otherwise it's the white player's turn */
			Inchesstigated.numberOfRound++; // after each black turn, raise the number of rounds
			Inchesstigated.lblForCurrentRound.setText("wei\u00DF - " + Inchesstigated.numberOfRound);
		}

		final StringBuffer sb = new StringBuffer();

		/*
		 * --------------------------------------------------------------------------
		 * Determine, which move type is in use, where BIG_CASTLING and SMALL_CASTLING
		 * and also it's sub castling types have a higher priority to being printed on
		 * screen than the other move types.
		 * --------------------------------------------------------------------------
		 */
		if (history.moveType == MoveType.BIG_CASTLING_MOVE) {
			sb.append("0-0-0");
		} else if (history.moveType == MoveType.BIG_BASTLING_MOVE_AND_THREAT) {
			sb.append("0-0-0+");
		} else if (history.moveType == MoveType.GAME_OVER_BIG_CASTLING) {
			sb.append("0-0-0++");
		} else if (history.moveType == MoveType.SMALL_CASTLING_MOVE) {
			sb.append("0-0");
		} else if (history.moveType == MoveType.SMALL_CASTLING_MOVE_AND_THREAT) {
			sb.append("0-0+");
		} else if (history.moveType == MoveType.GAME_OVER_SMALL_CASTLING) {
			sb.append("0-0++");
		} else {
			if (history.usedFigure == FigureSet.KING) {
				sb.append("K");
			} else if (history.usedFigure == FigureSet.QUEEN) {
				sb.append("D");
			} else if (history.usedFigure == FigureSet.ROOK) {
				sb.append("T");
			} else if (history.usedFigure == FigureSet.RUNNER) {
				sb.append("L");
			} else if (history.usedFigure == FigureSet.KNIGHT) {
				sb.append("S");
			}

			/* the Pawn doesn't have a notification --> it's empty in that case */

			/*
			 * --------------------------------------------------------------------------
			 * handle "en passant", "normal beaten move", "normal move" only
			 * --------------------------------------------------------------------------
			 */
			switch (history.moveType) {
				case EN_PASSANT_BEATEN_MOVE: // Pawn only
					sb.append(history.startCoord.toLowerCase() + ":" + history.destCoord.toLowerCase() + " e.p.");
					break;
				case NORMAL_BEATEN_MOVE:
					sb.append(history.startCoord.toLowerCase() + ":" + history.destCoord.toLowerCase());
					break;
				case NORMAL_MOVE:
					sb.append(history.startCoord.toLowerCase() + "-" + history.destCoord.toLowerCase());
					break;
				default:
					/* any move type left, except castling */
			}

			/*
			 * --------------------------------------------------------------------------
			 * pawn promotion only
			 * --------------------------------------------------------------------------
			 */
			if (history.moveType == MoveType.PAWN_PROMOTION) {
				if (history.promotedFigure == FigureSet.QUEEN) {
					sb.append("D");
				} else if (history.promotedFigure == FigureSet.ROOK) {
					sb.append("T");
				} else if (history.promotedFigure == FigureSet.RUNNER) {
					sb.append("L");
				} else if (history.promotedFigure == FigureSet.KNIGHT) {
					sb.append("S");
				}
			}

			/*
			 * --------------------------------------------------------------------------
			 * only in use, if the Kings is threatened or the threat can no longer blocked
			 * --------------------------------------------------------------------------
			 */
			if (history.moveType == MoveType.THREATENED_MOVE) {
				sb.append("+");
			} else if (history.moveType == MoveType.GAME_OVER) {
				sb.append("++");

				if (history.event == GameEvent.BLACK_HAS_WON) {
					sb.append("\n0:1");
				} else if (history.event == GameEvent.WHITE_HAS_WON) {
					sb.append("\n1:0");
				}
			}
		}

		// finally, update text to history
		sb.append("\n");
		Inchesstigated.GameHistory.append(sb.toString());
	}

	/**
	 * Only in use, if a player offers a remis, where the other player hasn't
	 * accepted/declined yet. In that case the game history shows <b>(=)</b>.
	 */
	public static void printRemisOfferOnHistory() {
		Inchesstigated.GameHistory.append("(=) \n");
	}

	/**
	 * Only in use, if an offered remis was accepted, where the unicode
	 * <b>"U+00BD"</b> prints <b>&half;</b>.<br>
	 * In that case the game history shows <b>&half;:&half;</b>
	 */
	public static void printAcceptedRemisOnHistory() {
		Inchesstigated.GameHistory.append("\u00BD:\u00BD \n");
	}

	/**
	 * Only in use, if the used Pawn is on it's last coordinate, thus an info
	 * dialogue pops up to let the user to decide, which kind of promotion is now in
	 * use.
	 *
	 * @return the promoted figure in words,<br>
	 *         or null, if the option dialogue has been closed
	 */
	public static String showPawnPromotion() {
		final String promotable[] = { "KNIGHT", "QUEEN", "ROOK", "RUNNER" };

		final int selected = JOptionPane.showOptionDialog(null, "Bef\u00F6rderung in...?", "Bauernbef\u00F6rderung", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, promotable, promotable[0]);
		if (selected != -1) { // equal to JOptionPane.CLOSED_OPTION;
			return promotable[selected]; // receive the selected figure
		}

		return null;
	}

	/**
	 * Shows an info dialogue depending on given dialogue ID.
	 *
	 * @param dialogid
	 *            ID to determine the message dialogue
	 * @param msg
	 *            optional: holds a given message for info dialogue
	 */
	public static void infoDialog(final int dialogid, final String msg) {
		int confirmNumber;
		final StringBuffer sb = new StringBuffer(); // holds a text by msg
		final FigureColor colorOfOpponent = MoveWatcherEvent.getLastFigureColor(); // get the last used color, if possible

		switch (dialogid) {
			/* --- holds questions and offers user decisions --- */

			case 0: // case when player presses forfeit button
				confirmNumber = JOptionPane.showConfirmDialog(null, "M\u00f6chtest du wirklich aufgeben?", "Aufgabe", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (confirmNumber == 0) {

					if (colorOfOpponent == null) {

						/* only valid, if the button has pressed in the first round */

						GameEvent.updateGameEvent(GameEvent.BLACK_HAS_WON);
						Inchesstigated.GameHistory.append("0:1");
					} else if (colorOfOpponent == FigureColor.WHITE) {

						/* we're now the black player and forfeits the game */
						GameEvent.updateGameEvent(GameEvent.WHITE_HAS_WON);
						Inchesstigated.GameHistory.append("1:0");
					} else {

						/*
						 * similar to the first condition check, but the color exists; white forfeits
						 * the game
						 */
						GameEvent.updateGameEvent(GameEvent.BLACK_HAS_WON);
						Inchesstigated.GameHistory.append("0:1");
					}
				}
				break;

			case 1: // offer a castling
				final String castlingOption[] = { "Kleine Rochade", "Gro\u00DFe Rochade" };
				final int selected = JOptionPane.showOptionDialog(null, "Auswahl der Rochade:", "Auswahl der Rochade", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE, null, castlingOption, castlingOption[0]);
				if (selected != JOptionPane.CLOSED_OPTION) { // equal to -1
					final String castlingType = castlingOption[selected];
					CastlingType option = null;

					if (castlingType.equals(castlingOption[0])) {
						option = CastlingType.SMALL_CASTLING;
					} else {
						option = CastlingType.BIG_CASTLING;
					}

					FigureProcedure.INSTANCE.collectCastlingDecision(option);
				}

				break;

			case 2: // case when remis is offered
				Inchesstigated.printRemisOfferOnHistory();

				confirmNumber = JOptionPane.showConfirmDialog(null, "Nimmst du das vorgeschlagene Remis an?", "Ein Remis wurde angeboten.", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
				if (confirmNumber == 0) {
					Inchesstigated.printAcceptedRemisOnHistory();
					GameEvent.updateGameEvent(GameEvent.REMIS);
				}

				break;

			case 3: // case when player is in check
				JOptionPane.showMessageDialog(null, "Du stehst im Schach! Bewege dich aus dem Schach, sonst hast du verloren!", "\"Schach!\"", JOptionPane.INFORMATION_MESSAGE);
				break;

			case 4: // case when player is check-mate
				JOptionPane.showMessageDialog(null, "Du bist Schachmatt und hast verloren!", "\"Schachmatt!\"", JOptionPane.INFORMATION_MESSAGE);

				if (colorOfOpponent == FigureColor.WHITE) {
					GameEvent.updateGameEvent(GameEvent.WHITE_HAS_WON); // in that case the white player has won this game
				} else { // otherwise the black player has won this game
					GameEvent.updateGameEvent(GameEvent.BLACK_HAS_WON);
				}

				Inchesstigated.disableAllButtons();

				break;

			/* --- holds error messages --- */

			case 10: // case when player makes an invalid move

				sb.append("Ung\u00FCltiger Zug!\n");
				sb.append(msg);

				JOptionPane.showMessageDialog(null, sb.toString(), "Fehler", JOptionPane.INFORMATION_MESSAGE);
				break;
			case 11: // castling is not possible
				sb.append("Es kann keine Rochade durchgef\\u00fchrt werden:" + "\n");
				sb.append(msg);

				JOptionPane.showMessageDialog(null, sb.toString(), "Fehler Rochade", JOptionPane.ERROR_MESSAGE);
				break;
		}
	}

	/** In use only, if the game is over. */
	public static void disableAllButtons() {
		for (final JButton tmp : Inchesstigated.btnsForGame) {
			tmp.setEnabled(false);
		}

		Inchesstigated.Castling.setEnabled(false);
		Inchesstigated.Forfeit.setEnabled(false);
		Inchesstigated.Remis.setEnabled(false);
	}

	/**
	 * Create a new JLabel for the current round.
	 * 
	 * @return the created JLabel
	 */
	private static JLabel getLabelForCurrentRound() {
		if (Inchesstigated.lblForCurrentRound == null) {
			Inchesstigated.lblForCurrentRound = new JLabel();
			Inchesstigated.lblForCurrentRound.setFont(new Font("Dialog", Font.BOLD, 18));
			Inchesstigated.lblForCurrentRound.setForeground(Color.WHITE);
			Inchesstigated.lblForCurrentRound.setBounds(1044, 49, 145, 15);
		}

		return Inchesstigated.lblForCurrentRound;
	}
}
