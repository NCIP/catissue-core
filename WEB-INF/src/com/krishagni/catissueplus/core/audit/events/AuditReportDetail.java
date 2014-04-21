
package com.krishagni.catissueplus.core.audit.events;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class AuditReportDetail {

	private List<Long> userIds = new ArrayList<Long>();

	private List<String> objectTypes = new ArrayList<String>();

	private List<String> operations = new ArrayList<String>();

	private Date startDate;

	private Date endDate;

	public List<Long> getUserIds() {
		return userIds;
	}

	public void setUserIds(List<Long> userIds) {
		this.userIds = userIds;
	}

	public List<String> getObjectTypes() {
		return objectTypes;
	}

	public void setObjectTypes(List<String> objectTypes) {
		this.objectTypes = objectTypes;
	}

	public List<String> getOperations() {
		return operations;
	}

	public void setOperations(List<String> operations) {
		this.operations = operations;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

}
