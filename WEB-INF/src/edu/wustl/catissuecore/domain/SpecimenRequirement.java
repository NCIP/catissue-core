
package edu.wustl.catissuecore.domain;

import java.util.Collection;

/**
 * SpecimenRequirement class.
 */
public class SpecimenRequirement extends AbstractSpecimen
{

	/**
	 * Serial Version ID.
	 */
	private static final long serialVersionUID = -34444448799655L;

	private Double concentrationInMicrogramPerMicroliter;
	/**
	 * storageType.
	 */
	private String storageType;

	/**
	 * collectionProtocolEvent.
	 */
	private CollectionProtocolEvent collectionProtocolEvent;

	/**
	 * specimenCollection.
	 */
	private Collection<Specimen> specimenCollection;

	/**
	 * labelFormat.
	 */
	private String labelFormat;
	
	private String specimenRequirementLabel;
	
	/**
	 * Defines the status of Specimen Requirement.
	 */
	private String activityStatus;
	

	public String getSpecimenRequirementLabel() {
		return specimenRequirementLabel;
	}

	public void setSpecimenRequirementLabel(String title) {
		this.specimenRequirementLabel = title;
	}

	/**
	 * Get CollectionProtocolEvent.
	 * @return CollectionProtocolEvent.
	 */
	public CollectionProtocolEvent getCollectionProtocolEvent()
	{
		return this.collectionProtocolEvent;
	}

	/**
	 * @param collectionProtocolEvent CollectionProtocolEvent.
	 */
	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	/**
	 * Get StorageType.
	 * @return String.
	 */
	public String getStorageType()
	{
		return this.storageType;
	}

	/**
	 * Set StorageType.
	 * @param storageType String.
	 */
	public void setStorageType(String storageType)
	{
		this.storageType = storageType;
	}

	/**
	 * Get SpecimenCollection.
	 * @return Collection of Specimen type.
	 */
	public Collection<Specimen> getSpecimenCollection()
	{
		return this.specimenCollection;
	}

	/**
	 * Set SpecimenCollection.
	 * @param specimenCollection of Collection containing specimen.
	 */
	public void setSpecimenCollection(Collection<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	/**
	 * Get LabelFormat.
	 * @return String.
	 */
	public String getLabelFormat()
	{
		return this.labelFormat;
	}

	/**
	 * Set LabelFormat.
	 * @param labelFormat of String type.
	 */
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}
	
	/**
	 * Returns whether this specimen requirement is active or disabled.
	 * @hibernate.property name="activityStatus" type="string" column="ACTIVITY_STATUS" length="50"
	 * @see #setActivityStatus(String)
	 */
	@Override
	public String getActivityStatus()
	{
		return this.activityStatus;
	}

	/**
	 * Sets activity status of the specimen requirement.
	 * @param activityStatus "Active" if this specimen requirement is not deleted else disabled.
	 * @see #getActivityStatus()
	 */
	@Override
	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

}