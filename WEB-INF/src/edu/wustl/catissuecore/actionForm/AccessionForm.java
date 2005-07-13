/**
 * <p>Title: AccessionForm Class>
 * <p>Description:  AccessionForm class is used to encapsulate all the request parameters passed 
 * from Accession Add/Edit webpage. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 * Created on Mar 3, 2005
 */

package edu.wustl.catissuecore.actionForm;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;

import javax.servlet.http.HttpServletRequest;

import org.apache.struts.action.ActionErrors;
import org.apache.struts.action.ActionMapping;

import edu.wustl.catissuecore.domain.AbstractDomainObject;
import edu.wustl.catissuecore.domain.Accession;
import edu.wustl.catissuecore.domain.User;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.catissuecore.util.global.Utility;
import edu.wustl.catissuecore.util.global.Validator;

/**
 * AccessionForm class is used to encapsulate all the request parameters passed 
 * from Accession Add/Edit webpage.
 * @author gautam_shetty
 */

public class AccessionForm extends AbstractActionForm implements Serializable
{

    /**
     * Serial Version ID
     */
    private static final long serialVersionUID = 1234567890L;

    /**
     * identifier is a unique id assigned to each Accession.
     * */
    protected long identifier;
    
    /**
     * Represents the operation(Add/Edit) to be performed.
     * */
    private String operation = "";

    /**
     * Protocol's participant ID number.
     */
    protected long protocolNumber;

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
     * The Collection Date of the Accession.
     */
    protected String collectionDate;

    /**
     * The hours of the collection Time of the Accession.
     */
    protected int collectionTimeHour;

    /**
     * The minutes of the collection Time of the Accession.
     */
    protected int collectionTimeMinutes;

    /**
     * Field indicating whether the collection time was before or after noon.
     */
    protected String collectionTimeAMPM;

    /**
     * The technician who collected the Biospecimen.
     */
    protected String collectedBy;

    /**
     * Site at which the Biospecimens are collected.
     */
    private String collectionSite;

    /**
     * The mode in which the accession was received.(by hand, courier, FedEX, UPS).
     */
    protected String receivedMode;

    /**
     * The tracking number of accession shipment.
     */
    protected String receivedTrackNumber;

    /**
     * The Received Date of the Accession.
     */
    protected String receivedDate;

    /**
     * The hours of the received Time of the Accession.
     */
    protected int receivedTimeHour;

    /**
     * The minutes of the received Time of the Accession.
     */
    protected int receivedTimeMinutes;

    /**
     * Field indicating whether the received time was before or after noon.
     */
    protected String receivedTimeAMPM;

    /**
     * The technician who received the Biospecimen.
     */
    protected String receivedBy;

    /**
     * The Site where the Biospecimens are received.
     */
    private String receivedSite;

    /**
     * The activity status of the accession.
     */
    protected String activityStatus;

    /**
     * The Protocol used for the accession.
     */
    private String protocol;

    /**
     * Associated Participant in the accession.
     */
    private long participant;

    private Collection specimenCollection = new HashSet();

    private Collection infectionAgentCollection = new HashSet();

    /**
     * Resets the values of all the fields.
     */
    private void reset()
    {
        this.identifier = -1;
        this.protocolNumber = -1;
        this.medicalRecordNumber = null;
        this.medicalReportURL = null;
        this.clinicalDiagnosis = null;
        this.diseaseStatus = null;
        this.surgicalPathologyNumber = null;
        this.surgicalPathologyReportURL = null;
        this.collectionDate = null;
        this.collectionTimeHour = -1;
        this.collectionTimeMinutes = -1;
        this.collectionTimeAMPM = null;
        this.collectedBy = null;
        this.receivedBy = null;
        this.activityStatus = null;
        this.protocol = null;
        this.participant = -1;
        this.receivedSite = null;
        this.specimenCollection = null;
        this.infectionAgentCollection = null;
    }

    /**
     *  Initializes a newly created AccessionForm object.
     */
    public AccessionForm()
    {
        reset();
    }

    /**
     * Returns the identifier assigned to accession.
     * @return the id assigned to accession.
     * @see #setIdentifier(int)
     * */
    public long getIdentifier()
    {
        return identifier;
    }

    /**
     * Sets an identifier for the accession.
     * @param identifier identifier for the accession.
     * @see #getIdentifier()
     * */
    public void setIdentifier(long identifier)
    {
        this.identifier = identifier;
    }

    /**
     * Returns the operation(Add/Edit) to be performed.
     * @return Returns the operation.
     */
    public String getOperation()
    {
        return operation;
    }
    
    /**
     * Sets the operation to be performed.
     * @param operation The operation to set.
     */
    public void setOperation(String operation)
    {
        this.operation = operation;
    }
    
    /**
     * Returns Protocol's participant ID number.
     * @return Protocol's participant ID number.
     * @see #setProtocolNumber(Long)
     */
    public long getProtocolNumber()
    {
        return protocolNumber;
    }

    /**
     * Sets Protocol's participant ID number.
     * @param protocolNumber the Protocol's participant ID number.
     * @see #getProtocolNumber()
     */
    public void setProtocolNumber(long protocolNumber)
    {
        this.protocolNumber = protocolNumber;
    }

    /**
     * Returns the Medical record number associated with this accession.
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
     * @return the Collection Time of the Accession.
     * @see #setCollectionTime(Date)
     */
    public String getCollectionDate()
    {
        return collectionDate;
    }

    /**
     * Sets the Collection date of the Accession.
     * @param collectionTime the Collection Time of the Accession.
     * @see #getCollectionTime()
     */
    public void setCollectionDate(String collectionDate)
    {
        this.collectionDate = collectionDate;
    }

    /**
     * Returns the hours in the collection time of the Accession.
     * @return the hours in the collection time of the Accession.
     * @see #setCollectionTimeHour(int)  
     */
    public int getCollectionTimeHour()
    {
        return collectionTimeHour;
    }

    /**
     * Sets the hours in the collection time of the Accession.
     * @param collectionTimeHour the hours in the collection time of the Accession.
     * @see #getCollectionTimeHour()
     */
    public void setCollectionTimeHour(int collectionTimeHour)
    {
        this.collectionTimeHour = collectionTimeHour;
    }

    /**
     * Returns the minutes in the collection time of the Accession.
     * @return the minutes in the collection time of the Accession.
     * @see #setCollectionTimeMinutes(int)  
     */
    public int getCollectionTimeMinutes()
    {
        return collectionTimeMinutes;
    }

    /**
     * Sets the minutes in the collection time of the Accession.
     * @param collectionTimeHour the minutes in the collection time of the Accession.
     * @see #getCollectionTimeMinutes()
     */
    public void setCollectionTimeMinutes(int collectionTimeMinutes)
    {
        this.collectionTimeMinutes = collectionTimeMinutes;
    }

    /**
     * Returns the am / pm in the collection time of the Accession.
     * @return the am / pm in the collection time of the Accession.
     * @see #setCollectionTimeAMPM(String)  
     */
    public String getCollectionTimeAMPM()
    {
        return collectionTimeAMPM;
    }

    /**
     * Sets the am / pm in the collection time of the Accession.
     * @param collectionTimeHour the am / pm in the collection time of the Accession.
     * @see #getCollectionTimeAMPM()
     */
    public void setCollectionTimeAMPM(String collectionTimeAMPM)
    {
        this.collectionTimeAMPM = collectionTimeAMPM;
    }

    /**
     * Returns the technician who collected the Biospecimen.
     * @return the technician who collected the Biospecimen.
     * @see #setCollectedBy(User)
     */
    public String getCollectedBy()
    {
        return collectedBy;
    }

    /**
     * Sets the technician who collected the Biospecimen.
     * @param collectedBy An User object representing the technician.
     * @see #getCollectedBy()
     */
    public void setCollectedBy(String collectedBy)
    {
        this.collectedBy = collectedBy;
    }

    /**
     * Returns the mode thruogh biospecimens was received(by hand, courier, FedEX, UPS).
     * @return the mode thruogh biospecimens was received(by hand, courier, FedEX, UPS).
     * @see #setReceivedMode(String)
     */
    public String getReceivedMode()
    {
        return receivedMode;
    }

    /**
     * Sets the mode through biospecimens was received(by hand, courier, FedEX, UPS).
     * @param receivedMode the mode thruogh biospecimens was received(by hand, courier, FedEX, UPS).
     * @see #getReceivedMode()
     */
    public void setReceivedMode(String receivedMode)
    {
        this.receivedMode = receivedMode;
    }

    /**
     * Returns the tracking number of accession shipment.
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
     * Returns the received date of the Accession.
     * @return the received date of the Accession.
     * @see #setReceivedDate(String)  
     */
    public String getReceivedDate()
    {
        return receivedDate;
    }

    /**
     * Sets the received date of the Accession.
     * @param receivedDate the received date of the Accession.
     * @see #getReceivedDate()
     */
    public void setReceivedDate(String receivedDate)
    {
        this.receivedDate = receivedDate;
    }

    /**
     * Returns the hours in the received time of the Accession.
     * @return the hours in the received time of the Accession.
     * @see #setReceivedTimeHour(int)  
     */
    public int getReceivedTimeHour()
    {
        return receivedTimeHour;
    }

    /**
     * Sets the hours in the received time of the Accession.
     * @param receivedTimeHour the hours in the received time of the Accession.
     * @see #getReceivedTimeHour()
     */
    public void setReceivedTimeHour(int receivedTimeHour)
    {
        this.receivedTimeHour = receivedTimeHour;
    }

    /**
     * Returns the minutes in the received time of the Accession.
     * @return the minutes in the received time of the Accession.
     * @see #setReceivedTimeMinutes(int)  
     */
    public int getReceivedTimeMinutes()
    {
        return receivedTimeMinutes;
    }

    /**
     * Sets the minutes in the received time of the Accession.
     * @param receivedTimeMinutes the minutes in the received time of the Accession.
     * @see #getReceivedTimeMinutes()
     */
    public void setReceivedTimeMinutes(int receivedTimeMinutes)
    {
        this.receivedTimeMinutes = receivedTimeMinutes;
    }

    /**
     * Returns the am/pm in the received time of the Accession.
     * @return the am/pm in the received time of the Accession.
     * @see #setReceivedTimeAMPM(String)  
     */
    public String getReceivedTimeAMPM()
    {
        return receivedTimeAMPM;
    }

    /**
     * Sets the am/pm in the received time of the Accession.
     * @param receivedTimeAMPM the am/pm in the received time of the Accession.
     * @see #getReceivedTimeAMPM()
     */
    public void setReceivedTimeAMPM(String receivedTimeAMPM)
    {
        this.receivedTimeAMPM = receivedTimeAMPM;
    }

    /**
     * Returns the technician who received the Biospecimen.
     * @return the technician who received the Biospecimen.
     * @see #setReceivedBy(String)
     */
    public String getReceivedBy()
    {
        return receivedBy;
    }

    /**
     * Sets the technician who received the Biospecimen.
     * @param receivedBy the technician who received the Biospecimen.
     * @see #getReceivedBy() 
     */
    public void setReceivedBy(String receivedBy)
    {
        this.receivedBy = receivedBy;
    }

    /**
     * Returns the activity status of the accession.
     * @return Returns the activity status of the accession.
     * @see #setActivityStatus(String)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets the activity status of the accession.
     * @param activityStatus the activity status of the accession.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the identifier of the participant associated with the accession.
     * @return the identifier of the participant associated with the accession.
     * @see #setParticipant(long)
     */
    public long getParticipant()
    {

        return participant;
    }

    /**
     * Sets the identifier of the participant associated with the accession. 
     * @param participant the identifier of the participant associated with the accession.
     * @see #getParticipant()
     */
    public void setParticipant(long participant)
    {
        this.participant = participant;
    }

    /**
     * Returns the Protocol used for this Accession.
     * @return the Protocol used for this Accession.
     * @see #setProtocol(String)
     */
    public String getProtocol()
    {

        return protocol;
    }

    /**
     * Sets the Protocol used for this Accession.
     * @param protocol the Protocol used for this Accession.
     * @see #getProtocol()
     */
    public void setProtocol(String protocol)
    {
        this.protocol = protocol;
    }

    /**
     * Returns the Site at which the Biospecimens are collected.
     * @return the Site at which the Biospecimens are collected.
     * @see #setCollectionSite(String)
     */
    public String getCollectionSite()
    {

        return collectionSite;
    }

    /**
     * Sets the Site at which the Biospecimens are collected.
     * @param collectionSite the Site at which the Biospecimens are collected.
     * @see #getCollectionSite()
     */
    public void setCollectionSite(String collectionSite)
    {
        this.collectionSite = collectionSite;
    }

    /**
     * Returns the Site at which the Biospecimens are received.
     * @return the Site at which the Biospecimens are received.
     * @see #setReceivedSite(String)
     */
    public String getReceivedSite()
    {
        return receivedSite;
    }

    /**
     * Sets the Site at which the Biospecimens are received.
     * @param receivedSite the Site at which the Biospecimens are received.
     * @see #getReceivedSite()
     */
    public void setReceivedSite(String receivedSite)
    {
        this.receivedSite = receivedSite;
    }

    public Collection getSpecimenCollection()
    {

        return specimenCollection;
    }

    public void setSpecimenCollection(Collection specimenCollection)
    {
        this.specimenCollection = specimenCollection;
    }

    public Collection getInfectionAgentCollection()
    {
        return infectionAgentCollection;
    }

    public void setInfectionAgentCollection(Collection infectionAgentCollection)
    {
        this.infectionAgentCollection = infectionAgentCollection;
    }

    /**
     * Resets the values of all the fields.
     * This method defined in ActionForm is overridden in this class.
     */
    public void reset(ActionMapping mapping, HttpServletRequest request)
    {
        reset();
    }

    /**
     * Returns the id assigned to form bean.
     * @return the id assigned to form bean.
     */
    public int getFormId()
    {
        return Constants.ACCESSION_FORM_ID;
    }
    
    /**
     * Checks the operation to be performed is add or not.
     * @return Returns true if operation is equal to "add", else it returns false
     * */
    public boolean isAddOperation()
    {
        return(getOperation().equals(Constants.ADD));
    }
    
    /**
     * This function Copies the data from an Accession object to a AccessionForm object.
     * @param accession An Accession object.  
     * */
    public void setAllValues(AbstractDomainObject abstractDomain)
    {
        Accession accession = (Accession) abstractDomain;
        this.identifier = accession.getIdentifier().longValue();
        this.protocolNumber = accession.getProtocolNumber().longValue();
        this.medicalRecordNumber = accession.getMedicalRecordNumber();
        this.medicalReportURL = accession.getMedicalReportURL();
        this.clinicalDiagnosis = accession.getClinicalDiagnosis();
        this.diseaseStatus = accession.getDiseaseStatus();
        this.surgicalPathologyNumber = accession.getSurgicalPathologyNumber();
        this.surgicalPathologyReportURL = accession
                .getSurgicalPathologyReportURL();
        Calendar cal = Calendar.getInstance();
        cal.setTime(accession.getCollectionTime());
        this.collectionTimeHour = cal.get(Calendar.HOUR);
        this.collectionTimeMinutes = cal.get(Calendar.MINUTE);
        this.collectionDate = Utility.parseDateToString(accession.getCollectionTime());
        //cal.get(Calendar.YEAR)+"-"+cal.get(Calendar.MONTH)+"-"+cal.get(Calendar.DATE);
        //this.collectedBy
        this.receivedMode = accession.getReceivedMode();
        this.receivedTrackNumber = accession.getReceivedTrackNumber();
        cal.setTime(accession.getReceivedTime());
        this.receivedTimeHour = cal.get(Calendar.HOUR);
        this.receivedTimeMinutes = cal.get(Calendar.MINUTE);
        this.receivedDate = Utility.parseDateToString(accession.getReceivedTime());
        this.setParticipant(accession.getParticipant().getIdentifier().longValue());
        //this.receivedBy =
        //        this.activityStatus.status = accession.getActivityStatus();
        //        System.out.println("........."+this.activityStatus);
        //this.protocol
        //this.participant
        //        this.collectionSite = new Site();
        //        this.collectionSite.name = accession.getCollectionSite();
        //        this.receivedSite = new Site();
        //        this.receivedSite.name = accession.getReceivedSite();
        //private Collection specimenCollection = new HashSet();

        //private Collection infectionAgentCollection = new HashSet();

        //private Site collectionSite;
    }
    
    /**
     * Overrides the validate method of ActionForm.
     */
    public ActionErrors validate(ActionMapping mapping, HttpServletRequest request)
    {
        ActionErrors errors = new ActionErrors();
        Validator validator = new Validator();
        
        if(operation.equals(Constants.SEARCH))
        {
            checkValidNumber(new Long(identifier).toString(),"user.identifier",errors, validator);
        }
        else
        {
            if (operation.equals(Constants.ADD) || operation.equals(Constants.EDIT))
            {
                checkValidNumber(new Long(protocolNumber).toString(),"accession.protocolNumber",errors,validator);
                checkValidNumber(receivedTrackNumber,"accession.receivedTrackNumber",errors,validator);
            }
        }
        return errors;
    }
}