package edu.wustl.catissuecore.factory;

import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.SpecimenRequirement;

public class CollectionProtocolEventFactory implements InstanceFactory<CollectionProtocolEvent>
{
	private static CollectionProtocolEventFactory collectionProtocolEventFactory;

	private CollectionProtocolEventFactory()
	{
		super();
	}

	public static synchronized CollectionProtocolEventFactory getInstance()
	{
		if(collectionProtocolEventFactory==null){
			collectionProtocolEventFactory = new CollectionProtocolEventFactory();
		}
		return collectionProtocolEventFactory;
	}

	public CollectionProtocolEvent createClone(CollectionProtocolEvent obj)
	{
		return null;
	}


	public CollectionProtocolEvent createObject()
	{
		CollectionProtocolEvent cpe= new CollectionProtocolEvent();
		initDefaultValues(cpe);
		return cpe;
	}

	public void initDefaultValues(CollectionProtocolEvent obj)
	{
		obj.setSpecimenRequirementCollection(new LinkedHashSet<SpecimenRequirement>());
	}
}
