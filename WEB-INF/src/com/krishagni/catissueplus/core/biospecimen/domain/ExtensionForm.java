package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.krishagni.catissueplus.core.de.domain.DeObject;

import edu.common.dynamicextensions.napi.ControlValue;
import edu.common.dynamicextensions.napi.FormData;

public abstract class ExtensionForm extends DeObject {
	private List<FieldValue> fieldValues = new ArrayList<FieldValue>();
	
	public List<FieldValue> getFieldValues() {
		loadRecordIfNotLoaded();
		return fieldValues;
	}

	public void setFieldValues(List<FieldValue> fieldValues) {
		this.fieldValues = fieldValues;
	}
	
	public ExtensionForm() {
		super(false);
	}
	
	protected void loadRecordIfNotLoaded() {
		if (isRecordLoaded()) {
			return;
		}
		
		setRecordLoaded(true);
		
		FormData formData = getFormData();		
		if (formData == null) {
			return;
		}
		
		for (ControlValue cv : formData.getFieldValues()) {
			FieldValue fv = new FieldValue();
			fv.setName(cv.getControl().getName());
			fv.setUdn(cv.getControl().getUserDefinedName());
			fv.setCaption(cv.getControl().getCaption());
			fv.setValue(cv.getValue());
			fieldValues.add(fv);
		}
	}
	
	@Override
	public Long getCpId() {
		return -1L;
	}

	@Override
	public Map<String, Object> getAttrValues() {
		Map<String, Object> vals = new HashMap<String, Object>();
		for (FieldValue fv: fieldValues) {
			vals.put(fv.getName(), fv.getValue());
		}
		
		return vals;
	}

	@Override
	public void setAttrValues(Map<String, Object> attrValues) {
		// TODO Auto-generated method stub
	}

	public static class FieldValue {
		private String name;
		
		private String udn;
		
		private String caption;
		
		private Object value;

		public String getName() {
			return name;
		}

		public void setName(String name) {
			this.name = name;
		}

		public String getUdn() {
			return udn;
		}

		public void setUdn(String udn) {
			this.udn = udn;
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
