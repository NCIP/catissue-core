package com.krishagni.catissueplus.core.biospecimen.events;


public class ConsentTierOp {
	public enum OP {
		ADD, UPDATE, REMOVE
	};
	
	private Long cpId;
	
	private ConsentTierDetail consentTier;
	
	private OP op;
	
	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public ConsentTierDetail getConsentTier() {
		return consentTier;
	}

	public void setConsentTier(ConsentTierDetail consentTier) {
		this.consentTier = consentTier;
	}

	public OP getOp() {
		return op;
	}

	public void setOp(OP op) {
		this.op = op;
	}
}
