package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DepartmentDao extends Dao<Department> {

	Department getDepartment(Long departmentId);

	Department getDepartmentByName(String anyString);
}
