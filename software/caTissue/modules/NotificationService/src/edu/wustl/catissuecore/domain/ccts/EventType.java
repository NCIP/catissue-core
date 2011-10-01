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

	public EventType fromInt(int id) {
		for (EventType status : values()) {
			if (status.id == id) {
				return status;
			}
		}
		return null;
	}

}
