
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.Collection;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SiteDao extends Dao<Site> {

	public List<Site> getSites(SiteListCriteria crit);

	public Long getSitesCount(SiteListCriteria crit);

	public List<Site> getSitesByNames(Collection<String> siteNames);
	
	public Site getSiteByName(String siteName);

	public Site getSiteByCode(String code);
	
	//
	// At present this is only returning count of CPs by site
	// in future this would be extended to return other stats
	// related to site
	//
	public Map<Long, Integer> getCpCountBySite(Collection<Long> siteIds);


	public Map<String, Object> getSiteIds(String key, Object value);
}
