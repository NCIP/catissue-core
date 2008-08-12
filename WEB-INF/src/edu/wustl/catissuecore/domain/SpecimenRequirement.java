package edu.wustl.catissuecore.domain;

import java.util.Collection;

public class SpecimenRequirement extends AbstractSpecimen
{
	private static final long serialVersionUID = -34444448799655L;
	
	private String storageType;
	
	private CollectionProtocolEvent collectionProtocolEvent;
	
	private Collection<Specimen> specimenCollection;
	
	private String labelFormat;
	
	
	public CollectionProtocolEvent getCollectionProtocolEvent()
	{
		return collectionProtocolEvent;
	}

	public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
	{
		this.collectionProtocolEvent = collectionProtocolEvent;
	}

	public String getStorageType()
	{
		return storageType;
	}
	
	public void setStorageType(String storageType)
	{
		this.storageType = storageType;
	}

	
	public Collection<Specimen> getSpecimenCollection()
	{
		return specimenCollection;
	}

	
	public void setSpecimenCollection(Collection<Specimen> specimenCollection)
	{
		this.specimenCollection = specimenCollection;
	}

	public String getLabelFormat() {
		return labelFormat;
	}

	public void setLabelFormat(String labelFormat) {
		this.labelFormat = labelFormat;
	}

	
	
}
