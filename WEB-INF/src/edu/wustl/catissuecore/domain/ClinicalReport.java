/**
 * <p>Title: ClinicalReport Class>
 * <p>Description:  A clinical report associated with the participant 
 * at the time of the SpecimenCollection Group receipt. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;


/**
 * A clinical report associated with the participant 
 * at the time of the SpecimenCollection Group receipt.
 * @hibernate.class table="CATISSUE_CLINICAL_REPORT"
 * @author gautam_shetty
 */
public class ClinicalReport extends AbstractDomainObject implements java.io.Serializable
{
    private static final long serialVersionUID = 1234567890L;

    /**
     * System generated unique id.
     */
    protected Long id;

    /**
     * Hospital surgical pathology number associated with the current specimen collection group.
     */
    protected String surgicalPathologyNumber;

    /**
     * A medical record identification number that refers to a Participant.
     */
    protected ParticipantMedicalIdentifier participantMedicalIdentifier;

    /**
     * Returns the system generated unique id.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_CLINICAL_REPORT_SEQ"
     * @return the system generated unique id.
     * @see #setId(Long)
     * */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets the system generated unique id.
     * @param id the system generated unique id.
     * @see #getId()
     * */
    public void setId(Long id)
    {
        this.id = id;
    }

    /**
     * Returns the hospital surgical pathology number associated with 
     * the current specimen collection group.
     * @hibernate.property name="surgicalPathologyNumber" type="string" column="SURGICAL_PATHOLOGICAL_NUMBER" length="50"
     * @return the hospital surgical pathology number associated with 
     * the current specimen collection group.
     * @see #setSurgicalPathologyNumber(String)
     */
    public String getSurgicalPathologyNumber()
    {
        return surgicalPathologyNumber;
    }

    /**
     * Sets the hospital surgical pathology number associated with 
     * the current specimen collection group. 
     * @param surgicalPathologyNumber the hospital surgical pathology number associated with 
     * the current specimen collection group.
     * @see #getSurgicalPathologyNumber()
     */
    public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
    {
        this.surgicalPathologyNumber = surgicalPathologyNumber;
    }

    /**
     * Returns the medical record identification number that refers to a Participant.
     * @hibernate.many-to-one column="PARTICIPENT_MEDI_IDENTIFIER_ID" 
     * class="edu.wustl.catissuecore.domain.ParticipantMedicalIdentifier" constrained="true"
     * @return the medical record identification number that refers to a Participant.
     * @see #setParticipantMedicalIdentifier(ParticipantMedicalIdentifier)
     */
    public ParticipantMedicalIdentifier getParticipantMedicalIdentifier()
    {
        return participantMedicalIdentifier;
    }

    /**
     * Sets the medical record identification number that refers to a Participant.
     * @param participantMedicalIdentifier the medical record identification number that refers to a Participant.
     * @see #getParticipantMedicalIdentifier()
     */
    public void setParticipantMedicalIdentifier(ParticipantMedicalIdentifier participantMedicalIdentifier)
    {
        this.participantMedicalIdentifier = participantMedicalIdentifier;
    }
 
	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
	{
		
	}
	
}