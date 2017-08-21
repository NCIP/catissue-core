
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.ContainerType;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeDao;
import com.krishagni.catissueplus.core.administrative.repository.ContainerTypeListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class ContainerTypeDaoImpl extends AbstractDao<ContainerType> implements ContainerTypeDao {
	
	@Override
	public Class<?> getType() {
		return ContainerType.class;
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<ContainerType> getTypes(ContainerTypeListCriteria crit) {
		return getTypesListQuery(crit)
			.setFirstResult(crit.startAt())
			.setMaxResults(crit.maxResults())
			.addOrder(Order.asc("name"))
			.list();
	}

	@Override
	public Long getTypesCount(ContainerTypeListCriteria crit) {
		Number count = (Number) getTypesListQuery(crit)
				.setProjection(Projections.rowCount())
				.uniqueResult();
		return count.longValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public ContainerType getByName(String name) {
		List<ContainerType> result = sessionFactory.getCurrentSession()
			.getNamedQuery(CONTAINER_TYPE_BY_NAME)
			.setParameter("name", name)
			.list();

		return result.isEmpty() ? null : result.iterator().next();
	}

	private Criteria getTypesListQuery(ContainerTypeListCriteria crit) {
		return addSearchConditions(getCurrentSession().createCriteria(ContainerType.class), crit);
	}

	private Criteria addSearchConditions(Criteria query, ContainerTypeListCriteria crit) {
		addNameRestriction(query, crit.query(), crit.matchMode());
		addCanHoldRestriction(query, crit.canHold());
		return query;
	}
	
	private void addNameRestriction(Criteria query, String name, MatchMode matchMode) {
		if (StringUtils.isBlank(name)) {
			return;
		}
		
		query.add(Restrictions.ilike("name", name, matchMode));
	}
	
	private void addCanHoldRestriction(Criteria query, String canHold) {
		if (StringUtils.isBlank(canHold)) {
			return;
		}
		
		query.createAlias("canHold", "canHold")
			.add(Restrictions.eq("canHold.name", canHold));
		
	}
	
	private static final String CONTAINER_TYPE_FQN = ContainerType.class.getName();
	
	private static final String CONTAINER_TYPE_BY_NAME = CONTAINER_TYPE_FQN + ".getByName";
}
