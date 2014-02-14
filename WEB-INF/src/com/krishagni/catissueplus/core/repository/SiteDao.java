package com.krishagni.catissueplus.core.repository;

import edu.wustl.catissuecore.domain.Site;


public interface SiteDao extends Dao<Site>{

	public Site getSite(String name);
	public Site getSite(Long Id);
	
}
