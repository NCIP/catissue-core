package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Resource;

public class ResourceDetails {
	private Long id;
	
	private String name;

	public ResourceDetails() {		
	}
	
	public ResourceDetails(Long id) {
		this.id = id;
	}
		
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
	
	public static ResourceDetails fromResource(Resource r) { 
		ResourceDetails rs = new ResourceDetails();
		r = r == null ? new Resource() : r;
		rs.setId(r.getId());
		rs.setName(r.getName());
		return rs;
	}
	
	public static List<ResourceDetails> fromResources(List<Resource> resources) {
		List<ResourceDetails> rs = new ArrayList<ResourceDetails>();
		
		for (Resource r : resources) {
			rs.add(ResourceDetails.fromResource(r));
		}
		return rs;
	}
}
