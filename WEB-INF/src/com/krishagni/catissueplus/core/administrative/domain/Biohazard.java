
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.factory.BiohazardErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.errors.CatissueException;
import com.krishagni.catissueplus.core.common.util.Status;

public class Biohazard {

	private Long id;

	private String name;

	private String comment;

	private String type;

	private String activityStatus;

	private Set<Specimen> specimenCollection = new HashSet<Specimen>();

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

	public Set<Specimen> getSpecimenCollection() {
		return specimenCollection;
	}

	public void setSpecimenCollection(Set<Specimen> specimenCollection) {
		this.specimenCollection = specimenCollection;
	}

	public void update(Biohazard biohazard) {
		this.setName(biohazard.getName());
		this.setType(biohazard.getType());
		this.setComment(biohazard.getComment());
		this.setActivityStatus(biohazard.getActivityStatus());
	}

	public void delete() {
		if (!this.getSpecimenCollection().isEmpty()) {
			throw new CatissueException(BiohazardErrorCode.ACTIVE_CHILDREN_FOUND);
		}
		else {
			this.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		}

	}

}
