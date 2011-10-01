
package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;

import edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry;
import edu.wustl.common.bizlogic.IActivityStatus;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.participant.domain.IParticipant;

/**
	*
	**/

public class Participant extends AbstractDomainObject
		implements
			Serializable,
			IParticipant<Race, ParticipantMedicalIdentifier>,
			IActivityStatus
{

	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;

	/**
	* Defines whether this participant record can be queried (Active) or not queried (Inactive) by any actor.
	**/

	private String activityStatus;

	/**
	* Retrieves the value of the activityStatus attribute
	* @return activityStatus
	**/

	public String getActivityStatus()
	{
		return activityStatus;
	}

	/**
	* Sets the value of activityStatus attribute
	**/

	public void setActivityStatus(String activityStatus)
	{
		this.activityStatus = activityStatus;
	}

	/**
	* Birth date of participant.
	**/

	private java.util.Date birthDate;

	/**
	* Retrieves the value of the birthDate attribute
	* @return birthDate
	**/

	public java.util.Date getBirthDate()
	{
		return birthDate;
	}

	/**
	* Sets the value of birthDate attribute
	**/

	public void setBirthDate(java.util.Date birthDate)
	{
		this.birthDate = birthDate;
	}

	/**
	* Death date of participant.
	**/

	private java.util.Date deathDate;

	/**
	* Retrieves the value of the deathDate attribute
	* @return deathDate
	**/

	public java.util.Date getDeathDate()
	{
		return deathDate;
	}

	/**
	* Sets the value of deathDate attribute
	**/

	public void setDeathDate(java.util.Date deathDate)
	{
		this.deathDate = deathDate;
	}

	/**
	* Participant's ethnicity status.
	**/

	private String ethnicity;

	/**
	* Retrieves the value of the ethnicity attribute
	* @return ethnicity
	**/

	public String getEthnicity()
	{
		return ethnicity;
	}

	/**
	* Sets the value of ethnicity attribute
	**/

	public void setEthnicity(String ethnicity)
	{
		this.ethnicity = ethnicity;
	}

	/**
	* First name of the participant.
	**/

	private String firstName;

	/**
	* Retrieves the value of the firstName attribute
	* @return firstName
	**/

	public String getFirstName()
	{
		return firstName;
	}

	/**
	* Sets the value of firstName attribute
	**/

	public void setFirstName(String firstName)
	{
		this.firstName = firstName;
	}

	/**
	* The gender of the participant.
	**/

	private String gender;

	/**
	* Retrieves the value of the gender attribute
	* @return gender
	**/

	public String getGender()
	{
		return gender;
	}

	/**
	* Sets the value of gender attribute
	**/

	public void setGender(String gender)
	{
		this.gender = gender;
	}

	/**
	* System generated unique id.
	**/

	private Long id;

	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId()
	{
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	* Last name of the participant.
	**/

	private String lastName;

	/**
	* Retrieves the value of the lastName attribute
	* @return lastName
	**/

	public String getLastName()
	{
		return lastName;
	}

	/**
	* Sets the value of lastName attribute
	**/

	public void setLastName(String lastName)
	{
		this.lastName = lastName;
	}

	/**
	* metaPhone Code
	**/

	private String metaPhoneCode;

	/**
	* Retrieves the value of the metaPhoneCode attribute
	* @return metaPhoneCode
	**/

	public String getMetaPhoneCode()
	{
		return metaPhoneCode;
	}

	/**
	* Sets the value of metaPhoneCode attribute
	**/

	public void setMetaPhoneCode(String metaPhoneCode)
	{
		this.metaPhoneCode = metaPhoneCode;
	}

	/**
	* Middle name of the participant.
	**/

	private String middleName;

	/**
	* Retrieves the value of the middleName attribute
	* @return middleName
	**/

	public String getMiddleName()
	{
		return middleName;
	}

	/**
	* Sets the value of middleName attribute
	**/

	public void setMiddleName(String middleName)
	{
		this.middleName = middleName;
	}

	/**
	* The genetic constitution of the individual.
	**/

	private String sexGenotype;

	/**
	* Retrieves the value of the sexGenotype attribute
	* @return sexGenotype
	**/

	public String getSexGenotype()
	{
		return sexGenotype;
	}

	/**
	* Sets the value of sexGenotype attribute
	**/

	public void setSexGenotype(String sexGenotype)
	{
		this.sexGenotype = sexGenotype;
	}

	/**
	* Social Security Number of participant.
	**/

	private String socialSecurityNumber;

	/**
	* Retrieves the value of the socialSecurityNumber attribute
	* @return socialSecurityNumber
	**/

	public String getSocialSecurityNumber()
	{
		return socialSecurityNumber;
	}

	/**
	* Sets the value of socialSecurityNumber attribute
	**/

	public void setSocialSecurityNumber(String socialSecurityNumber)
	{
		this.socialSecurityNumber = socialSecurityNumber;
	}

	/**
	* Defines the vital status of the participant like 'Dead', 'Alive' or 'Unknown'.
	**/

	private String vitalStatus;

	/**
	* Retrieves the value of the vitalStatus attribute
	* @return vitalStatus
	**/

	public String getVitalStatus()
	{
		return vitalStatus;
	}

	/**
	* Sets the value of vitalStatus attribute
	**/

	public void setVitalStatus(String vitalStatus)
	{
		this.vitalStatus = vitalStatus;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.CollectionProtocolRegistration object's collection
	**/

	private Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection;

	/**
	* Retrieves the value of the collectionProtocolRegistrationCollection attribute
	* @return collectionProtocolRegistrationCollection
	**/

	public Collection<CollectionProtocolRegistration> getCollectionProtocolRegistrationCollection()
	{
		return collectionProtocolRegistrationCollection;
	}

	/**
	* Sets the value of collectionProtocolRegistrationCollection attribute
	**/

	public void setCollectionProtocolRegistrationCollection(
			Collection<CollectionProtocolRegistration> collectionProtocolRegistrationCollection)
	{
		this.collectionProtocolRegistrationCollection = collectionProtocolRegistrationCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Race object's collection
	**/

	private Collection<Race> raceCollection;

	/**
	* Retrieves the value of the raceCollection attribute
	* @return raceCollection
	**/

	public Collection<Race> getRaceCollection()
	{
		return raceCollection;
	}

	/**
	* Sets the value of raceCollection attribute
	**/

	public void setRaceCollection(Collection<Race> raceCollection)
	{
		this.raceCollection = raceCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier object's collection
	**/

	private Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection;

	/**
	* Retrieves the value of the participantMedicalIdentifierCollection attribute
	* @return participantMedicalIdentifierCollection
	**/

	public Collection<ParticipantMedicalIdentifier> getParticipantMedicalIdentifierCollection()
	{
		return participantMedicalIdentifierCollection;
	}

	/**
	* Sets the value of participantMedicalIdentifierCollection attribute
	**/

	public void setParticipantMedicalIdentifierCollection(
			Collection<ParticipantMedicalIdentifier> participantMedicalIdentifierCollection)
	{
		this.participantMedicalIdentifierCollection = participantMedicalIdentifierCollection;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.deintegration.ParticipantRecordEntry object's collection
	**/

	private Collection<ParticipantRecordEntry> participantRecordEntryCollection;

	/**
	* Retrieves the value of the participantRecordEntryCollection attribute
	* @return participantRecordEntryCollection
	**/

	public Collection<ParticipantRecordEntry> getParticipantRecordEntryCollection()
	{
		return participantRecordEntryCollection;
	}

	/**
	* Sets the value of participantRecordEntryCollection attribute
	**/

	public void setParticipantRecordEntryCollection(
			Collection<ParticipantRecordEntry> participantRecordEntryCollection)
	{
		this.participantRecordEntryCollection = participantRecordEntryCollection;
	}
	
	private String gridId;

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if (obj instanceof Participant)
		{
			Participant c = (Participant) obj;
			if (getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if (getId() != null)
			return getId().hashCode();
		return 0;
	}

	/**
	 * @return the gridId
	 */
	public String getGridId() {
		return gridId;
	}

	/**
	 * @param gridId the gridId to set
	 */
	public void setGridId(String gridId) {
		this.gridId = gridId;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "Participant ["
				+ (activityStatus != null ? "activityStatus=" + activityStatus
						+ ", " : "")
				+ (birthDate != null ? "birthDate=" + birthDate + ", " : "")
				+ (deathDate != null ? "deathDate=" + deathDate + ", " : "")
				+ (ethnicity != null ? "ethnicity=" + ethnicity + ", " : "")
				+ (firstName != null ? "firstName=" + firstName + ", " : "")
				+ (gender != null ? "gender=" + gender + ", " : "")
				+ (id != null ? "id=" + id + ", " : "")
				+ (lastName != null ? "lastName=" + lastName + ", " : "")
				+ (metaPhoneCode != null ? "metaPhoneCode=" + metaPhoneCode
						+ ", " : "")
				+ (middleName != null ? "middleName=" + middleName + ", " : "")
				+ (sexGenotype != null ? "sexGenotype=" + sexGenotype + ", "
						: "")
				+ (socialSecurityNumber != null ? "socialSecurityNumber="
						+ socialSecurityNumber + ", " : "")
				+ (vitalStatus != null ? "vitalStatus=" + vitalStatus + ", "
						: "") + (gridId != null ? "gridId=" + gridId : "")
				+ "]";
	}

}