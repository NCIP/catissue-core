/**
 * <p>Title: Accession Class>
 * <p>Description:  Models the Accession information.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import edu.wustl.catissuecore.actionForm.AbstractActionForm;
import edu.wustl.catissuecore.actionForm.AccessionForm;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.common.util.logger.Logger;

/**
 * Models the Accession information.
 * @author gautam_shetty
 * @hibernate.class table="CATISSUE_ACCESSION"
 */
public class Accession extends AbstractDomainObject implements Serializable
{
	/**
	 * Serial Version ID
	 */ 
    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each Accession.
     * */
    protected Long identifier;

    /**
     * Protocol's participant ID number.
     */
    protected Long protocolNumber;

    /**
     * The Medical record number associated with this accession.
     */
    protected String medicalRecordNumber;

    /**
     * The URL to text of medical summary report for this accession.
     */
    protected String medicalReportURL;

    /**
     * The Participant's clinical diagnosis at this collection event.
     */
    protected String clinicalDiagnosis;

    /**
     * The Participant's disease status at this collection event.
     */
    protected String diseaseStatus;

    /**
     * The hospital surgical pathology number associated with this accession.
     */
    protected String surgicalPathologyNumber;

    /**
     * The URL to text of surgical pathology report for this accession.
     */
    protected String surgicalPathologyReportURL;

    /**
     * The Collection Time of the Accession.
     */
    protected Date collectionTime;
    
    /**
     * Site at which the Biospecimens are collected.
     */
    private Site collectionSite;
    
    /**
     * The technician who collected the Biospecimen.
     */
    protected User collectedBy;

    /**
     * The mode in which the accession was received.(by hand, courier, FedEX, UPS).
     */
    protected String receivedMode;

    /**
     * The tracking number of accession shipment.
     */
    protected String receivedTrackNumber;

    /**
     * The Received Time of the Accession.
     */
    protected Date receivedTime;
    
    /**
     * The Site where the Biospecimens are received.
     */
    private Site receivedSite;
    
    /**
     * The technician who received the Biospecimen.
     */
    protected User receivedBy;

    /**
     * The activity status of the accession.
     */
    protected ActivityStatus activityStatus;

    /**
     * The Protocol used for the accession.
     */
    private Protocol protocol;

    /**
     * Associated Participant in the accession.
	 * NOTE: Used by hibernate for mapping Experiment and Group.
     */  
    private Participant participant;
    
    private Collection specimenCollection = new HashSet();

    private Collection infectionAgentCollection = new HashSet();
	
    /**
     * Initialize a new Accession instance. 
     * Note: Hibernate invokes this constructor through reflection API.  
     */
    public Accession()
    {
    }

    /**
     * This Constructor Copies the data from an AccessionForm object to a Accession object.
     * @param accessionForm An AccessionForm object.  
     */
    public Accession(AccessionForm accessionForm)
    {
        setAllValues(accessionForm);
    }

    /**
	 * Returns the unique identifier assigned to accession.
	 * @hibernate.id name="identifier" column="IDENTIFIER" type="long" length="30"
     * unsaved-value="null" generator-class="native"
     * @return returns a unique identifier assigned to the accession.
     * @see #setIdentifier(Long)
	 * */
    public Long getIdentifier()
    {
        return identifier;
    }

    /**
	 * Sets an identifier for the accession.
	 * @param identifier identifier for the accession.
	 * @see #getIdentifier()
	 * */
    public void setIdentifier(Long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns Protocol's participant ID number.
     * @hibernate.property name="protocolNumber" type="long" 
     * column="PROTOCOL_NUMBER" length="50"
     * @return Protocol's participant ID number.
     * @see #setProtocolNumber(Long)
     */
    public Long getProtocolNumber()
    {
        return protocolNumber;
    }

    /**
     * Sets Protocol's participant ID number.
     * @param protocolNumber the Protocol's participant ID number.
     * @see #getProtocolNumber()
     */
    public void setProtocolNumber(Long protocolNumber)
    {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Returns the Medical record number associated with this accession.
     * @hibernate.property name="medicalRecordNumber" type="string" 
     * column="MEDICAL_RECORD_NUMBER" length="50"
     * @return the Medical record number associated with this accession.
     * @see #setMedicalRecordNumber(String)
     */
    public String getMedicalRecordNumber()
    {
        return medicalRecordNumber;
    }

    /**
     * Sets the Medical record number associated with this accession.
     * @param medicalRecordNumber the Medical record number associated with this accession.
     * @see #getMedicalRecordNumber()
     */
    public void setMedicalRecordNumber(String medicalRecordNumber)
    {
        this.medicalRecordNumber = medicalRecordNumber;
    }

    /**
     * Returns the URL to text of medical summary report for this accession.
     * @hibernate.property name="medicalReportURL" type="string" 
     * column="MEDICAL_REPORT_URL" length="100"
     * @return the URL to text of medical summary report for this accession.
     * @see #setMedicalReportURL(String)
     */
    public String getMedicalReportURL()
    {
        return medicalReportURL;
    }

    /**
     * Sets the URL to text of medical summary report for this accession.
     * @param medicalReportURL the URL to text of medical summary report for this accession.
     * @see #getMedicalReportURL()
     */
    public void setMedicalReportURL(String medicalReportURL)
    {
        this.medicalReportURL = medicalReportURL;
    }

    /**
     * Returns the Participant's clinical diagnosis at this collection event.
     * @hibernate.property name="clinicalDiagnosis" type="string" 
     * column="CLINICAL_DIAGNOSIS" length="50" 
     * @return the Participant's clinical diagnosis.
     * @see #setClinicalDiagnosis(String) 
     */
    public String getClinicalDiagnosis()
    {
        return clinicalDiagnosis;
    }

    /**
     * Sets the Participant's clinical diagnosis at this collection event.
     * @param clinicalDiagnosis the Participant's clinical diagnosis.
     * @see #getClinicalDiagnosis()
     */
    public void setClinicalDiagnosis(String clinicalDiagnosis)
    {
        this.clinicalDiagnosis = clinicalDiagnosis;
    }

    /**
     * Returns the Participant's disease status at this collection event.
     * @hibernate.property name="diseaseStatus" type="string" 
     * column="DISEASE_STATUS" length="50" 
     * @return the disease status of the Participant.
     * @see #setDiseaseStatus(String)
     */
    public String getDiseaseStatus()
    {
        return diseaseStatus;
    }

    /**
     * Sets the Participant's disease status at this collection event.
     * @param diseaseStatus disease status of the participant.
     * @see #getDiseaseStatus()
     */
    public void setDiseaseStatus(String diseaseStatus)
    {
        this.diseaseStatus = diseaseStatus;
    }

    /**
     * Returns the hospital's surgical pathology number associated with this accession.
     * @hibernate.property name="surgicalPathologyNumber" type="string" 
     * column="SURGICAL_PATHOLOGY_NUMBER" length="50"
     * @return the hospital's surgical pathology number associated with this accession.
     * @see #setSurgicalPathologyNumber(String)
     */
    public String getSurgicalPathologyNumber()
    {
        return surgicalPathologyNumber;
    }

    /**
     * Sets the hospital surgical pathology number associated with this accession.
     * @param surgicalPathologyNumber the hospital surgical pathology number associated with this accession.
     * @see #getSurgicalPathologyNumber()
     */
    public void setSurgicalPathologyNumber(String surgicalPathologyNumber)
    {
        this.surgicalPathologyNumber = surgicalPathologyNumber;
    }

    /**
     * Returns the URL to text of surgical pathology report for this accession.
     * @hibernate.property name="surgicalPathologyReportURL" type="string" 
     * column="SURGICAL_PATHOLOGY_REPORT_URL" length="100"
     * @return the URL to text of surgical pathology report for this accession.
     * @see #setSurgicalPathologyReportURL(String)
     */
    public String getSurgicalPathologyReportURL()
    {
        return surgicalPathologyReportURL;
    }
    
    /**
     * Sets the URL to text of surgical pathology report for this accession.
     * @param surgicalPathologyReportURL the URL to text of surgical pathology report for this accession.
     * @see #getSurgicalPathologyReportURL()
     */
    public void setSurgicalPathologyReportURL(String surgicalPathologyReportURL)
    {
        this.surgicalPathologyReportURL = surgicalPathologyReportURL;
    }
    
    /**
     * Returns the Collection Time of the Accession.
     * @hibernate.property name="collectionTime" type="date"
     * column="COLLECTION_TIME" length="50"
     * @return the Collection Time of the Accession.
     * @see #setCollectionTime(Date)
     */
    public Date getCollectionTime()
    {
        return collectionTime;
    }

    /**
     * Sets the Collection Time of the Accession.
     * @param collectionTime the Collection Time of the Accession.
     * @see #getCollectionTime()
     */
    public void setCollectionTime(Date collectionTime)
    {
        this.collectionTime = collectionTime;
    }
    
    /**
     * Returns the technician who collected the Biospecimen.
     * @hibernate.many-to-one column="COLLECTED_BY" 
	 * class="edu.wustl.catissuecore.domain.User" constrained="true"
     * @return the technician who collected the Biospecimen.
     * @see #setCollectedBy(User)
     */
    public User getCollectedBy()
    {
        return collectedBy;
    }

    /**
     * Sets the technician who collected the Biospecimen.
     * @param collectedBy An User object representing the technician.
     * @see #getCollectedBy()
     */
    public void setCollectedBy(User collectedBy)
    {
        this.collectedBy = collectedBy;
    }
    
//    /**
//     * Returns the Site at which the Biospecimens are collected.
//     * @hibernate.many-to-one column="COLLECTION_SITE_IDENTIFIER" 
//	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
//     * @return the Site at which the Biospecimens are collected.
//     */
//    public Site getCollectionSite()
//    {
//        return collectionSite;
//    }
//    
//    /**
//     * Sets the Site at which the Biospecimens are collected.
//     * @param collectionSite the Site at which the Biospecimens are collected.
//     */
//    public void setCollectionSite(Site collectionSite)
//    {
//        this.collectionSite = collectionSite;
//    }
    
    /**
     * Returns the mode through which the biospecimens was received(by hand, courier, FedEX, UPS).
     * @hibernate.property name="receivedMode" type="string"
     * column="RECEIVED_MODE" length="50"
     * @return the mode thruogh biospecimens was received(by hand, courier, FedEX, UPS).
     * @see #setReceivedMode(String)
     */
    public String getReceivedMode()
    {
        return receivedMode;
    }

    /**
     * Sets the mode through which the biospecimens was received(by hand, courier, FedEX, UPS).
     * @param receivedMode the mode thruogh biospecimens was received(by hand, courier, FedEX, UPS).
     * @see #getReceivedMode()
     */
    public void setReceivedMode(String receivedMode)
    {
        this.receivedMode = receivedMode;
    }

    /**
     * Returns the tracking number of accession shipment.
     * @hibernate.property name="receivedTrackNumber" type="string"
     * column="RECEIVED_TRACK_NUMBER" length="50"
     * @return the tracking number of accession shipment.
     * @see #setReceivedTrackNumber(String)
     */
    public String getReceivedTrackNumber()
    {
        return receivedTrackNumber;
    }

    /**
     * Sets the tracking number of accession shipment.
     * @param receivedTrackNumber the tracking number of accession shipment.
     * @see #getReceivedTrackNumber()
     */
    public void setReceivedTrackNumber(String receivedTrackNumber)
    {
        this.receivedTrackNumber = receivedTrackNumber;
    }

    /**
     * Returns the received time of the Accession.
     * @hibernate.property name="receivedTime" type="date"
     * column="RECEIVED_TIME" length="50"
     * @return the received time of the Accession.
     * @see #setReceivedTime(Date)  
     */
    public Date getReceivedTime()
    {
        return receivedTime;
    }

    /**
     * Sets the received time of the Accession.
     * @param receivedTime the received time of the Accession.
     * @see #getReceivedTime()
     */
    public void setReceivedTime(Date receivedTime)
    {
        this.receivedTime = receivedTime;
    }

    /**
     * Returns the technician who received the Biospecimen.
     * @hibernate.many-to-one column="RECEIVED_BY" 
	 * class="edu.wustl.catissuecore.domain.User" constrained="true"
     * @return the technician who received the Biospecimen.
     * @see #setReceivedBy(User)
     */
    public User getReceivedBy()
    {
        return receivedBy;
    }

    /**
     * Sets the technician who received the Biospecimen.
     * @param receivedBy the technician who received the Biospecimen.
     * @see #getReceivedBy() 
     */
    public void setReceivedBy(User receivedBy)
    {
        this.receivedBy = receivedBy;
    }
    
//    /**
//     * Returns the Site at which the Biospecimens are received.
//     * @hibernate.many-to-one column="RECEIVED_SITE_ID" 
//	 * class="edu.wustl.catissuecore.domain.Site" constrained="true"
//     * @return the Site at which the Biospecimens are received.
//     */
//    public Site getReceivedSite()
//    {
//        return receivedSite;
//    }
//
//    /**
//     * Sets the Site at which the Biospecimens are received.
//     * @param receivedSite the Site at which the Biospecimens are received.
//     */	
//    public void setReceivedSite(Site receivedSite)
//    {
//        this.receivedSite = receivedSite;
//    }

    /**
	 * Returns the activity status of the accession.
	 * @hibernate.many-to-one column="ACTIVITY_STATUS_IDENTIFIER" 
	 * class="edu.wustl.catissuecore.domain.ActivityStatus" constrained="true"
	 * @return Returns the activity status of the accession.
	 * @see #setActivityStatus(ActivityStatus)
	 */
    public ActivityStatus getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the accession.
     * @param activityStatus the activity status of the accession.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(ActivityStatus activityStatus)
    {
        this.activityStatus = activityStatus;
    }

//    /**
//     * Returns the Protocol used for this Accession.
//     * @hibernate.many-to-one column="PROTOCOL_IDENTIFIER" 
//	 * class="edu.wustl.catissuecore.domain.Protocol" constrained="true"
//     * @return A Protocol object representing the Protocol used for this Accession.
//     */
//    public Protocol getProtocol()
//    {
//        return protocol;
//    }
//
//    /**
//     * Sets the Protocol used for this Accession.
//     * @param protocol A Protocol object representing the Protocol used for this Accession.
//     */
//    public void setProtocol(Protocol protocol)
//    {
//        this.protocol = protocol;
//    }
  
    /**
     * Returns the participant associated with the accession.
     * @hibernate.many-to-one column="PARTICIPANT_IDENTIFIER" 
	 * class="edu.wustl.catissuecore.domain.Participant"
     * @return the participant associated with the accession.
     * @see #setParticipant(Participant)
     */
    public Participant getParticipant()
    {
        return participant;
    }

    /**
     * Sets the participant associated with the accession. 
     * @param participant the participant associated with the accession.
     * @see #getParticipant()
     */
    public void setParticipant(Participant participant)
    {
        this.participant = participant;
    }

    
    
//    /**
//	 * Returns the collection of specimens collected in this accession.
//	 * @return the collection of specimens collected in this accession.
//	 * @hibernate.set name="specimenCollection" table="CATISSUE_SPECIMEN" cascade="save-update" inverse="true" lazy="false"
//	 * @hibernate.collection-key column="ACCESSION_IDENTIFIER"
//	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
//	 * @see #setSpecimenCollection(Collection)
//	 */
//    public Collection getSpecimenCollection()
//    {
//        return specimenCollection;
//    }
//
//    /**
//     * Sets the collection of specimens collected in this accession.
//     * @param specimenCollection the collection of specimens collected in this accession.
//     */
//    public void setSpecimenCollection(Collection specimenCollection)
//    {
//        this.specimenCollection = specimenCollection;
//    }

//    /**
//	 * Returns the collection of specimens collected in this accession.
//	 * @return the collection of specimens collected in this accession.
//	 * @hibernate.set name="specimenCollection" table="CATISSUE_SPECIMEN" cascade="save-update" inverse="true" lazy="false"
//	 * @hibernate.collection-key column="ACCESSION_IDENTIFIER"
//	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
//	 * @see #setSpecimenCollection(Collection)
//	 */
//    public Collection getInfectionAgentCollection()
//    {
//        return infectionAgentCollection;
//    }
//
//    public void setInfectionAgentCollection(Collection infectionAgentCollection)
//    {
//        this.infectionAgentCollection = infectionAgentCollection;
//    }

    
    /**
     * This function Copies the data from an AccessionForm object to a Accession object.
     * @param accessionForm An AccessionForm object containing the information about the accession.  
     * */
    public void setAllValues(AbstractActionForm abstractForm)
    {
        try
        {
            AccessionForm accessionForm = (AccessionForm) abstractForm;
            this.identifier = new Long(accessionForm.getIdentifier());
            this.protocolNumber = new Long(accessionForm.getProtocolNumber());
            this.medicalRecordNumber = accessionForm.getMedicalRecordNumber();
            this.medicalReportURL = accessionForm.getMedicalReportURL();
            this.clinicalDiagnosis = accessionForm.getClinicalDiagnosis();
            this.diseaseStatus = accessionForm.getDiseaseStatus();
            this.surgicalPathologyNumber = accessionForm
                    .getSurgicalPathologyNumber();
            this.surgicalPathologyReportURL = accessionForm
                    .getSurgicalPathologyReportURL();
            this.collectionTime = new Date();
            this.collectionTime = Utility.parseTime(accessionForm.getCollectionDate(),accessionForm.getCollectionTimeHour(),accessionForm.getCollectionTimeMinutes(),accessionForm.getCollectionTimeAMPM());
            //this.collectedBy
            this.receivedMode = accessionForm.getReceivedMode();
            this.receivedTrackNumber = accessionForm.getReceivedTrackNumber();
            this.receivedTime = new Date();
            this.receivedTime = Utility.parseTime(accessionForm.getReceivedDate(),accessionForm.getReceivedTimeHour(),accessionForm.getReceivedTimeMinutes(),accessionForm.getReceivedTimeAMPM());
            //this.receivedBy =
//            this.activityStatus.status = accessionForm.getActivityStatus();
//            System.out.println("........."+this.activityStatus);
            //this.protocol
            this.participant = new Participant();
            this.participant.setIdentifier(new Long(accessionForm.getParticipant()));
//            this.collectionSite = new Site();
//            this.collectionSite.name = accessionForm.getCollectionSite();
//            this.receivedSite = new Site();
//            this.receivedSite.name = accessionForm.getReceivedSite();
            //private Collection specimenCollection = new HashSet();

            //private Collection infectionAgentCollection = new HashSet();

            //private Site collectionSite;
        }
        catch (Exception excp)
        {
            excp.printStackTrace();
            Logger.out.error(excp.getMessage());
        }
    }
}