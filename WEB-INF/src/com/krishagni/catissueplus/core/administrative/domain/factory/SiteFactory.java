
package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.events.SiteDetails;
import com.krishagni.catissueplus.core.administrative.events.SitePatchDetails;

public interface SiteFactory {

	public Site createSite(SiteDetails details);

	public Site patchSite(Site site, SitePatchDetails siteDetails);

}
