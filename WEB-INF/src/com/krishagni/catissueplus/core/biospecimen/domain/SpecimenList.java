package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.util.Utility;

public class SpecimenList {
	private Long id;
	
	private String name;
	
	private User owner;

	private String description;

	private Date createdOn;
	
	private Set<User> sharedWith = new HashSet<User>();
	
	private Set<Specimen> specimens = new HashSet<Specimen>();
	
	private Date deletedOn;

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

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreatedOn() {
		return createdOn;
	}

	public void setCreatedOn(Date createdOn) {
		this.createdOn = createdOn;
	}

	public Set<User> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(Set<User> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
	}
	
	public Date getDeletedOn() {
		return deletedOn;
	}

	public void setDeletedOn(Date deletedOn) {
		this.deletedOn = deletedOn;
	}

	public void addSpecimens(List<Specimen> specimens) {
		this.specimens.addAll(specimens);
	}
	
	public void updateSpecimens(List<Specimen> specimens) {
		this.specimens.retainAll(specimens);

		for (Specimen specimen : specimens) {
			if (!this.specimens.contains(specimen)) {
				this.specimens.add(specimen);
			}
		}
	}
	
	public void removeSpecimens(List<Specimen> specimens) {
		this.specimens.removeAll(specimens);
	}

	public void clear() {
		specimens.clear();
	}

	public void addSharedUsers(List<User> users) {
		sharedWith.addAll(users);
	}
	
	public void removeSharedUsers(List<User> users) {
		sharedWith.removeAll(users);
	}
	
	public void updateSharedUsers(Collection<User> users) {
		sharedWith.retainAll(users);

		for (User user : users) {
			if (!sharedWith.contains(user)) {
				sharedWith.add(user);
			}
		}
	}
		
	public boolean canUserAccess(Long userId) {
		if (owner != null && userId.equals(owner.getId())) {
			return true;
		}
		
		boolean shared = false;
		for (User user : sharedWith) {
			if (user.getId().equals(userId)) {
				shared = true;
				break;
			}			
		}
		
		return shared;
	}	
	
	public void update(SpecimenList specimenList) {
		setName(specimenList.getName());
		setDescription(specimenList.getDescription());
		setSpecimens(specimenList.getSpecimens());
		updateSharedUsers(specimenList.getSharedWith());
	}

	public void delete() {
		setName(Utility.getDisabledValue(getName(), 255));
		setDeletedOn(Calendar.getInstance().getTime());
	}

	public boolean isDefaultList(User user) {
		return getDefaultListName(user).equals(getName());
	}

	public boolean isDefaultList() {
		return isDefaultList(getOwner());
	}

	public static String getDefaultListName(User user) {
		return getDefaultListName(user.getId());
	}

	public static String getDefaultListName(Long userId) {
		return String.format("$$$$user_%d", userId);
	}
}
