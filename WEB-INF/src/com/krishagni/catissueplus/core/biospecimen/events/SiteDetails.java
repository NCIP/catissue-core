
package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Address;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;

public class SiteDetails {

	private String name;

	private List<String> coordinatorCollection = new ArrayList<String>();

	private Long id;

	private String type;

	private String activityStatus;

	private String emailAddress;

	private String street;

	private String city;

	private String state;

	private String country;

	private String zipCode;

	private String faxNumber;

	private String phoneNumber;

	private List<String> modifiedAttributes = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public List<String> getCoordinatorCollection() {
		return coordinatorCollection;
	}

	public void setCoordinatorCollection(List<String> coordinatorCollection) {
		this.coordinatorCollection = coordinatorCollection;
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

	public String getStreet() {
		return street;
	}

	public void setStreet(String street) {
		this.street = street;
	}

	public String getCity() {
		return city;
	}

	public void setCity(String city) {
		this.city = city;
	}

	public String getState() {
		return state;
	}

	public void setState(String state) {
		this.state = state;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getFaxNumber() {
		return faxNumber;
	}

	public void setFaxNumber(String faxNumber) {
		this.faxNumber = faxNumber;
	}

	public String getPhoneNumber() {
		return phoneNumber;
	}

	public void setPhoneNumber(String phoneNumber) {
		this.phoneNumber = phoneNumber;
	}

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	//following methods verify the modified attributes

	public boolean isSiteNameModified() {
		return modifiedAttributes.contains("name");
	}

	public boolean isCoordinatorCollectionModified() {
		return modifiedAttributes.contains("coordinatorCollection");
	}

	public boolean isSiteTypeModified() {
		return modifiedAttributes.contains("type");
	}

	public boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}
	
	public boolean isEmailAddressModified() {
		return modifiedAttributes.contains("emailAddress");
	}

	public boolean isCityModified() {
		return modifiedAttributes.contains("city");
	}

	public boolean isStreetModified() {
		return modifiedAttributes.contains("street");
	}

	public boolean isStateModified() {
		return modifiedAttributes.contains("state");
	}

	public boolean isCountryModified() {
		return modifiedAttributes.contains("country");
	}

	public boolean isZipCodeModified() {
		return modifiedAttributes.contains("zipCode");
	}

	public boolean isFaxNumberModified() {
		return modifiedAttributes.contains("faxNumber");
	}

	public boolean isPhoneNumberModified() {
		return modifiedAttributes.contains("phoneNumber");
	}

	public static SiteDetails fromDomain(Site site) {
		SiteDetails siteDto = new SiteDetails();
		siteDto.setId(site.getId());
		siteDto.setName(site.getName());
		siteDto.setType(site.getType());
		siteDto.setActivityStatus(site.getActivityStatus());
		siteDto.setEmailAddress(site.getEmailAddress());
		populateAddress(siteDto, site.getAddress());
		siteDto.setCoordinatorCollection(getCoordinatorList(site.getCoordinatorCollection()));

		return siteDto;
	}

	private static void populateAddress(SiteDetails siteDto, Address address) {
		siteDto.setStreet(address.getStreet());
		siteDto.setCountry(address.getCountry());
		siteDto.setFaxNumber(address.getFaxNumber());
		siteDto.setPhoneNumber(address.getPhoneNumber());
		siteDto.setState(address.getState());
		siteDto.setCity(address.getCity());
		siteDto.setZipCode(address.getZipCode());
	}

	private static List<String> getCoordinatorList(Set<User> coordinatorCollection) {
		List<String> loginNames = new ArrayList<String>();
		for (User user : coordinatorCollection) {
			loginNames.add(user.getLoginName());
		}
		return loginNames;
	}

}
