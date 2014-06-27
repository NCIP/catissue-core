
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.administrative.domain.Biohazard;

public class BiohazardDetails {

	private Long id;

	private String name;

	private String type;

	private String comment;

	private String activityStatus;

	private List<String> modifiedAttributes = new ArrayList<String>();

	public String getName() {
		return name;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getComment() {
		return comment;
	}

	public void setComment(String comment) {
		this.comment = comment;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public boolean isBiohazardNameModified() {
		return modifiedAttributes.contains("name");
	}

	public boolean isCommentModified() {
		return modifiedAttributes.contains("comment");
	}

	public boolean isBiohazardTypeModified() {
		return modifiedAttributes.contains("type");
	}

	public boolean isActivityStatusModified() {
		return modifiedAttributes.contains("activityStatus");
	}

	public static BiohazardDetails fromDomain(Biohazard biohazard) {
		BiohazardDetails biohazardDto = new BiohazardDetails();
		biohazardDto.setId(biohazard.getId());
		biohazardDto.setName(biohazard.getName());
		biohazardDto.setType(biohazard.getType());
		biohazardDto.setComment(biohazard.getComment());
		biohazardDto.setActivityStatus(biohazard.getActivityStatus());
		return biohazardDto;

	}

}
