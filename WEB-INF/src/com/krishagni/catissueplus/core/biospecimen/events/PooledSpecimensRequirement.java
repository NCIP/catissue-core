package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

public class PooledSpecimensRequirement {
	private Long pooledSpmnHeadId;

	private List<SpecimenRequirementDetail> pooledSpmnReqs;

	public Long getPooledSpmnHeadId() {
		return pooledSpmnHeadId;
	}

	public void setPooledSpmnHeadId(Long pooledSpmnHeadId) {
		this.pooledSpmnHeadId = pooledSpmnHeadId;
	}

	public List<SpecimenRequirementDetail> getPooledSpmnReqs() {
		return pooledSpmnReqs;
	}

	public void setPooledSpmnReqs(List<SpecimenRequirementDetail> pooledSpmnReqs) {
		this.pooledSpmnReqs = pooledSpmnReqs;
	}
}
