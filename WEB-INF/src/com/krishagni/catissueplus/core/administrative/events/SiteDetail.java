
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.common.AttributeModifiedSupport;
import com.krishagni.catissueplus.core.common.ListenAttributeChanges;
import com.krishagni.catissueplus.core.common.events.UserSummary;

@ListenAttributeChanges
public class SiteDetail extends AttributeModifiedSupport {
	private Long id;
	
	private String name;
	
	private String instituteName;

	private String code;

	private List<UserSummary> coordinators = new ArrayList<UserSummary>();

	private String type;

	private String address;

	private String activityStatus;

	private int cpCount;

	public Long getId() {
		return id;
	}

	public void setId(Long siteId) {
		this.id = siteId;
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

	public List<UserSummary> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(List<UserSummary> coordinatorCollection) {
		this.coordinators = coordinatorCollection;
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

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getCpCount() {
		return cpCount;
	}

	public void setCpCount(int cpCount) {
		this.cpCount = cpCount;
	}

	public static SiteDetail from(Site site) {
		SiteDetail detail = new SiteDetail();
		detail.setId(site.getId());
		detail.setName(site.getName());
		detail.setInstituteName(site.getInstitute().getName());
		detail.setCode(site.getCode());
		detail.setType(site.getType());
		detail.setActivityStatus(site.getActivityStatus());
		detail.setAddress(site.getAddress());
		detail.setCoordinators(UserSummary.from(site.getCoordinators()));
		return detail;
	}
	
	public static List<SiteDetail> from(Collection<Site> sites) {
		List<SiteDetail> result = new ArrayList<SiteDetail>();
		
		if (CollectionUtils.isEmpty(sites)) {
			return result;
		}
		
		for (Site site : sites) {
			result.add(from(site));
		}
		
		return result;
	}
}
