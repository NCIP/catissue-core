package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class InstituteDependencyChecker extends AbstractDependencyChecker<Institute> {
	
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Map<String, Object>> getDependentEntities(Institute institute) {
		List<Map<String, Object>> dependencyStat = new ArrayList<Map<String, Object>>();
		
		List<Object[]> stats = daoFactory.getInstituteDao().getInstituteDependentEntities(institute.getId());
		setDependentEntities(stats, dependencyStat);
		
		return dependencyStat;
	}
	
}
