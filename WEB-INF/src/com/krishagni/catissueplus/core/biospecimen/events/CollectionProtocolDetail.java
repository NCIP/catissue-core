package com.krishagni.catissueplus.core.biospecimen.events;

import java.util.List;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocol;
import com.krishagni.catissueplus.core.common.events.UserSummary;

public class CollectionProtocolDetail extends CollectionProtocolSummary {
	private List<UserSummary> coordinators;

	private Boolean consentsWaived;

	private String irbId;

	private String ppidFmt;

	private Long anticipatedParticipantsCount;

	private String descriptionUrl;

	private String specimenLabelFmt;

	private String derivativeLabelFmt;

	private String aliquotLabelFmt;

	private Boolean aliquotsInSameContainer;

	private String activityStatus;

	public List<UserSummary> getCoordinators() {
		return coordinators;
	}

	public void setCoordinators(List<UserSummary> coordinators) {
		this.coordinators = coordinators;
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

	public String getPpidFmt() {
		return ppidFmt;
	}

	public void setPpidFmt(String ppidFmt) {
		this.ppidFmt = ppidFmt;
	}

	public Long getAnticipatedParticipantsCount() {
		return anticipatedParticipantsCount;
	}

	public void setAnticipatedParticipantsCount(Long anticipatedParticipantsCount) {
		this.anticipatedParticipantsCount = anticipatedParticipantsCount;
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

	public Boolean getAliquotsInSameContainer() {
		return aliquotsInSameContainer;
	}

	public void setAliquotsInSameContainer(Boolean aliquotsInSameContainer) {
		this.aliquotsInSameContainer = aliquotsInSameContainer;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public static CollectionProtocolDetail from(CollectionProtocol cp) {
		CollectionProtocolDetail result = new CollectionProtocolDetail();
		CollectionProtocolSummary.copy(cp, result);
		result.setCoordinators(UserSummary.from(cp.getCoordinators()));

		result.setConsentsWaived(cp.getConsentsWaived());
		result.setIrbId(cp.getIrbIdentifier());
		result.setPpidFmt(cp.getPpidFormat());
		result.setAnticipatedParticipantsCount(cp.getEnrollment());
		result.setDescriptionUrl(cp.getDescriptionURL());
		result.setSpecimenLabelFmt(cp.getSpecimenLabelFormat());
		result.setDerivativeLabelFmt(cp.getDerivativeLabelFormat());
		result.setAliquotLabelFmt(cp.getAliquotLabelFormat());
		result.setAliquotsInSameContainer(cp.getAliquotInSameContainer());
		result.setActivityStatus(cp.getActivityStatus());
		
		return result;
	}
}
