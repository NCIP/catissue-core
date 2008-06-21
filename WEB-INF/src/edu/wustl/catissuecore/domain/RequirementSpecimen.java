package edu.wustl.catissuecore.domain;

import java.util.Collection;

public class RequirementSpecimen extends AbstractSpecimen
{
	private static final long serialVersionUID = -34444448799655L;
	
	private String storageType;
	
	private CollectionProtocolEvent collectionProtocolEvent;
	
	private Collection<Specimen> specimenCollection;
	
	
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
	
}
