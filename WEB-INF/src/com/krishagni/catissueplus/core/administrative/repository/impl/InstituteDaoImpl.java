package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.InstituteDetail;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class InstituteDaoImpl extends AbstractDao<Institute> implements InstituteDao {
	
	@Override
	public Class<?> getType() {
		return Institute.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<InstituteDetail> getInstitutes(InstituteListCriteria listCrit) {
		Criteria query = getInstituteListQuery(listCrit)
			.addOrder(Order.asc("institute.name"))
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults());
		
		addProjectionFields(query);
		
		List<Object[]> rows = query.list();
		List<InstituteDetail> institutes = new ArrayList<InstituteDetail>();
		Map<Long, InstituteDetail> instituteMap = new HashMap<Long, InstituteDetail>();
		
		for (Object[] row : rows) {
			InstituteDetail institute = new InstituteDetail();
			institute.setId((Long)row[0]);
			institute.setName((String)row[1]);
			institute.setActivityStatus((String)row[2]);
			institutes.add(institute);
			
			if (listCrit.includeStat()) {
				instituteMap.put(institute.getId(), institute);
			}
		}
		
		if (listCrit.includeStat()) {
			addInstituteStats(instituteMap);
		}
		
		return institutes;
	}

	@Override
	public Long getInstitutesCount(InstituteListCriteria listCrit) {
		Number count = ((Number)getInstituteListQuery(listCrit)
			.setProjection(Projections.rowCount())
			.uniqueResult());
		return count.longValue();
	}

	@Override
	@SuppressWarnings("unchecked")
	public List<Institute> getInstituteByNames(List<String> names) {
		return sessionFactory.getCurrentSession()
			.getNamedQuery(GET_INSTITUTES_BY_NAME)
			.setParameterList("names", names)
			.list();
	}
	
	@Override
	public Institute getInstituteByName(String name) {
		List<Institute> result = getInstituteByNames(Collections.singletonList(name));
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}
	
	private Criteria getInstituteListQuery(InstituteListCriteria crit) {
		Criteria query = sessionFactory.getCurrentSession()
			.createCriteria(Institute.class, "institute");

		return addSearchConditions(query, crit);
	}

	private Criteria addSearchConditions(Criteria query, InstituteListCriteria listCrit) {
		if (StringUtils.isNotBlank(listCrit.query())) {
			query.add(Restrictions.ilike("name", listCrit.query(), listCrit.matchMode()));
		}

		return query;
	}
	
	private void addProjectionFields(Criteria query) {
		query.setProjection(Projections.distinct(
			Projections.projectionList()
				.add(Projections.property("institute.id"), "id")
				.add(Projections.property("institute.name"), "name")
				.add(Projections.property("institute.activityStatus"), "activityStatus")
		));
	}
	
	@SuppressWarnings("unchecked")
	private void addInstituteStats(Map<Long, InstituteDetail> institutesMap) {
		if (institutesMap == null || institutesMap.isEmpty()) {
			return;
		}
		
		List<Object[]> stats = getSessionFactory().getCurrentSession()
			.getNamedQuery(GET_INSTITUTE_STATS)
			.setParameterList("instituteIds", institutesMap.keySet())
			.list();
		
		for (Object[] stat : stats) {
			InstituteDetail institute = institutesMap.get((Long)stat[0]);
			institute.setUsersCount(((Long)stat[1]).intValue());
		}
	}
	
	
	private static final String INSTITUTE_FQN = Institute.class.getName();
	
	private static final String GET_INSTITUTES_BY_NAME = INSTITUTE_FQN + ".getInstitutesByName";
	
	private static final String GET_INSTITUTE_STATS = INSTITUTE_FQN + ".getInstituteStats";
}
