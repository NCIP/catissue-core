package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;

@ListenAttributeChanges
public class SiteSummary extends AttributeModifiedSupport {
	private Long id;
	
	private String name;
	
	private String instituteName;

	private String code;
	
	private String type;
	
	private String activityStatus;
	
	private int cpCount;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getInstituteName() {
		return instituteName;
	}

	public void setInstituteName(String instituteName) {
		this.instituteName = instituteName;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}
	
	public int getCpCount() {
		return cpCount;
	}

	public void setCpCount(int cpCount) {
		this.cpCount = cpCount;
	}
	
	public static SiteSummary from(Site site) {
		SiteSummary summary = new SiteSummary();
		copy(site, summary);
		return summary;
	}
	
	public static <T extends SiteSummary> void copy(Site site, T detail) {
		detail.setId(site.getId());
		detail.setName(site.getName());
		detail.setInstituteName(site.getInstitute().getName());
		detail.setCode(site.getCode());
		detail.setType(site.getType());
		detail.setActivityStatus(site.getActivityStatus());
	}
	
	public static List<SiteSummary> from(List<Site> sites) {
		List<SiteSummary> result = new ArrayList<SiteSummary>();
		
		for (Site site: sites) {
			result.add(from(site));
		}
		
		return result;
	}
	
}
