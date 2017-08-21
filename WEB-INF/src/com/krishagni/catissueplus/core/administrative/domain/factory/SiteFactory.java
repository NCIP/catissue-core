
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;

public interface SiteFactory {
	public Site createSite(SiteDetail detail);
	
	public Site createSite(Site existing, SiteDetail detail);
}
