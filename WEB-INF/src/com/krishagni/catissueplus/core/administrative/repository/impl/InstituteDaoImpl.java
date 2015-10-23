package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.events.InstituteSummary;
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
	public List<InstituteSummary> getInstitutes(InstituteListCriteria listCrit) {
		Criteria query = sessionFactory.getCurrentSession()
			.createCriteria(Institute.class, "institute")
			.addOrder(Order.asc("institute.name"))
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults());
		
		addSearchConditions(query, listCrit);
		addProjectionFields(query);
		
		List<Object[]> rows = query.list();
		List<InstituteSummary> institutes = new ArrayList<InstituteSummary>();
		Map<Long, InstituteSummary> instituteMap = new HashMap<Long, InstituteSummary>();
		
		for (Object[] row : rows) {
			InstituteSummary institute = new InstituteSummary();
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
	@SuppressWarnings("unchecked")
	public List<Institute> getInstituteByNames(List<String> names) {
		return sessionFactory.getCurrentSession()
				.createCriteria(Institute.class)
				.add(Restrictions.in("name", names))
				.list();
	}
	
	@Override
	public Institute getInstituteByName(String name) {
		List<Institute> result = getInstituteByNames(Collections.singletonList(name));
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Department getDepartment(Long id, Long instituteId ) {
		List<Department> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_DEPARTMENT)
				.setLong("id", id)
				.setLong("instituteId", instituteId)
				.list();
		
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}
	
	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Department getDeptByNameAndInstitute(String deptName, String instituteName) {
		List<Department> results  = sessionFactory.getCurrentSession()
			.getNamedQuery(GET_DEPT_BY_NAME_AND_INSTITUTE)
			.setString("deptName", deptName)
			.setString("instituteName", instituteName)
			.list();
		
		return results.isEmpty() ? null : results.get(0);
	}

	private void addSearchConditions(Criteria query, InstituteListCriteria listCrit) {
		if (StringUtils.isBlank(listCrit.query())) {
			return;
		}
		
		MatchMode matchMode = listCrit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;  
		query.add(Restrictions.ilike("name", listCrit.query(), matchMode));		
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
	private void addInstituteStats(Map<Long, InstituteSummary> institutesMap) {
		if (institutesMap == null || institutesMap.isEmpty()) {
			return;
		}
		
		List<Object[]> stats = getSessionFactory().getCurrentSession()
			.getNamedQuery(GET_INSTITUTE_STATS)
			.setParameterList("instituteIds", institutesMap.keySet())
			.list();
		
		for (Object[] stat : stats) {
			InstituteSummary institute = institutesMap.get((Long)stat[0]);
			institute.setDepartmentsCount(((Long)stat[1]).intValue());
			institute.setUsersCount(((Long)stat[2]).intValue());
		}
	}
	
	
	private static final String INSTITUTE_FQN = Institute.class.getName();
	
	private static final String DEPARTMENT_FQN = Department.class.getName();
	
	private static final String GET_INSTITUTE_STATS = INSTITUTE_FQN + ".getInstituteStats";
	
	private static final String GET_DEPARTMENT = DEPARTMENT_FQN + ".getDepartment";
	
	private static final String GET_DEPT_BY_NAME_AND_INSTITUTE = DEPARTMENT_FQN + ".getDeptByNameAndInstitute";	
}
