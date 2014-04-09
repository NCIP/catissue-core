
package com.krishagni.catissueplus.core.notification.domain;

import com.krishagni.catissueplus.core.audit.domain.Audit;

public class ExtAppNotificationStatus {

	private Long id;

	private Audit audit;

	private ExternalApplication externalApplication;

	private String status;

	private String comments;

	private int noOfAttempts;

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Audit getAudit() {
		return audit;
	}

	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	public ExternalApplication getExternalApplication() {
		return externalApplication;
	}

	public void setExternalApplication(ExternalApplication externalApplication) {
		this.externalApplication = externalApplication;
	}

	public String getStatus() {
		return status;
	}

	public void setStatus(String status) {
		this.status = status;
		this.increaseNoAttempts();
	}

	public String getComments() {
		return comments;
	}

	public void setComments(String comments) {
		this.comments = comments;
	}

	public int getNoOfAttempts() {
		return noOfAttempts;
	}

	public void setNoOfAttempts(int noOfAttempts) {
		this.noOfAttempts = noOfAttempts;
	}

	public void increaseNoAttempts() {
		this.noOfAttempts ++;

	}

}
