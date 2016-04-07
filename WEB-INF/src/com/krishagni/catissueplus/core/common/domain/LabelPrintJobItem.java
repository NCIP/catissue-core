package com.krishagni.catissueplus.core.common.domain;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class LabelPrintJobItem extends BaseEntity {
	public enum Status {
		QUEUED,
		PRINTED,
		EXPIRED
	};
	
	private LabelPrintJob job;
	
	private String itemLabel;

	private int copies;
	
	private Date printDate;
	
	private String printerName;
	
	private Status status;
	
	private String labelType;
	
	private String data;

	public LabelPrintJob getJob() {
		return job;
	}

	public void setJob(LabelPrintJob job) {
		this.job = job;
	}

	public String getItemLabel() {
		return itemLabel;
	}

	public void setItemLabel(String itemLabel) {
		this.itemLabel = itemLabel;
	}

	public int getCopies() {
		return copies;
	}

	public void setCopies(int copies) {
		this.copies = copies;
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