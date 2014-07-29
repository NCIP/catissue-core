
package com.krishagni.catissueplus.core.administrative.events;

import java.util.ArrayList;
import java.util.List;

import com.krishagni.catissueplus.core.common.events.RequestEvent;

public class ValidatePvEvent extends RequestEvent {

	private String attribute;

	private List<String> values = new ArrayList<String>();

	private String parentValue;

	public String getAttribute() {
		return attribute;
	}

	public void setAttribute(String attribute) {
		this.attribute = attribute;
	}

	public List<String> getValues() {
		return values;
	}

	public void setValues(List<String> values) {
		this.values = values;
	}

	public String getParentValue() {
		return parentValue;
	}

	public void setParentValue(String parentValue) {
		this.parentValue = parentValue;
	}

}
