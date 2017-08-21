package com.krishagni.rbac.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.rbac.domain.Resource;
import com.krishagni.rbac.repository.ResourceDao;
import com.krishagni.rbac.repository.ResourceListCriteria;

public class ResourceDaoImpl extends AbstractDao<Resource> implements ResourceDao {
	private static final String FQN = Resource.class.getName();
	
	private static final String GET_RESOURCE_BY_NAME = FQN + ".getResourceByName";
	
	private static final String GET_ALL_RESOURCES = FQN + ".getAllResources";
	
	@Override
	@SuppressWarnings("unchecked")
	public Resource getResourceByName(String resourceName) {
		List<Resource> resources = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_RESOURCE_BY_NAME)
				.setString("name" , resourceName)
				.list();
		return resources.isEmpty() ? null : resources.get(0);
	}

	@Override
	public Resource getResource(Long resourceId) {
		return (Resource)sessionFactory.getCurrentSession()
				.get(Resource.class, resourceId);
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Resource> getResources(ResourceListCriteria listCriteria) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Resource.class)
				.setFirstResult(listCriteria.startAt())
				.setMaxResults(listCriteria.maxResults());
		
		if (StringUtils.isNotBlank(listCriteria.query())) {
			query.add(Restrictions.ilike("name", listCriteria.query(), MatchMode.ANYWHERE));
		}
		
		return query.list();
	}
}
