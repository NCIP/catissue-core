
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DepartmentDaoImpl extends AbstractDao<Department> implements DepartmentDao {

	private static final String FQN = Department.class.getName();

	private static final String GET_DEPARTMENT_BY_NAME = FQN + ".getDepartmentByName";

	private static final String GET_DEPARTMENT_BY_ID = FQN + ".getDepartmentById";

	private static final String GET_DEPARTMENT_BY_NAME_AND_INST = FQN + ".getDepartmentByNameAndInst";

	private static final String GET_ALL_DEPARTMENTS = FQN + ".getAllDepartments";

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Department getDepartment(Long id) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DEPARTMENT_BY_ID);
		query.setLong("id", id);
		List<Department> results = query.list();
		return results.isEmpty() ? null : results.get(0);
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Department getDepartmentByName(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DEPARTMENT_BY_NAME);
		query.setString("name", name);
		List<Department> results = query.list();
		return results.isEmpty() ? null : results.get(0);
	}

	@Override
	public Boolean isUniqueDepartmentInInstitute(String name, String instName) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DEPARTMENT_BY_NAME_AND_INST);
		query.setString("name", name);
		query.setString("instName", instName);
		List<Department> results = query.list();
		return results.isEmpty() ? true : false;
	}

	@SuppressWarnings("unchecked")
	@Override
	public List<Department> getAllDepartments(int maxResults) {
		return sessionFactory.getCurrentSession().getNamedQuery(GET_ALL_DEPARTMENTS).list();
	}

}
