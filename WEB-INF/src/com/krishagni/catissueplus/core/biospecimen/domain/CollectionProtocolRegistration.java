
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Collection;
import java.util.Collections;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Configurable;
import org.springframework.beans.factory.annotation.Qualifier;

import com.krishagni.catissueplus.core.administrative.domain.User;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.ParticipantErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.events.DependentEntityDetail;
import com.krishagni.catissueplus.core.common.service.LabelGenerator;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Configurable
@Audited
public class CollectionProtocolRegistration {
	private static final String ENTITY_NAME = "collection_protocol_registration";
	
	private Long id;

	private String ppid;

	private Date registrationDate;

	private Participant participant;

	private CollectionProtocol collectionProtocol;

	private Collection<Visit> visits = new HashSet<Visit>();

	private String activityStatus;

	private Date consentSignDate;

	private User consentWitness;
	
	private String consentComments;
	
	private String signedConsentDocumentName;

	private Set<ConsentTierResponse> consentResponses = new HashSet<ConsentTierResponse>();

	private String barcode;
	
	@Autowired
	@Qualifier("ppidGenerator")
	private LabelGenerator labelGenerator;

	private transient boolean forceDelete;

	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Date getRegistrationDate() {
		return registrationDate;
	}

	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}

	public Participant getParticipant() {
		return participant;
	}

	public void setParticipant(Participant participant) {
		this.participant = participant;
	}

	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getCpShortTitle() {
		return getCollectionProtocol().getShortTitle();
	}

	@NotAudited
	public Collection<Visit> getVisits() {
		return visits;
	}

	public void setVisits(Collection<Visit> visits) {
		this.visits = visits;
	}

	public List<Visit> getOrderedVisits() {
		if (getVisits() == null) {
			return Collections.emptyList();
		}

		return getVisits().stream()
			.sorted((v1, v2) -> v1.getVisitDate().compareTo(v2.getVisitDate()))
			.collect(Collectors.toList());
	}

	public Visit getLatestVisit() {
		List<Visit> sortedVisits = getOrderedVisits();
		if (CollectionUtils.isEmpty(sortedVisits)) {
			return null;
		}

		return sortedVisits.get(sortedVisits.size() - 1);
	}

	public Visit getVisitByName(String visitName) {
		return getVisits().stream().filter(v -> v.getName().equals(visitName)).findFirst().orElse(null);
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		if (StringUtils.isBlank(activityStatus)) {
			activityStatus = Status.ACTIVITY_STATUS_ACTIVE.getStatus();
		}
		
		this.activityStatus = activityStatus;
	}
	
	public Date getConsentSignDate() {
		return consentSignDate;
	}

	public void setConsentSignDate(Date consentSignDate) {
		this.consentSignDate = consentSignDate;
	}

	public User getConsentWitness() {
		return consentWitness;
	}

	public void setConsentWitness(User consentWitness) {
		this.consentWitness = consentWitness;
	}

	public String getConsentComments() {
		return consentComments;
	}

	public void setConsentComments(String consentComments) {
		this.consentComments = consentComments;
	}

	public String getSignedConsentDocumentName() {
		return signedConsentDocumentName;
	}

	public void setSignedConsentDocumentName(String signedConsentDocumentName) {
		this.signedConsentDocumentName = signedConsentDocumentName;
	}

	public Set<ConsentTierResponse> getConsentResponses() {
		return consentResponses;
	}

	public void setConsentResponses(Set<ConsentTierResponse> consentResponses) {
		this.consentResponses = consentResponses;
	}

	public String getBarcode() {
		return barcode;
	}

	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}

	public boolean isForceDelete() {
		return forceDelete;
	}

	public void setForceDelete(boolean forceDelete) {
		this.forceDelete = forceDelete;
	}

	public boolean isActive() {
		return Status.ACTIVITY_STATUS_ACTIVE.getStatus().equals(this.getActivityStatus());
	}

	public void setActive() {
		setActivityStatus(Status.ACTIVITY_STATUS_ACTIVE.getStatus());
	}

	public List<DependentEntityDetail> getDependentEntities() {
		return DependentEntityDetail.singletonList(Visit.getEntityName(), getActiveVisits()); 
	}
	
	public void updateActivityStatus(String activityStatus) {
		if (this.activityStatus != null && this.activityStatus.equals(activityStatus)) {
			return;
		}
		
		if (Status.ACTIVITY_STATUS_DISABLED.getStatus().equals(activityStatus)) {
			delete();
		}
	}
	
	public void delete() {
		delete(!isForceDelete());
	}

	public void delete(boolean checkDependency) {
		if (checkDependency) {
			ensureNoActiveChildObjects();
		}
		
		for (Visit visit : getVisits()) {
			visit.delete(checkDependency);
		}
		
		setBarcode(Utility.getDisabledValue(getBarcode(), 255));
		setPpid(Utility.getDisabledValue(getPpid(), 255));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());

		//
		// If participant is registered to only this deleted registration
		// then delete participant as well
		//
		int regCount = getParticipant().getCprs().size();
		CollectionProtocolRegistration cpr = getParticipant().getCprs().iterator().next();
		if (regCount == 1 && this.equals(cpr)) {
			getParticipant().delete();
		}
	}

	public void update(CollectionProtocolRegistration cpr) {
		setForceDelete(cpr.isForceDelete());
		updateActivityStatus(cpr.getActivityStatus());
		if (!isActive()) {
			return;
		}

		if (StringUtils.isNotBlank(cpr.getPpid())) {
			setPpid(cpr.getPpid());
		}

		if (cpr.getRegistrationDate() != null) {
			setRegistrationDate(cpr.getRegistrationDate());
		}

		if (cpr.getParticipant() != null) {
			setParticipant(cpr.getParticipant());
		}

		setBarcode(cpr.getBarcode());
	}
	
	public void updateConsents(ConsentResponses consentResponses) {
		setConsentSignDate(consentResponses.getConsentSignDate());
		setConsentWitness(consentResponses.getConsentWitness());
		setConsentComments(consentResponses.getConsentComments());
		updateConsentResponses(consentResponses.getConsentResponses());
	}
	
	public void setPpidIfEmpty() {
		if (StringUtils.isNotBlank(ppid)) {
			return;
		}
		
		CollectionProtocol cp = getCollectionProtocol();
		String ppidFmt = cp.getPpidFormat();
		if (StringUtils.isNotBlank(ppidFmt)) {
			setPpid(labelGenerator.generateLabel(ppidFmt, this));
		} else {
			setPpid(cp.getId() + "_" + participant.getId());
		}
	}
	
	public void addVisits(Collection<Visit> visits) {
		for (Visit visit : visits) {
			visit.setRegistration(this); 
			getVisits().add(visit);
		}
	}

	private void updateConsentResponses(Collection<ConsentTierResponse> consentResponses) {
		Map<String, ConsentTierResponse> existingResps = getConsentResponses().stream()
			.collect(Collectors.toMap(ConsentTierResponse::getStatement, resp -> resp));

		for(ConsentTierResponse newResp : consentResponses) {
			ConsentTierResponse existingResp = existingResps.remove(newResp.getConsentTier().getStatement());
			if (existingResp == null) {
				getConsentResponses().add(newResp);
			} else {
				existingResp.setResponse(newResp.getResponse());
			}
		}
		
		getConsentResponses().removeAll(existingResps.values());
	}

	private void ensureNoActiveChildObjects() {
		for (Visit visit : getVisits()) {
			if (visit.isActive() && visit.isCompleted()) {
				throw OpenSpecimenException.userError(ParticipantErrorCode.REF_ENTITY_FOUND);
			}			
		}
	}	
	
	private int getActiveVisits() {
		int count = 0;
		for (Visit visit : getVisits()) {
			if (visit.isActive() && visit.isCompleted()) {
				++count;
			}
		}
		
		return count;
	}
}
