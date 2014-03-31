
package com.krishagni.catissueplus.core.notification.domain;

import edu.wustl.catissuecore.domain.CollectionProtocol;

public class CollectionProtocolStudyMapping {

	protected Long id;

	protected CollectionProtocol collectionProtocol;

	protected ExternalApplication externalApplication;

	protected String studyId;

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
	 * @return the collectionProtocol
	 */
	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	/**
	 * @param collectionProtocol the collectionProtocol to set
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
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
	 * @return the studyId
	 */
	public String getStudyId() {
		return studyId;
	}

	/**
	 * @param studyId the studyId to set
	 */
	public void setStudyId(String studyId) {
		this.studyId = studyId;
	}

}
