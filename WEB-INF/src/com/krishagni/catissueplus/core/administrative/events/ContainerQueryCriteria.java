package com.krishagni.catissueplus.core.administrative.events;

public class ContainerQueryCriteria {	
	private Long id;
	
	private String name;

	private boolean includeStats;
	
	public ContainerQueryCriteria(Long id) {
		this.id = id;
	}
	
	public ContainerQueryCriteria(String name) {
		this.name = name;
	}

	public Long getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public boolean includeStats() {
		return includeStats;
	}

	public ContainerQueryCriteria includeStats(boolean includeStats) {
		this.includeStats = includeStats;
		return this;
	}
}
