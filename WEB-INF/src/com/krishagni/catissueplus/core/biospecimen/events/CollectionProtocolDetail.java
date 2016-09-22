package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonFilter;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.events.UserSummary;
import com.krishagni.catissueplus.core.de.events.ExtensionDetail;

@JsonFilter("withoutId")
@JsonInclude(Include.NON_NULL)
public class CollectionProtocolDetail extends CollectionProtocolSummary {
	private List<UserSummary> coordinators;
	
	private List<CollectionProtocolSiteDetail> cpSites;

	private Boolean consentsWaived;

	private String irbId;

	private Long anticipatedParticipantsCount;

	private String sopDocumentUrl;

	private String sopDocumentName;

	private String descriptionUrl;

	private String specimenLabelFmt;

	private String derivativeLabelFmt;

	private String aliquotLabelFmt;
	
	private String visitNameFmt;
	
	private Boolean manualPpidEnabled;
	
	private Boolean manualVisitNameEnabled;
	
	private Boolean manualSpecLabelEnabled;

	private String containerSelectionStrategy;

	private Boolean aliquotsInSameContainer;

	private String visitNamePrintMode;

	private Integer visitNamePrintCopies;
	
	private String spmnLabelPrePrintMode;
	
	private List<CpSpecimenLabelPrintSettingDetail> spmnLabelPrintSettings;

	private String activityStatus;
	
	//
	// mostly used for export and import of CP
	// 
	private List<ConsentTierDetail> consents;
	
	private List<CollectionProtocolEventDetail> events;
	
	private ExtensionDetail extensionDetail;

	public List<UserSummary> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(List<UserSummary> coordinators) {
		this.coordinators = coordinators;
	}

	public List<CollectionProtocolSiteDetail> getCpSites() {
		return cpSites;
	}

	public void setCpSites(List<CollectionProtocolSiteDetail> cpSites) {
		this.cpSites = cpSites;
	}

	public Boolean getConsentsWaived() {
		return consentsWaived;
	}

	public void setConsentsWaived(Boolean consentsWaived) {
		this.consentsWaived = consentsWaived;
	}

	public String getIrbId() {
		return irbId;
	}

	public void setIrbId(String irbId) {
		this.irbId = irbId;
	}

	public Long getAnticipatedParticipantsCount() {
		return anticipatedParticipantsCount;
	}

	public void setAnticipatedParticipantsCount(Long anticipatedParticipantsCount) {
		this.anticipatedParticipantsCount = anticipatedParticipantsCount;
	}

	public String getSopDocumentUrl() {
		return sopDocumentUrl;
	}

	public void setSopDocumentUrl(String sopDocumentUrl) {
		this.sopDocumentUrl = sopDocumentUrl;
	}

	public String getSopDocumentName() {
		return sopDocumentName;
	}

	public void setSopDocumentName(String sopDocumentName) {
		this.sopDocumentName = sopDocumentName;
	}

	public String getDescriptionUrl() {
		return descriptionUrl;
	}

	public void setDescriptionUrl(String descriptionUrl) {
		this.descriptionUrl = descriptionUrl;
	}

	public String getSpecimenLabelFmt() {
		return specimenLabelFmt;
	}

	public void setSpecimenLabelFmt(String specimenLabelFmt) {
		this.specimenLabelFmt = specimenLabelFmt;
	}

	public String getDerivativeLabelFmt() {
		return derivativeLabelFmt;
	}

	public void setDerivativeLabelFmt(String derivativeLabelFmt) {
		this.derivativeLabelFmt = derivativeLabelFmt;
	}

	public String getAliquotLabelFmt() {
		return aliquotLabelFmt;
	}

	public void setAliquotLabelFmt(String aliquotLabelFmt) {
		this.aliquotLabelFmt = aliquotLabelFmt;
	}

	public String getVisitNameFmt() {
		return visitNameFmt;
	}

	public void setVisitNameFmt(String visitNameFmt) {
		this.visitNameFmt = visitNameFmt;
	}

	public Boolean getManualPpidEnabled() {
		return manualPpidEnabled;
	}

	public void setManualPpidEnabled(Boolean manualPpidEnabled) {
		this.manualPpidEnabled = manualPpidEnabled;
	}

	public Boolean getManualVisitNameEnabled() {
		return manualVisitNameEnabled;
	}

	public void setManualVisitNameEnabled(Boolean manualVisitNameEnabled) {
		this.manualVisitNameEnabled = manualVisitNameEnabled;
	}

	public Boolean getManualSpecLabelEnabled() {
		return manualSpecLabelEnabled;
	}

	public void setManualSpecLabelEnabled(Boolean manualSpecLabelEnabled) {
		this.manualSpecLabelEnabled = manualSpecLabelEnabled;
	}

	public String getContainerSelectionStrategy() {
		return containerSelectionStrategy;
	}

	public void setContainerSelectionStrategy(String containerSelectionStrategy) {
		this.containerSelectionStrategy = containerSelectionStrategy;
	}

	public Boolean getAliquotsInSameContainer() {
		return aliquotsInSameContainer;
	}

	public void setAliquotsInSameContainer(Boolean aliquotsInSameContainer) {
		this.aliquotsInSameContainer = aliquotsInSameContainer;
	}

	public String getVisitNamePrintMode() {
		return visitNamePrintMode;
	}

	public void setVisitNamePrintMode(String visitNamePrintMode) {
		this.visitNamePrintMode = visitNamePrintMode;
	}

	public Integer getVisitNamePrintCopies() {
		return visitNamePrintCopies;
	}

	public void setVisitNamePrintCopies(Integer visitNamePrintCopies) {
		this.visitNamePrintCopies = visitNamePrintCopies;
	}

	public String getSpmnLabelPrePrintMode() {
		return spmnLabelPrePrintMode;
	}

	public void setSpmnLabelPrePrintMode(String spmnLabelPrePrintMode) {
		this.spmnLabelPrePrintMode = spmnLabelPrePrintMode;
	}

	public List<CpSpecimenLabelPrintSettingDetail> getSpmnLabelPrintSettings() {
		return spmnLabelPrintSettings;
	}

	public void setSpmnLabelPrintSettings(List<CpSpecimenLabelPrintSettingDetail> spmnLabelPrintSettings) {
		this.spmnLabelPrintSettings = spmnLabelPrintSettings;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public List<ConsentTierDetail> getConsents() {
		return consents;
	}

	public void setConsents(List<ConsentTierDetail> consents) {
		this.consents = consents;
	}

	public List<CollectionProtocolEventDetail> getEvents() {
		return events;
	}

	public void setEvents(List<CollectionProtocolEventDetail> events) {
		this.events = events;
	}

	public ExtensionDetail getExtensionDetail() {
		return extensionDetail;
	}

	public void setExtensionDetail(ExtensionDetail extensionDetail) {
		this.extensionDetail = extensionDetail;
	}

	public static CollectionProtocolDetail from(CollectionProtocol cp) {
		return from(cp, false);
	}
	
	public static CollectionProtocolDetail from(CollectionProtocol cp, boolean fullObject) {
		CollectionProtocolDetail result = new CollectionProtocolDetail();
		CollectionProtocolSummary.copy(cp, result);
		result.setCoordinators(UserSummary.from(cp.getCoordinators()));

		result.setConsentsWaived(cp.isConsentsWaived());
		result.setIrbId(cp.getIrbIdentifier());
		result.setAnticipatedParticipantsCount(cp.getEnrollment());
		result.setSopDocumentUrl(cp.getSopDocumentUrl());
		result.setSopDocumentName(cp.getSopDocumentName());
		result.setDescriptionUrl(cp.getDescriptionURL());
		result.setSpecimenLabelFmt(cp.getSpecimenLabelFormat());
		result.setDerivativeLabelFmt(cp.getDerivativeLabelFormat());
		result.setAliquotLabelFmt(cp.getAliquotLabelFormat());
		result.setVisitNameFmt(cp.getVisitNameFormat());
		result.setManualPpidEnabled(cp.isManualPpidEnabled());
		result.setManualVisitNameEnabled(cp.isManualVisitNameEnabled());
		result.setManualSpecLabelEnabled(cp.isManualSpecLabelEnabled());
		result.setContainerSelectionStrategy(cp.getContainerSelectionStrategy());
		result.setAliquotsInSameContainer(cp.getAliquotsInSameContainer());
		result.setVisitNamePrintMode(cp.getVisitNamePrintMode().name());
		result.setVisitNamePrintCopies(cp.getVisitNamePrintCopies());
		result.setSpmnLabelPrePrintMode(cp.getSpmnLabelPrePrintMode().name());
		result.setSpmnLabelPrintSettings(CpSpecimenLabelPrintSettingDetail.from(cp.getSpmnLabelPrintSettings()));
		result.setActivityStatus(cp.getActivityStatus());
		result.setCpSites(CollectionProtocolSiteDetail.from(cp.getSites()));
		result.setExtensionDetail(ExtensionDetail.from(cp.getExtension()));

		if (fullObject) {
			result.setConsents(ConsentTierDetail.from(cp.getConsentTier()));
			result.setEvents(CollectionProtocolEventDetail.from(cp.getOrderedCpeList(), true));
		}
		
		return result;
	}
	
}
