package com.krishagni.catissueplus.core.biospecimen.events;


public class RegistrationQueryCriteria {
	//
	// Request to fetch CPR details either by cpr ID or (cp ID, ppid)
	// combination
	//
	
	private Long cprId;  
	
	private Long cpId;
	
	private String ppid;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}
}
