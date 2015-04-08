package com.krishagni.openspecimen.custom.le.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.domain.CollectionProtocolRegistration;

public class ParticipantRegDetail {
	private Long cprId;
	
	private String empi;
	
	private String ppid;
	
	private Date regDate;

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getEmpi() {
		return empi;
	}

	public void setEmpi(String empi) {
		this.empi = empi;
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
	
	public static ParticipantRegDetail from(CollectionProtocolRegistration cpr) {
		ParticipantRegDetail detail = new ParticipantRegDetail();
		detail.setCprId(cpr.getId());
		detail.setEmpi(cpr.getParticipant().getEmpi());
		detail.setPpid(cpr.getPpid());
		detail.setRegDate(cpr.getRegistrationDate());
		
		return detail;
	}

}
