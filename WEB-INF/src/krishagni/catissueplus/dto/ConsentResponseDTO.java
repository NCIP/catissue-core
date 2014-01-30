package krishagni.catissueplus.dto;


public class ConsentResponseDTO
{
	private String consentStatment;
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
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
    
}
