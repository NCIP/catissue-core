package com.krishagni.catissueplus.core.administrative.events;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ShipmentListCriteria extends AbstractListCriteria<ShipmentListCriteria> {
	private String name;
	
	private String institute;
	
	private String site;
	
	private Set<Long> siteIds;
	
	@Override
	public ShipmentListCriteria self() {
		return this;
	}
	
	public String name() {
		return name;
	}
	
	public ShipmentListCriteria name(String name) {
		this.name = name;
		return self();
	}
	
	public String institute() {
		return institute;
	}
	
	public ShipmentListCriteria institute(String institute) {
		this.institute = institute;
		return self();
	}
	
	public String site() {
		return site;
	}
	
	public ShipmentListCriteria site(String site) {
		this.site = site;
		return self();
	}
	
	public Set<Long> siteIds() {
		return siteIds;
	}
	
	public ShipmentListCriteria siteIds(Set<Long> siteIds) {
		this.siteIds = siteIds;
		return self();
	}
	
}
