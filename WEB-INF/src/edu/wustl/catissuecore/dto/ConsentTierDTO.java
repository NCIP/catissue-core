package edu.wustl.catissuecore.dto;

import com.krishagni.catissueplus.core.biospecimen.events.SpecimenInfo;


public class ConsentTierDTO
{
	private String consentStatment;
    private String status;
    private Long id;
	private String participantResponses;
	
	
	public String getParticipantResponses() {
		return participantResponses;
	}
	public void setParticipantResponses(String participantResponses) {
		this.participantResponses = participantResponses;
	}
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
	
	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		if (obj instanceof ConsentTierDTO) {
			ConsentTierDTO dto = (ConsentTierDTO) obj;
			if (this.getConsentStatment() != null) {
				return this.getConsentStatment().equals(dto.getConsentStatment());
			}
			return false;
		}
		return true;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((consentStatment == null) ? 0 : consentStatment.hashCode());
		return result;
	}
    
}
