package com.krishagni.catissueplus.core.common.events;

public enum Resource {
	CP("CollectionProtocol"),
	
	CPR("ParticipantPhi"),
	
	VISIT("Visit"),
	
	SPECIMEN("Specimen"),
	
	STORAGE_CONTAINER("StorageContainer"),
	
	USER("User"),
	
	DISTRIBUTION_ORDER("DistributionOrder"),
	
	DP("DistributionProtocol"),
	
	SITE("Site");
	
	private final String name;
	
	private Resource(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
}
