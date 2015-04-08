package com.krishagni.rbac.events;

import java.util.HashSet;
import java.util.Set;

public class UserAccessCriteria {
	private Long subjectId;
	
	private Long groupId;
	
	private Long cpId;
	
	private Set<Long> sites = new HashSet<Long>();
	
	private String resource;
	
	private String operation;
	
	private Long objectId;
	
	public Long subjectId() {
		return subjectId;
	}

	public UserAccessCriteria subjectId(Long subjectId) {
		this.subjectId = subjectId;
		return this;
	}

	public Long groupId() {
		return groupId;
	}

	public UserAccessCriteria groupId(Long groupId) {
		this.groupId = groupId;
		return this;
	}

	public Long cpId() {
		return cpId;
	}
	
	public UserAccessCriteria cpId(Long cpId) {
		this.cpId = cpId;
		return this;
	}
	
	public Set<Long> sites() {
		return sites;
	}
	
	public UserAccessCriteria sites(Set<Long> sites) {
		this.sites = sites;
		return this;
	}

	public String resource() {
		return resource;
	}

	public UserAccessCriteria resource(String resourceName) {
		this.resource = resourceName;
		return this;
	}

	public String operation() {
		return operation;
	}

	public UserAccessCriteria operation(String operationName) {
		this.operation = operationName;
		return this;
	}

	public Long objectId() {
		return objectId;
	}

	public UserAccessCriteria objectId(Long resourceInstanceId) {
		this.objectId = resourceInstanceId;
		return this;
	}
}