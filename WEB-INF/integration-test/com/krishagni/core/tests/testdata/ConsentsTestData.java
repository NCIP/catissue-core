package com.krishagni.core.tests.testdata;

import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierDetail;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp;
import com.krishagni.catissueplus.core.biospecimen.events.ConsentTierOp.OP;

public class ConsentsTestData {
	public static ConsentTierOp getConsentTierOp() {
		ConsentTierOp op = new ConsentTierOp();
		op.setConsentTier(getConsentTierDetail(null, "default statement"));
		op.setCpId(1L);
		op.setOp(OP.ADD);
		return op;
	}
	
	public static ConsentTierDetail getConsentTierDetail(Long id, String statement) {
		ConsentTierDetail req = new ConsentTierDetail();
		req.setId(id);
		req.setStatement(statement);
		return req;
	}
}
