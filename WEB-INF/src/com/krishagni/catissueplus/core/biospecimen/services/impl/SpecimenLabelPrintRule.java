package com.krishagni.catissueplus.core.biospecimen.services.impl;

import java.util.ArrayList;
import java.util.List;

public class SpecimenLabelPrintRule {
	private String specimenClass;
	
	private String specimenType;
	
	private String labelType;
	
	private String subnetMask;
	
	private String printerName;
	
	private List<String> labelDataItems = new ArrayList<String>();

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

	public String getSubnetMask() {
		return subnetMask;
	}

	public void setSubnetMask(String subnetMask) {
		this.subnetMask = subnetMask;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public List<String> getLabelDataItems() {
		return labelDataItems;
	}

	public void setLabelDataItems(List<String> labelDataItems) {
		this.labelDataItems = labelDataItems;
	}

}
