
package com.krishagni.catissueplus.core.biospecimen.events;

import edu.wustl.catissuecore.domain.Participant;

public class ParticipantInfo {

	public ParticipantInfo(Long cprId, Long id, String ppId,
			String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.ppId = ppId;
		this.cprId = cprId;
	}

	private String firstName = "";

	private String lastName = "";

	private Long id;

	private String ppId = "";

	private Long cprId;

	public ParticipantInfo() {
		super();
	}

	public Long getCprId() {
		return cprId;
	}

	public void setCprId(Long cprId) {
		this.cprId = cprId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getPpId() {
		return ppId;
	}

	public void setPpId(String ppId) {
		this.ppId = ppId;
	}

	public static ParticipantInfo fromParticipant(Participant participant) {
		ParticipantInfo participantInfo = new ParticipantInfo();
		participantInfo.setId(participant.getId());
		participantInfo.setFirstName(participant.getFirstName());
		participantInfo.setLastName(participant.getLastName());
		//		participantInfo.setProtocolParticipantIdentifier(participant.getp)
		//		participantInfo.setCollectionProtocolRegistrationId(participant.getc)
		return participantInfo;
	}

}
