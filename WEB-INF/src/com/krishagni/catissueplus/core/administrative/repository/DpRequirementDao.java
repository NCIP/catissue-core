package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.DpRequirement;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementDetail;
import com.krishagni.catissueplus.core.administrative.events.DpRequirementListCriteria;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DpRequirementDao extends Dao<DpRequirement> {
	public List<DpRequirementDetail> getRequirements(DpRequirementListCriteria crit);
	
}
