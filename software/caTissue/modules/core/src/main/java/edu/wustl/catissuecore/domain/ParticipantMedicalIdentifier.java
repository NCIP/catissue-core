package edu.wustl.catissuecore.domain;

import java.io.Serializable;

import org.apache.commons.lang.StringUtils;

import edu.wustl.catissuecore.util.IStringIdentifiable;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.participant.domain.IParticipantMedicalIdentifier;
/**
	*
	**/

public class ParticipantMedicalIdentifier extends AbstractDomainObject implements Serializable, IParticipantMedicalIdentifier<Participant, Site>, IStringIdentifiable
{
	/**
	* An attribute to allow serialization of the domain objects
	*/
	private static final long serialVersionUID = 1234567890L;


	/**
	* System generated unique id.
	**/

	private Long id;
	/**
	* Retrieves the value of the id attribute
	* @return id
	**/

	public Long getId(){
		return id;
	}

	/**
	* Sets the value of id attribute
	**/

	public void setId(Long id){
		this.id = id;
	}

	/**
	* Participant's medical record number used in their medical treatment.
	**/

	private String medicalRecordNumber;
	/**
	* Retrieves the value of the medicalRecordNumber attribute
	* @return medicalRecordNumber
	**/

	public String getMedicalRecordNumber(){
		return medicalRecordNumber;
	}

	/**
	* Sets the value of medicalRecordNumber attribute
	**/

	public void setMedicalRecordNumber(String medicalRecordNumber){
		this.medicalRecordNumber = medicalRecordNumber;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Site object
	**/

	private Site site;
	/**
	* Retrieves the value of the site attribute
	* @return site
	**/

	public Site getSite(){
		return site;
	}
	/**
	* Sets the value of site attribute
	**/

	public void setSite(Site site){
		this.site = site;
	}

	/**
	* An associated edu.wustl.catissuecore.domain.Participant object
	**/

	private Participant participant;
	/**
	* Retrieves the value of the participant attribute
	* @return participant
	**/

	public Participant getParticipant(){
		return participant;
	}
	/**
	* Sets the value of participant attribute
	**/

	public void setParticipant(Participant participant){
		this.participant = participant;
	}

	/**
	* Compares <code>obj</code> to it self and returns true if they both are same
	*
	* @param obj
	**/
	public boolean equals(Object obj)
	{
		if(obj instanceof ParticipantMedicalIdentifier)
		{
			ParticipantMedicalIdentifier c =(ParticipantMedicalIdentifier)obj;
			if(getId() != null && getId().equals(c.getId()))
				return true;
		}
		return false;
	}

	/**
	* Returns hash code for the primary key of the object
	**/
	public int hashCode()
	{
		if(getId() != null)
			return getId().hashCode();
		return 0;
	}

	@Override
	public int compareTo(IStringIdentifiable o) {		
		return (getAsString()+"").compareTo(o.getAsString()+"");
	}

	@Override
	public String getAsString() {
		String str = "";
		if (StringUtils.isNotBlank(medicalRecordNumber)) {
			if (site != null) {
				str += (site.getName() + ": ");
			}
			str += medicalRecordNumber;
		}
		return str;

	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ParticipantMedicalIdentifier [id=" + id
				+ ", medicalRecordNumber=" + medicalRecordNumber + ", site="
				+ site + "]";
	}

}