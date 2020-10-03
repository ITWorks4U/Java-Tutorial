/**
 * @package: gui
 * @project: Chess
 * @author: wunschi
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package gui;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.MediaTracker;
import java.awt.Toolkit;
import java.net.URL;

import javax.swing.JPanel;

/**
 * @author swunsch
 *
 *         This class is loading the background image for the game
 *         "Inchesstigated".
 */
public class Background extends JPanel {
	private static final long serialVersionUID = 1L;

	private Image img;

	public Background(final URL source) {
		if (source != null) {
			final MediaTracker mt = new MediaTracker(this);
			this.img = Toolkit.getDefaultToolkit().getImage(source);

			try {
				mt.waitForAll();
			} catch (final Exception e) {
				System.err.println("Error with MediaTracker: " + mt.getErrorsAny().toString());
				System.exit(1);
			}
		}
	}

	/**
	 * Draw the custom picture on the surface.
	 * 
	 * @param g
	 *            using Graphics-class by "java.awt.Graphics"
	 */
	@Override
	protected void paintComponent(final Graphics g) {
		super.paintComponent(g);
		g.drawImage(this.img, 0, 0, this.getWidth(), this.getHeight(), this);
	}
}
