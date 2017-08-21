package com.krishagni.catissueplus.core.de.domain;

import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class QueryFolder {

	private Long id;

	private String name;

	private User owner;
	
	private Boolean sharedWithAll;

	private Set<User> sharedWith = new HashSet<User>();

	private Set<SavedQuery> savedQueries = new HashSet<SavedQuery>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Boolean isSharedWithAll() {
		return sharedWithAll != null && sharedWithAll.equals(true);
	}

	public void setSharedWithAll(Boolean sharedWithAll) {
		this.sharedWithAll = sharedWithAll;
	}
	
	public Set<User> getSharedWith() {
		return sharedWith;
	}

	public void setSharedWith(Set<User> sharedWith) {
		this.sharedWith = sharedWith;
	}

	public Set<SavedQuery> getSavedQueries() {
		return savedQueries;
	}

	public void setSavedQueries(Set<SavedQuery> savedQueries) {
		this.savedQueries = savedQueries;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public User getOwner() {
		return owner;
	}

	public void setOwner(User owner) {
		this.owner = owner;
	}

	public void addQueries(List<SavedQuery> queries) {
		savedQueries.addAll(queries);
	}
	
	public void updateQueries(List<SavedQuery> queries) {
		savedQueries.retainAll(queries);

		for (SavedQuery query : queries) {
			if (!savedQueries.contains(query)) {
				savedQueries.add(query);
			}
		}
	}
	
	public void removeQueries(List<SavedQuery> queries) {
		savedQueries.removeAll(queries);
	}
	
	public void removeQueriesById(List<Long> queryIds) {
		Iterator<SavedQuery> iterator = savedQueries.iterator();
		while (iterator.hasNext()) {
			SavedQuery query = iterator.next();
			if (queryIds.contains(query.getId())) {
				iterator.remove();
			}
		}		
	}

	public Collection<User> addSharedUsers(Collection<User> users) {
		Set<User> addedUsers = new HashSet<User>(users);
		addedUsers.removeAll(sharedWith);
		
		sharedWith.addAll(addedUsers);
		return addedUsers;
	}
	
	public void removeSharedUsers(Collection<User> users) {
		sharedWith.removeAll(users);
	}
	
	public Collection<User> updateSharedUsers(Collection<User> users) {
		Set<User> newUsers = new HashSet<User>(users);
		newUsers.removeAll(sharedWith);
		
		sharedWith.retainAll(users);
		sharedWith.addAll(newUsers);
		return newUsers;
	}
	
	public boolean canUserAccess(Long userId) {
		if (owner != null && userId.equals(owner.getId())) {
			return true;
		}
		
		if (sharedWithAll != null && sharedWithAll.equals(true)) {
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
		
	public void update(QueryFolder folder) {
		this.setName(folder.getName());
		this.setSavedQueries(folder.getSavedQueries());
		this.setSharedWithAll(folder.isSharedWithAll());
		if (folder.isSharedWithAll()) {
			this.sharedWith.clear();
		} else {
			this.setSharedWith(folder.getSharedWith());
		}		
	}
}
