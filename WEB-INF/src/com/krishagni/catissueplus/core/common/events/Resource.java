package com.krishagni.catissueplus.core.common.events;

public enum Resource {
	CP("CollectionProtocol"),
	
	PARTICIPANT("ParticipantPhi"),
	
	PARTICIPANT_DEID("ParticipantDeid"),
	
	VISIT_N_SPECIMEN("VisitAndSpecimen"),
	
	STORAGE_CONTAINER("StorageContainer"),
	
	USER("User"),
	
	ORDER("Order"),
	
	DP("DistributionProtocol");

	private final String name;
	
	private Resource(String name) {
		this.name = name;
	}
	
	public String getName() { 
		return name;
	}
}
