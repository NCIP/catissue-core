package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;
import com.krishagni.catissueplus.core.administrative.events.ListPvCriteria;
import com.krishagni.catissueplus.core.administrative.repository.PermissibleValueDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;


public class PermissibleValueDaoImpl extends AbstractDao<PermissibleValue> implements PermissibleValueDao {

	@Override
	public PermissibleValue getById(Long id) {
		return (PermissibleValue) sessionFactory.getCurrentSession().get(PermissibleValue.class, id);
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<PermissibleValue> getPvs(ListPvCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(PermissibleValue.class);
		if (StringUtils.isNotBlank(crit.parentAttribute()) || StringUtils.isNotBlank(crit.parentValue())) {
			query.createAlias("parent", "p");
		}
				
		if (StringUtils.isNotBlank(crit.attribute())) {
			query.add(Restrictions.eq("attribute", crit.attribute()));
		} else if (StringUtils.isNotBlank(crit.parentAttribute())) {
			query.add(Restrictions.eq("p.attribute", crit.parentAttribute()));
		}
		
		if (StringUtils.isNotBlank(crit.parentValue())) {
			query.add(Restrictions.eq("p.value", crit.parentValue()));
		}
		
		if (StringUtils.isNotBlank(crit.query())) {
			query.add(Restrictions.ilike("value", crit.query(), MatchMode.ANYWHERE));
		}
		
		int maxResults = crit.maxResults() < 0 ? 100 : crit.maxResults();
		return query.setMaxResults(maxResults)
			.addOrder(Order.asc("value"))
			.list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSpecimenClasses() {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(PermissibleValue.class)
				.add(Restrictions.eq("attribute", "2003991"));
		return query.setProjection(Projections.property("value")).list();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSpecimenTypes(Collection<String> specimenClasses) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(PermissibleValue.class)
				.createAlias("parent", "ppv")
				.add(Restrictions.eq("ppv.attribute", "2003991"));
		
		if (CollectionUtils.isNotEmpty(specimenClasses)) {
			query.add(Restrictions.in("ppv.value", specimenClasses));
		}
		
		return query.setProjection(Projections.property("value")).list();
	}	
}
