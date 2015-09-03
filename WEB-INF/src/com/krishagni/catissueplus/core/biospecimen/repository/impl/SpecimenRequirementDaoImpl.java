package com.krishagni.catissueplus.core.biospecimen.repository.impl;

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
	
	private static final String FQN = SpecimenRequirement.class.getName();
	
	private static final String GET_SPECIMENS_COUNT = FQN + ".getSpecimensCount";
}
