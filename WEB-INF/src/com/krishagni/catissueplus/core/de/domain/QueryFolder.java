package com.krishagni.catissueplus.core.de.domain;

import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.User;

public class QueryFolder {

	private Long id;

	private String name;

	private User owner;

	private Set<User> sharedWith = new HashSet<User>();

	private Set<SavedQuery> savedQueries = new HashSet<SavedQuery>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
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
		
	public void update(QueryFolder folder) {
		this.setName(folder.getName());
		this.setSavedQueries(folder.getSavedQueries());
		this.setSharedWith(folder.getSharedWith());
	}
}
