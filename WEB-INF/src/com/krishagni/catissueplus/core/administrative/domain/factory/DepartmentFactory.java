package com.krishagni.catissueplus.core.administrative.domain.factory;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.events.DepartmentDetails;


public interface DepartmentFactory {

	public Department createDepartment(DepartmentDetails details);

}
