package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.errors.ObjectCreationException;
import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class AliquotsRequirementCreatedEvent extends ResponseEvent {
	private List<SpecimenRequirementDetail> aliquots;

	public List<SpecimenRequirementDetail> getAliquots() {
		return aliquots;
	}

	public void setAliquots(List<SpecimenRequirementDetail> aliquots) {
		this.aliquots = aliquots;
	}
	
	public static AliquotsRequirementCreatedEvent ok(List<SpecimenRequirementDetail> aliquots) {
		AliquotsRequirementCreatedEvent resp = new AliquotsRequirementCreatedEvent();
		resp.setStatus(EventStatus.OK);
		resp.setAliquots(aliquots);
		return resp;
	}
	
	public static AliquotsRequirementCreatedEvent badRequest(Exception e) {
		AliquotsRequirementCreatedEvent resp = new AliquotsRequirementCreatedEvent();
		resp.setStatus(EventStatus.BAD_REQUEST);
		resp.setException(e);
		if (e instanceof ObjectCreationException) {
			ObjectCreationException oce = (ObjectCreationException)e;
			resp.setErroneousFields(oce.getErroneousFields());
			resp.setMessage(oce.getMessage());
		}
		
		return resp;
	}
	
	public static AliquotsRequirementCreatedEvent serverError(Exception e) {
		AliquotsRequirementCreatedEvent resp = new AliquotsRequirementCreatedEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setException(e);
		return resp;
	}
}
