package com.krishagni.rbac.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.rbac.domain.Resource;

public class ResourceDetail {
	private Long id;
	
	private String name;

	public ResourceDetail() {		
	}
	
	public ResourceDetail(Long id) {
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
	
	public static ResourceDetail from(Resource r) { 
		ResourceDetail rs = new ResourceDetail();
		r = r == null ? new Resource() : r;
		rs.setId(r.getId());
		rs.setName(r.getName());
		return rs;
	}
	
	public static List<ResourceDetail> from(List<Resource> resources) {
		List<ResourceDetail> rs = new ArrayList<ResourceDetail>();
		
		for (Resource r : resources) {
			rs.add(ResourceDetail.from(r));
		}
		return rs;
	}
}
