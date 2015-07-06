package com.krishagni.catissueplus.core.biospecimen.services.impl;

import com.krishagni.catissueplus.core.biospecimen.events.ParticipantDetail;
import com.krishagni.catissueplus.core.biospecimen.services.ParticipantService;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;
import com.krishagni.catissueplus.core.importer.events.ImportObjectDetail;
import com.krishagni.catissueplus.core.importer.services.ObjectImporter;

public class ParticipantImporter implements ObjectImporter<ParticipantDetail, ParticipantDetail> {
	
	private ParticipantService participantSvc;
	
	public void setParticipantSvc(ParticipantService participantSvc) {
		this.participantSvc = participantSvc;
	}

	@Override
	public ResponseEvent<ParticipantDetail> importObject(RequestEvent<ImportObjectDetail<ParticipantDetail>> req) {
		try {
			ImportObjectDetail<ParticipantDetail> detail = req.getPayload();		
			RequestEvent<ParticipantDetail> partReq = new RequestEvent<ParticipantDetail>(detail.getObject());
			
			if (!detail.isCreate()) {
				return participantSvc.patchParticipant(partReq); 
			}
			
			return null;			
		} catch (Exception e) {
			return ResponseEvent.serverError(e);
		}
	}
}
