package com.krishagni.catissueplus.core.common.events;

public class EntityQueryCriteria {
	private Long id;
	
	private String name;
	
	public EntityQueryCriteria(Long id) {
		this.id = id;		
	}
	
	public EntityQueryCriteria(String name) {
		this.name = name;
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

}
