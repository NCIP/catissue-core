package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.BeanUtils;

import com.krishagni.catissueplus.core.de.domain.DeObject;
import com.krishagni.catissueplus.core.de.domain.DeObject.Attr;

public class ExtensionDetail {
	private Long id;
	
	private Long objectId;
	
	private Long formId;
	
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
	
	public Long getFormId() {
		return formId;
	}

	public void setFormId(Long formId) {
		this.formId = formId;
	}

	public List<AttrDetail> getAttrs() {
		return attrs;
	}

	public void setAttrs(List<AttrDetail> attrs) {
		this.attrs = attrs;
	}

	public void setAttrsMap(Map<String, Object> attrsMap) {
		attrs.clear();
		
		for (Map.Entry<String, Object> entry : attrsMap.entrySet()) {
			AttrDetail attr = new AttrDetail();
			attr.setName(entry.getKey()); 
			attr.setValue(entry.getValue());
			attrs.add(attr);
		}
	}
	
	public static ExtensionDetail from(DeObject extension) {
		return from(extension, true);
	}

	public static ExtensionDetail from(DeObject extension, boolean excludePhi) {
		if (extension == null || extension.getId() == null) {
			return null;
		}
		
		ExtensionDetail detail = new ExtensionDetail();
		detail.setId(extension.getId());
		detail.setObjectId(extension.getObjectId());
		detail.setFormId(extension.getFormId());
		detail.setAttrs(AttrDetail.from(extension.getAttrs(), excludePhi));	
		return detail;
	}
	
	public static class AttrDetail {
		private String name;
		
		private String udn;
		
		private String caption;
		
		private Object value;
		
		private String type;

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
		
		public String getType() {
			return type;
		}

		public void setType(String type) {
			this.type = type;
		}

		public static AttrDetail from(Attr attr, boolean excludePhi) {
			AttrDetail detail = new AttrDetail();
			BeanUtils.copyProperties(attr, detail);
			if (excludePhi && attr.isPhi()) {
				detail.setValue("###");
			}
			
			return detail;
		}
		
		public static List<AttrDetail> from(List<Attr> attrs, boolean excludePhi) {
			List<AttrDetail> result = new ArrayList<AttrDetail>();
			for (Attr attr: attrs) {
				result.add(from(attr, excludePhi));
			}
			
			return result;
		}
	}
}
