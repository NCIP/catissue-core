
package com.krishagni.catissueplus.core.administrative.domain;

public class PermissibleValue {

	private Long id;

	private PermissibleValue parent;

	private String value;

	private String attribute;

	private String conceptCode;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public PermissibleValue getParent() {
		return parent;
	}

	public void setParent(PermissibleValue parent) {
		this.parent = parent;
	}

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

	public void update(PermissibleValue permissibleValue) {
		setConceptCode(permissibleValue.getConceptCode());
		setAttribute(permissibleValue.getAttribute());
		setParent(permissibleValue.getParent());
		setValue(permissibleValue.getValue());
	}

}
