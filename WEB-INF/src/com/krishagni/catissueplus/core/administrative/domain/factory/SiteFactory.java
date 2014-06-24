package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;


public interface SiteFactory {

	public Site createSite(SiteDetails details);

	public Site patchSite(Site site,SiteDetails siteDetails);

}
