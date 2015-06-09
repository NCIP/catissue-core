package com.krishagni.catissueplus.core.common;

import java.util.HashSet;
import java.util.Set;

public class AttributeModifiedSupport {
	private Set<String> modifiedAttrs = new HashSet<String>();
	
	public void attrModified(String attr) {
		modifiedAttrs.add(attr);
	}
	
	public boolean isAttrModified(String attr) {
		return modifiedAttrs.contains(attr);
	}

	public Set<String> getModifiedAttrs() {
		return modifiedAttrs;
	}

	public void setModifiedAttrs(Set<String> modifiedAttrs) {
		this.modifiedAttrs = modifiedAttrs;
	}
	
}
