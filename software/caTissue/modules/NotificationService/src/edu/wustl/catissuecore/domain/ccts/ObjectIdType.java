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
public enum ObjectIdType {
	GRID_ID(1);

	private int id;

	private ObjectIdType(int id) {
		this.id = id;
	}

	public int toInt() {
		return id;
	}

	public ObjectIdType fromInt(int id) {
		for (ObjectIdType status : values()) {
			if (status.id == id) {
				return status;
			}
		}
		return null;
	}

}
