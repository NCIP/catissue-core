/*L
 *  Copyright Washington University in St. Louis
 *  Copyright SemanticBits
 *  Copyright Persistent Systems
 *  Copyright Krishagni
 *
 *  Distributed under the OSI-approved BSD 3-Clause License.
 *  See http://ncip.github.com/catissue-core/LICENSE.txt for details.
 */

package edu.wustl.catissuecore.uiobject;

import java.util.Map;

import edu.wustl.common.domain.UIObject;

public class ParticipantUIObject implements UIObject{
	/**
	 * Will store the the collectionProtocolRegistration UI object for each Collection Protocol.
	 */

	protected Map<Long, CollectionProtocolRegistrationUIObject> cprUIObject;

	/**
	 * @return the cprUIObject
	 */
	public Map<Long, CollectionProtocolRegistrationUIObject> getCprUIObject() {
		return cprUIObject;
	}

	/**
	 * @param cprUIObject the cprUIObject to set
	 */
	public void setCprUIObject(Map<Long, CollectionProtocolRegistrationUIObject> cprUIObject) {
		this.cprUIObject = cprUIObject;
	}






}
