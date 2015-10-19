package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocolRequirement;
import com.krishagni.catissueplus.core.administrative.events.DistributionProtocolRequirementListCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DistributionProtocolRequirementDao extends Dao<DistributionProtocolRequirement> {
	public List<DistributionProtocolRequirement> getRequirements(DistributionProtocolRequirementListCriteria crit);
	
}
