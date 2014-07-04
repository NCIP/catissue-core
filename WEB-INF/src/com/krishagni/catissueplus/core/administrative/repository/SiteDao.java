
package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface SiteDao extends Dao<Site> {

	public Site getSite(Long Id);

	public Site getSite(String name);

	public Boolean isUniqueSiteName(String siteName);

}
