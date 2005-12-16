/**
 * <p>Title: ParticipantMedicalIdentifier Class>
 * <p>Description:  A medical record identification inforatioin that refers to a Participant. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Aniruddha Phadnis
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * A medical record identification number that refers to a Participant.
 * @hibernate.class table="CATISSUE_PARTICIPANT_MEDICAL_IDENTIFIER"
 */
public class ParticipantMedicalIdentifier extends AbstractDomainObject implements java.io.Serializable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique systemIdentifier
     */
	protected Long systemIdentifier;

	/**
     * Participant's medical record number used in their medical treatment.
     */
	protected String medicalRecordNumber;
	
	/**
     * Source of medical record number.
     */
	protected Site site = new Site();
	
	/**
     * 
     */
	protected Participant participant;

	/**
     * Returns System generated unique identifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return System generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     */
	public Long getSystemIdentifier()
	{
		return systemIdentifier;
	}

	/**
     * Sets a system systemIdentifier for a particular medical record.
     * @param systemIdentifier systemIdentifier for a particular medical record.
     * @see #getSystemIdentifier()
     * */
	public void setSystemIdentifier(Long systemIdentifier)
	{
		this.systemIdentifier = systemIdentifier;
	}

	/**
     * Returns the Medical Record Number.
     * @hibernate.property name="medicalRecordNumber" type="string" 
     * column="MEDICAL_RECORD_NUMBER" length="50"
     * @return the Medical Record Number.
     * @see #setMedicalRecordNumber(String)
     */
	public String getMedicalRecordNumber()
	{
		return medicalRecordNumber;
	}

	/**
     * Sets a Medical Record Number.
     * @param medicalRecordNumber Medical Record Number.
     * @see #getMedicalRecordNumber()
     */
	public void setMedicalRecordNumber(String medicalRecordNumber)
	{
		this.medicalRecordNumber = medicalRecordNumber;
	}

	/**
     * Returns the source of medical record number.
     * @hibernate.many-to-one column="SITE_ID"  class="edu.wustl.catissuecore.domain.Site" constrained="true"
     * @return the source of medical record number.
	 * @see #setSite(Site)
     */
	public Site getSite()
	{
		return site;
	}

	/**
     * Returns the source of medical record number.
     * @return the source of medical record number.
	 * @see #setSite(Site)
     */
	public void setSite(Site site)
	{
		this.site = site;
	}
	
	/**
     * @hibernate.many-to-one column="PARTICIPANT_ID"  class="edu.wustl.catissuecore.domain.Participant" constrained="true"
	 * @see #setParticipant(Site)
     */
	public Participant getParticipant() 
	{
		return participant;
	}
	
	/**
	 * @param participant The participant to set.
	 */
	public void setParticipant(Participant participant) 
	{
		this.participant = participant;
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
	
	}
	
	public String toString()
	{
		return "ParticipantMedicalIdentifier: "+systemIdentifier+", "+medicalRecordNumber+", "+site;
	}
}