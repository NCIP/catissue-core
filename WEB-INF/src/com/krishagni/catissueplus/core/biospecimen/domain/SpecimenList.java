package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.common.util.Utility;

public class SpecimenList {
	private Long id;
	
	private String name;
	
	private User owner;

	private String description;

	private Date createdOn;

	private Date lastUpdatedOn;
	
	private Set<User> sharedWith = new HashSet<>();
	
	private Set<Specimen> specimens = new HashSet<>();
	
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

	public Date getLastUpdatedOn() {
		return lastUpdatedOn;
	}

	public void setLastUpdatedOn(Date lastUpdatedOn) {
		this.lastUpdatedOn = lastUpdatedOn;
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
		setLastUpdatedOn(Calendar.getInstance().getTime());
	}
	
	public void updateSpecimens(List<Specimen> specimens) {
		this.specimens.retainAll(specimens);

		for (Specimen specimen : specimens) {
			if (!this.specimens.contains(specimen)) {
				this.specimens.add(specimen);
			}
		}

		setLastUpdatedOn(Calendar.getInstance().getTime());
	}
	
	public void removeSpecimens(List<Specimen> specimens) {
		this.specimens.removeAll(specimens);
		setLastUpdatedOn(Calendar.getInstance().getTime());
	}

	public boolean contains(Specimen specimen) {
		return specimens.contains(specimen);
	}

	public void clear() {
		specimens.clear();
		setLastUpdatedOn(Calendar.getInstance().getTime());
	}

	public void addSharedUsers(List<User> users) {
		sharedWith.addAll(users);
		setLastUpdatedOn(Calendar.getInstance().getTime());
	}
	
	public void removeSharedUsers(List<User> users) {
		sharedWith.removeAll(users);
		setLastUpdatedOn(Calendar.getInstance().getTime());
	}
	
	public void updateSharedUsers(Collection<User> users) {
		sharedWith.retainAll(users);

		for (User user : users) {
			if (!sharedWith.contains(user)) {
				sharedWith.add(user);
			}
		}

		setLastUpdatedOn(Calendar.getInstance().getTime());
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
		setLastUpdatedOn(Calendar.getInstance().getTime());
	}

	public void delete() {
		setName(Utility.getDisabledValue(getName(), 255));
		setLastUpdatedOn(Calendar.getInstance().getTime());
		setDeletedOn(Calendar.getInstance().getTime());
	}

	public int size() {
		return specimens.size();
	}

	public boolean isDefaultList(User user) {
		return getDefaultListName(user).equals(getName());
	}

	public boolean isDefaultList() {
		return isDefaultList(getOwner());
	}

	public List<Specimen> getSpecimensGroupedByAncestors() {
		return groupByAncestors(specimens);
	}

	public static String getDefaultListName(User user) {
		return getDefaultListName(user.getId());
	}

	public static String getDefaultListName(Long userId) {
		return String.format("$$$$user_%d", userId);
	}

	public static List<Specimen> groupByAncestors(Collection<Specimen> spmns) {
		Map<String, Set<Specimen>> spmnsMap = new LinkedHashMap<>();
		for (Specimen spmn : spmns) {
			while (spmn != null) {
				String parentLabel = null;
				if (spmn.getParentSpecimen() != null) {
					parentLabel = spmn.getParentSpecimen().getLabel();
				}

				Set<Specimen> childSpmns = spmnsMap.get(parentLabel);
				if (childSpmns == null) {
					childSpmns = new LinkedHashSet<>();
					spmnsMap.put(parentLabel, childSpmns);
				}

				childSpmns.add(spmn);
				spmn = spmn.getParentSpecimen();
			}
		}

		List<Specimen> result = new ArrayList<>();
		addSpecimens(spmnsMap, null, new HashSet<>(spmns), result);
		return result;
	}

	private static void addSpecimens(Map<String, Set<Specimen>> spmnsMap, String parentLabel, Set<Specimen> listSpecimens, List<Specimen> result) {
		Set<Specimen> childSpmns = spmnsMap.get(parentLabel);
		if (childSpmns == null || listSpecimens.isEmpty()) {
			return;
		}

		for (Specimen childSpmn : childSpmns) {
			if (listSpecimens.remove(childSpmn)) {
				result.add(childSpmn);
			}

			if (listSpecimens.isEmpty()) {
				break;
			}

			addSpecimens(spmnsMap, childSpmn.getLabel(), listSpecimens, result);
		}
	}

}
