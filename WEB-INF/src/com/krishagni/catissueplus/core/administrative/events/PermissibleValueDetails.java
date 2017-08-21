
package com.krishagni.catissueplus.core.administrative.events;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.krishagni.catissueplus.core.administrative.domain.PermissibleValue;

public class PermissibleValueDetails {

	private Long id;

	private Long parentId;
	
	private String parentValue;

	private String value;

	private String attribute;

	private String conceptCode;

	private Map<String, String> props = new HashMap<>();
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getParentId() {
		return parentId;
	}

	public void setParentId(Long parentId) {
		this.parentId = parentId;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
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

	public Map<String, String> getProps() {
		return props;
	}

	public void setProps(Map<String, String> props) {
		this.props = props;
	}

	//
	// For BO Template
	//
	public void setPropMap(List<Map<String, String>> propMap) {
		this.props = propMap.stream().collect(Collectors.toMap(p -> p.get("name"), p -> p.get("value")));
	}

	public static PermissibleValueDetails fromDomain(PermissibleValue permissibleValue) {
		PermissibleValueDetails details = new PermissibleValueDetails();
		details.setConceptCode(permissibleValue.getConceptCode());
		details.setId(permissibleValue.getId());
		details.setAttribute(permissibleValue.getAttribute());
		details.setValue(permissibleValue.getValue());
		if (permissibleValue.getParent() != null) {
			details.setParentId(permissibleValue.getParent().getId());
			details.setParentValue(permissibleValue.getParent().getValue());
		}
		
		if (permissibleValue.getProps() != null) {
			details.setProps(permissibleValue.getProps());;
		}
		
		return details;
	}
}
