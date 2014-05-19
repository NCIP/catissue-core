
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;
import com.krishagni.catissueplus.core.administrative.domain.Institute;

public class DepartmentDetails {

	private Long id;

	private String name;

	private List<String> instituteNames;

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

	public List<String> getInstituteNames() {
		return instituteNames;
	}

	public void setInstituteNames(List<String> instituteNames) {
		this.instituteNames = instituteNames;
	}

	public static DepartmentDetails fromDomain(Department department) {
		DepartmentDetails details = new DepartmentDetails();
		details.setId(department.getId());
		details.setName(department.getName());
		details.setInstituteNames(getIntituteNames(department.getInstitutes()));
		return details;
	}

	private static List<String> getIntituteNames(Set<Institute> institutes) {
		List<String> instNames = new ArrayList<String>();
		for (Institute institute : institutes) {
			instNames.add(institute.getName());
		}
		return instNames;
	}

}
