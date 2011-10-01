/**
 * 
 */
package edu.wustl.catissuecore.domain.ccts;

/**
 * @see http://community.jboss.org/wiki/Java5EnumUserType
 * @author Denis G. Krylov
 * 
 */
public enum ProcessingStatus {
	CANCELLED(1), COMPLETED(2), PENDING(3), REJECTED(4);

	private int id;

	private ProcessingStatus(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public ProcessingStatus fromInt(int id) {
		for (ProcessingStatus status : values()) {
			if (status.id == id) {
				return status;
			}
		}
		return null;
	}

}
