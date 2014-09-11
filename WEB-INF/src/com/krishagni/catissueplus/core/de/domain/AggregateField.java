package com.krishagni.catissueplus.core.de.domain;

public class AggregateField {
	private String name;
	
	private String aggFn;
	
	private String label;
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAggFn() {
		return aggFn;
	}

	public void setAggFn(String aggFn) {
		this.aggFn = aggFn;
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}				
}
