package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.ResourceInstanceOp;
import com.krishagni.rbac.domain.RoleAccessControl;

public class RoleAccessControlDetails {
	private Long id;
	
	private String resourceName;
	
	private List<ResourceInstanceOpDetails> operations = new ArrayList<ResourceInstanceOpDetails>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getResourceName() {
		return resourceName;
	}

	public void setResourceName(String resourceName) {
		this.resourceName = resourceName;
	}

	public List<ResourceInstanceOpDetails> getOperations() {
		return operations;
	}

	public void setOperations(List<ResourceInstanceOpDetails> operations) {
		this.operations = operations;
	}

	public static RoleAccessControlDetails fromRoleAccessControl(RoleAccessControl rac) {
		RoleAccessControlDetails racd = new RoleAccessControlDetails();
		
		racd.setId(rac.getId());
		racd.setResourceName(rac.getResource() == null ? null : rac.getResource().getName());
		
		for (ResourceInstanceOp rio : rac.getOperations()) {
			racd.getOperations().add(ResourceInstanceOpDetails.fromResourceInstanceOp(rio));
		}
		
		return racd;
	}
}
