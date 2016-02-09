package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.UserSummary;

import edu.common.dynamicextensions.napi.ControlValue;

public class FormRecordSummary {
	private Long id;
	
	private Long fcId;
	
	private Long recordId;
	
	private UserSummary user;
	
	private Date updateTime;
	
	private List<FieldValue> fieldValues = new ArrayList<FieldValue>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getFcId() {
		return fcId;
	}

	public void setFcId(Long fcId) {
		this.fcId = fcId;
	}
	
	public Long getRecordId() {
		return recordId;
	}

	public void setRecordId(Long recordId) {
		this.recordId = recordId;
	}

	public UserSummary getUser() {
		return user;
	}

	public void setUser(UserSummary user) {
		this.user = user;
	}

	public Date getUpdateTime() {
		return updateTime;
	}

	public void setUpdateTime(Date updateTime) {
		this.updateTime = updateTime;
	}
	
	public List<FieldValue> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	public void addFieldValue(ControlValue cv) {
		fieldValues.add(FieldValue.from(cv));
	}
	
	public void addFieldValues(Collection<ControlValue> cvs) {
		for (ControlValue cv : cvs) {
			addFieldValue(cv);
		}
	}
}
