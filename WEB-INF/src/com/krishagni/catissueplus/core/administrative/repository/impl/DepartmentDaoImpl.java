
package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DepartmentDaoImpl extends AbstractDao<User> implements DepartmentDao {

	private DaoFactory daoFactory;

	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}

	@Override
	@SuppressWarnings(value = {"unchecked"})
	public Department getDepartment(String name) {
		Query query = sessionFactory.getCurrentSession().getNamedQuery(GET_DEPARTMENT_BY_NAME);
		query.setString("name", name);
		List<Department> results = query.list();
		return results.isEmpty() ? null : results.get(0);
	}

	private static final String FQN = Department.class.getName();

	private static final String GET_DEPARTMENT_BY_NAME = FQN + ".getDepartmentByName";

}
