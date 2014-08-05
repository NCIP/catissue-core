
package com.krishagni.catissueplus.core.printer.printRule.domain;

import java.util.HashSet;
import java.util.Set;

public class SpecimenPrintRule {

	private Long id;

	private String name;

	private String specimenClass = "Any";

	private String specimenType = "Any";

	private String labelType;

	private String workstationIP = "Any";

	private Set<String> dataOnLabel = new HashSet<String>();

	private String printerName;

	private String cpShortTitle;

	private String loginName;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSpecimenClass() {
		return specimenClass;
	}

	public void setSpecimenClass(String specimenClass) {
		this.specimenClass = specimenClass;
	}

	public String getSpecimenType() {
		return specimenType;
	}

	public void setSpecimenType(String specimenType) {
		this.specimenType = specimenType;
	}

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public String getWorkstationIP() {
		return workstationIP;
	}

	public void setWorkstationIP(String workstationIP) {
		this.workstationIP = workstationIP;
	}

	public Set<String> getDataOnLabel() {
		return dataOnLabel;
	}

	public void setDataOnLabel(Set<String> dataOnLabel) {
		this.dataOnLabel = dataOnLabel;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public void update(SpecimenPrintRule specimenPrintRule) {
		this.setDataOnLabel(specimenPrintRule.getDataOnLabel());
		this.setLabelType(specimenPrintRule.getLabelType());
		this.setName(specimenPrintRule.getName());
		this.setSpecimenClass(specimenPrintRule.getSpecimenClass());
		this.setSpecimenType(specimenPrintRule.getSpecimenType());
		this.setWorkstationIP(specimenPrintRule.getWorkstationIP());
		this.setPrinterName(specimenPrintRule.getPrinterName());
		this.setCpShortTitle(specimenPrintRule.getCpShortTitle());
		this.setLoginName(specimenPrintRule.getLoginName());
		this.setCpShortTitle(specimenPrintRule.getCpShortTitle());
		this.setLoginName(specimenPrintRule.getLoginName());
	}

}
