/**
 * 
 */
package edu.wustl.catissuecore.domain.ccts;

/**
 * @see http://community.jboss.org/wiki/Java5EnumUserType
 * @author Denis G. Krylov
 * 
 */
public enum Application {
	C3PR(1), PSC(2);

	private int id;

	private Application(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public static Application fromInt(int id) {
		for (Application status : values()) {
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
