
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

	public Double getConcentrationInMicrogramPerMicroliter() {
		return concentrationInMicrogramPerMicroliter;
	}

	public void setConcentrationInMicrogramPerMicroliter(
			Double concentrationInMicrogramPerMicroliter) {
		this.concentrationInMicrogramPerMicroliter = concentrationInMicrogramPerMicroliter;
	}

	/**
	 * labelFormat.
	 */
	private String labelFormat;
	
	private String specimenRequirementLabel;
	


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
}