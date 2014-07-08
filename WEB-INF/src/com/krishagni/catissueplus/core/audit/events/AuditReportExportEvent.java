
package com.krishagni.catissueplus.core.audit.events;

import java.util.List;

public class AuditReportExportEvent {

	private String exportOn;

	private String dateFrom;

	private String dateTo;

	private String ExportBy;

	private List<ReportDetail> reportDetailsList;

	private String userList;

	private String objectTypes;

	private String operations;

	public String getUserList() {
		return userList;
	}

	public void setUserList(String userList) {
		this.userList = userList;
	}

	public String getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(String objectTypes) {
		this.objectTypes = objectTypes;
	}

	public String getOperations() {
		return operations;
	}

	public void setOperations(String operations) {
		this.operations = operations;
	}

	public List<ReportDetail> getReportDetailsList() {
		return reportDetailsList;
	}

	public void setReportDetailsList(List<ReportDetail> reportDetailsList) {
		this.reportDetailsList = reportDetailsList;
	}

	public String getExportOn() {
		return exportOn;
	}

	public void setExportOn(String exportOn) {
		this.exportOn = exportOn;
	}

	public String getDateFrom() {
		return dateFrom;
	}

	public void setDateFrom(String dateFrom) {
		this.dateFrom = dateFrom;
	}

	public String getDateTo() {
		return dateTo;
	}

	public void setDateTo(String dateTo) {
		this.dateTo = dateTo;
	}

	public String getExportBy() {
		return ExportBy;
	}

	public void setExportBy(String exportBy) {
		ExportBy = exportBy;
	}

}
