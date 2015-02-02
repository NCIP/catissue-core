
package com.krishagni.catissueplus.core.administrative.events;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.events.UserSummary;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class SiteDetails {

	private String name;

	private List<UserSummary> coordinatorCollection = new ArrayList<UserSummary>();

	private Long id;

	private String code;

	private String type;

	private String activityStatus;

	private String address;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public List<UserSummary> getCoordinatorCollection() {
		return coordinatorCollection;
	}

	public void setCoordinatorCollection(List<UserSummary> coordinatorCollection) {
		this.coordinatorCollection = coordinatorCollection;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long siteId) {
		this.id = siteId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public static SiteDetails fromDomain(Site site) {
		SiteDetails siteDto = new SiteDetails();
		siteDto.setId(site.getId());
		siteDto.setName(site.getName());
		siteDto.setType(site.getType());
		siteDto.setActivityStatus(site.getActivityStatus());
		siteDto.setCoordinatorCollection(getCoordinatorList(site.getCoordinatorCollection()));
		return siteDto;
	}

	private static List<UserSummary> getCoordinatorList(Set<User> coordinatorCollection) {
		List<UserSummary> users = new ArrayList<UserSummary>();
		for (User user : coordinatorCollection) {
			UserSummary userSummary = new UserSummary();
			userSummary.setId(user.getId());
			userSummary.setFirstName(user.getFirstName());
			userSummary.setLastName(user.getLastName());
			userSummary.setLoginName(user.getLoginName());
			users.add(userSummary);
		}
		return users;
	}

}
