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

import edu.wustl.catissuecore.actionForm.SpecimenCollectionGroupForm;
import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;
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
	protected Site site;

    /**
     * A required specimen collection event associated with a Collection Protocol.
     */
    protected CollectionProtocolEvent collectionProtocolEvent;

    /**
     * A clinical report associated with the participant at the 
     * time of the SpecimenCollection Group receipt.
     */
    protected ClinicalReport clinicalReport;

    /**
     * The Specimens in this SpecimenCollectionGroup.
     */
    protected Collection specimenCollection = new HashSet();

    /**
     * Name: Sachin Lale 
     * Bug ID: 3052
     * Patch ID: 3052_1
     * See also: 1-4 
     * Description : A comment field at the Specimen Collection Group level.
     */
    protected String comment;

    /**
     * A registration of a Participant to a Collection Protocol.
     */
    protected CollectionProtocolRegistration collectionProtocolRegistration;

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
     * @see #setSite(Site)
     */
    public Site getSite()
    {
        return site;
    }

    /**
     * Sets the physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     * @param site physical location associated with biospecimen collection, 
     * storage, processing, or utilization.
     * @see #getSite()
     */
    public void setSite(Site site)
    {
        this.site = site;
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
     * Returns the clinical report associated with the participant at the 
     * time of the SpecimenCollection Group receipt.
     * @hibernate.many-to-one column="CLINICAL_REPORT_ID" 
	 * class="edu.wustl.catissuecore.domain.ClinicalReport" constrained="true"
     * @return the clinical report associated with the participant at the 
     * time of the SpecimenCollection Group receipt.
     * @see #setClinicalReport(ClinicalReport)
     */
    public ClinicalReport getClinicalReport()
    {
        return clinicalReport;
    }

    /**
     * Sets the clinical report associated with the participant at the 
     * time of the SpecimenCollection Group receipt.
     * @param clinicalReport the clinical report associated with the participant at the 
     * time of the SpecimenCollection Group receipt.
     * @see #getClinicalReport()
     */
    public void setClinicalReport(ClinicalReport clinicalReport)
    {
        this.clinicalReport = clinicalReport;
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
			site = new Site();
			site.setId(new Long(form.getSiteId()));
			
			collectionProtocolEvent= new CollectionProtocolEvent();
			collectionProtocolEvent.setId(new Long(form.getCollectionProtocolEventId()));

			Logger.out.debug("form.getParticipantsMedicalIdentifierId() "+form.getParticipantsMedicalIdentifierId());
			
			if(abstractForm.isAddOperation())
				clinicalReport = new ClinicalReport();
			
			clinicalReport.setSurgicalPathologyNumber(form.getSurgicalPathologyNumber());

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
				
				if(form.getParticipantsMedicalIdentifierId()!=-1)
					clinicalReport.setParticipantMedicalIdentifier(participantMedicalIdentifier);
				else
					clinicalReport.setParticipantMedicalIdentifier(null);
			}
			else
			{
				collectionProtocolRegistration.setProtocolParticipantIdentifier(form.getProtocolParticipantIdentifier());
				collectionProtocolRegistration.setParticipant(null);
			}
			
			CollectionProtocol collectionProtocol = new CollectionProtocol();
			collectionProtocol.setId(new Long(form.getCollectionProtocolId()));
			collectionProtocolRegistration.setCollectionProtocol(collectionProtocol);
		}
		catch(Exception e)
		{
			Logger.out.error(e.getMessage(),e);
			throw new AssignDataException();
		}
	}
	
	 /**
     * Returns message label to display on success add or edit
     * @return String
     */
	public String getMessageLabel() {		
		return this.name;
	}
	
	/**
	 * Name: Sachin Lale 
     * Bug ID: 3052
     * Patch ID: 3052_2
     * Seea also: 1-4
	 * Returns the Specimen Collection Group comment .
	 * @hibernate.property name="comment" type="string" column="COMMENTS" length="500"
	 * @return comment.
	 * @see #setComment(String)
	 */
	public String getComment() {
		return comment;
	}
	/**
	 * @param name The name to set.
	 */
	public void setComment(String comment) {
		this.comment = comment;
	}
}