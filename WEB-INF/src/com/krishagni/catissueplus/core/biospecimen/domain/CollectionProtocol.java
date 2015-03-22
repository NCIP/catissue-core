
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;

public class CollectionProtocol extends BaseEntity {
	private String title;

	private String shortTitle;

	private Date startDate;

	private Date endDate;
	
	private String activityStatus;

	private User principalInvestigator;
	
	private String irbIdentifier;
	
	private Long enrollment;
	
	private String descriptionURL;
	
	private String specimenLabelFormat;
	
	private String derivativeLabelFormat;
	
	private String aliquotLabelFormat;
	
	private String ppidFormat;
	
	private String unsignedConsentDocumentURL;
	
	private Set<ConsentTier> consentTier = new HashSet<ConsentTier>();
	
	private Set<User> coordinators = new HashSet<User>();
	
	private Set<Site> repositories = new HashSet<Site>();
	
	private Set<CollectionProtocolEvent> collectionProtocolEvents = new HashSet<CollectionProtocolEvent>();
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getShortTitle() {
		return shortTitle;
	}

	public void setShortTitle(String shortTitle) {
		this.shortTitle = shortTitle;
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

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public User getPrincipalInvestigator() {
		return principalInvestigator;
	}

	public void setPrincipalInvestigator(User principalInvestigator) {
		this.principalInvestigator = principalInvestigator;
	}

	public String getIrbIdentifier() {
		return irbIdentifier;
	}

	public void setIrbIdentifier(String irbIdentifier) {
		this.irbIdentifier = irbIdentifier;
	}

	public Long getEnrollment() {
		return enrollment;
	}

	public void setEnrollment(Long enrollment) {
		this.enrollment = enrollment;
	}

	public String getDescriptionURL() {
		return descriptionURL;
	}

	public void setDescriptionURL(String descriptionURL) {
		this.descriptionURL = descriptionURL;
	}

	public String getSpecimenLabelFormat() {
		return specimenLabelFormat;
	}

	public void setSpecimenLabelFormat(String specimenLabelFormat) {
		this.specimenLabelFormat = specimenLabelFormat;
	}

	public String getDerivativeLabelFormat() {
		return derivativeLabelFormat;
	}

	public void setDerivativeLabelFormat(String derivativeLabelFormat) {
		this.derivativeLabelFormat = derivativeLabelFormat;
	}

	public String getAliquotLabelFormat() {
		return aliquotLabelFormat;
	}

	public void setAliquotLabelFormat(String aliquotLabelFormat) {
		this.aliquotLabelFormat = aliquotLabelFormat;
	}

	public String getPpidFormat() {
		return ppidFormat;
	}

	public void setPpidFormat(String ppidFormat) {
		this.ppidFormat = ppidFormat;
	}

	public String getUnsignedConsentDocumentURL() {
		return unsignedConsentDocumentURL;
	}

	public void setUnsignedConsentDocumentURL(String unsignedConsentDocumentURL) {
		this.unsignedConsentDocumentURL = unsignedConsentDocumentURL;
	}

	public Set<ConsentTier> getConsentTier() {
		return consentTier;
	}

	public void setConsentTier(Set<ConsentTier> consentTier) {
		this.consentTier = consentTier;
	}

	public Set<User> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(Set<User> coordinators) {
		this.coordinators = coordinators;
	}
	
	public Set<Site> getRepositories() {
		return repositories;
	}

	public void setRepositories(Set<Site> repositories) {
		this.repositories = repositories;
	}

	public Set<CollectionProtocolEvent> getCollectionProtocolEvents() {
		return collectionProtocolEvents;
	}

	public void setCollectionProtocolEvents(Set<CollectionProtocolEvent> collectionProtocolEvents) {
		this.collectionProtocolEvents = collectionProtocolEvents;
	}

	// new	
	public ConsentTier addConsentTier(ConsentTier ct) {
		ct.setId(null);
		ct.setCollectionProtocol(this);
		consentTier.add(ct);
		return ct;
	}
	
	public void update(CollectionProtocol cp) {
		this.setTitle(cp.getTitle()); 
		this.setShortTitle(cp.getShortTitle()); 
		this.setStartDate(cp.getStartDate());
		this.setEndDate(cp.getEndDate());
		this.setActivityStatus(cp.getActivityStatus());
		this.setPrincipalInvestigator(cp.getPrincipalInvestigator());
		this.setIrbIdentifier(cp.getIrbIdentifier());
		this.setEnrollment(cp.getEnrollment());
		this.setDescriptionURL(cp.getDescriptionURL());
		this.setSpecimenLabelFormat(cp.getSpecimenLabelFormat());
		this.setDerivativeLabelFormat(cp.getDerivativeLabelFormat());
		this.setAliquotLabelFormat(cp.getAliquotLabelFormat());
		this.setPpidFormat(cp.getPpidFormat());
		this.setUnsignedConsentDocumentURL(cp.getUnsignedConsentDocumentURL());
		
		CollectionUpdater.update(this.repositories, cp.getRepositories());
		CollectionUpdater.update(this.coordinators, cp.getCoordinators());
	}
	
	public ConsentTier updateConsentTier(ConsentTier ct) {
		if (ct.getId() == null) {
			throw OpenSpecimenException.userError(CpErrorCode.CONSENT_TIER_NOT_FOUND);
		}

		ConsentTier existing = getConsentTierById(ct.getId());
		if (existing == null) {
			throw OpenSpecimenException.userError(CpErrorCode.CONSENT_TIER_NOT_FOUND);
		}
		
		existing.setStatement(ct.getStatement());
		return ct;		
	}
	
	public ConsentTier removeConsentTier(Long ctId) {		
		ConsentTier ct = getConsentTierById(ctId);
		if (ct == null) {
			return null;
		}
		
		consentTier.remove(ct);
		return ct;
	}	
	
	public void addCpe(CollectionProtocolEvent cpe) {
		CollectionProtocolEvent existing = getCpe(cpe.getEventLabel());
		if (existing != null) {
			throw OpenSpecimenException.userError(CpeErrorCode.DUP_LABEL);
		}
				
		cpe.setId(null);
		collectionProtocolEvents.add(cpe);
	}
	
	public void updateCpe(CollectionProtocolEvent cpe) {
		CollectionProtocolEvent existing = getCpe(cpe.getId());
		if (existing == null) {
			throw OpenSpecimenException.userError(CpeErrorCode.NOT_FOUND);
		}

		CollectionProtocolEvent sameLabelEvent = getCpe(cpe.getEventLabel());
		if (sameLabelEvent != null && !sameLabelEvent.getId().equals(existing.getId())) {
			throw OpenSpecimenException.userError(CpeErrorCode.DUP_LABEL);
		}

		existing.update(cpe);
	}
	
	public CollectionProtocolEvent getCpe(Long cpeId) {
		for (CollectionProtocolEvent existing : collectionProtocolEvents) {
			if (existing.getId().equals(cpeId)) {
				return existing;
			}
		}
		
		return null;		
	}
	
	public CollectionProtocolEvent getCpe(String eventLabel) {
		for (CollectionProtocolEvent existing : collectionProtocolEvents) {
			if (existing.getEventLabel().equalsIgnoreCase(eventLabel)) {
				return existing;
			}
		}
		
		return null;
	}

	private ConsentTier getConsentTierById(Long ctId) {
		for (ConsentTier ct : consentTier) {
			if (ct.getId().equals(ctId)) {
				return ct;
			}
		}
		
		return null;
	}
}
