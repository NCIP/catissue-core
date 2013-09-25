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
public enum Application {
	C3PR(1), PSC(2);

	private int id;

	private Application(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public Application fromInt(int id) {
		for (Application status : values()) {
			if (status.id == id) {
				return status;
			}
		}
		return null;
	}

}
