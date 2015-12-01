package com.krishagni.catissueplus.core.importer.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringUtils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;

public class ImportJob extends BaseEntity {
	public static enum Status {
		COMPLETED,
		FAILED,
		IN_PROGRESS
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
	
	private Status status;
	
	private Long totalRecords;
	
	private Long failedRecords;
	
	private User createdBy;
	
	private Date creationTime;
	
	private Date endTime;
	
	private Map<String, Object> params = new HashMap<String, Object>();
	
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

	public Map<String, Object> getParams() {
		return params;
	}

	public void setParams(Map<String, Object> params) {
		this.params = params;
	}
	
	public String getParamsStr() {
		try {
			return new ObjectMapper().writeValueAsString(params);
		} catch (Exception e) {
			throw new RuntimeException("Error converting params map to string", e);
		}		
	}
	
	@SuppressWarnings("unchecked")
	public void setParamsStr(String paramsStr) {
		try {
			if (StringUtils.isNotBlank(paramsStr)) {
				this.params = (Map<String, Object>)new ObjectMapper().readValue(paramsStr, HashMap.class);
			}			
		} catch (Exception e) {
			throw new RuntimeException("Error converting params str to map", e);
		}
	}
}
