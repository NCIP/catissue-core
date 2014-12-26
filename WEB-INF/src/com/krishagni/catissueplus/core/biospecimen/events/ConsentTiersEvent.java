package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ConsentTiersEvent extends ResponseEvent {
	private Long cpId;
	
	private List<ConsentTierDetail> consentTiers;

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public List<ConsentTierDetail> getConsentTiers() {
		return consentTiers;
	}

	public void setConsentTiers(List<ConsentTierDetail> consentTiers) {
		this.consentTiers = consentTiers;
	}
	
	public static ConsentTiersEvent ok(Long cpId, List<ConsentTierDetail> consentTiers) {
		ConsentTiersEvent resp = new ConsentTiersEvent();
		resp.setCpId(cpId);
		resp.setConsentTiers(consentTiers);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static ConsentTiersEvent notFound(Long cpId) {
		ConsentTiersEvent resp = new ConsentTiersEvent();
		resp.setCpId(cpId);
		resp.setStatus(EventStatus.NOT_FOUND);
		return resp;
	}	
	
	public static ConsentTiersEvent serverError(Long cpId, Exception e) {
		ConsentTiersEvent resp = new ConsentTiersEvent();
		resp.setCpId(cpId);
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;		
	}
}
