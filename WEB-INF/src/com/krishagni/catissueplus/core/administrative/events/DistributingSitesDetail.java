package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Site;

public class DistributingSitesDetail {

	private String instituteName;
	
	private List<String> sites = new ArrayList<String>();
	
	public String getInstituteName() {
		return instituteName;
	}
	
	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}
	
	public List<String> getSites() {
		return sites;
	}
	
	public void setSites(List<String> sites) {
		this.sites = sites;
	}
	
	public static DistributingSitesDetail from(Site site) {
		DistributingSitesDetail detail = new DistributingSitesDetail();
		detail.setInstituteName(site.getInstitute().getName());
		detail.getSites().add(site.getName());
		return detail;
	}
	
	public static List<DistributingSitesDetail> from(Set<Site> sites) {
		List<DistributingSitesDetail> list = new ArrayList<DistributingSitesDetail>();
		for (Site site: sites) {
			boolean isInstitutePresent = false;
			for (DistributingSitesDetail detail: list) {
				if (detail.getInstituteName() != null &&
						detail.getInstituteName().equals(site.getInstitute().getName())) {
					detail.getSites().add(site.getName());
					isInstitutePresent = true;
					break;
				}
			}
			if (!isInstitutePresent) {
				DistributingSitesDetail detail = new DistributingSitesDetail();
				detail.setInstituteName(site.getInstitute().getName());
				detail.getSites().add(site.getName());
				list.add(detail);
			}
		}
		
		return list;
	}
}
