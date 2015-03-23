package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class DistributionProtocolDependencyChecker extends AbstractDependencyChecker<DistributionProtocol> {
	private DaoFactory daoFactory;

	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Map<String, Object>> getDependentEntities(DistributionProtocol t) {
		List<Map<String, Object>> dependencyStat = new ArrayList<Map<String, Object>>();
		
		return dependencyStat;
	}

}
