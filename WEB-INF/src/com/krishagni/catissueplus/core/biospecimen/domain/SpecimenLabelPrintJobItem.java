package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;

public class SpecimenLabelPrintJobItem extends BaseEntity {
	public enum Status {
		QUEUED,
		PRINTED,
		EXPIRED
	};
	
	private SpecimenLabelPrintJob job;
	
	private Specimen specimen;
	
	private Date printDate;
	
	private String printerName;
	
	private Status status;
	
	private String labelType;
	
	private String data;

	public SpecimenLabelPrintJob getJob() {
		return job;
	}

	public void setJob(SpecimenLabelPrintJob job) {
		this.job = job;
	}

	public Specimen getSpecimen() {
		return specimen;
	}

	public void setSpecimen(Specimen specimen) {
		this.specimen = specimen;
	}

	public Date getPrintDate() {
		return printDate;
	}

	public void setPrintDate(Date printDate) {
		this.printDate = printDate;
	}

	public String getPrinterName() {
		return printerName;
	}

	public void setPrinterName(String printerName) {
		this.printerName = printerName;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public String getLabelType() {
		return labelType;
	}

	public void setLabelType(String labelType) {
		this.labelType = labelType;
	}

	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
	}
}
