package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class SiteDependencyChecker extends AbstractDependencyChecker<Site> {
	
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Map<String, Object>> getDependentEntities(Site site) {
		List<Map<String, Object>> dependencyStat = new ArrayList<Map<String, Object>>();
		
		List<Object[]> stats =  daoFactory.getSiteDao().getSiteDependentEntities(site.getId());
		setDependentEntities(stats, dependencyStat);
		
		//TODO: Check for site contains any CP or not. 	
		
		//TODO: Check for site has distribution order.
			
		//TODO: Check for Site is reffered as MRN Site 
				
		//TODO: Check Site is set as CP Event Default Site
			
		//TODO: Check any user belongs to the site
			
		//TODO: Check any form has site has fancy control and uses this site in the values.
		
		return dependencyStat;
	}

}
