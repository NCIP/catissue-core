package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ReqConsentTiersEvent;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOpEvent.OP;

public class ConsentsTestData {

	
	public static ConsentTierOpEvent getConsentTierOpEvent() {
		ConsentTierOpEvent req = new ConsentTierOpEvent();
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		req.setOp(OP.ADD);
		req.setConsentTier(getConsentTierDetail(null, "default statement"));
		req.setCpId(1L);
		return req;
	}
	
	public static ConsentTierDetail getConsentTierDetail(Long id, String statement) {
		ConsentTierDetail req = new ConsentTierDetail();
		req.setId(id);
		req.setStatement(statement);
		return req;
	}
	
	public static ReqConsentTiersEvent getReqConsentTiersEvent() {
		ReqConsentTiersEvent req = new ReqConsentTiersEvent();
		req.setCpId(1L);
		req.setSessionDataBean(CprTestData.getSessionDataBean());
		return req;
	}
	
}
