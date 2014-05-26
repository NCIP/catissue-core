
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.ConsentTierResponse;
import com.krishagni.catissueplus.core.common.SetUpdater;

public class Department {

	private Long id;

	private String name;

	private Set<Institute> institutes = new HashSet<Institute>();

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

	public Set<Institute> getInstitutes() {
		return institutes;
	}

	public void setInstitutes(Set<Institute> institutes) {
		this.institutes = institutes;
	}

	public void update(Department department) {
		this.setName(department.getName());
		setAllInstitutes(department.getInstitutes());
	}

	private void setAllInstitutes(Set<Institute> instituteCollection) {
		SetUpdater.<Institute> newInstance().update(this.getInstitutes(), instituteCollection);
	}
}
