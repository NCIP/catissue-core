package com.krishagni.catissueplus.core.common.events;

public enum Resource {
	CP("CollectionProtocol"),
	
	PARTICIPANT("ParticipantPhi"),
	
	PARTICIPANT_DEID("ParticipantDeid"),
	
	VISIT("Visit"),
	
	SPECIMEN("Specimen"),
	
	STORAGE_CONTAINER("StorageContainer"),
	
	USER("User"),
	
	ORDER("Order"),
	
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
