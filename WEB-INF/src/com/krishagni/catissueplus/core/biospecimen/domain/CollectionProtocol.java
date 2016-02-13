
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.AuditTable;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.administrative.domain.StorageContainer;
import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpErrorCode;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.CpeErrorCode;
import com.krishagni.catissueplus.core.common.CollectionUpdater;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
@AuditTable(value="CAT_COLLECTION_PROTOCOL_AUD")
public class CollectionProtocol extends BaseExtensionEntity {
	public enum SpecimenLabelPrePrintMode {
		ON_REGISTRATION,
		ON_VISIT,
		NONE;
	}

	public enum SpecimenLabelAutoPrintMode {
		PRE_PRINT,
		ON_COLLECTION,
		NONE;
	}

	private static final String ENTITY_NAME = "collection_protocol";
	
	private static final Pattern digitsPtrn = Pattern.compile("%(\\d+)d");
	
	private String title;

	private String shortTitle;
	
	private String code;

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
	
	private String visitNameFormat;
	
	private String unsignedConsentDocumentURL;
	
	private Boolean manualPpidEnabled;
	
	private Boolean manualVisitNameEnabled;
	
	private Boolean manualSpecLabelEnabled;
	
	private SpecimenLabelPrePrintMode spmnLabelPrePrintMode = SpecimenLabelPrePrintMode.NONE;
	
	private Set<CpSpecimenLabelPrintSetting> spmnLabelPrintSettings = new HashSet<CpSpecimenLabelPrintSetting>();
	
	private Boolean consentsWaived;

	private Set<ConsentTier> consentTier = new HashSet<ConsentTier>();
	
	private Set<User> coordinators = new HashSet<User>();
	
	private Set<CollectionProtocolSite> sites = new HashSet<CollectionProtocolSite>();
	
	private Set<CollectionProtocolEvent> collectionProtocolEvents = new HashSet<CollectionProtocolEvent>();

	private Set<StorageContainer> storageContainers = new HashSet<StorageContainer>();
	
	private Set<CollectionProtocolRegistration> collectionProtocolRegistrations = new HashSet<CollectionProtocolRegistration>();
	
	public static String getEntityName() {
		return ENTITY_NAME;
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

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
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

	public String getVisitNameFormat() {
		return visitNameFormat;
	}

	public void setVisitNameFormat(String visitNameFormat) {
		this.visitNameFormat = visitNameFormat;
	}

	public String getUnsignedConsentDocumentURL() {
		return unsignedConsentDocumentURL;
	}

	public void setUnsignedConsentDocumentURL(String unsignedConsentDocumentURL) {
		this.unsignedConsentDocumentURL = unsignedConsentDocumentURL;
	}

	public void setManualPpidEnabled(Boolean manualPpidEnabled) {
		this.manualPpidEnabled = manualPpidEnabled;
	}
	
	public boolean isManualPpidEnabled() {
		return manualPpidEnabled != null ? manualPpidEnabled : false;
	}

	public void setManualVisitNameEnabled(Boolean manualVisitNameEnabled) {
		this.manualVisitNameEnabled = manualVisitNameEnabled;
	}
	
	public boolean isManualVisitNameEnabled() {
		return manualVisitNameEnabled != null ? manualVisitNameEnabled : false;
	}
	
	public void setManualSpecLabelEnabled(Boolean manualSpecLabelEnabled) {
		this.manualSpecLabelEnabled = manualSpecLabelEnabled;
	}
	
	public boolean isManualSpecLabelEnabled() {
		return manualSpecLabelEnabled != null ? manualSpecLabelEnabled : false;
	}

	public SpecimenLabelPrePrintMode getSpmnLabelPrePrintMode() {
		return spmnLabelPrePrintMode != null ? spmnLabelPrePrintMode : SpecimenLabelPrePrintMode.NONE;
	}

	public void setSpmnLabelPrePrintMode(SpecimenLabelPrePrintMode spmnLabelPrePrintMode) {
		this.spmnLabelPrePrintMode = spmnLabelPrePrintMode;
	}

	public Set<CpSpecimenLabelPrintSetting> getSpmnLabelPrintSettings() {
		return spmnLabelPrintSettings;
	}

	public void setSpmnLabelPrintSettings(Set<CpSpecimenLabelPrintSetting> spmnLabelPrintSettings) {
		this.spmnLabelPrintSettings = spmnLabelPrintSettings;
	}
	
	public CpSpecimenLabelPrintSetting getSpmnLabelPrintSetting(String lineage) {
		Optional<CpSpecimenLabelPrintSetting> setting = getSpmnLabelPrintSettings()
			.stream()
			.filter((s) -> s.getLineage().equals(lineage))
			.findFirst();
		return setting.isPresent() ? setting.get() : null;
	}
	
	public Boolean isConsentsWaived() {
		return consentsWaived != null ? consentsWaived: false;
	}

	public void setConsentsWaived(Boolean consentsWaived) {
		this.consentsWaived = consentsWaived;
	}

	@NotAudited
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
		return Utility.<Set<Site>>collect(sites, "site", true);
	}

	@NotAudited
	public Set<CollectionProtocolSite> getSites() {
		return sites;
	}

	public void setSites(Set<CollectionProtocolSite> sites) {
		this.sites = sites;
	}

	@NotAudited
	public Set<CollectionProtocolEvent> getCollectionProtocolEvents() {
		return collectionProtocolEvents;
	}

	public void setCollectionProtocolEvents(Set<CollectionProtocolEvent> collectionProtocolEvents) {
		this.collectionProtocolEvents = collectionProtocolEvents;
	}

	@NotAudited
	public Set<StorageContainer> getStorageContainers() {
		return storageContainers;
	}

	public void setStorageContainers(Set<StorageContainer> storageContainers) {
		this.storageContainers = storageContainers;
	}

	@NotAudited
	public Set<CollectionProtocolRegistration> getCollectionProtocolRegistrations() {
		return collectionProtocolRegistrations;
	}

	public void setCollectionProtocolRegistrations(
			Set<CollectionProtocolRegistration> collectionProtocolRegistrations) {
		this.collectionProtocolRegistrations = collectionProtocolRegistrations;
	}

	// new	
	public ConsentTier addConsentTier(ConsentTier ct) {
		ct.setId(null);
		ct.setCollectionProtocol(this);
		ct.setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
		consentTier.add(ct);
		return ct;
	}
	
	public void update(CollectionProtocol cp) {
		setTitle(cp.getTitle()); 
		setShortTitle(cp.getShortTitle());
		setCode(cp.getCode());
		setStartDate(cp.getStartDate());
		setEndDate(cp.getEndDate());
		setActivityStatus(cp.getActivityStatus());
		setPrincipalInvestigator(cp.getPrincipalInvestigator());
		setIrbIdentifier(cp.getIrbIdentifier());
		setEnrollment(cp.getEnrollment());
		setDescriptionURL(cp.getDescriptionURL());
		setPpidFormat(cp.getPpidFormat());
		setManualPpidEnabled(cp.isManualPpidEnabled());
		setVisitNameFormat(cp.getVisitNameFormat());
		setManualVisitNameEnabled(cp.isManualVisitNameEnabled());
		setSpecimenLabelFormat(cp.getSpecimenLabelFormat());
		setDerivativeLabelFormat(cp.getDerivativeLabelFormat());
		setAliquotLabelFormat(cp.getAliquotLabelFormat());
		setManualSpecLabelEnabled(cp.isManualSpecLabelEnabled());
		setUnsignedConsentDocumentURL(cp.getUnsignedConsentDocumentURL());
		setExtension(cp.getExtension());
		
		updateSites(cp.getSites());
		updateSpecimenLabelPrintSettings(cp.getSpmnLabelPrintSettings());
		CollectionUpdater.update(this.coordinators, cp.getCoordinators());
		updateLabelPrePrintMode(cp.getSpmnLabelPrePrintMode());
	}
	
	public boolean isValidPpid(String ppid) {
		String ppidFmt = getPpidFormat();
		if (StringUtils.isBlank(ppidFmt)) {
			return true;
		}
		
		Matcher matcher = digitsPtrn.matcher(ppidFmt);
		if (!matcher.find()) {
			return ppidFmt.equals(ppid);
		}
		
		int matchStartIdx = ppidFmt.indexOf(matcher.group(0));
		String beforeDigits = ppidFmt.substring(0, matchStartIdx);
		String afterDigits = ppidFmt.substring(matchStartIdx + matcher.group(0).length());
		
		String regex = beforeDigits + "\\d{" + matcher.group(1) + "}" + afterDigits;
		return Pattern.matches(regex, ppid);		
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
			throw OpenSpecimenException.userError(CpErrorCode.CONSENT_TIER_NOT_FOUND);
		}
		
		List<DependentEntityDetail> dependentEntities = ct.getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw OpenSpecimenException.userError(CpErrorCode.CONSENT_REF_ENTITY_FOUND, ct.getStatement());
		}
		ct.setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
		return ct;
	}	
	
	public void addCpe(CollectionProtocolEvent cpe) {
		CollectionProtocolEvent existing = getCpe(cpe.getEventLabel());
		if (existing != null) {
			throw OpenSpecimenException.userError(CpeErrorCode.DUP_LABEL, cpe.getEventLabel());
		}
		
		if (StringUtils.isNotBlank(cpe.getCode()) && getCpeByCode(cpe.getCode()) != null) {
			throw OpenSpecimenException.userError(CpeErrorCode.DUP_CODE, cpe.getCode());
		}
				
		cpe.setId(null);
		getCollectionProtocolEvents().add(cpe);
	}
	
	public void updateCpe(CollectionProtocolEvent cpe) {
		CollectionProtocolEvent existing = getCpe(cpe.getId());
		if (existing == null) {
			throw OpenSpecimenException.userError(CpeErrorCode.NOT_FOUND);
		}

		if (!existing.getEventLabel().equals(cpe.getEventLabel())) {
			if (getCpe(cpe.getEventLabel()) != null) {
				throw OpenSpecimenException.userError(CpeErrorCode.DUP_LABEL, cpe.getEventLabel());
			}			
		}
		
		if (StringUtils.isNotBlank(cpe.getCode()) && !cpe.getCode().equals(existing.getCode())) {
			if (getCpeByCode(cpe.getCode()) != null) {
				throw OpenSpecimenException.userError(CpeErrorCode.DUP_CODE, cpe.getCode());
			}
		}
		
		existing.update(cpe);
	}
	
	public CollectionProtocolEvent getCpe(Long cpeId) {
		for (CollectionProtocolEvent cpe : getCollectionProtocolEvents()) {
			if (cpe.getId().equals(cpeId)) {
				return cpe;
			}
		}
		
		return null;		
	}
	
	public CollectionProtocolEvent getCpe(String eventLabel) {
		for (CollectionProtocolEvent cpe : getCollectionProtocolEvents()) {
			if (cpe.getEventLabel().equalsIgnoreCase(eventLabel)) {
				return cpe;
			}
		}
		
		return null;
	}
	
	public CollectionProtocolEvent getCpeByCode(String code) {
		for (CollectionProtocolEvent cpe : getCollectionProtocolEvents()) {
			if (code.equals(cpe.getCode())) {
				return cpe;
			}
		}
		
		return null;
	}
	
	//TODO: need to check few more dependencies like user role 
	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail
				.listBuilder()
				.add(CollectionProtocolRegistration.getEntityName(), getCollectionProtocolRegistrations().size())
				.add(StorageContainer.getEntityName(), getStorageContainers().size())
				.build();
	}
	
	public void delete() {
		List<DependentEntityDetail> dependentEntities = getDependentEntities();
		if (!dependentEntities.isEmpty()) {
			throw OpenSpecimenException.userError(CpeErrorCode.REF_ENTITY_FOUND);
		}

		setTitle(Utility.getDisabledValue(getTitle(), 255));
		setShortTitle(Utility.getDisabledValue(getShortTitle(), 50));
		setCode(Utility.getDisabledValue(getCode(), 32));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}
	
	@Override
	public String getEntityType() {
		return "CollectionProtocolExtension";
	}
	
	public CollectionProtocolEvent firstEvent() {
		if (!getCollectionProtocolEvents().isEmpty()) {
			return getOrderedCpeList().get(0);
		}
		
		return null;
	}
	
	public List<CollectionProtocolEvent> getOrderedCpeList() {
		List<CollectionProtocolEvent> events = new ArrayList<CollectionProtocolEvent>(getCollectionProtocolEvents());
		Collections.sort(events);
		return events;
	}

	private ConsentTier getConsentTierById(Long ctId) {
		for (ConsentTier ct : consentTier) {
			if (ct.getId().equals(ctId)) {
				return ct;
			}
		}
		
		return null;
	}
	
	private void updateSites(Set<CollectionProtocolSite> newSites) {
		Map<Site, CollectionProtocolSite> existingSites = new HashMap<Site, CollectionProtocolSite>();
		for (CollectionProtocolSite cpSite: getSites()) {
			existingSites.put(cpSite.getSite(), cpSite);
		}
		
		for (CollectionProtocolSite newSite: newSites) {
			CollectionProtocolSite oldSite = existingSites.get(newSite.getSite());
			if (oldSite != null) {
				oldSite.update(newSite);
				existingSites.remove(oldSite.getSite());
			} else {
				getSites().add(newSite);
			}
		}
		
		getSites().removeAll(existingSites.values());
	}
	
	private void updateLabelPrePrintMode(SpecimenLabelPrePrintMode prePrintMode) {
		if (getSpmnLabelPrePrintMode() == prePrintMode) {
			//
			// Nothing has changed
			//
			return;
		}

		setSpmnLabelPrePrintMode(prePrintMode);
		if (prePrintMode != SpecimenLabelPrePrintMode.NONE) {
			//
			// pre-printing is not disabled
			//
			return;
		}

		//
		// Disable pre-print for all specimen requirements
		//
		for (CollectionProtocolEvent cpe : getCollectionProtocolEvents()) {
			for (SpecimenRequirement sr : cpe.getSpecimenRequirements()) {
				if (sr.getLabelAutoPrintMode() == SpecimenLabelAutoPrintMode.PRE_PRINT) {
					sr.setLabelAutoPrintMode(SpecimenLabelAutoPrintMode.NONE);
				}
			}
		}
	}
	
	private void updateSpecimenLabelPrintSettings(Set<CpSpecimenLabelPrintSetting> newSpmnLblPrintSettings) {
		Map<String, CpSpecimenLabelPrintSetting> existingSettings = new HashMap<String, CpSpecimenLabelPrintSetting>();
		for (CpSpecimenLabelPrintSetting spmnLblPrintSetting : getSpmnLabelPrintSettings()) {
			existingSettings.put(spmnLblPrintSetting.getLineage(), spmnLblPrintSetting);
		}
		
		for (CpSpecimenLabelPrintSetting newSpmnLblPrintSetting : newSpmnLblPrintSettings) {
			CpSpecimenLabelPrintSetting oldSetting = existingSettings.get(newSpmnLblPrintSetting.getLineage());
			if (oldSetting != null) {
				oldSetting.update(newSpmnLblPrintSetting);
				existingSettings.remove(oldSetting.getLineage());
			} else {
				getSpmnLabelPrintSettings().add(newSpmnLblPrintSetting);
			}
		}
		
		getSpmnLabelPrintSettings().removeAll(existingSettings.values());
	}
}
