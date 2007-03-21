/**
 * <p>Title: SpecimenCollectionGroup Class>
 * <p>Description: An event that results in the collection 
 * of one or more specimen from a participant.</p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Gautam Shetty
 * @version 1.00
 */

package edu.wustl.catissuecore.domain;

import java.io.Serializable;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.catissuecore.bean.ConsentBean;
import edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport;
import edu.wustl.catissuecore.util.global.Constants;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.util.MapDataParser;
import edu.wustl.common.util.logger.Logger;

/**
 * An event that results in the collection 
 * of one or more specimen from a participant.
 * @hibernate.class table="CATISSUE_SPECIMEN_COLL_GROUP"
 * @author gautam_shetty
 */
public class SpecimenCollectionGroup extends AbstractDomainObject implements Serializable
{
    private static final long serialVersionUID = 1234567890L;

     /**
     * System generated unique id.
     */
    protected Long id;
    
    /**
     * name assigned to Specimen Collection Group
     */
    protected String name;
    /**
     * Participant's clinical diagnosis at 
     * this collection event (e.g. Prostate Adenocarcinoma).
     */
    protected String clinicalDiagnosis;

    /**
     * The clinical status of the participant at the time of specimen collection. 
     * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
     */
    protected String clinicalStatus;
     
    /**
     * Defines whether this  record can be queried (Active) 
     * or not queried (Inactive) by any actor.
     */
    protected String activityStatus;
    
    /**
     * A physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     */
	protected Site specimenCollectionSite;

    /**
     * A required specimen collection event associated with a Collection Protocol.
     */
    protected CollectionProtocolEvent collectionProtocolEvent;

    /**
     * The Specimens in this SpecimenCollectionGroup.
     */
    protected Collection specimenCollection = new HashSet();

    /**
     * A registration of a Participant to a Collection Protocol.
     */
    protected CollectionProtocolRegistration collectionProtocolRegistration;
    
    /**
     * An identified surgical pathology report associated with 
     * current specimen collection group  
     */
    
    protected IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport;

    /**
     * An deidentified surgical pathology report associated with 
     * current specimen collection group  
     */

    protected DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport;
    
    //----For Consent Tracking. Ashish 22/11/06
    /**
     * The consent tier status by multiple participants for a particular specimen collection group.
     */
    protected Collection consentTierStatusCollection;
    
	//Mandar 15-jan-07 
	/*
	 * To perform operation based on withdraw button clicked.
	 * Default No Action to allow normal behaviour. 
	 */
	protected String consentWithdrawalOption=Constants.WITHDRAW_RESPONSE_NOACTION;
	
	//Mandar 19-jan-07 
	/*
	 * To apply changes to specimen based on consent status changes.
	 * Default Apply none to allow normal behaviour. 
	 */
	protected String applyChangesTo=Constants.APPLY_NONE;

	//Mandar 22-jan-07 
	/*
	 * To apply changes to specimen based on consent status changes.
	 * Default empty. 
	 */
	
	protected String stringOfResponseKeys="";
	
	/**
	 * Surgical Pathology Number of the associated pathology report, erlier was Present in Clinical Report
	 */
	protected String surgicalPathologyNumber;
	/**
	 * @return the consentTierStatusCollection
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.ConsentTierStatus" lazy="false" cascade="save-update"
	 * @hibernate.set table="CATISSUE_CONSENT_TIER_STATUS" name="consentTierStatusCollection"
	 * @hibernate.collection-key column="SPECIMEN_COLL_GROUP_ID"
	 */
	public Collection getConsentTierStatusCollection()
	{
		return consentTierStatusCollection;
	}
	
	/**
	 * @param consentTierStatusCollection the consentTierStatusCollection to set
	 */
	public void setConsentTierStatusCollection(Collection consentTierStatusCollection)
	{
		this.consentTierStatusCollection = consentTierStatusCollection;
	}
    //----Consent Tracking End

   	public SpecimenCollectionGroup()
    {
    
    }
    
	public SpecimenCollectionGroup(AbstractActionForm form) throws AssignDataException
	{
		Logger.out.debug("<<< Before setting Values >>>");
		setAllValues(form);
	}

	/**
	 * Returns the system generated unique id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_SPECIMEN_COLL_GRP_SEQ"
	 * @return the system generated unique id.
	 * @see #setId(Long)
	 */
	public Long getId() 
	{
		return id;
	}


	/**
	 * @param id
	 */
	public void setId(Long id) 
	{
		this.id = id;
	}
	/**
	 * Returns the system generated unique Specimen Collection Group name.
	 * @hibernate.property name="name" column="NAME" type="string" length="255"
	 * @return the system generated unique name.
	 * @see #setName(String)
	 */
	public String getName() {
		return name;
	}
	/**
	 * @param name The name to set.
	 */
	public void setName(String name) {
		this.name = name;
	}
	
    /**
     * Returns the participant's clinical diagnosis at 
     * this collection event (e.g. Prostate Adenocarcinoma).
     * @hibernate.property name="clinicalDiagnosis" type="string" 
     * column="CLINICAL_DIAGNOSIS" length="150"
     * @return the participant's clinical diagnosis at 
     * this collection event (e.g. Prostate Adenocarcinoma).
     * @see #setClinicalDiagnosis(String)
     */
    public String getClinicalDiagnosis()
    {
        return clinicalDiagnosis;
    }

    /**
     * Sets the participant's clinical diagnosis at 
     * this collection event (e.g. Prostate Adenocarcinoma).
     * @param clinicalDiagnosis the participant's clinical diagnosis at 
     * this collection event (e.g. Prostate Adenocarcinoma).
     * @see #getClinicalDiagnosis()
     */
    public void setClinicalDiagnosis(String clinicalDiagnosis)
    {
        this.clinicalDiagnosis = clinicalDiagnosis;
    }

    /**
     * Returns the clinical status of the participant at the time of specimen collection. 
     * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
     * @hibernate.property name="clinicalStatus" type="string" 
     * column="CLINICAL_STATUS" length="50"
     * @return clinical status of the participant at the time of specimen collection.
     * @see #setClinicalStatus(String)
     */
    public String getClinicalStatus()
    {
        return clinicalStatus;
    }

    /**
     * Sets the clinical status of the participant at the time of specimen collection. 
     * (e.g. New DX, pre-RX, pre-OP, post-OP, remission, relapse)
     * @param clinicalStatus the clinical status of the participant at the time of specimen collection.
     * @see #getClinicalStatus()
     */
    public void setClinicalStatus(String clinicalStatus)
    {
        this.clinicalStatus = clinicalStatus;
    }

    /**
     * Returns whether this  record can be queried (Active) 
     * or not queried (Inactive) by any actor.
     * @hibernate.property name="activityStatus" type="string" 
     * column="ACTIVITY_STATUS" length="50"
     * @return Active if this record can be queried else returns InActive.
     * @see #setActivityStatus(String)
     */
    public String getActivityStatus()
    {
        return activityStatus;
    }

    /**
     * Sets whether this  record can be queried (Active) 
     * or not queried (Inactive) by any actor.
     * @param activityStatus Active if this record can be queried else returns InActive.
     * @see #getActivityStatus()
     */
    public void setActivityStatus(String activityStatus)
    {
        this.activityStatus = activityStatus;
    }

    /**
     * Returns the physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     * @hibernate.many-to-one column="SITE_ID" 
     * class="edu.wustl.catissuecore.domain.Site" constrained="true"
     * @return the physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     * @see #setSpecimenCollectionSite(Site)
     */
    public Site getSpecimenCollectionSite()
    {
        return specimenCollectionSite;
    }

    /**
     * Sets the physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     * @param site physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     * @see #getSpecimenCollectionSite()
     */
    public void setSpecimenCollectionSite(Site site)
    {
        this.specimenCollectionSite = site;
    }

    /**
     * Returns the required specimen collection event 
     * associated with a Collection Protocol.
     * @hibernate.many-to-one column="COLLECTION_PROTOCOL_EVENT_ID" 
     * class="edu.wustl.catissuecore.domain.CollectionProtocolEvent" constrained="true"
     * @return the required specimen collection event 
     * associated with a Collection Protocol.
     * @see #setCollectionProtocolEvent(CollectionProtocolEvent)
     */
    public CollectionProtocolEvent getCollectionProtocolEvent()
    {
        return collectionProtocolEvent;
    }

    /**
     * Sets the required specimen collection event 
     * associated with a Collection Protocol.
     * @param collectionProtocolEvent the required specimen collection event 
     * associated with a Collection Protocol.
     * @see #getCollectionProtocolEvent()
     */
    public void setCollectionProtocolEvent(CollectionProtocolEvent collectionProtocolEvent)
    {
        this.collectionProtocolEvent = collectionProtocolEvent;
    }

     /**
     * Returns the collection Specimens in this SpecimenCollectionGroup.
     * @hibernate.set name="specimenCollection" table="CATISSUE_SPECIMEN"
	 * cascade="none" inverse="true" lazy="false"
	 * @hibernate.collection-key column="SPECIMEN_COLLECTION_GROUP_ID"
	 * @hibernate.collection-one-to-many class="edu.wustl.catissuecore.domain.Specimen"
     * @return the collection Specimens in this SpecimenCollectionGroup.
     * @see #setSpecimenCollection(Collection)
     */
    public Collection getSpecimenCollection()
    {
        return specimenCollection;
    }

    /**
     * Sets the collection Specimens in this SpecimenCollectionGroup.
     * @param specimenCollection the collection Specimens in this SpecimenCollectionGroup.
     * @see #getSpecimenCollection()
     */
    public void setSpecimenCollection(Collection specimenCollection)
    {
        this.specimenCollection = specimenCollection;
    }

    /**
     * Returns the registration of a Participant to a Collection Protocol.
     * @hibernate.many-to-one column="COLLECTION_PROTOCOL_REG_ID" 
     * class="edu.wustl.catissuecore.domain.CollectionProtocolRegistration" constrained="true"
     * @return the registration of a Participant to a Collection Protocol.
     * @see #setCollectionProtocolRegistration(CollectionProtocolRegistration)
     */
    public CollectionProtocolRegistration getCollectionProtocolRegistration()
    {
        return collectionProtocolRegistration;
    }

    /**
     * Sets the registration of a Participant to a Collection Protocol.
     * @param collectionProtocolRegistration the registration of a Participant 
     * to a Collection Protocol.
     * @see #getCollectionProtocolRegistration()
     */
    public void setCollectionProtocolRegistration(
            CollectionProtocolRegistration collectionProtocolRegistration)
    {
        this.collectionProtocolRegistration = collectionProtocolRegistration;
    }

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException 
	{
		
		SpecimenCollectionGroupForm form = (SpecimenCollectionGroupForm)abstractForm;
		try
		{
			this.setClinicalDiagnosis(form.getClinicalDiagnosis());
	        this.setClinicalStatus(form.getClinicalStatus());
	        this.setActivityStatus(form.getActivityStatus());
			this.setName(form.getName());
			specimenCollectionSite = new Site();
			specimenCollectionSite.setId(new Long(form.getSiteId()));
			
			collectionProtocolEvent= new CollectionProtocolEvent();
			collectionProtocolEvent.setId(new Long(form.getCollectionProtocolEventId()));

			Logger.out.debug("form.getParticipantsMedicalIdentifierId() "+form.getParticipantsMedicalIdentifierId());
			
//			if(abstractForm.isAddOperation())
//				clinicalReport = new ClinicalReport();
//			
//			clinicalReport.setSurgicalPathologyNumber(form.getSurgicalPathologyNumber());

			collectionProtocolRegistration = new CollectionProtocolRegistration();
			if(form.getCheckedButton() == 1)
			{    
				//value of radio button is 2 when participant name is selected
				Participant participant = new Participant();
				participant.setId(new Long(form.getParticipantId()));
				collectionProtocolRegistration.setParticipant(participant);
				collectionProtocolRegistration.setProtocolParticipantIdentifier(null);
				
				ParticipantMedicalIdentifier participantMedicalIdentifier = new ParticipantMedicalIdentifier();
				participantMedicalIdentifier.setId(new Long(form.getParticipantsMedicalIdentifierId()));
				
//				if(form.getParticipantsMedicalIdentifierId()!=-1)
//					clinicalReport.setParticipantMedicalIdentifier(participantMedicalIdentifier);
//				else
//					clinicalReport.setParticipantMedicalIdentifier(null);
			}
			else
			{
				collectionProtocolRegistration.setProtocolParticipantIdentifier(form.getProtocolParticipantIdentifier());
				collectionProtocolRegistration.setParticipant(null);
			}
			
			CollectionProtocol collectionProtocol = new CollectionProtocol();
			collectionProtocol.setId(new Long(form.getCollectionProtocolId()));
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
			
			/**
			 * Setting the consentTier responses for SCG Level. 
			 * Virender Mehta
			 */
			this.consentTierStatusCollection = prepareParticipantResponseCollection(form);
			
			// ----------- Mandar --15-Jan-07
			this.consentWithdrawalOption = form.getWithdrawlButtonStatus();  
			//Mandar: 19-Jan-07 :- For applying changes to specimen
			this.applyChangesTo = form.getApplyChangesTo(); 
			this.stringOfResponseKeys = form.getStringOfResponseKeys();
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			throw new AssignDataException();
		}
	}
	
	/**
	* For Consent Tracking
	* Setting the Domain Object 
	* @param  form CollectionProtocolRegistrationForm
	* @return consentResponseColl
	*/
	private Collection prepareParticipantResponseCollection(SpecimenCollectionGroupForm form) 
	{
		MapDataParser mapdataParser = new MapDataParser("edu.wustl.catissuecore.bean");
        Collection beanObjColl=null;
		try
		{
			beanObjColl = mapdataParser.generateData(form.getConsentResponseForScgValues());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}
        Iterator iter = beanObjColl.iterator();
        Collection consentResponseColl = new HashSet();
        while(iter.hasNext())
        {
        	ConsentBean consentBean = (ConsentBean)iter.next();
        	ConsentTierStatus consentTierstatus = new ConsentTierStatus();
        	//Setting response
        	consentTierstatus.setStatus(consentBean.getSpecimenCollectionGroupLevelResponse());
        	if(consentBean.getSpecimenCollectionGroupLevelResponseID()!=null&&consentBean.getSpecimenCollectionGroupLevelResponseID().trim().length()>0)
        	{
        		consentTierstatus.setId(Long.parseLong(consentBean.getSpecimenCollectionGroupLevelResponseID()));
        	}
        	//Setting consent tier
        	ConsentTier consentTier = new ConsentTier();
        	consentTier.setId(Long.parseLong(consentBean.getConsentTierID()));
        	consentTierstatus.setConsentTier(consentTier);	        	
        	consentResponseColl.add(consentTierstatus);
        }
        return consentResponseColl;
	}
	
	 /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {		
		return this.name;
	}
	
	 /**
	 * Returns deidentified surgical pathology report of the current specimen collection group
	 * @hibernate.one-to-one  name="deIdentifiedSurgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.DeidentifiedSurgicalPathologyReport"
	 * property-ref="specimenCollectionGroup" not-null="false" cascade="save-update"
	 */
    public DeidentifiedSurgicalPathologyReport getDeIdentifiedSurgicalPathologyReport() {
		return deIdentifiedSurgicalPathologyReport;
	}

	public void setDeIdentifiedSurgicalPathologyReport(
			DeidentifiedSurgicalPathologyReport deIdentifiedSurgicalPathologyReport) {
		this.deIdentifiedSurgicalPathologyReport = deIdentifiedSurgicalPathologyReport;
	}
	
	/**
	 * Returns deidentified surgical pathology report of the current specimen collection group
	 * @hibernate.one-to-one  name="identifiedSurgicalPathologyReport"
	 * class="edu.wustl.catissuecore.domain.pathology.IdentifiedSurgicalPathologyReport"
	 * propertyref="specimenCollectionGroup" not-null="false" cascade="save-update"
	 */
	public IdentifiedSurgicalPathologyReport getIdentifiedSurgicalPathologyReport() {
		return identifiedSurgicalPathologyReport;
	}

	public void setIdentifiedSurgicalPathologyReport(
			IdentifiedSurgicalPathologyReport identifiedSurgicalPathologyReport) {
		this.identifiedSurgicalPathologyReport = identifiedSurgicalPathologyReport;
	}



	//----------------------------Mandar 15-jan-07
	public String getConsentWithdrawalOption() {
		return consentWithdrawalOption;
	}

	public void setConsentWithdrawalOption(String consentWithdrawalOption) {
		this.consentWithdrawalOption = consentWithdrawalOption;
	}
	
	//------------------------- Mandar 19-Jan-07
	public String getApplyChangesTo() {
		return applyChangesTo;
	}
	public void setApplyChangesTo(String applyChangesTo) {
		this.applyChangesTo = applyChangesTo;
	}
	// ------------------- Mandar : - 22-Jan-07

	public String getStringOfResponseKeys() {
		return stringOfResponseKeys;
	}

	public void setStringOfResponseKeys(String stringOfResponseKeys) {
		this.stringOfResponseKeys = stringOfResponseKeys;
	}

	/**
     * Returns the surgicalPathologyNumber of the report at the time of specimen collection. 
     * @hibernate.property name="surgicalPathologyNumber" type="string" 
     * column="SURGICAL_PATHOLOGY_NUMBER" length="50"
     * @return surgical pathology number of the report at the time of specimen collection.
     * @see #setSurgicalPathologyNumber(String)
     */
	public String getSurgicalPathologyNumber() 
	{
		return surgicalPathologyNumber;
	}

	/**
     * Sets the surgical pathology number of the report at the time of specimen collection. 
     * @param surgicalPathologyNumber the surgical pathology report of the report at the time of specimen collection.
     * @see #getSurgicalPathologyNumber()
     */
	public void setSurgicalPathologyNumber(String surgicalPathologyNumber) 
	{
		this.surgicalPathologyNumber = surgicalPathologyNumber;
	}
	
	
	
}