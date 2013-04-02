package edu.wustl.catissuecore.dto;


public class ConsentTierDTO
{
	private String consentStatment;
    private String status;
    private Long id;
	
	public String getConsentStatment() {
		return consentStatment;
	}
	public void setConsentStatment(String consentStatment) {
		this.consentStatment = consentStatment;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
}
