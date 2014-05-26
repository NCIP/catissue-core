
package com.krishagni.catissueplus.core.administrative.domain;

public class Institute {

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

	public void update(Institute institute) {
		this.setName(institute.getName());
	}

}
