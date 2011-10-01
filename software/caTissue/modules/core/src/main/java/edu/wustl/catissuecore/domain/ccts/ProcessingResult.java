/**
 * 
 */
package edu.wustl.catissuecore.domain.ccts;

/**
 * @see http://community.jboss.org/wiki/Java5EnumUserType
 * @author Denis G. Krylov
 * 
 */
public enum ProcessingResult {
	FAILURE(1), SUCCESS(2);

	private int id;

	private ProcessingResult(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public static ProcessingResult fromInt(int id) {
		for (ProcessingResult status : values()) {
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
