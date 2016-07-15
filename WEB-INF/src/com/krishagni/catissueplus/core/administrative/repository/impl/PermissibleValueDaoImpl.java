package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.Collection;
import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Disjunction;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.hibernate.sql.JoinType;

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
		Criteria query = getPvQuery(crit);		
		if (StringUtils.isNotBlank(crit.query())) {
			query.add(Restrictions.ilike("value", crit.query(), MatchMode.ANYWHERE));
		}
		
		int maxResults = crit.maxResults() < 0 ? 100 : crit.maxResults();
		return query.setMaxResults(maxResults)
			.addOrder(Order.asc("sortOrder"))
			.addOrder(Order.asc("value"))
			.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public PermissibleValue getByConceptCode(String attribute, String conceptCode) {
		List<PermissibleValue> pvs = getSessionFactory().getCurrentSession()
				.getNamedQuery(GET_BY_CONCEPT_CODE)
				.setString("attribute", attribute)
				.setString("conceptCode", conceptCode)
				.list();
		
		return CollectionUtils.isEmpty(pvs) ? null : pvs.iterator().next();
	}
	
	@SuppressWarnings("unchecked")
	@Override
	public PermissibleValue getByValue(String attribute, String value) {
		List<PermissibleValue> pvs = getSessionFactory().getCurrentSession()
				.getNamedQuery(GET_BY_VALUE)
				.setString("attribute", attribute)
				.setString("value", value)
				.list();
		
		return CollectionUtils.isEmpty(pvs) ? null : pvs.iterator().next();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSpecimenClasses() {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_CLASSES)
				.list();
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<String> getSpecimenTypes(Collection<String> specimenClasses) {
		return sessionFactory.getCurrentSession()
				.getNamedQuery(GET_SPECIMEN_TYPES)
				.setParameterList("specimenClasses", specimenClasses)
				.list();
	}
	
	@Override
	public boolean exists(String attribute, Collection<String> values) {
		return exists(attribute, values, false);
	}

	@Override
	public boolean exists(String attribute, String parentValue, Collection<String> values) {
		Number count = (Number)sessionFactory.getCurrentSession().createCriteria(PermissibleValue.class)
				.createAlias("parent", "ppv")
				.add(Restrictions.eq("ppv.attribute", attribute))
				.add(Restrictions.eq("ppv.value", parentValue))
				.add(Restrictions.in("value", values))
				.setProjection(Projections.count("id"))
				.uniqueResult();
		return count.intValue() == values.size();
	}

	public boolean exists(String attribute, Collection<String> values, boolean leafLevelCheck) {
		Criteria query = sessionFactory.getCurrentSession()
				.createCriteria(PermissibleValue.class)
				.add(Restrictions.eq("attribute", attribute))
				.add(Restrictions.in("value", values))
				.setProjection(Projections.count("id"));
		
		if (leafLevelCheck) {
			query.createAlias("children", "c", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.isNull("c.id"));
		}
		
		Number count = (Number)query.uniqueResult();				
		return count.intValue() == values.size();	
	}
	
	@Override
	public boolean exists(String attribute, int depth, Collection<String> values) {
		return exists(attribute, depth, values, false);
	}
	
	@Override
	public boolean exists(String attribute, int depth, Collection<String> values, boolean anyLevel) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(PermissibleValue.class)
				.add(Restrictions.in("value", values))
				.setProjection(Projections.count("id"));
		
		for (int i = 1; i <= depth; ++i) {			
			if (i == 1) {
				query.createAlias("parent", "pv" + i, anyLevel ? JoinType.LEFT_OUTER_JOIN : JoinType.INNER_JOIN);
			} else {
				query.createAlias("pv" + (i - 1) + ".parent", "pv" + i, anyLevel ? JoinType.LEFT_OUTER_JOIN : JoinType.INNER_JOIN);
			}			
		}
		
		Disjunction attrCond = Restrictions.disjunction();
		attrCond.add(Restrictions.eq("pv" + depth + ".attribute", attribute));
		if (anyLevel) {
			for (int i = depth - 1; i >= 1; i--) {
				attrCond.add(Restrictions.eq("pv" + i + ".attribute", attribute));
			}
			
			attrCond.add(Restrictions.eq("attribute", attribute));
		}
		
		Number count = (Number)query.add(attrCond).uniqueResult();
		return count.intValue() == values.size();
	}
	
	private Criteria getPvQuery(ListPvCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(PermissibleValue.class);
		if (StringUtils.isNotBlank(crit.parentAttribute()) || StringUtils.isNotBlank(crit.parentValue())) {
			query.createAlias("parent", "p");
		} else {
			query.createAlias("parent", "p", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.isNull("p.id"));
		}
				
		if (StringUtils.isNotBlank(crit.attribute())) {
			query.add(Restrictions.eq("attribute", crit.attribute()));
		} else if (StringUtils.isNotBlank(crit.parentAttribute())) {
			query.add(Restrictions.eq("p.attribute", crit.parentAttribute()));
		}
		
		if (StringUtils.isNotBlank(crit.parentValue())) {
			query.add(Restrictions.eq("p.value", crit.parentValue()));
		}
		
		if (crit.includeOnlyLeafValue()) {
			query.createAlias("children", "c", JoinType.LEFT_OUTER_JOIN)
				.add(Restrictions.isNull("c.id"));			
		}
		
		return query;
	}

	private static final String FQN = PermissibleValue.class.getName();

	private static final String GET_BY_CONCEPT_CODE = FQN + ".getByConceptCode";

	private static final String GET_BY_VALUE = FQN + ".getByValue";

	private static final String GET_SPECIMEN_CLASSES = FQN + ".getSpecimenClasses";

	private static final String GET_SPECIMEN_TYPES = FQN + ".getSpecimenTypes";
}
