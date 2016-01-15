package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;
import com.krishagni.catissueplus.core.common.util.AuthUtil;
import com.krishagni.catissueplus.core.common.util.Status;

public class SpecimenRequest extends BaseEntity {

	private CollectionProtocol cp;

	private User requestor;

	private Date dateOfRequest;

	private User processedBy;

	private Date dateOfProcessing;

	private Set<SpecimenRequestItem> items = new LinkedHashSet<SpecimenRequestItem>();

	private String activityStatus;

	private String comments;

	private transient int specimensCount;

	public CollectionProtocol getCp() {
		return cp;
	}

	public void setCp(CollectionProtocol cp) {
		this.cp = cp;
	}

	public User getRequestor() {
		return requestor;
	}

	public void setRequestor(User requestor) {
		this.requestor = requestor;
	}

	public Date getDateOfRequest() {
		return dateOfRequest;
	}

	public void setDateOfRequest(Date dateOfRequest) {
		this.dateOfRequest = dateOfRequest;
	}

	public User getProcessedBy() {
		return processedBy;
	}

	public void setProcessedBy(User processedBy) {
		this.processedBy = processedBy;
	}

	public Date getDateOfProcessing() {
		return dateOfProcessing;
	}

	public void setDateOfProcessing(Date dateOfProcessing) {
		this.dateOfProcessing = dateOfProcessing;
	}

	public Set<SpecimenRequestItem> getItems() {
		return items;
	}

	public void setItems(Set<SpecimenRequestItem> items) {
		this.items = items;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getSpecimensCount() {
		return specimensCount;
	}

	public void setSpecimensCount(int specimensCount) {
		this.specimensCount = specimensCount;
	}

	public Institute getInstitute() {
		return requestor.getInstitute();
	}

	public void closeIfFulfilled() {
		boolean anyPending = getItems().stream().anyMatch(SpecimenRequestItem::isPending);
		if (anyPending) {
			return;
		}

		close("Automatic closure of request");
	}

	public void close(String comments) {
		setProcessedBy(AuthUtil.getCurrentUser());
		setDateOfProcessing(Calendar.getInstance().getTime());
		setComments(comments);
		setActivityStatus(Status.ACTIVITY_STATUS_CLOSED.getStatus());
	}

	public void delete() {
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

	public boolean isClosed() {
		return Status.ACTIVITY_STATUS_CLOSED.getStatus().equals(getActivityStatus());
	}
}
