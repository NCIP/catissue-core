package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.ExtensionForm.FieldValue;
import com.krishagni.catissueplus.core.biospecimen.domain.SiteExtension;

public class ExtensionDetail {
	private Long id;
	
	private Long objectId;
	
	private List<FieldValueDetail> fieldValues = new ArrayList<FieldValueDetail>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}
	
	public Long getObjectId() {
		return objectId;
	}

	public void setObjectId(Long objectId) {
		this.objectId = objectId;
	}

	public List<FieldValueDetail> getFieldValues() {
		return fieldValues;
	}

	public void setFieldValues(List<FieldValueDetail> fieldValues) {
		this.fieldValues = fieldValues;
	}

	public static ExtensionDetail from(SiteExtension extension) {
		ExtensionDetail detail = new ExtensionDetail();
		detail.setId(extension.getId());
		detail.setObjectId(extension.getObjectId()); 
		detail.setFieldValues(FieldValueDetail.from(extension.getFieldValues()));
		
		return detail;
	}
	
	public static class FieldValueDetail {
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
		
		public static FieldValueDetail from(FieldValue fieldvalue) {
			FieldValueDetail detail = new FieldValueDetail();
			BeanUtils.copyProperties(fieldvalue, detail);
			
			return detail;
		}
		
		public static List<FieldValueDetail> from(List<FieldValue> fields) {
			List<FieldValueDetail> result = new ArrayList<FieldValueDetail>();
			for (FieldValue fv: fields) {
				result.add(from(fv));
			}
			
			return result;
		}
	}
}
