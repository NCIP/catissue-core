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

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.exception.AssignDataException;


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
     * System generated unique systemIdentifier.
     */
    protected Long systemIdentifier;

    /**
     * Hospital surgical pathology number associated with the current specimen collection group.
     */
    protected String surgicalPathologyNumber;

    /**
     * A medical record identification number that refers to a Participant.
     */
    protected ParticipantMedicalIdentifier participantMedicalIdentifier;

    /**
     * Returns the system generated unique systemIdentifier.
     * @hibernate.id name="systemIdentifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return the system generated unique systemIdentifier.
     * @see #setSystemIdentifier(Long)
     * */
    public Long getSystemIdentifier()
    {
        return systemIdentifier;
    }

    /**
     * Sets the system generated unique systemIdentifier.
     * @param systemIdentifier the system generated unique systemIdentifier.
     * @see #getSystemIdentifier()
     * */
    public void setSystemIdentifier(Long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
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
     * @hibernate.many-to-one column="PARTICIPENT_MEDICAL_IDENTIFIER_ID" 
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
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
	{
		
	}
}