
package com.krishagni.catissueplus.core.administrative.domain;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class PermissibleValue extends BaseEntity {
	private String value;

	private String attribute;

	private String conceptCode;

	private PermissibleValue parent;
	
	private Long sortOrder;
	
	private Set<PermissibleValue> children = new HashSet<PermissibleValue>();
	
	private Map<String, String> props = new HashMap<String, String>();

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public String getConceptCode() {
		return conceptCode;
	}

	public void setConceptCode(String conceptCode) {
		this.conceptCode = conceptCode;
	}

	public PermissibleValue getParent() {
		return parent;
	}

	public void setParent(PermissibleValue parent) {
		this.parent = parent;
	}
	
	public Long getSortOrder() {
		return sortOrder;
	}

	public void setSortOrder(Long sortOrder) {
		this.sortOrder = sortOrder;
	}

	public Set<PermissibleValue> getChildren() {
		return children;
	}

	public void setChildren(Set<PermissibleValue> children) {
		this.children = children;
	}

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	public void update(PermissibleValue other) {
		setConceptCode(other.getConceptCode());
		setAttribute(other.getAttribute());
		setParent(other.getParent());
		setValue(other.getValue());
		setSortOrder(other.getSortOrder());
		setProps(other);
	}
	
	private void setProps(PermissibleValue other) {
		getProps().keySet().retainAll(other.getProps().keySet());
		getProps().putAll(other.getProps());
	}
}
