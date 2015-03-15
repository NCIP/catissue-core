package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.events.SiteDetail;


public class UserDependencyChecker implements EntityDependencyChecker<User> {

	@Override
	//TODO: Revisit and check other depedencies like cp, dp, AQ
	public Map<String, List> getDependencies(User user) {
		List<SiteDetail> sites = new ArrayList<SiteDetail>();
		for (Site site: user.getSites()) {
			sites.add(SiteDetail.from(site));
		}
		
		Map<String, List> dependencies = new HashMap<String, List>();
		if (!sites.isEmpty()) {
		  dependencies.put("sites", sites);
		}
		
		return dependencies;
	}

}
