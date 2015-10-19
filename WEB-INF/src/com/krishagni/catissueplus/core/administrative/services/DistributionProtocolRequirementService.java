package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementListCriteria;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DistributionProtocolRequirementService {
	public ResponseEvent<List<DistributionProtocolRequirementDetail>> getRequirements (
			RequestEvent<DistributionProtocolRequirementListCriteria> req);
	
	public ResponseEvent<DistributionProtocolRequirementDetail> getRequirement (RequestEvent<Long> req);
	
	public ResponseEvent<DistributionProtocolRequirementDetail> createRequirement (
			RequestEvent<DistributionProtocolRequirementDetail> req);
	
	public ResponseEvent<DistributionProtocolRequirementDetail> updateRequirement (
			RequestEvent<DistributionProtocolRequirementDetail> req);
	
	public ResponseEvent<DistributionProtocolRequirementDetail> deleteRequirement (RequestEvent<Long> req);
}
