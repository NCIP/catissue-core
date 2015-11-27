package com.krishagni.catissueplus.core.de.events;

import java.util.ArrayList;
import java.util.List;

public class FacetDetail {
	private String expr;

	private String caption;

	private List<Object> values = new ArrayList<Object>();

	public String getExpr() {
		return expr;
	}

	public void setExpr(String expr) {
		this.expr = expr;
	}

	public String getCaption() {
		return caption;
	}

	public void setCaption(String caption) {
		this.caption = caption;
	}

	public List<Object> getValues() {
		return values;
	}

	public void setValues(List<Object> values) {
		this.values = values;
	}
}
