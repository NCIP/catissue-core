package edu.wustl.catissuecore.factory;

import java.util.HashSet;
import java.util.LinkedHashSet;

import edu.wustl.catissuecore.domain.ClinicalDiagnosis;
import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.CollectionProtocolEvent;
import edu.wustl.catissuecore.domain.CollectionProtocolRegistration;
import edu.wustl.catissuecore.domain.DistributionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.StudyFormContext;
import edu.wustl.catissuecore.domain.User;


public class CollectionProtocolFactory implements InstanceFactory<CollectionProtocol>
{
	private static CollectionProtocolFactory collectionProtocolFactory;

	private CollectionProtocolFactory()
	{
		super();
	}

	public static synchronized CollectionProtocolFactory getInstance()
	{
		if(collectionProtocolFactory==null)
		{
			collectionProtocolFactory = new CollectionProtocolFactory();
		}
		return collectionProtocolFactory;
	}

	public CollectionProtocol createClone(CollectionProtocol t)
	{
		// TODO Auto-generated method stub
		return null;
	}

	public CollectionProtocol createObject()
	{
		CollectionProtocol cp=new CollectionProtocol();
		initDefaultValues(cp);
		return cp;
	}

	public void initDefaultValues(CollectionProtocol obj)
	{
		obj.setClinicalDiagnosisCollection(new LinkedHashSet<ClinicalDiagnosis>());
		obj.setDistributionProtocolCollection( new LinkedHashSet<DistributionProtocol>());
		obj.setCoordinatorCollection(new LinkedHashSet<User>());
		obj.setCollectionProtocolEventCollection( new LinkedHashSet<CollectionProtocolEvent>());
		obj.setChildCollectionProtocolCollection(new LinkedHashSet<CollectionProtocol>());
		obj.setCollectionProtocolRegistrationCollection(new HashSet<CollectionProtocolRegistration>());
		obj.setAssignedProtocolUserCollection(new HashSet<User>());
		obj.setSiteCollection( new HashSet<Site>());
		obj.setStudyFormContextCollection(new HashSet<StudyFormContext>());
	}

}
