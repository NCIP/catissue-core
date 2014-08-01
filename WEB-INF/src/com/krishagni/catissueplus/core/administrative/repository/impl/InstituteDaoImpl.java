package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Institute;
import com.krishagni.catissueplus.core.administrative.repository.InstituteDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class InstituteDaoImpl extends AbstractDao<Institute> implements InstituteDao {

	@Override
	@SuppressWarnings("unchecked")
	public Institute getInstituteByName(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_INSTITUTE_BY_NAME);
		query.setString("name", name);
		List<Institute> instituteList = query.list();
		return !instituteList.isEmpty() ? instituteList.get(0) : null;
	}

	@Override
	public Institute getInstitute(Long id) {
		return (Institute) sessionFactory.getCurrentSession().get(Institute.class, id);
	}
	
	@Override
	@SuppressWarnings("unchecked")
	public List<Institute> getAllInstitutes(int maxResults) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_INSTITUTES);
		query.setMaxResults(maxResults);
		return query.list();
	}
	
	private static final String FQN = Institute.class.getName();

	private static final String GET_INSTITUTE_BY_NAME = FQN + ".getInstituteByName";
	
	private static final String GET_INSTITUTES = FQN + ".getInstitutes";

}
