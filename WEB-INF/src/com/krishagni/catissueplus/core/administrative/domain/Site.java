
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.ibm.icu.util.Calendar;
import com.krishagni.catissueplus.core.common.SetUpdater;
import com.krishagni.catissueplus.core.common.util.Status;

public class Site {

	private Long id;

	private String name;

	private String Type;

	private Address address;

	private String activityStatus;

	private String emailAddress;

	protected Set<User> coordinatorCollection = new HashSet<User>();

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

	public String getType() {
		return Type;
	}

	public void setType(String type) {
		Type = type;
	}

	public Address getAddress() {
		return address;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getEmailAddress() {
		return emailAddress;
	}

	public void setEmailAddress(String emailAddress) {
		this.emailAddress = emailAddress;
	}

	public void setAddress(Address address) {
		this.address = address;
	}

	public Set<User> getCoordinatorCollection() {
		return coordinatorCollection;
	}

	public void setCoordinatorCollection(Set<User> coordinatorCollection) {
		this.coordinatorCollection = coordinatorCollection;
	}

	public void update(Site site) {
		if (activityStatus.equals(Status.ACTIVITY_STATUS_DISABLED.getStatus())) {
			this.setName(appendTimestamp(site.getActivityStatus(), site.getName()));
		}
		else {
			this.setName(site.getName());
		}
		this.setType(site.getType());
		this.setActivityStatus(site.getActivityStatus());
		this.setEmailAddress(site.getEmailAddress());
		SetUpdater.<User> newInstance().update(this.getCoordinatorCollection(), site.getCoordinatorCollection());
		updateAddressDetails(this.getAddress(), site.getAddress());
	}

	private String appendTimestamp(String activityStatus, String name) {
		Calendar cal = Calendar.getInstance();
		name = name + "_" + cal.getTimeInMillis();
		return name;
	}

	private void updateAddressDetails(Address oldAddress, Address address) {
		oldAddress.setStreet(address.getStreet());
		oldAddress.setCountry(address.getCountry());
		oldAddress.setFaxNumber(address.getFaxNumber());
		oldAddress.setPhoneNumber(address.getPhoneNumber());
		oldAddress.setState(address.getState());
		oldAddress.setCity(address.getCity());
		oldAddress.setZipCode(address.getZipCode());
	}

}
