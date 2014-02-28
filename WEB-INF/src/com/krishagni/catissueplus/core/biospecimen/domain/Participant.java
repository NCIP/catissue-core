
package com.krishagni.catissueplus.core.biospecimen.domain;

import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

public class Participant {

	/**
	 * System generated unique id.
	 * */
	protected Long id;

	/**
	 * Last name of the participant.
	 */
	protected String lastName;

	/**
	 * First name of the participant.
	 */
	protected String firstName;

	/**
	 * Middle name of the participant.
	 */
	protected String middleName;

	/**
	 * Birth date of participant.
	 */
	protected Date birthDate;

	/**
	 * The gender of the participant.
	 */
	protected String gender;

	/**
	 * The genetic constitution of the individual.
	 */
	protected String sexGenotype;

	/**
	 * Participant's race origination.
	 */
	protected Set<String> raceCollection = new HashSet<String>();

	/**
	 * Participant's ethnicity status.
	 */
	protected String ethnicity;

	/**
	 * Social Security Number of participant.
	 */
	protected String socialSecurityNumber;

	/**
	 * Defines whether this participant record can be queried (Active) or not
	 * queried (Inactive) by any actor.
	 */
	protected String activityStatus;

	/**
	 * Death date of participant.
	 */
	protected Date deathDate;

	/**
	 * Defines the vital status of the participant like 'Dead', 'Alive' or
	 * 'Unknown'.
	 */
	protected String vitalStatus;

	protected Map<String, ParticipantMedicalIdentifier> participantMedicalIdentifierCollection = new HashMap<String, ParticipantMedicalIdentifier>();

	/**
	 * A collection of registration of a Participant to a Collection Protocol.
	 */
	protected Map<String, CollectionProtocolRegistration> collectionProtocolRegistrationCollection = new HashMap<String, CollectionProtocolRegistration>();

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getMiddleName() {
		return middleName;
	}

	public void setMiddleName(String middleName) {
		this.middleName = middleName;
	}

	public Date getBirthDate() {
		return birthDate;
	}

	public void setBirthDate(Date birthDate) {
		this.birthDate = birthDate;
	}

	public String getGender() {
		return gender;
	}

	public void setGender(String gender) {
		this.gender = gender;
	}

	public String getSexGenotype() {
		return sexGenotype;
	}

	public void setSexGenotype(String sexGenotype) {
		this.sexGenotype = sexGenotype;
	}

	public Set<String> getRaceCollection() {
		return raceCollection;
	}

	public void setRaceCollection(Set<String> raceCollection) {
		this.raceCollection = raceCollection;
	}

	public String getEthnicity() {
		return ethnicity;
	}

	public void setEthnicity(String ethnicity) {
		this.ethnicity = ethnicity;
	}

	public String getSocialSecurityNumber() {
		return socialSecurityNumber;
	}

	public void setSocialSecurityNumber(String socialSecurityNumber) {
		this.socialSecurityNumber = socialSecurityNumber;
	}

	public String getActivityStatus() {
		return activityStatus;
	}

	public void setActivityStatus(String activityStatus) {
		this.activityStatus = activityStatus;
	}

	public Date getDeathDate() {
		return deathDate;
	}

	public void setDeathDate(Date deathDate) {
		this.deathDate = deathDate;
	}

	public String getVitalStatus() {
		return vitalStatus;
	}

	public void setVitalStatus(String vitalStatus) {
		this.vitalStatus = vitalStatus;
	}

	public Map<String, ParticipantMedicalIdentifier> getParticipantMedicalIdentifierCollection() {
		return participantMedicalIdentifierCollection;
	}

	public void setParticipantMedicalIdentifierCollection(
			Map<String, ParticipantMedicalIdentifier> participantMedicalIdentifierCollection) {
		this.participantMedicalIdentifierCollection = participantMedicalIdentifierCollection;
	}

	public Map<String, CollectionProtocolRegistration> getCollectionProtocolRegistrationCollection() {
		return collectionProtocolRegistrationCollection;
	}

	public void setCollectionProtocolRegistrationCollection(
			Map<String, CollectionProtocolRegistration> collectionProtocolRegistrationCollection) {
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}

	public void update(Participant participant) {
		this.setFirstName(participant.getFirstName());
		this.setLastName(participant.getLastName());
		this.setMiddleName(participant.getMiddleName());
		this.setSocialSecurityNumber(participant.getSocialSecurityNumber());
		this.setActivityStatus(participant.getActivityStatus());
		this.setSexGenotype(participant.getSexGenotype());
		this.setVitalStatus(participant.getVitalStatus());
		this.setGender(participant.getGender());
		this.setEthnicity(participant.getEthnicity());
		this.setBirthDate(participant.getBirthDate());
		this.setDeathDate(participant.getDeathDate());
		updateRace(participant);
		updatePmi(participant);
	}

	private void updateRace(Participant participant) {
		Set<String> newRaceColection = new HashSet<String>();
		for (String race : participant.getRaceCollection()) {
			if (!this.raceCollection.contains(race)) {
				newRaceColection.add(race);
			}
		}
		Set<String> deletedRaces = new HashSet<String>();
		for (String race : raceCollection) {
			if (!participant.getRaceCollection().contains(race)) {
				deletedRaces.add(race);
			}
		}

		raceCollection.removeAll(deletedRaces);
		raceCollection.addAll(newRaceColection);
	}

	private void updatePmi(Participant participant) {
		Iterator<Entry<String, ParticipantMedicalIdentifier>> entries = participantMedicalIdentifierCollection.entrySet()
				.iterator();
		while (entries.hasNext()) {
			Entry<String, ParticipantMedicalIdentifier> entry = entries.next();
			if (!participant.getParticipantMedicalIdentifierCollection().containsKey(entry.getKey())) {
				entries.remove();
			}
		}

		participantMedicalIdentifierCollection.putAll(participant.getParticipantMedicalIdentifierCollection());
	}

}
