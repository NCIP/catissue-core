
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class StorageContainerDaoImpl extends AbstractDao<StorageContainer> implements StorageContainerDao {
	
	private static final String FQN = StorageContainer.class.getName();

	private static final String GET_CONTAINER_BY_BARCODE = FQN+".getContainerByBarcode";
	
	private static final String GET_CONTAINERS = FQN+".getContainers";

	private static final String GET_CONTAINER_BY_NAME = FQN+".getContainerByName";

	@Override
	@SuppressWarnings("unchecked")
	public StorageContainer getStorageContainerByName(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CONTAINER_BY_NAME);
		query.setString("name", name);
		List<StorageContainer> result = query.list();
		if (!result.isEmpty()) {
			return result.get(0);
		}
		return null;
	}

	@Override
	public Boolean isUniqueContainerName(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CONTAINER_BY_NAME);
		query.setString("name", name);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public StorageContainer getStorageContainer(Long id) {
		return (StorageContainer) sessionFactory.getCurrentSession().get(StorageContainer.class, id);
	}

	@Override
	public boolean isUniqueBarcode(String barcode) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CONTAINER_BY_BARCODE);
		query.setString("barcode", barcode);
		return query.list().isEmpty() ? true : false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StorageContainer> getAllStorageContainers(int maxResults) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_CONTAINERS);
		query.setMaxResults(maxResults);
		return query.list();
	}

}
