package com.krishagni.catissueplus.core.administrative.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.ObjectUtils;
import org.hibernate.proxy.HibernateProxyHelper;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class DPDistributionSite extends BaseEntity {
	private DistributionProtocol distributionProtocol;
	
	private Institute institute;
	
	private Site site;
	
	public DistributionProtocol getDistributionProtocol() {
		return distributionProtocol;
	}
	
	public void setDistributionProtocol(DistributionProtocol distributionProtocol) {
		this.distributionProtocol = distributionProtocol;
	}
	
	public Institute getInstitute() {
		return institute;
	}
	
	public void setInstitute(Institute institute) {
		this.institute = institute;
	}
	
	public Site getSite() {
		return site;
	}
	
	public void setSite(Site site) {
		this.site = site;
	}
	
	@Override
	public int hashCode() {
		int prime = 31;
		return prime + getDistributionProtocol().hashCode() + getInstitute().hashCode() +
				(getSite() == null ? 0 : getSite().hashCode());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		
		if (obj == null	|| 
			getClass() != HibernateProxyHelper.getClassWithoutInitializingProxy(obj)) {
			return false;
		}
		
		DPDistributionSite other = (DPDistributionSite) obj;

		if (ObjectUtils.notEqual(getDistributionProtocol(), other.getDistributionProtocol())) {
			return false;
		}
		
		if (ObjectUtils.notEqual(getInstitute(), other.getInstitute())) {
			return false;
		}

		if (ObjectUtils.notEqual(getSite(), other.getSite())) {
			return false;
		}
		
		return true;
	}
	
	public static Map<String, List<String>> getInstituteSitesMap(Collection<DPDistributionSite> distSites) {
		Map<String, List<String>> instSiteMap = new HashMap<String, List<String>>();
		
		for (DPDistributionSite distSite : distSites) {
			String instituteName = distSite.getInstitute().getName();
			List<String> siteNames = instSiteMap.get(instituteName);
			if (siteNames == null) {
				siteNames = new ArrayList<String>();
				instSiteMap.put(instituteName, siteNames);
			}
			
			if (distSite.getSite() != null) {
				siteNames.add(distSite.getSite().getName());
			}
		}
		
		return instSiteMap;
	}
}
