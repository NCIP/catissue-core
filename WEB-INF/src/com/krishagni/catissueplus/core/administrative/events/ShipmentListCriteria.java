package com.krishagni.catissueplus.core.administrative.events;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ShipmentListCriteria extends AbstractListCriteria<ShipmentListCriteria> {
	private String name;
	
	private String recInstitute;
	
	private String recSite;
	
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
	
	public String recInstitute() {
		return recInstitute;
	}
	
	public ShipmentListCriteria recInstitute(String recInstitute) {
		this.recInstitute = recInstitute;
		return self();
	}
	
	public String recSite() {
		return recSite;
	}
	
	public ShipmentListCriteria recSite(String recSite) {
		this.recSite = recSite;
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
