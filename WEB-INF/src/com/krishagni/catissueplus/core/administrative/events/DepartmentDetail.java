
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;

public class DepartmentDetail {

	private Long id;

	private String name;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public static DepartmentDetail from(Department department) {
		DepartmentDetail details = new DepartmentDetail();
		details.setId(department.getId());
		details.setName(department.getName());
		return details;
	}
	
	public static List<DepartmentDetail> from(Set<Department> departments) {
		List<DepartmentDetail> departmentsDetails = new ArrayList<DepartmentDetail>();
		for(Department department : departments) {
			departmentsDetails.add(from(department));
		}
		return departmentsDetails;
	}

}