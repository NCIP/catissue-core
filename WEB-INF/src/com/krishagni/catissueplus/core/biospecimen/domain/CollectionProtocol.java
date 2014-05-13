
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.privileges.domain.CPSiteRole;

public class CollectionProtocol {

	private Long id;

	private String title;

	private String shortTitle;

	private Date startDate;

	private Date endDate;

	private User principalInvestigator;

	private Set<CPSiteRole> cpSiteRoles;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public User getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(User principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public Set<CPSiteRole> getCpSiteRoles() {
		return cpSiteRoles;
	}

	public void setCpSiteRoles(Set<CPSiteRole> cpSiteRoles) {
		this.cpSiteRoles = cpSiteRoles;
	}

}
