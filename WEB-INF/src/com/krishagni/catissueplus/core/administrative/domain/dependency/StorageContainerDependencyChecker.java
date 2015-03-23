package com.krishagni.catissueplus.core.administrative.domain.dependency;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;

public class StorageContainerDependencyChecker extends AbstractDependencyChecker<StorageContainer>{
	private DaoFactory daoFactory;
	
	public DaoFactory getDaoFactory() {
		return daoFactory;
	}

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	public List<Map<String, Object>> getDependentEntities(StorageContainer container) {
		List<Map<String, Object>> dependencyStat = new ArrayList<Map<String, Object>>();
		
		List<Object[]> stats = daoFactory.getStorageContainerDao().getStorageContainerDependentEntities(container.getId());
		setDependentEntities(stats, dependencyStat);
		
		return dependencyStat;
	}

}
