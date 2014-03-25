
package com.krishagni.catissueplus.core.extapp.domain;

import com.krishagni.catissueplus.core.audit.domain.Audit;

public class ExternalAppNotification {

	private Long id;

	private Audit audit;

	private ExternalApplication externalApplication;

	private String status;

	private String comments;

	/**
	 * @return the id
	 */
	public Long getId() {
		return id;
	}

	/**
	 * @param id the id to set
	 */
	public void setId(Long id) {
		this.id = id;
	}

	/**
	 * @return the audit
	 */
	public Audit getAudit() {
		return audit;
	}

	/**
	 * @param audit the audit to set
	 */
	public void setAudit(Audit audit) {
		this.audit = audit;
	}

	/**
	 * @return the externalApplication
	 */
	public ExternalApplication getExternalApplication() {
		return externalApplication;
	}

	/**
	 * @param externalApplication the externalApplication to set
	 */
	public void setExternalApplication(ExternalApplication externalApplication) {
		this.externalApplication = externalApplication;
	}

	/**
	 * @return the status
	 */
	public String getStatus() {
		return status;
	}

	/**
	 * @param status the status to set
	 */
	public void setStatus(String status) {
		this.status = status;
	}

	/**
	 * @return the comments
	 */
	public String getComments() {
		return comments;
	}

	/**
	 * @param comments the comments to set
	 */
	public void setComments(String comments) {
		this.comments = comments;
	}

}
