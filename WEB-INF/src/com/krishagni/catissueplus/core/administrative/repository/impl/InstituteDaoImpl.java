package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.Criteria;
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
			.add(Restrictions.eq("activityStatus", Constants.ACTIVITY_STATUS_ACTIVE))
			.addOrder(Order.asc("name"))
			.setFirstResult(listCrit.startAt())
			.setMaxResults(listCrit.maxResults());
				
		if (StringUtils.isNotBlank(listCrit.query())) {
			query.add(Restrictions.ilike("name", listCrit.query()));
		}

		return query.list();
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Institute getInstituteByName(String name) {
		List<Institute> result = sessionFactory.getCurrentSession().createCriteria(Institute.class)
				.add(Restrictions.eq("name", name))
				.add(Restrictions.eq("activityStatus", Status.ACTIVITY_STATUS_ACTIVE.getStatus()))
				.list();
		return CollectionUtils.isEmpty(result) ? null : result.get(0);
	}

	@Override
	public Department getDepartment(Long id) {
		return (Department) sessionFactory.getCurrentSession().get(Department.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public Department getDeptByNameAndInstitute(String name, String instituteName) {
		List<Department> result = sessionFactory.getCurrentSession().createCriteria(Department.class)
				.add(Restrictions.eq("name", name))
				.createAlias("institute", "institute")
				.add(Restrictions.eq("institute.name", instituteName))
				.list();
		return CollectionUtils.isEmpty(result) ? null : result.get(0);		
	}
	
}
