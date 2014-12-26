package com.krishagni.catissueplus.core.biospecimen.events;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class ConsentTierOpRespEvent extends ResponseEvent {
	private Long cpId;
	
	private ConsentTierDetail consentTier;
	
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
	
	public static ConsentTierOpRespEvent ok(Long cpId, ConsentTierDetail consentTier) {
		ConsentTierOpRespEvent resp = new ConsentTierOpRespEvent();
		resp.setConsentTier(consentTier);
		resp.setCpId(cpId);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static ConsentTierOpRespEvent notFound(Long cpId) {
		return event(EventStatus.NOT_FOUND, cpId, null);
	}
	
	public static ConsentTierOpRespEvent badRequest(Long cpId, Exception e) {
		return event(EventStatus.BAD_REQUEST, cpId, e);
	}
	
	public static ConsentTierOpRespEvent serverError(Long cpId, Exception e) {
		return event(EventStatus.INTERNAL_SERVER_ERROR, cpId, e);
	}
	
	private static ConsentTierOpRespEvent event(EventStatus status, Long cpId, Exception e) {
		ConsentTierOpRespEvent resp = new ConsentTierOpRespEvent();
		resp.setCpId(cpId);
		resp.setStatus(status);
		resp.setException(e);
		return resp;								
	}
}
