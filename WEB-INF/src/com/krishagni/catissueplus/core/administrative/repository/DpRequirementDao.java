package com.krishagni.catissueplus.core.administrative.repository;

import java.math.BigDecimal;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.events.DprStat;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DpRequirementDao extends Dao<DpRequirement> {
	public Map<Long, DprStat> getDistributionStatByDp(Long dpId);
	
}
