package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class CollectionProtocolDependencyChecker extends AbstractDependencyChecker<CollectionProtocol>{
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Map<String, Object>> getDependentEntities(CollectionProtocol cp) {
		List<Map<String, Object>> dependencyStat = new ArrayList<Map<String, Object>>();
		
		List<Object[]> stats = daoFactory.getCollectionProtocolDao().getCpDependentEntities(cp.getId());
		setDependentEntities(stats, dependencyStat);
		
		return dependencyStat;
	}

}
