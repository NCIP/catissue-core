package com.krishagni.openspecimen.custom.sgh.events;

import java.util.Date;

import com.krishagni.catissueplus.core.biospecimen.events.CollectionProtocolRegistrationDetail;

public class ParticipantRegDetail {
  private Long cprId;
	
  private String ppid;
	
  private Date regDate;

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
	
  public static ParticipantRegDetail from(CollectionProtocolRegistrationDetail cprDetail) {
    ParticipantRegDetail detail = new ParticipantRegDetail();
    detail.setCprId(cprDetail.getId());
    detail.setPpid(cprDetail.getPpid());
    detail.setRegDate(cprDetail.getRegistrationDate());
		
    return detail;
  }

}
