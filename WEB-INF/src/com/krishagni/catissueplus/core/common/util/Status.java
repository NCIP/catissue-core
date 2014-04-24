
package com.krishagni.catissueplus.core.common.util;

public final class Status {

	private String statusName;

	private int statusNo;

	private Status(String statusName, int statusNo) {
		this.statusName = statusName;
		this.statusNo = statusNo;
	}

	public static final Status ACTIVITY_STATUS_ACTIVE = new Status("Active", 1);

	public static final Status ACTIVITY_STATUS_DISABLED = new Status("Disabled", 2);

	public static final Status ACTIVITY_STATUS_CLOSED = new Status("Closed", 3);

	public static final Status ACTIVITY_STATUS = new Status("activityStatus", 4);
	
	public static final Status SPECIMEN_COLLECTION_STATUS_COLLECTED = new Status("Collected", 5);
	
	public static final Status SPECIMEN_COLLECTION_STATUS_PENDING = new Status("Pending", 6);
	
	public static final Status SCG_COLLECTION_STATUS_COMPLETED = new Status("Completed", 7);
	public static final Status SCG_COLLECTION_STATUS_PENDING = new Status("Pending", 8);

	public String getStatus() {
		return this.statusName;
	}

	public int getStatusNo() {
		return statusNo;
	}
}
