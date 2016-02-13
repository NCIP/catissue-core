package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;

import org.apache.commons.lang.StringUtils;
import org.hibernate.envers.Audited;
import org.hibernate.envers.NotAudited;

import com.krishagni.catissueplus.core.administrative.domain.Site;
import com.krishagni.catissueplus.core.biospecimen.domain.factory.SrErrorCode;
import com.krishagni.catissueplus.core.common.errors.OpenSpecimenException;
import com.krishagni.catissueplus.core.common.util.Status;
import com.krishagni.catissueplus.core.common.util.Utility;

@Audited
public class CollectionProtocolEvent implements Comparable<CollectionProtocolEvent> {
	private static final String ENTITY_NAME = "collection_protocol_event";
	
	private Long id;

	private String eventLabel;

	private Double eventPoint;

	private CollectionProtocol collectionProtocol;
	
	private String code;
	
	private Site defaultSite;

	private String clinicalDiagnosis;
	
	private String clinicalStatus;
	
	private String activityStatus;
	
	private Set<SpecimenRequirement> specimenRequirements = new LinkedHashSet<SpecimenRequirement>();

	private Set<Visit> specimenCollectionGroups = new HashSet<Visit>();

	private transient int offset = 0;

	public static String getEntityName() {
		return ENTITY_NAME;
	}
	
	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getEventLabel() {
		return eventLabel;
	}

	public void setEventLabel(String eventLabel) {
		this.eventLabel = eventLabel;
	}

	public Double getEventPoint() {
		return eventPoint;
	}

	public void setEventPoint(Double eventPoint) {
		this.eventPoint = eventPoint;
	}

	@NotAudited
	public CollectionProtocol getCollectionProtocol() {
		return collectionProtocol;
	}

	public void setCollectionProtocol(CollectionProtocol collectionProtocol) {
		this.collectionProtocol = collectionProtocol;
	}

	public String getCode() {
		return code;
	}

	public void setCode(String code) {
		this.code = code;
	}

	public Site getDefaultSite() {
		return defaultSite;
	}

	public void setDefaultSite(Site defaultSite) {
		this.defaultSite = defaultSite;
	}

	public String getClinicalDiagnosis() {
		return clinicalDiagnosis;
	}

	public void setClinicalDiagnosis(String clinicalDiagnosis) {
		this.clinicalDiagnosis = clinicalDiagnosis;
	}

	public String getClinicalStatus() {
		return clinicalStatus;
	}

	public void setClinicalStatus(String clinicalStatus) {
		this.clinicalStatus = clinicalStatus;
	}
	
	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	@NotAudited
	public Set<SpecimenRequirement> getSpecimenRequirements() {
		return specimenRequirements;
	}
	
	public void setSpecimenRequirements(Set<SpecimenRequirement> specimenRequirements) {
		this.specimenRequirements = specimenRequirements;
	}
	
	public Set<SpecimenRequirement> getTopLevelAnticipatedSpecimens() {
		Set<SpecimenRequirement> anticipated = new LinkedHashSet<SpecimenRequirement>();
		if (getSpecimenRequirements() == null) {
			return anticipated;
		}
		
		for (SpecimenRequirement sr : getSpecimenRequirements()) {
			if (sr.getParentSpecimenRequirement() == null && sr.getPooledSpecimenRequirement() == null) {
				anticipated.add(sr);
			}
		}
		
		return anticipated;
	}

	@NotAudited
	public Set<Visit> getSpecimenCollectionGroups() {
		return specimenCollectionGroups;
	}

	public void setSpecimenCollectionGroups(Set<Visit> specimenCollectionGroups) {
		this.specimenCollectionGroups = specimenCollectionGroups;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}

	// updates all but specimen requirements
	public void update(CollectionProtocolEvent other) { 
		setEventPoint(other.getEventPoint());
		setEventLabel(other.getEventLabel());
		setCollectionProtocol(other.getCollectionProtocol());
		setCode(other.getCode());
		setDefaultSite(other.getDefaultSite());
		setClinicalDiagnosis(other.getClinicalDiagnosis());
		setClinicalStatus(other.getClinicalStatus());
		setActivityStatus(other.getActivityStatus());
	}
	
	public void addSpecimenRequirement(SpecimenRequirement sr) {
		ensureUniqueSrCode(sr);
		getSpecimenRequirements().add(sr);
		sr.setCollectionProtocolEvent(this);
	}
	
	public void copySpecimenRequirementsTo(CollectionProtocolEvent cpe) {
		List<SpecimenRequirement> topLevelSrs = new ArrayList<SpecimenRequirement>(getTopLevelAnticipatedSpecimens());
		Collections.sort(topLevelSrs);

		int order = 1;
		for (SpecimenRequirement sr : topLevelSrs) {
			SpecimenRequirement copiedSr = sr.deepCopy(cpe);
			copiedSr.setSortOrder(order++);
			cpe.addSpecimenRequirement(copiedSr);
		}
	}
	
	public SpecimenRequirement getSrByCode(String code) {
		for (SpecimenRequirement sr : getSpecimenRequirements()) {
			if (code.equals(sr.getCode())) {
				return sr;
			}
		}
		
		return null;
	}
	
	public void delete() {
		for (SpecimenRequirement sr : getSpecimenRequirements()) {
			if (sr.isPrimary() && !sr.isSpecimenPoolReq()) {
				sr.delete();
			}
		}

		setEventLabel(Utility.getDisabledValue(getEventLabel(), 255));
		setCode(Utility.getDisabledValue(getCode(), 32));
		setActivityStatus(Status.ACTIVITY_STATUS_DISABLED.getStatus());
	}

	public void ensureUniqueSrCode(SpecimenRequirement sr) {
		if (StringUtils.isNotBlank(sr.getCode()) && getSrByCode(sr.getCode()) != null) {
			throw OpenSpecimenException.userError(SrErrorCode.DUP_CODE, sr.getCode());
		}

		if (sr.isPooledSpecimenReq()) {
			ensureUniqueSrCodes(sr.getSpecimenPoolReqs());
		}
	}

	public void ensureUniqueSrCodes(Collection<SpecimenRequirement> srs) {
		Set<String> codes = new HashSet<String>();
		for (SpecimenRequirement sr : srs) {
			if (StringUtils.isBlank(sr.getCode())) {
				continue;
			}

			if (codes.contains(sr.getCode()) || getSrByCode(sr.getCode()) != null) {
				throw OpenSpecimenException.userError(SrErrorCode.DUP_CODE, sr.getCode());
			}

			codes.add(sr.getCode());
		}
	}

	@Override
	public int compareTo(CollectionProtocolEvent other) {
		Double thisEventPoint = this.eventPoint == null ? 0d : this.eventPoint;
		Double otherEventPoint = other.eventPoint == null ? 0d : other.eventPoint;

		if (thisEventPoint.equals(otherEventPoint)) {
			return id.compareTo(other.id);
		} else {
			return thisEventPoint.compareTo(otherEventPoint);
		}
	}
}