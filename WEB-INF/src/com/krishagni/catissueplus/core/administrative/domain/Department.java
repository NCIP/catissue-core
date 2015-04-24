
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

@Audited
public class Department extends BaseEntity {
	private String name;

	private Institute institute;

	private Set<User> users = new HashSet<User>();

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@NotAudited
	public Institute getInstitute() {
		return institute;
	}

	public void setInstitute(Institute institute) {
		this.institute = institute;
	}

	@NotAudited
	public Set<User> getUsers() {
		return users;
	}

	public void setUsers(Set<User> userCollection) {
		this.users = userCollection;
	}

}
