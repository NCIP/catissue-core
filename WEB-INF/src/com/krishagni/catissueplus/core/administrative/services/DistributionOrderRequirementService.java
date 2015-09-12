package com.krishagni.catissueplus.core.administrative.services;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.events.RequirementDetail;
import com.krishagni.catissueplus.core.common.events.RequestEvent;
import com.krishagni.catissueplus.core.common.events.ResponseEvent;

public interface DistributionOrderRequirementService {
	public ResponseEvent<List<RequirementDetail>> getRequirements (RequestEvent<Long> req);
	
	public ResponseEvent<RequirementDetail> getRequirementById (RequestEvent<Long> req);
	
	public ResponseEvent<RequirementDetail> addRequirement (RequestEvent<RequirementDetail> req);
	
	public ResponseEvent<RequirementDetail> updateRequirement (RequestEvent<RequirementDetail> req);
	
	public ResponseEvent<Long> deleteRequirement (RequestEvent<Long> req);
}