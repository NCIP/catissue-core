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
public enum ProcessingStatus {
	CANCELLED(1), COMPLETED(2), PENDING(3), REJECTED(4), PROCESSING(5);

	private int id;

	private ProcessingStatus(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public static ProcessingStatus fromInt(int id) {
		for (ProcessingStatus status : values()) {
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
