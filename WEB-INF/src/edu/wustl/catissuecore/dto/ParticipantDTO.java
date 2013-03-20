package edu.wustl.catissuecore.dto;

import java.util.Date;

public class ParticipantDTO {
	private String ppid;
	private Date registrationDate;
	private String barcode;
	private Long cprId;
	
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
	public Date getRegistrationDate() {
		return registrationDate;
	}
	public void setRegistrationDate(Date registrationDate) {
		this.registrationDate = registrationDate;
	}
	public String getBarcode() {
		return barcode;
	}
	public void setBarcode(String barcode) {
		this.barcode = barcode;
	}
	

}
