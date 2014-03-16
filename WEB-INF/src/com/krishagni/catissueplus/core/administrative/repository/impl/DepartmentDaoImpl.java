package com.krishagni.catissueplus.core.administrative.repository.impl;

import java.util.List;

import org.hibernate.Query;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.repository.DaoFactory;
import com.krishagni.catissueplus.core.administrative.repository.DepartmentDao;
import com.krishagni.catissueplus.core.common.repository.AbstractDao;

public class DepartmentDaoImpl extends AbstractDao<User> implements DepartmentDao {
	
private DaoFactory daoFactory;
	
	public void setDaoFactory(DaoFactory daoFactory) {
		this.daoFactory = daoFactory;
	}
	
	@Override
	public Department getDepartment(String name) {
		String hql = "FROM "+ Department.class.getName() +" d WHERE name = :name";
		Query query = sessionFactory.getCurrentSession().createQuery(hql);
		query.setString("name", name);
		List<Department> rows = query.list();
		Department department = rows.get(0);
		return department; 
	}

}
