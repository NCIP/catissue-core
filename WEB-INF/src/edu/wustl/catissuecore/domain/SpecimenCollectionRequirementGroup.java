package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import edu.wustl.common.exception.BizLogicException;

/**
 *@hibernate.class table="CATISSUE_SPECI_COLL_REQ_GROUP"
 *
 * @author abhijit_naik
 *
 */
public class SpecimenCollectionRequirementGroup extends
		AbstractSpecimenCollectionGroup implements Serializable {

	/**
	 * Unique serial version UID.
	 */
	private static final long serialVersionUID = -3467476740948799655L;

	protected CollectionProtocolEvent collectionProtocolEvent;

	
	/**
	 * Returns collection protocol event of the specimen collection requirement group.
	 * @hibernate.one-to-one  name="collectionProtocolEvent"
	 * class="edu.wustl.catissuecore.domain.CollectionProtocolEvent"
	 * propertyref="specimenCollectionRequirementGroup" not-null="false" cascade="save-update"
	 */
	public CollectionProtocolEvent getCollectionProtocolEvent() {
		return collectionProtocolEvent;
	}

	public void setCollectionProtocolEvent(
			CollectionProtocolEvent collectionProtocolEvent) {
		this.collectionProtocolEvent = collectionProtocolEvent;
	}
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup#getGroupName()
	 */
	@Override
	public String getGroupName() {
		return null; //for specimen collection requirement group name is always null.
	}		
	
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractSpecimenCollectionGroup#setGroupName(java.lang.String)
	 */
	@Override
	protected void setGroupName(String name)throws BizLogicException{
		throw new BizLogicException ("Cannot set name to SpecimenCollectionRequirementGroup Object");
	}
	
    public CollectionProtocolRegistration getCollectionProtocolRegistration(){
    	//throw new BizLogicException ("Cannot set name to SpecimenCollectionRequirementGroup Object");
    	return null;
    }
}
