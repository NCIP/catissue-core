
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Query;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainerPosition;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerDao;
import com.krishagni.catissueplus.core.administrative.repository.StorageContainerListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.util.global.Constants;

public class StorageContainerDaoImpl extends AbstractDao<StorageContainer> implements StorageContainerDao {

	@Override
	public Class<?> getType() {
		return StorageContainer.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<StorageContainer> getStorageContainers(StorageContainerListCriteria listCrit) {
		StringBuilder from = new StringBuilder("select c from ").append(getType().getName()).append(" c");
		StringBuilder where = new StringBuilder("where c.activityStatus = :activityStatus");
		
		Map<String, Object> params = new HashMap<String, Object>();
		params.put("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		
		addNameRestriction(listCrit, from, where, params);
		addFreeContainersRestriction(listCrit, from, where);
		addSiteRestriction(listCrit, from, where, params);
		addParentRestriction(listCrit, from, where, params);
		addSpecimenRestriction(listCrit, from, where, params);
		addCpRestriction(listCrit, from, where, params);
		
		String hql = new StringBuilder(from).append(" ").append(where)
			.append(" order by c.name asc")
			.toString();
		
		Query query = sessionFactory.getCurrentSession().createQuery(hql)
				.setFirstResult(listCrit.startAt())
				.setMaxResults(listCrit.maxResults());
		
		for (Map.Entry<String, Object> param : params.entrySet()) {
			query.setParameter(param.getKey(), param.getValue());
		}
		
		return query.list();
	}
	
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
	
	@SuppressWarnings("unchecked")
	public StorageContainer getStorageContainerByBarcode(String barcode) {		
		List<StorageContainer> result = sessionFactory.getCurrentSession()
				.createCriteria(StorageContainer.class)
				.add(Restrictions.eq("barcode", barcode))
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.list();

		return result.isEmpty() ? null : result.iterator().next();		
	}

	@Override
	public StorageContainer getStorageContainer(Long id) {
		return (StorageContainer) sessionFactory.getCurrentSession().get(StorageContainer.class, id);
	}

	@Override
	public void delete(StorageContainerPosition position) {
		sessionFactory.getCurrentSession().delete(position);		
	}
	
	private void addNameRestriction(
			StorageContainerListCriteria crit, 
			StringBuilder from, 
			StringBuilder where, 
			Map<String, Object> params) {
		
		if (StringUtils.isBlank(crit.query())) {
			return;
		}
		
		if (where.length() != 0) {
			where.append(" and ");			
		}
		
		where.append("upper(c.name) like :name");
		params.put("name", "%" + crit.query().toUpperCase() + "%");
	}
	
	private void addFreeContainersRestriction(
			StorageContainerListCriteria crit, 
			StringBuilder from, 
			StringBuilder where) {
		
		if (!crit.onlyFreeContainers()) {
			return;
		}
		
		from.append(" join c.stats stats");
				
		if (where.length() != 0) {
			where.append(" and ");			
		}
		
		where.append("stats.freePositions > 0");
	}
	
	private void addSiteRestriction(
			StorageContainerListCriteria crit, 
			StringBuilder from, 
			StringBuilder where, 
			Map<String, Object> params) {
		
		if (StringUtils.isBlank(crit.siteName())) {
			return;
		}
		
		from.append(" join c.site site");
		
		if (where.length() != 0) {
			where.append(" and ");
		}
		
		where.append("site.name = :siteName");
		params.put("siteName", crit.siteName());
	}
	
	private void addParentRestriction(
			StorageContainerListCriteria crit, 
			StringBuilder from, 
			StringBuilder where, 
			Map<String, Object> params) {
		
		if (!crit.topLevelContainers() && crit.parentContainerId() == null) {
			return;
		}
		
		if (crit.topLevelContainers()) {
			from.append(" left join c.parentContainer pc");			
		} else if (crit.parentContainerId() != null) {
			from.append(" join c.parentContainer pc");
		} 
		
		if (where.length() != 0) {
			where.append(" and ");
		}
		
		Long parentId = crit.parentContainerId();
		if (parentId == null) {
			where.append("pc is null");
		} else {
			where.append("pc.id = :parentId");
			params.put("parentId", parentId);						
		}
	}
	
	private void addSpecimenRestriction(
			StorageContainerListCriteria crit, 
			StringBuilder from, 
			StringBuilder where, 
			Map<String, Object> params) {
		
		String specimenClass = crit.specimenClass(), specimenType = crit.specimenType();
		
		if (StringUtils.isBlank(specimenClass) && StringUtils.isBlank(specimenType)) {
			return;
		}
		
		if (where.length() != 0) {
			where.append(" and ");
		}

		where.append("(:specimenClass in elements(c.allowedSpecimenClasses)")
			.append(" or ")
			.append(":specimenType in elements(c.allowedSpecimenTypes)")
			.append(")");
		
		params.put("specimenClass", specimenClass);
		params.put("specimenType", specimenType);
	}
	
	private void addCpRestriction(
			StorageContainerListCriteria crit, 
			StringBuilder from, 
			StringBuilder where, 
			Map<String, Object> params) {
		
		Long cpId = crit.cpId();
		if (cpId == null || cpId == -1) {
			return;
		}
		
		from.append(" left join c.allowedCps cp");
		
		if (where.length() != 0) {
			where.append(" and ");
		}
		
		where.append("(cp is null or cp.id = :cpId)");
		params.put("cpId", cpId);
	}	
}
