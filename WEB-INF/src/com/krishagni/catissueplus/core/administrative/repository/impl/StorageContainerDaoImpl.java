
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

import edu.wustl.catissuecore.util.global.Constants;

public class StorageContainerDaoImpl extends AbstractDao<StorageContainer> implements StorageContainerDao {
	
	@Override
	@SuppressWarnings("unchecked")
	public StorageContainer getStorageContainerByName(String name) {
		List<StorageContainer> result = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE))
				.list();

		return result.isEmpty() ? null : result.iterator().next();
	}

	@Override
	public Boolean isUniqueContainerName(String name) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("name", name));
		return query.list().isEmpty() ? true : false;
	}

	@Override
	public StorageContainer getStorageContainer(Long id) {
		return (StorageContainer) sessionFactory.getCurrentSession().get(StorageContainer.class, id);
	}

	@Override
	public boolean isUniqueBarcode(String barcode) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("barcode", barcode))
				.add(Restrictions.eq("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE));
		return query.list().isEmpty() ? true : false;
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<StorageContainer> getAllStorageContainers(int maxResults) {
		return sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.setMaxResults(maxResults)
				.list();
		
	}

}
