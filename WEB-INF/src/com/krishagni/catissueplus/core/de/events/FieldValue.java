package com.krishagni.catissueplus.core.de.events;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import edu.common.dynamicextensions.napi.ControlValue;

public class FieldValue {
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

	public static FieldValue from(ControlValue cv) {
		FieldValue fv = new FieldValue();
		fv.setName(cv.getControl().getUserDefinedName());
		fv.setCaption(cv.getControl().getCaption());
		fv.setValue(cv.getValue());
		return fv;
	}

	public static List<FieldValue> from(Collection<ControlValue> cvs) {
		return cvs.stream().map(FieldValue::from).collect(Collectors.toList());
	}
}
