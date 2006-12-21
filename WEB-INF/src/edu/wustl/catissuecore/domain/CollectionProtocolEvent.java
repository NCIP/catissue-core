/**
 * <p>Title: CollectionProtocolEvent Class</p>
 * <p>Description: A required specimen collection event associated with a Collection Protocol. </p>
 * Copyright:    Copyright (c) year
 * Company: Washington University, School of Medicine, St. Louis.
 * @author Mandar Deshmukh
 * @version 1.00
 * Created on July 12, 2005
 */

package edu.wustl.catissuecore.domain;

import java.util.Collection;
import java.util.LinkedHashSet;

import edu.wustl.common.actionForm.AbstractActionForm;
import edu.wustl.common.domain.AbstractDomainObject;
import edu.wustl.common.exception.AssignDataException;

/**
 * A required specimen collection event associated with a Collection Protocol.
 * 
 * @hibernate.class table="CATISSUE_COLL_PROT_EVENT"
 * @author Mandar Deshmukh
 */
public class CollectionProtocolEvent extends AbstractDomainObject implements java.io.Serializable, Comparable
{
	private static final long serialVersionUID = 1234567890L;
	
	/**
	 * System generated unique id.
	 */
	protected Long id;
	
	/**
	 * Defines the required clinical status of the participant at the time of specimen collection. e.g. Pre-Op, Post-op, Pre-RX etc.
	 */
	protected String clinicalStatus;
	
	/**
	 * Defines the relative time point in days, with respect to the registration date of participant on this protocol, when the specimen should be collected from participant.
	 */
	protected Double studyCalendarEventPoint;
	
	/**
	 * Collection of SpecimenRequirements associated with the CollectionProtocolEvent.
	 */
	protected Collection specimenRequirementCollection = new LinkedHashSet();
	
	/**
	 * CollectionProtocol associated with the CollectionProtocolEvent.
	 */
	protected CollectionProtocol collectionProtocol;

	/**
	 * Returns the id.
	 * @hibernate.id name="id" column="IDENTIFIER" type="long" length="30"
	 * unsaved-value="null" generator-class="native"
	 * @hibernate.generator-param name="sequence" value="CATISSUE_COLL_PROT_EVENT_SEQ"
	 * @return Returns the id.
	 */
	public Long getId()
	{
		return id;
	}

	/**
	 * @param id The id to set.
	 */
	public void setId(Long id)
	{
		this.id = id;
	}

	/**
	 * Returns the clinicalStatus.
	 * @hibernate.property name="clinicalStatus" type="string"
	 * column="CLINICAL_STATUS" length="50"
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
	 * Returns the studyCalendarEventPoint.
	 * @hibernate.property name="studyCalendarEventPoint" type="double"
	 * column="STUDY_CALENDAR_EVENT_POINT" length="50"
	 * @return Returns the studyCalendarEventPoint.
	 */
	public Double getStudyCalendarEventPoint()
	{
		return studyCalendarEventPoint;
	}

	/**
	 * @param studyCalendarEventPoint The studyCalendarEventPoint to set.
	 */
	public void setStudyCalendarEventPoint(Double studyCalendarEventPoint)
	{
		this.studyCalendarEventPoint = studyCalendarEventPoint;
	}

	/**
	 * Returns the collection of SpecimenRequirements for this Protocol.
	 * @hibernate.set name="specimenRequirementCollection" table="CATISSUE_COLL_SPECIMEN_REQ" 
	 * cascade="save-update" inverse="false" lazy="false"
	 * @hibernate.collection-key column="COLLECTION_PROTOCOL_EVENT_ID"
	 * @hibernate.collection-many-to-many class="edu.wustl.catissuecore.domain.SpecimenRequirement" column="SPECIMEN_REQUIREMENT_ID"
	 * @return Returns the collection of SpecimenRequirements for this Protocol.
	 */
	public Collection getSpecimenRequirementCollection()
	{
		return specimenRequirementCollection;
	}

	/**
	 * @param specimenRequirementCollection
	 *  The specimenRequirementCollection to set.
	 */
	public void setSpecimenRequirementCollection(Collection specimenRequirementCollection)
	{
		this.specimenRequirementCollection = specimenRequirementCollection;
	}

	/**
	 * Returns the collectionProtocol.
	 * @hibernate.many-to-one column="COLLECTION_PROTOCOL_ID" class="edu.wustl.catissuecore.domain.CollectionProtocol"
	 * constrained="true"
	 * @return Returns the collectionProtocol.
	 */
	public CollectionProtocol getCollectionProtocol()
	{
		return collectionProtocol;
	}

	/**
	 * @param collectionProtocol
	 *  The collectionProtocol to set.
	 */
	public void setCollectionProtocol(CollectionProtocol collectionProtocol)
	{
		this.collectionProtocol = collectionProtocol;
	}
	
	public String toString()
	{
		return "CPE: "+clinicalStatus+" | "+studyCalendarEventPoint +" | "+ specimenRequirementCollection.toString();
	}

    /* (non-Javadoc)
     * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
     */
    public void setAllValues(AbstractActionForm abstractForm) throws AssignDataException
    {
        
    }
    
    public int compareTo(Object obj)
	{
		if(obj instanceof CollectionProtocolEvent)
		{
			CollectionProtocolEvent collectionProtocolEvent = (CollectionProtocolEvent)obj;
			if(studyCalendarEventPoint.doubleValue()< collectionProtocolEvent.getStudyCalendarEventPoint().doubleValue())
				return -1;
			else if(studyCalendarEventPoint.doubleValue() > collectionProtocolEvent.getStudyCalendarEventPoint().doubleValue())
				return 1;
			else
				return 0;
		}
		return 0;
	}
   
}