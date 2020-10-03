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

/**
 * @author swunsch
 *
 *         A set of informations for the King's destination field.
 */
public enum KingDestinationNotifier {
	/*
	 * this field is free, but there is no information if this field may be covered
	 * by any opponent's figure
	 */
	FIELD_IS_FREE,

	/*
	 * this field is blocked by any allied figure, thus the King may not move to
	 * this field
	 */
	FIELD_IS_BLOCKED_BY_ALLY,

	/*
	 * this field is blocked by any figure from the opponent; this figure may be
	 * beaten by this King, but there is no information, if this figure may be
	 * protected by any other opponent's allied figure
	 */
	FIELD_IS_BLOCKED_BY_OPPONENT
}
