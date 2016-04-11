package com.krishagni.openspecimen.rde.events;

import java.util.Date;

import org.apache.commons.collections.CollectionUtils;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;
import com.krishagni.catissueplus.core.biospecimen.domain.Participant;
import com.krishagni.catissueplus.core.biospecimen.events.PmiDetail;

public class ParticipantRegDetail {
	private String cpShortTitle;

	private Long cpId;

	private Long cprId;

	private String ppid;

	private Date regDate;

	private Long participantId;

	private PmiDetail pmi;

	private String nextEventLabel;

	private String errorMessage;

	public String getCpShortTitle() {
		return cpShortTitle;
	}

	public void setCpShortTitle(String cpShortTitle) {
		this.cpShortTitle = cpShortTitle;
	}

	public Long getCpId() {
		return cpId;
	}

	public void setCpId(Long cpId) {
		this.cpId = cpId;
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getPpid() {
		return ppid;
	}

	public void setPpid(String ppid) {
		this.ppid = ppid;
	}

	public Date getRegDate() {
		return regDate;
	}

	public void setRegDate(Date regDate) {
		this.regDate = regDate;
	}

	public Long getParticipantId() {
		return participantId;
	}

	public void setParticipantId(Long participantId) {
		this.participantId = participantId;
	}

	public PmiDetail getPmi() {
		return pmi;
	}

	public void setPmi(PmiDetail pmi) {
		this.pmi = pmi;
	}

	public String getNextEventLabel() {
		return nextEventLabel;
	}

	public void setNextEventLabel(String nextEventLabel) {
		this.nextEventLabel = nextEventLabel;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

	public static ParticipantRegDetail from(CollectionProtocolRegistration cpr) {
		ParticipantRegDetail detail = new ParticipantRegDetail();
		detail.setCprId(cpr.getId());
		detail.setPpid(cpr.getPpid());
		detail.setRegDate(cpr.getRegistrationDate());
		detail.setCpId(cpr.getCollectionProtocol().getId());
		detail.setCpShortTitle(cpr.getCollectionProtocol().getShortTitle());

		Participant p = cpr.getParticipant();
		detail.setParticipantId(p.getId());
		if (CollectionUtils.isNotEmpty(p.getPmis())) {
			detail.setPmi(PmiDetail.from(p.getPmis().iterator().next(), false));
		}

		return detail;
	}
}
