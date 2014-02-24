
package com.krishagni.catissueplus.core.biospecimen.events;

import edu.wustl.catissuecore.domain.Participant;

public class ParticipantInfo {

	public ParticipantInfo(Long collectionProtocolRegistrationId, Long id, String protocolParticipantIdentifier,
			String firstName, String lastName) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.id = id;
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
		this.collectionProtocolRegistrationId = collectionProtocolRegistrationId;
	}

	private String firstName;

	private String lastName;

	private Long id;

	private String protocolParticipantIdentifier;

	private Long collectionProtocolRegistrationId;

	public ParticipantInfo() {
		super();
	}

	public Long getCollectionProtocolRegistrationId() {
		return collectionProtocolRegistrationId;
	}

	public void setCollectionProtocolRegistrationId(Long collectionProtocolRegistrationId) {
		this.collectionProtocolRegistrationId = collectionProtocolRegistrationId;
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

	public String getProtocolParticipantIdentifier() {
		return protocolParticipantIdentifier;
	}

	public void setProtocolParticipantIdentifier(String protocolParticipantIdentifier) {
		this.protocolParticipantIdentifier = protocolParticipantIdentifier;
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
