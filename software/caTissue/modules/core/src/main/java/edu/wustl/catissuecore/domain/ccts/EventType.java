/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

/**
 * 
 */
package edu.wustl.catissuecore.domain.ccts;

/**
 * @see http://community.jboss.org/wiki/Java5EnumUserType
 * @author Denis G. Krylov
 * 
 */
public enum EventType {
	SUBJECT_CREATION(1), STUDY_CREATION(2), SUBJECT_REGISTRATION(3), STUDY_CALENDAR_UPDATE(
			4);

	private int id;

	private EventType(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public static EventType fromInt(int id) {
		for (EventType status : values()) {
			if (status.id == id) {
				return status;
			}
		}
		return null;
	}
	
	/**
	 * Needed a getter method for UI purposes.
	 * @return
	 */
	public String getName() {
		return name();
	}


}
