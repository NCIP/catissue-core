
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SiteDao extends Dao<Site> {

	public List<Site> getSites(SiteListCriteria crit);

	public List<Site> getSitesByNames(List<String> siteNames);
	
	public Site getSiteByName(String siteName);

	public Site getSiteByCode(String code);
	
	public List<Object[]> getSiteDependentEntities(Long siteId);
}
