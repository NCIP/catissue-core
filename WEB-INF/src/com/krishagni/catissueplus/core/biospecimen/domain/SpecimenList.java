package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class SpecimenList {
	private Long id;
	
	private String name;
	
	private User owner;
	
	private Set<User> sharedWith = new HashSet<User>();
	
	private Set<Specimen> specimens = new HashSet<Specimen>();

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
	
	public void removeSpecimensByLabel(List<String> labels) {
		Iterator<Specimen> iterator = this.specimens.iterator();
		while (iterator.hasNext()) {
			Specimen specimen = iterator.next();
			if (labels.contains(specimen.getLabel())) {
				iterator.remove();
			}
		}		
	}

	public void addSharedUsers(List<User> users) {
		sharedWith.addAll(users);
	}
	
	public void removeSharedUsers(List<User> users) {
		sharedWith.removeAll(users);
	}
	
	public void updateSharedUsers(List<User> users) {
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
		this.setName(specimenList.getName());
		this.setSpecimens(specimenList.getSpecimens());
		this.setSharedWith(specimenList.getSharedWith());		
	}
}
