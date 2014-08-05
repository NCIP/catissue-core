
package com.krishagni.catissueplus.core.administrative.repository;

import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.common.repository.Dao;

public interface DepartmentDao extends Dao<Department> {

	Department getDepartment(Long departmentId);

	Department getDepartmentByName(String anyString);

	Boolean isUniqueDepartmentInInstitute(String name, String instName);

	List<Department> getAllDepartments(int maxResults);

}
