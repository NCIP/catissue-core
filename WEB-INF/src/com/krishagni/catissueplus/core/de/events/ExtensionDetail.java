package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.domain.DeObject.Attr;

public class ExtensionDetail {
	private Long id;
	
	private Long objectId;
	
	private List<AttrDetail> attrs = new ArrayList<AttrDetail>();

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

	public List<AttrDetail> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<AttrDetail> attrs) {
		this.attrs = attrs;
	}

	public static ExtensionDetail from(DeObject extension) {
		ExtensionDetail detail = new ExtensionDetail();
		detail.setId(extension.getId());
		detail.setObjectId(extension.getObjectId()); 
		detail.setAttrs(AttrDetail.from(extension.getAttrs()));		
		return detail;
	}
	
	public static class AttrDetail {
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
		
		public static AttrDetail from(Attr attr) {
			AttrDetail detail = new AttrDetail();
			BeanUtils.copyProperties(attr, detail);
			return detail;
		}
		
		public static List<AttrDetail> from(List<Attr> attrs) {
			List<AttrDetail> result = new ArrayList<AttrDetail>();
			for (Attr attr: attrs) {
				result.add(from(attr));
			}
			
			return result;
		}
	}
}
