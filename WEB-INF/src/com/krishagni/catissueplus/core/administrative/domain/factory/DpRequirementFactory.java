package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;

public interface DpRequirementFactory {
	public DpRequirement createDistributionProtocolRequirement(DpRequirementDetail detail);
}
