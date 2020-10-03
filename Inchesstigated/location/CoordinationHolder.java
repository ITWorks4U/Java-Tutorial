/**
 * @package: location
 * @project: Chess
 * @author: swunsch
 *
 * -----------------------------------------------------
 * Hochschule Mittweida - University of Applied Sciences
 * Project "Inchesstigated" WW1 / 2019
 * All rights reserved.
 * -----------------------------------------------------
 */
package location;

import figure.BaseFigure;
import figure.properties.FigureColor;
import figure.properties.FigureSet;

/**
 * @author swunsch
 *
 *         Holds the coordination for each field. There are also properties to
 *         identify a figure on a specified field.
 */
public class CoordinationHolder {
	/* horizontal coordinate from A to H */
	public char pos_horizon;

	/* vertical coordinate from 1 to 8 */
	public char pos_vertical;

	/* the color of the current area */
	public FigureColor areaColor;

	/* for any figure */
	public BaseFigure figureChess;

	/* the color of the figure */
	public FigureColor figureColor;

	/* the type of figure */
	public FigureSet figureType;

	/* a flag to check, if this field is not reserved by any figure */
	public boolean isReserved;
}
