
package com.krishagni.catissueplus.core.printer.printRule.domain;

public class SpecimenPrintRule {

	private Long id;

	private String name;

	private String specimenClass = "Any";

	private String specimenType = "Any";

	private String labelType;

	private String workstationIP = "Any";

	private String dataOnLabel;

	private String printerName;

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

	public String getDataOnLabel() {
		return dataOnLabel;
	}

	public void setDataOnLabel(String dataOnLabel) {
		this.dataOnLabel = dataOnLabel;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public void update(SpecimenPrintRule specimenPrintRule) {
		this.setDataOnLabel(specimenPrintRule.getDataOnLabel());
		this.setLabelType(specimenPrintRule.getLabelType());
		this.setName(specimenPrintRule.getName());
		this.setSpecimenClass(specimenPrintRule.getSpecimenClass());
		this.setSpecimenType(specimenPrintRule.getSpecimenType());
		this.setWorkstationIP(specimenPrintRule.getWorkstationIP());
		this.setPrinterName(specimenPrintRule.getPrinterName());
	}

}
