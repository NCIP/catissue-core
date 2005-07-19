/**
 * <p>Title: CheckInCheckOutEventParameter Class</p>
 * <p>Description: A binary event to indicate whether a specimen has been removed from or returned to its recorded storage location. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

/**
 * A binary event to indicate whether a specimen has been removed from or returned to its recorded storage location.
 * @hibernate.joined-subclass table="CATISSUE_CHECKIN_CHECKOUT_EVENT_PARAMETER"
 * @hibernate.joined-subclass-key column="IDENTIFIER" 
 */
public class CheckInCheckOutEventParameter extends SpecimenEventParameters
		implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * Type of the movement e.g. Check-in or Check-out.
	 */
	protected String storageStatus;

	/**
	 * Returns the Type of the movement e.g. Check-in or Check-out.
	 * @hibernate.property name="storageStatus" type="string" column="STORAGE_STATUS" length="100"
	 * not-null="true"
	 * @return storageStatus of the EVENT.
	 */
	public String getStorageStatus()
	{
		return storageStatus;
	}

	/**
	 * Sets the storageStatus of the event.
	 * @param storageStatus storageStatus of the event.
	 */
	public void setStorageStatus(String storageStatus)
	{
		this.storageStatus = storageStatus;
	}
}