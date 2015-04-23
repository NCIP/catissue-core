package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.administrative.repository.InstituteListCriteria;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;
import com.krishagni.catissueplus.core.common.util.Status;

import edu.wustl.catissuecore.util.global.Constants;

public class InstituteDaoImpl extends AbstractDao<Institute> implements InstituteDao {
	
	@Override
	public Class<?> getType() {
		return Institute.class;
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Institute> getInstitutes(InstituteListCriteria listCrit) {
		Criteria query = sessionFactory.getCurrentSession().createCriteria(Institute.class)
			.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
			.addOrder(Order.asc("name"))
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults());
				
		if (StringUtils.isNotBlank(listCrit.query())) {
			MatchMode matchMode = listCrit.exactMatch() ? MatchMode.EXACT : MatchMode.ANYWHERE;  
			query.add(Restrictions.ilike("name", listCrit.query(), matchMode));
		}

		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Institute getInstituteByName(String name) {
		List<Institute> result = sessionFactory.getCurrentSession()
				.getNamedQuery(GET_INSTITUTE_BY_NAME)
				.setString("name", name)
				.list();
		
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
	
	private static final String INSTITUTE_FQN = Institute.class.getName();
	
	private static final String DEPARTMENT_FQN = Department.class.getName();
	
	private static final String GET_INSTITUTE_BY_NAME = INSTITUTE_FQN + ".getInstituteByName";
	
	private static final String GET_DEPARTMENT = DEPARTMENT_FQN + ".getDepartment";
	
	private static final String GET_DEPT_BY_NAME_AND_INSTITUTE = DEPARTMENT_FQN + ".getDeptByNameAndInstitute";
	
}
