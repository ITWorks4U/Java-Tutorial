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

import figure.BaseFigure;
import location.Coordinates;
import location.Orientation;

/**
 * @author swunsch
 *
 *         Holds the figure and it's required properties, like location, color
 *         etc. Necessary to know, if the opponent's King wants to hit this
 *         figure which may be protected by it's allied figure(s).
 */
public final class ProtectedFigure {

	/* properties */
	private BaseFigure figureToProtect;
	private Coordinates location;
	private Orientation orientation;

	/**
	 * Create a new protected figure entry by given figure and it's location.
	 * 
	 * @param figure
	 *            the figure to protect
	 * @param location
	 *            the figure's location
	 */
	public ProtectedFigure(final BaseFigure figure, final Coordinates location) {
		this.figureToProtect = figure;
		this.location = location;
	}

	/**
	 * Create a new protected figure entry by given figure, it's location and it's
	 * orientation.
	 * 
	 * @param figure
	 *            the figure to protect
	 * @param location
	 *            the figure's location
	 * @param orientation
	 *            the figure's orientation
	 */
	public ProtectedFigure(final BaseFigure figure, final Coordinates location, final Orientation orientation) {
		this.figureToProtect = figure;
		this.location = location;
		this.orientation = orientation;
	}

	/**
	 * Remove the protected figure it this has been moved or beaten by any
	 * opponent's figure.
	 */
	public void removeProtectedFigure() {
		this.figureToProtect = null;
		this.location = null;
		this.orientation = null;
	}

	/**
	 * @return the figureToProtect
	 */
	public BaseFigure getProtectedFigure() {
		return this.figureToProtect;
	}

	/**
	 * @return the location
	 */
	public Coordinates getLocationOfprotectdFigure() {
		return this.location;
	}

	/**
	 * @return the orientation
	 */
	public Orientation getOrientation() {
		return this.orientation;
	}
}
