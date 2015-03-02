package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.events.StorageContainerDetail;
import com.krishagni.catissueplus.core.biospecimen.domain.Visit;
import com.krishagni.catissueplus.core.biospecimen.events.VisitDetail;

public class SiteDependencyChecker implements EntityDependencyChecker<Site> {

	@Override
	public Map<String, List> getDependencies(Site site) {
		Map<String, List> depedencies = new HashMap<String, List>();
		
		Set<Visit> visits = site.getVisits();
		if (CollectionUtils.isNotEmpty(visits)) {
			depedencies.put("visits", VisitDetail.from(visits));
		}
		
		Set<StorageContainer> storageContainers = site.getStorageContainers();
		if (CollectionUtils.isNotEmpty(storageContainers)) {
			depedencies.put("storageContainers", StorageContainerDetail.from(storageContainers));
		}
		
		//TODO: Check for site contains any CP or not. 
		
		//TODO: Check for site has distribution order.
		
		//TODO: Check for Site is reffered as MRN Site 
		
		//TODO: Check Site is set as CP Event Default Site
		
		//TODO: Check any user belongs to the site
		
		//TODO: Check any form has site has fancy control and uses this site in the values.
		
		return depedencies;
	}

}
