package com.krishagni.catissueplus.core.biospecimen.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.SpecimenRequirement;
import com.krishagni.catissueplus.core.biospecimen.repository.SpecimenRequirementDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class SpecimenRequirementDaoImpl extends AbstractDao<SpecimenRequirement> implements SpecimenRequirementDao {

	@Override
	public Class<SpecimenRequirement> getType() {
		return SpecimenRequirement.class;
	}
	
	@Override
	public SpecimenRequirement getSpecimenRequirement(Long id) {
		return (SpecimenRequirement) getSessionFactory()
				.getCurrentSession()
				.get(SpecimenRequirement.class, id);
	}
	
	@Override
	public int getSpecimensCount(Long srId) {
		return ((Number) getSessionFactory()
			.getCurrentSession()
			.getNamedQuery(GET_SPECIMENS_COUNT)
			.setLong("srId", srId)
			.uniqueResult()).intValue();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public SpecimenRequirement getByCpEventLabelAndSrCode(String cpShortTitle, String eventLabel, String code) {
		List<SpecimenRequirement> result = getSessionFactory().getCurrentSession()
			.getNamedQuery(GET_SR_BY_CP_EVENT_AND_SR_CODE)
			.setString("cpShortTitle", cpShortTitle)
			.setString("eventLabel", eventLabel)
			.setString("code", code)
			.list();
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}
	
	private static final String FQN = SpecimenRequirement.class.getName();
	
	private static final String GET_SPECIMENS_COUNT = FQN + ".getSpecimensCount";
	
	private static final String GET_SR_BY_CP_EVENT_AND_SR_CODE = FQN + ".getByCpEventLabelAndSrCode";
}
