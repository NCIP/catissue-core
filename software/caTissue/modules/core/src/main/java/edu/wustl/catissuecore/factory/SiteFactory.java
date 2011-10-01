package edu.wustl.catissuecore.factory;

import java.util.HashSet;

import edu.wustl.catissuecore.domain.CollectionProtocol;
import edu.wustl.catissuecore.domain.Site;
import edu.wustl.catissuecore.domain.User;


public class SiteFactory implements InstanceFactory<Site>
{
	private static SiteFactory siteFactory;

	private SiteFactory() {
		super();
	}

	public static synchronized SiteFactory getInstance()
	{
		if(siteFactory == null) {
			siteFactory = new SiteFactory();
		}
		return siteFactory;
	}
	public Site createClone(Site site)
	{
		Site s=createObject();
		s.setId(Long.valueOf(site.getId().longValue()));
		s.setName(site.getName());
		s.setCtepId(site.getCtepId());
	/*	s.assignedSiteUserCollection = null;
		s.collectionProtocolCollection = null;
		s.abstractSpecimenCollectionGroupCollection = null;
		s.coordinator = null;
		s.address = null;*/
		return s;
	}

	public Site createObject()
	{
		Site site=new Site();
		initDefaultValues(site);
		return site;
	}

	public void initDefaultValues(Site t)
	{
		t.setCollectionProtocolCollection(new HashSet<CollectionProtocol>());
		t.setAssignedSiteUserCollection(new HashSet<User>());

	}

}
