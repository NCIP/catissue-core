
package com.krishagni.catissueplus.core.printer.printRule.events;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.krishagni.catissueplus.core.printer.printRule.domain.SpecimenPrintRule;

public class SpecimenPrintRuleDetails {

	private Long id;

	private String name;

	private String specimenClass = "";

	private String specimenType = "";

	private Set<String> labelType;

	private String workstationIP;

	private Set<String> dataOnLabel = new HashSet<String>();

	private String printerName;

	private List<String> modifiedAttributes = new ArrayList<String>();

	public boolean isNameModified() {
		return modifiedAttributes.contains("name");
	}

	public boolean isSpecimenClassModified() {
		return modifiedAttributes.contains("specimenClass");
	}

	public boolean isSpecimenTypeModified() {
		return modifiedAttributes.contains("specimenType");
	}

	public boolean isLabelTypeModified() {
		return modifiedAttributes.contains("labelType");
	}

	public boolean isWorkstationIPModified() {
		return modifiedAttributes.contains("workstationIP");
	}

	public boolean isDataOnLabelModified() {
		return modifiedAttributes.contains("dataOnLabel");
	}

	public boolean isPrinterNameModified() {
		return modifiedAttributes.contains("printerName");
	}

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

	public Set<String> getLabelType() {
		return labelType;
	}

	public void setLabelType(Set<String> labelType) {
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

	public List<String> getModifiedAttributes() {
		return modifiedAttributes;
	}

	public void setModifiedAttributes(List<String> modifiedAttributes) {
		this.modifiedAttributes = modifiedAttributes;
	}

	public static SpecimenPrintRuleDetails fromDomain(SpecimenPrintRule specimenPrintRule) {
		SpecimenPrintRuleDetails details = new SpecimenPrintRuleDetails();
		details.setId(specimenPrintRule.getId());
		details.setLabelType(populateLabelType(specimenPrintRule.getLabelType()));
		details.setName(specimenPrintRule.getName());
		details.setDataOnLabel(populateDataOnLabels(specimenPrintRule.getDataOnLabel()));
		details.setSpecimenClass(specimenPrintRule.getSpecimenClass());
		details.setSpecimenType(specimenPrintRule.getSpecimenType());
		details.setWorkstationIP(specimenPrintRule.getWorkstationIP());
		details.setPrinterName(specimenPrintRule.getPrinterName());
		return details;
	}

	private static Set<String> populateLabelType(String labelType) {
		String[] labelTypes = labelType.split("\\,");
		Set<String> labelTypeSet = new HashSet<String>();
		for (String label : labelTypes) {
			labelTypeSet.add(label);
		}
		return labelTypeSet;
	}

	private static Set<String> populateDataOnLabels(String dataOnLabel) {
		String[] dataOnLabels = dataOnLabel.split("\\,");
		Set<String> dataOnLabelsSet = new HashSet<String>();
		for (String label : dataOnLabels) {
			dataOnLabelsSet.add(label);
		}
		return dataOnLabelsSet;
	}

}
