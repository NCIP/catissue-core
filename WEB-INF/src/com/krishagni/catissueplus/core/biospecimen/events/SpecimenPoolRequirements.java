package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class SpecimenPoolRequirements {
	private Long pooledSpecimenReqId;

	private List<SpecimenRequirementDetail> specimenPoolReqs;

	public Long getPooledSpecimenReqId() {
		return pooledSpecimenReqId;
	}

	public void setPooledSpecimenReqId(Long pooledSpecimenReqId) {
		this.pooledSpecimenReqId = pooledSpecimenReqId;
	}

	public List<SpecimenRequirementDetail> getSpecimenPoolReqs() {
		return specimenPoolReqs;
	}

	public void setSpecimenPoolReqs(List<SpecimenRequirementDetail> specimenPoolReqs) {
		this.specimenPoolReqs = specimenPoolReqs;
	}
}
