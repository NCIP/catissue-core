package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.repository.ContainerDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;


public class ContainerDaoImpl extends AbstractDao<StorageContainer> implements ContainerDao {

	@Override
	@SuppressWarnings("unchecked")
	public StorageContainer getContainer(String containerName) {
		List<StorageContainer> containers = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("name", containerName))
				.add(Restrictions.eq("activityStatus", "Active"))
				.list();
		
		return containers.isEmpty() ? null : containers.iterator().next();
	}
}
