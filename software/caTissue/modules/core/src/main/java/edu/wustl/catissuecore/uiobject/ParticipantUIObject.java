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
