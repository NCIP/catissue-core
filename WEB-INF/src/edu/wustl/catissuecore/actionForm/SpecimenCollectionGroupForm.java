/**
 * <p>Title: SpecimenCollectionGroupForm Class>
 * <p>Description:  SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.actionForm;

import org.apache.struts.action.ActionForm;


/**
 * SpecimenCollectionGroupForm Class is used to encapsulate 
 * all the request parameters passed from New SpecimenCollectionGroup webpage.
 * @author gautam_shetty
 */
public class SpecimenCollectionGroupForm extends ActionForm
{

    private long systemIdentifier;
    
    private String protocolTitle = "";
    
    private String collectedByParticipant = "";
    
    private String participantName = "";
    
    private String collectedByProtocolParticipantNumber = "";
    
    private String protocolParticipantNumber= "";
    
    private String studyCalendarEventPoint = "";
    
    private String clinicalDiagnosis = "";
    
    private String clinicalStatus  = "";
    
    private String medicalRecordNumber = "";
    
    private String surgicalPathologyNumber = "";
    
    /**
     * @return Returns the protocolTitle.
     */
    public String getProtocolTitle()
    {
        return protocolTitle;
    }
    /**
     * @param protocolTitle The protocolTitle to set.
     */
    public void setProtocolTitle(String protocolTitle)
    {
        this.protocolTitle = protocolTitle;
    }
    /**
     * @return Returns the collectedByParticipant.
     */
    public String getCollectedByParticipant()
    {
        return collectedByParticipant;
    }
    /**
     * @param collectedByParticipant The collectedByParticipant to set.
     */
    public void setCollectedByParticipant(String collectedByParticipant)
    {
        this.collectedByParticipant = collectedByParticipant;
    }
    /**
     * @return Returns the collectedByProtocolParticipantNumber.
     */
    public String getCollectedByProtocolParticipantNumber()
    {
        return collectedByProtocolParticipantNumber;
    }
    /**
     * @param collectedByProtocolParticipantNumber The collectedByProtocolParticipantNumber to set.
     */
    public void setCollectedByProtocolParticipantNumber(
            String collectedByProtocolParticipantNumber)
    {
        this.collectedByProtocolParticipantNumber = collectedByProtocolParticipantNumber;
    }
    /**
     * @return Returns the clinicalDiagnosis.
     */
    public String getClinicalDiagnosis()
    {
        return clinicalDiagnosis;
    }
    /**
     * @param clinicalDiagnosis The clinicalDiagnosis to set.
     */
    public void setClinicalDiagnosis(String cinicalDiagnosis)
    {
        this.clinicalDiagnosis = cinicalDiagnosis;
    }
    /**
     * @return Returns the clinicalStatus.
     */
    public String getClinicalStatus()
    {
        return clinicalStatus;
    }
    /**
     * @param clinicalStatus The clinicalStatus to set.
     */
    public void setClinicalStatus(String clinicalStatus)
    {
        this.clinicalStatus = clinicalStatus;
    }
    /**
     * @return Returns the medicalRecordNumber.
     */
    public String getMedicalRecordNumber()
    {
        return medicalRecordNumber;
    }
    /**
     * @param medicalRecordNumber The medicalRecordNumber to set.
     */
    public void setMedicalRecordNumber(String medicalRecordNumber)
    {
        this.medicalRecordNumber = medicalRecordNumber;
    }
    /**
     * @return Returns the participantName.
     */
    public String getParticipantName()
    {
        return participantName;
    }
    /**
     * @param participantName The participantName to set.
     */
    public void setParticipantName(String participantName)
    {
        this.participantName = participantName;
    }
    /**
     * @return Returns the protocolParticipantNumber.
     */
    public String getProtocolParticipantNumber()
    {
        return protocolParticipantNumber;
    }
    /**
     * @param protocolParticipantNumber The protocolParticipantNumber to set.
     */
    public void setProtocolParticipantNumber(String protocolParticipantNumber)
    {
        this.protocolParticipantNumber = protocolParticipantNumber;
    }
    /**
     * @return Returns the studyCalendarEventPoint.
     */
    public String getStudyCalendarEventPoint()
    {
        return studyCalendarEventPoint;
    }
    /**
     * @param studyCalendarEventPoint The studyCalendarEventPoint to set.
     */
    public void setStudyCalendarEventPoint(String studyCalendarEventPoint)
    {
        this.studyCalendarEventPoint = studyCalendarEventPoint;
    }
    /**
     * @return Returns the surgicalPathologyNumber.
     */
    public String getSurgicalPathologyNumber()
    {
        return surgicalPathologyNumber;
    }
    /**
     * @param surgicalPathologyNumber The surgicalPathologyNumber to set.
     */
    public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
    {
        this.surgicalPathologyNumber = surgicalPathologyNumber;
    }
    /**
     * @return Returns the systemIdentifier.
     */
    public long getSystemIdentifier()
    {
        return systemIdentifier;
    }
    /**
     * @param systemIdentifier The systemIdentifier to set.
     */
    public void setSystemIdentifier(long systemIdentifier)
    {
        this.systemIdentifier = systemIdentifier;
    }
}
