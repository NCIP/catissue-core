package com.krishagni.openspecimen.rde.events;

import java.util.List;

public class SpecimenPrintDetail {
	private String label;
	
	private Long reqId;
	
	private String visitName;
	
	private Boolean print = Boolean.TRUE;
	
	private List<SpecimenPrintDetail> specimensPool;
	
	private List<SpecimenPrintDetail> children;

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public Long getReqId() {
		return reqId;
	}

	public void setReqId(Long reqId) {
		this.reqId = reqId;
	}

	public String getVisitName() {
		return visitName;
	}

	public void setVisitName(String visitName) {
		this.visitName = visitName;
	}

	public Boolean getPrint() {
		return print;
	}

	public void setPrint(Boolean print) {
		this.print = print;
	}

	public List<SpecimenPrintDetail> getSpecimensPool() {
		return specimensPool;
	}

	public void setSpecimensPool(List<SpecimenPrintDetail> specimensPool) {
		this.specimensPool = specimensPool;
	}

	public List<SpecimenPrintDetail> getChildren() {
		return children;
	}

	public void setChildren(List<SpecimenPrintDetail> children) {
		this.children = children;
	}
}
