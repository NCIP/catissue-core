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
		FieldValue fv = new FieldValue();
		fv.setName(cv.getControl().getUserDefinedName());
		fv.setCaption(cv.getControl().getCaption());
		fv.setValue(cv.getValue());
		
		fieldValues.add(fv);
	}
	
	public void addFieldValues(Collection<ControlValue> cvs) {
		for (ControlValue cv : cvs) {
			addFieldValue(cv);
		}
	}

	public static class FieldValue {
		private String name;
		
		private String caption;
		
		private Object value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getCaption() {
			return caption;
		}

		public void setCaption(String caption) {
			this.caption = caption;
		}

		public Object getValue() {
			return value;
		}

		public void setValue(Object value) {
			this.value = value;
		}
	}
}
