package com.krishagni.catissueplus.core.biospecimen.repository;

import com.krishagni.catissueplus.core.common.repository.Dao;

import edu.wustl.catissuecore.domain.Site;


public interface SiteDao extends Dao<Site>{

	public Site getSite(String name);
	public Site getSite(Long Id);
	
	public com.krishagni.catissueplus.core.biospecimen.domain.Site getSiteByName(String name);
	
}
