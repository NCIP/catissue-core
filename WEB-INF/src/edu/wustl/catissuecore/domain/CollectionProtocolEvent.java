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

import edu.wustl.common.actionForm.IValueObject;
import edu.wustl.common.exception.AssignDataException;
import edu.wustl.common.exception.BizLogicException;

/**
 * A required specimen collection event associated with a Collection Protocol.
 * 
 * @hibernate.class table="CATISSUE_COLL_PROT_EVENT"
 * @author Mandar Deshmukh
 */
public class CollectionProtocolEvent extends AbstractSpecimenCollectionGroup implements java.io.Serializable, Comparable
{
	private static final long serialVersionUID = 1234567890L;

	/**
	 * System generated unique id.
	 */
	protected Long id;

	/**
	 * Patch Id : FutureSCG_13
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Defines the required collectionPointLabel.
	 */
	protected String collectionPointLabel;

	/**
	 * Defines the relative time point in days, with respect to the registration date of participant on this protocol, when the specimen should be collected from participant.
	 */
	protected Double studyCalendarEventPoint;


	/**
	 * CollectionProtocol associated with the CollectionProtocolEvent.
	 */
	protected CollectionProtocol collectionProtocol;
	
	protected Collection<SpecimenRequirement> specimenRequirementCollection  = new LinkedHashSet<SpecimenRequirement>();
	
	protected Collection specimenCollectionGroupCollection;
	
	/**
	 * For SCG labeling,this will be exposed through API and not in the model
	 */
	private String labelFormat;
	
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
		return "CPE: "+clinicalStatus+" | "+ studyCalendarEventPoint +" | "+ this.getId();
	}

	/* (non-Javadoc)
	 * @see edu.wustl.catissuecore.domain.AbstractDomainObject#setAllValues(edu.wustl.catissuecore.actionForm.AbstractActionForm)
	 */
	public void setAllValues(IValueObject abstractForm) throws AssignDataException
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
	/**
	 * Patch Id : FutureSCG_14
	 * Description : collectionPointLabel attribute added
	 */
	/**
	 * Returns the collectionPointLabel
	 * @hibernate.property name="collectionPointLabel" type="string"
	 * column="COLLECTION_POINT_LABEL" length="255"
	 * @return Returns the collectionPointLabel of the participant.
	 * @see #setCollectionPointLabel(String)
	 */
	public String getCollectionPointLabel() {
		return collectionPointLabel;
	}
	/**
	 * Sets collectionPointLabel
	 * @param collectionPointLabel
	 * @see #getCollectionPointLabel()
	 */
	public void setCollectionPointLabel(String collectionPointLabel) 
	{
		this.collectionPointLabel = collectionPointLabel;
	}

	public Collection getSpecimenCollectionGroupCollection()
	{
		return specimenCollectionGroupCollection;
	}

	public void setSpecimenCollectionGroupCollection(Collection specimenCollectionGroupCollection) 
	{
		this.specimenCollectionGroupCollection = specimenCollectionGroupCollection;
	}

	
	public Collection<SpecimenRequirement> getSpecimenRequirementCollection()
	{
		return specimenRequirementCollection;
	}

	
	public void setSpecimenRequirementCollection(Collection<SpecimenRequirement> requirementSpecimenCollection)
	{
		this.specimenRequirementCollection = requirementSpecimenCollection;
	}

	@Override
	public CollectionProtocolRegistration getCollectionProtocolRegistration()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getGroupName()
	{
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected void setGroupName(String name) throws BizLogicException
	{
		// TODO Auto-generated method stub
		
	}

	
	public String getLabelFormat()
	{
		return labelFormat;
	}

	
	public void setLabelFormat(String labelFormat)
	{
		this.labelFormat = labelFormat;
	}
	
}