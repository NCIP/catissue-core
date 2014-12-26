
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import com.krishagni.catissueplus.core.administrative.domain.DistributionProtocol;
import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.privileges.domain.CPSiteRole;

public class CollectionProtocol {
	private Long id;

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
	
	private CollectionProtocol parentCollectionProtocol;
	
	private String type;
	
	private Long sequenceNumber;
	
	private Double studyCalendarEventPoint;
	
	private Boolean aliquotInSameContainer;
	
	private Boolean consentsWaived;
	
	private Set<ClinicalDiagnosis> clinicalDiagnosis = new HashSet<ClinicalDiagnosis>();
	
	private Set<ConsentTier> consentTier = new HashSet<ConsentTier>();
	
	private Set<DistributionProtocol> distributionProtocols = new HashSet<DistributionProtocol>();
	
	private Set<User> coordinators = new HashSet<User>();
	
	private Set<User> assignedProtocolUsers = new HashSet<User>();
	
	private Set<Site> sites = new HashSet<Site>();
	
	private Set<CollectionProtocol> childCollectionProtocols = new HashSet<CollectionProtocol>();

	private Set<CollectionProtocolEvent> collectionProtocolEvents = new HashSet<CollectionProtocolEvent>();
	
	private Set<CPSiteRole> cpSiteRoles = new HashSet<CPSiteRole>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

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

	public CollectionProtocol getParentCollectionProtocol() {
		return parentCollectionProtocol;
	}

	public void setParentCollectionProtocol(
			CollectionProtocol parentCollectionProtocol) {
		this.parentCollectionProtocol = parentCollectionProtocol;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Long getSequenceNumber() {
		return sequenceNumber;
	}

	public void setSequenceNumber(Long sequenceNumber) {
		this.sequenceNumber = sequenceNumber;
	}

	public Double getStudyCalendarEventPoint() {
		return studyCalendarEventPoint;
	}

	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint) {
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	public Boolean getAliquotInSameContainer() {
		return aliquotInSameContainer;
	}

	public void setAliquotInSameContainer(Boolean aliquotInSameContainer) {
		this.aliquotInSameContainer = aliquotInSameContainer;
	}

	public Boolean getConsentsWaived() {
		return consentsWaived;
	}

	public void setConsentsWaived(Boolean consentsWaived) {
		this.consentsWaived = consentsWaived;
	}

	public Set<ClinicalDiagnosis> getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(Set<ClinicalDiagnosis> clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public Set<ConsentTier> getConsentTier() {
		return consentTier;
	}

	public void setConsentTier(Set<ConsentTier> consentTier) {
		this.consentTier = consentTier;
	}

	public Set<DistributionProtocol> getDistributionProtocols() {
		return distributionProtocols;
	}

	public void setDistributionProtocols(
			Set<DistributionProtocol> distributionProtocols) {
		this.distributionProtocols = distributionProtocols;
	}

	public Set<User> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(Set<User> coordinators) {
		this.coordinators = coordinators;
	}

	public Set<User> getAssignedProtocolUsers() {
		return assignedProtocolUsers;
	}

	public void setAssignedProtocolUsers(Set<User> assignedProtocolUsers) {
		this.assignedProtocolUsers = assignedProtocolUsers;
	}

	public Set<Site> getSites() {
		return sites;
	}

	public void setSites(Set<Site> sites) {
		this.sites = sites;
	}

	public Set<CollectionProtocol> getChildCollectionProtocols() {
		return childCollectionProtocols;
	}

	public void setChildCollectionProtocols(
			Set<CollectionProtocol> childCollectionProtocols) {
		this.childCollectionProtocols = childCollectionProtocols;
	}

	public Set<CollectionProtocolEvent> getCollectionProtocolEvents() {
		return collectionProtocolEvents;
	}

	public void setCollectionProtocolEvents(
			Set<CollectionProtocolEvent> collectionProtocolEvents) {
		this.collectionProtocolEvents = collectionProtocolEvents;
	}

	public Set<CPSiteRole> getCpSiteRoles() {
		return cpSiteRoles;
	}

	public void setCpSiteRoles(Set<CPSiteRole> cpSiteRoles) {
		this.cpSiteRoles = cpSiteRoles;
	}
	
	
	// new
	
	public ConsentTier addConsentTier(ConsentTier ct) {
		ct.setId(null);
		ct.setCollectionProtocol(this);
		consentTier.add(ct);
		return ct;
	}
	
	public ConsentTier updateConsentTier(ConsentTier ct) {
		if (ct.getId() == null) {
			throw new IllegalArgumentException("Non existing consent tier for update operation");
		}

		ConsentTier existing = getConsentTierById(ct.getId());
		if (existing == null) {
			throw new IllegalArgumentException("Non existing consent tier for update operation");
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
		if (!isEventLabelUnique(cpe.getEventLabel(), null)) {
			throw new IllegalArgumentException("Event label not unique");
		}
				
		cpe.setId(null);
		collectionProtocolEvents.add(cpe);
	}
	
	public void updateCpe(CollectionProtocolEvent cpe) {
		CollectionProtocolEvent existing = getCpe(cpe.getId());
		if (existing == null) {
			throw new IllegalArgumentException("Invalid event for update");
		}
		
		if (!isEventLabelUnique(cpe.getEventLabel(), existing)) {
			throw new IllegalArgumentException("Event label not unique");
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

	private boolean isEventLabelUnique(String eventLabel, CollectionProtocolEvent exclude) {
		boolean unique = true;
		for (CollectionProtocolEvent event : collectionProtocolEvents) {
			if (exclude != null && event.getId().equals(exclude.getId())) {
				continue;
			}
			
			if (event.getEventLabel().equalsIgnoreCase(eventLabel)) {
				unique = false;
				break;
			}
		}
		
		return unique;
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
