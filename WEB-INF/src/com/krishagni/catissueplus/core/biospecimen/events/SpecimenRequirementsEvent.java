package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.common.events.EventStatus;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public class SpecimenRequirementsEvent extends ResponseEvent {
	private Long cpeId;
	
	private List<SpecimenRequirementDetail> specimenRequirements;

	public Long getCpeId() {
		return cpeId;
	}

	public void setCpeId(Long cpeId) {
		this.cpeId = cpeId;
	}

	public List<SpecimenRequirementDetail> getSpecimenRequirements() {
		return specimenRequirements;
	}

	public void setSpecimenRequirements(List<SpecimenRequirementDetail> specimenRequirements) {
		this.specimenRequirements = specimenRequirements;
	}
	
	public static SpecimenRequirementsEvent ok(Long cpeId, List<SpecimenRequirementDetail> srs) {
		SpecimenRequirementsEvent resp = new SpecimenRequirementsEvent();
		resp.setCpeId(cpeId);
		resp.setSpecimenRequirements(srs);
		resp.setStatus(EventStatus.OK);
		return resp;
	}
	
	public static SpecimenRequirementsEvent notFound(Long cpeId) {
		SpecimenRequirementsEvent resp = new SpecimenRequirementsEvent();
		resp.setStatus(EventStatus.NOT_FOUND);
		resp.setCpeId(cpeId);
		return resp;
	}
	
	public static SpecimenRequirementsEvent serverError(Long cpeId, Exception e) {
		SpecimenRequirementsEvent resp = new SpecimenRequirementsEvent();
		resp.setStatus(EventStatus.INTERNAL_SERVER_ERROR);
		resp.setCpeId(cpeId);
		return resp;		
	}

}
