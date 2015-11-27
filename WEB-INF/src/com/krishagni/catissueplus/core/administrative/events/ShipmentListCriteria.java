package com.krishagni.catissueplus.core.administrative.events;

import java.util.Set;

import com.krishagni.catissueplus.core.common.events.AbstractListCriteria;

public class ShipmentListCriteria extends AbstractListCriteria<ShipmentListCriteria> {
	private String name;
	
	private String recvInstitute;
	
	private String recvSite;
	
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
	
	public String recvInstitute() {
		return recvInstitute;
	}
	
	public ShipmentListCriteria recvInstitute(String recvInstitute) {
		this.recvInstitute = recvInstitute;
		return self();
	}
	
	public String recvSite() {
		return recvSite;
	}
	
	public ShipmentListCriteria recvSite(String recvSite) {
		this.recvSite = recvSite;
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
