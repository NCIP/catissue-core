package com.krishagni.catissueplus.core.importer.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ImportJob extends BaseEntity {
	public static enum Status {
		COMPLETED,
		FAILED,
		IN_PROGRESS,
		STOPPED,
		TXN_SIZE_EXCEEDED
	}
	
	public static enum Type {
		CREATE,
		UPDATE
	}
	
	public static enum CsvType {
		SINGLE_ROW_PER_OBJ,
		MULTIPLE_ROWS_PER_OBJ
	}
	
	private String name; 
	
	private Type type;
	
	private CsvType csvtype;

	private String dateFormat;

	private String timeFormat;

	private volatile Status status;
	
	private Long totalRecords;
	
	private Long failedRecords;
	
	private User createdBy;
	
	private Date creationTime;
	
	private Date endTime;

	private transient volatile  boolean stopRunning;
	
	private Map<String, String> params = new HashMap<>();
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Type getType() {
		return type;
	}

	public void setType(Type type) {
		this.type = type;
	}

	public CsvType getCsvtype() {
		return csvtype;
	}

	public void setCsvtype(CsvType csvtype) {
		this.csvtype = csvtype;
	}

	public String getDateFormat() {
		return dateFormat;
	}

	public void setDateFormat(String dateFormat) {
		this.dateFormat = dateFormat;
	}

	public String getTimeFormat() {
		return timeFormat;
	}

	public void setTimeFormat(String timeFormat) {
		this.timeFormat = timeFormat;
	}

	public Status getStatus() {
		return status;
	}

	public void setStatus(Status status) {
		this.status = status;
	}

	public Long getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(Long totalRecords) {
		this.totalRecords = totalRecords;
	}

	public Long getFailedRecords() {
		return failedRecords;
	}

	public void setFailedRecords(Long failedRecords) {
		this.failedRecords = failedRecords;
	}

	public User getCreatedBy() {
		return createdBy;
	}

	public void setCreatedBy(User createdBy) {
		this.createdBy = createdBy;
	}

	public Date getCreationTime() {
		return creationTime;
	}

	public void setCreationTime(Date creationTime) {
		this.creationTime = creationTime;
	}

	public Date getEndTime() {
		return endTime;
	}

	public void setEndTime(Date endTime) {
		this.endTime = endTime;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public boolean isAskedToStop() {
		return stopRunning;
	}

	public void stop() {
		this.stopRunning = true;
	}

	public boolean isInProgress() {
		return getStatus() == Status.IN_PROGRESS;
	}

	public boolean isStopped() {
		return getStatus() == Status.STOPPED;
	}

	public boolean isFailed() {
		return getStatus() == Status.FAILED;
	}
}
