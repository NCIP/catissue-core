
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Department;

public class DepartmentDetail implements Comparable<DepartmentDetail> {

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

	@Override
	public int compareTo(DepartmentDetail o) {
		return this.name.compareTo(o.name);
	}

	public static DepartmentDetail from(Department department) {
		DepartmentDetail result = new DepartmentDetail();
		result.setId(department.getId());
		result.setName(department.getName());
		return result;
	}

	public static List<DepartmentDetail> from(Set<Department> departments) {
		List<DepartmentDetail> result = new ArrayList<DepartmentDetail>();
		for(Department department : departments) {
			result.add(from(department));
		}

		Collections.sort(result);
		return result;
	}
}
