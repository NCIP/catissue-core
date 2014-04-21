package com.krishagni.catissueplus.core.administrative.repository;

import com.krishagni.catissueplus.core.administrative.domain.Department;

public interface DepartmentDao {

	Department getDepartment(String name);
}
