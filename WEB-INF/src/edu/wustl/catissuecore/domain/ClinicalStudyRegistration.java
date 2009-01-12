/**
 * <p>Title: CollectionProtocolRegistration Class>
 * <p>Description:  A registration of a Participant to a Clinical Study.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Shital Lawhale
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Date;

import edu.wustl.catissuecore.actionForm.ClinicalStudyRegistrationForm;
import edu.wustl.catissuecore.util.SearchUtil;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * A registration of a Participant to a Clinical Study.
 * @hibernate.class table="CATISSUE_CLINICAL_STUDY_REG"
 */

public class ClinicalStudyRegistration extends AbstractDomainObject implements Serializable
{
	/**
	 * Serial Version ID of the class.
	 */
	private static final long serialVersionUID = 1234567890L;
    /**
     * identifier - System generated unique id.
     */
    protected Long id;
    /**
     * clinicalStudyParticipantIdentifier - A unique number given by a User to a
     * Participant registered to a Clinical Study.
     */
    protected String clinicalStudyParticipantIdentifier;
    /**
     * registrationDate on which the Participant is registered to the Collection Protocol.
     */
    protected Date registrationDate;
    /**
     * An individual from whom a specimen is to be collected.
     */
    protected Participant participant = null;
    /**
     * A set of written procedures that describe how a
     * biospecimen is prospectively collected.
     */
    private ClinicalStudy clinicalStudy;

    /**
     * Defines whether this ClinicalStudyRegistration record can be queried
     * (Active) or not queried (Inactive) by any actor.
     * */
    protected String activityStatus;

    /**
     * Default Constructor.
     */
    public ClinicalStudyRegistration()
    {}

    /**
     * One argument constructor.
     * @param form - AbstractActionForm object.
     * @throws AssignDataException assignDataException.
     */
    public ClinicalStudyRegistration(AbstractActionForm form) throws AssignDataException
    {
        setAllValues(form);
    }

    /**
     * Returns the system generated unique id.
     * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @hibernate.generator-param name="sequence" value="CATISSUE_CLINICAL_STUDY_REG_SEQ"
     * @return the system generated unique id.
     * @see #setId(Long)
     * */
    public Long getId()
    {
        return id;
    }

    /**
     * Sets the system generated unique id.
     * @param identifier the system generated unique id.
     * @see #getId()
     * */
    public void setId(Long identifier)
    {
        this.id = identifier;
    }

    /**
     * Returns the unique number given by a User to a Participant
     * registered to a Clinical Study.
     * @hibernate.property name="clinicalStudyParticipantIdentifier" type="string"
     * column="CLINICAL_STUDY_PARTICIPANT_ID" length="255"
     * @return the unique number given by a User to a Participant
     * registered to a Clinical Study.
     * @see #setClinicalStudyParticipantIdentifier(Long)
     */
    public String getClinicalStudyParticipantIdentifier()
    {
        return clinicalStudyParticipantIdentifier;
    }

    /**
     * Sets the unique number given by a User to a Participant
     * registered to a Collection Protocol.
     * @param clinicalStudyParticipantIdentifier the unique number given by a User to a Participant
     * registered to a Collection Protocol.
     * @see #getClinicalStudyParticipantIdentifier()
     */
    public void setClinicalStudyParticipantIdentifier(String clinicalStudyParticipantIdentifier)
    {
        this.clinicalStudyParticipantIdentifier = clinicalStudyParticipantIdentifier;
    }

    /**
     * Returns the date on which the Participant is
     * registered to the clinicalStudy.
     * @hibernate.property name="registrationDate" column="REGISTRATION_DATE" type="date"
     * @return the date on which the Participant is
     * registered to the clinicalStudy.
     * @see #setRegistrationDate(Date)
     */
    public Date getRegistrationDate()
    {
        return registrationDate;
    }

    /**
     * Sets the date on which the Participant is
     * registered to the Collection Protocol.
     * @param registrationDate the date on which the Participant is
     * registered to the Collection Protocol.
     * @see #getRegistrationDate()
     */
    public void setRegistrationDate(Date registrationDate)
    {
        this.registrationDate = registrationDate;
    }

    /**
     * Returns the individual from whom a specimen is to be collected.
     * @hibernate.many-to-one column="PARTICIPANT_ID"
     * class="edu.wustl.catissuecore.domain.Participant" constrained="true"
     * @return the individual from whom a specimen is to be collected.
     * @see #setParticipant(Participant)
     */
    public Participant getParticipant()
    {
        return participant;
    }

    /**
     * Sets the individual from whom a specimen is to be collected.
     * @param participant the individual from whom a specimen is to be collected.
     * @see #getParticipant()
     */
    public void setParticipant(Participant participant)
    {
        this.participant = participant;
    }

    /**
     * Returns the activity status of the participant.
     * @hibernate.property name="activityStatus" type="string"
     * column="ACTIVITY_STATUS" length="50"
     * @return Returns the activity status of the participant.
     * @see #setActivityStatus(String)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the participant.
     * @param activityStatus activity status of the participant.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Set all values from clinicalStudyRegistrationForm to the member variables of class.
     * @param abstractForm - clinicalStudyRegistrationForm object.
     * @throws AssignDataException assignDataException.
     */
    public void setAllValues(IValueObject abstractForm) throws AssignDataException
    {
        ClinicalStudyRegistrationForm form = (ClinicalStudyRegistrationForm) abstractForm;
        try
        {
            this.activityStatus      = form.getActivityStatus();

            if (SearchUtil.isNullobject(clinicalStudy))
            {
                clinicalStudy = new ClinicalStudy();
            }

            if (SearchUtil.isNullobject(this.registrationDate))
            {
                registrationDate  = new Date();
            }

            this.clinicalStudy.setId(new Long(form.getClinicalStudyId()));

            if(form.getParticipantID() == -1 && form.getParticipantID() == 0)
            {
            	this.participant = null;
            }
            else
            {
            	this.participant = new Participant();
                this.participant.setId(new Long(form.getParticipantID()));
            }

            this.clinicalStudyParticipantIdentifier = form.getParticipantClinicalStudyID().trim();
            if(Constants.DOUBLE_QUOTES.equals(clinicalStudyParticipantIdentifier))
            {
                this.clinicalStudyParticipantIdentifier = null;
            }
            this.registrationDate = Utility.parseDate(form.getRegistrationDate(),
            		Utility.datePattern(form.getRegistrationDate()));
        }
        catch (Exception e)
        {
            Logger.out.error(e.getMessage());
            throw new AssignDataException();
        }
    }

    /**
     * @hibernate.many-to-one column="CLINICAL_STUDY_ID" class="edu.wustl.catissuecore.domain.ClinicalStudy"
     * constrained="true"
     * @return ClinicalStudy object.
     */
    public ClinicalStudy getClinicalStudy()
    {
        return clinicalStudy;
    }

    /**
     * Set the clinicalStudy.
     * @param clinicalStudy of type ClinicalStudy.
     */
    public void setClinicalStudy(ClinicalStudy clinicalStudy)
    {
        this.clinicalStudy = clinicalStudy;
    }

    /**
     * Returns message label to display on success add or edit.
     * @return String with message label.
     */
    public String getMessageLabel()
    {
        if (SearchUtil.isNullobject(clinicalStudy))
        {
            clinicalStudy = new ClinicalStudy();
        }
        StringBuffer message = new StringBuffer();
        message.append(this.clinicalStudy.title + " ");

        if (this.participant != null)
        {
            if (this.participant.lastName!= null && !this.participant.lastName.equals("") &&
            		this.participant.firstName != null && !this.participant.firstName.equals(""))
            {
                message.append(this.participant.lastName + "," + this.participant.firstName);
            }
            else if(this.participant.lastName!= null && !this.participant.lastName.equals(""))
            {
                message.append(this.participant.lastName);
            }
            else if(this.participant.firstName!= null && !this.participant.firstName.equals(""))
            {
                message.append(this.participant.firstName);
            }
        }
        else if (this.clinicalStudyParticipantIdentifier != null)
        {
            message.append(this.clinicalStudyParticipantIdentifier);
        }
        return message.toString();
    }
}