
package com.krishagni.catissueplus.core.administrative.domain;

public class Biohazard {

	private Long id;

	private String name;

	private String comment;

	private String type;

	private String activityStatus;

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

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public void update(Biohazard biohazard) {
		this.setName(biohazard.getName());
		this.setType(biohazard.getType());
		this.setComment(biohazard.getComment());
		this.setActivityStatus(biohazard.getActivityStatus());
	}

}
