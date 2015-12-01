package com.krishagni.catissueplus.core.administrative.domain;

import java.util.Date;
import java.util.LinkedHashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.biospecimen.domain.BaseExtensionEntity;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.biospecimen.domain.Specimen;

public class SpecimenRequest extends BaseExtensionEntity {

	private static final String ENTITY_TYPE = "SpecimenRequest";

	private CollectionProtocol cp;

	private User requestor;

	private Date dateOfRequest;

	private User processedBy;

	private Date dateOfProcessing;

	private Set<Specimen> specimens = new LinkedHashSet<Specimen>();

	private String activityStatus;

	private String comments;

	private transient int specimensCount;

	@Override
	public String getEntityType() {
		return ENTITY_TYPE;
	}

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

	public Set<Specimen> getSpecimens() {
		return specimens;
	}

	public void setSpecimens(Set<Specimen> specimens) {
		this.specimens = specimens;
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
}
